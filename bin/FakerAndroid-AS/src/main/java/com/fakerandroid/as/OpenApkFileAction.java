package com.fakerandroid.as;
import com.intellij.ide.GeneralSettings;
import com.intellij.ide.IdeBundle;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.PlatformProjectOpenProcessor;

import java.awt.*;
import java.io.File;
public class OpenApkFileAction extends DumbAwareAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Disposable disposable = Disposer.newDisposable();
        try {
            Project project = e.getProject();
            boolean showFiles = project != null || PlatformProjectOpenProcessor.getInstanceIfItExists() != null;
            OpenProjectFileChooserDescriptorWithAsyncIcon descriptor =
                    showFiles ? new ProjectOrFileChooserDescriptor() : new ProjectOnlyFileChooserDescriptor();
            descriptor.putUserData(PathChooserDialog.PREFER_LAST_OVER_EXPLICIT, showFiles);
            Disposer.register(disposable, descriptor);
            VirtualFile explicitPreferredDirectory = ((project != null) && !project.isDefault()) ? project.getBaseDir() : null;
            if (explicitPreferredDirectory == null) {
                if (StringUtil.isNotEmpty(GeneralSettings.getInstance().getDefaultProjectDirectory())) {
                    explicitPreferredDirectory = VfsUtil.findFileByIoFile(new File(GeneralSettings.getInstance().getDefaultProjectDirectory()), true);
                }
                else {
                    explicitPreferredDirectory = VfsUtil.getUserHomeDir();
                }
            }
            // The chooseFiles method shows a FileChooserDialog and it doesn't return control until
            // a user closes the dialog.
            // Note: this method is invoked from the main thread but chooseFiles uses a nested message
            // loop to avoid the IDE from freeze.
            FileChooser.chooseFiles(descriptor, project, explicitPreferredDirectory, files -> {
                VirtualFile apk = files.get(0);
                if(!apk.getPath().endsWith(".apk")){
                    return;
                }
                ProjectPathDialog projectPathDialog = new ProjectPathDialog(project,true,apk);
                projectPathDialog.showAndGet();
            });
        } finally {
            Disposer.dispose(disposable);
        }
    }

    public OpenApkFileAction() {
    }

    public OpenApkFileAction(String text) {
        super(text);
    }

    private static class ProjectOnlyFileChooserDescriptor extends OpenProjectFileChooserDescriptorWithAsyncIcon {
        public ProjectOnlyFileChooserDescriptor() {
            setTitle(IdeBundle.message("title.open.project"));
        }
    }
    @Override
    public void update(AnActionEvent e) {
    }
    private static class ProjectOrFileChooserDescriptor extends OpenProjectFileChooserDescriptorWithAsyncIcon {
        private final FileChooserDescriptor myStandardDescriptor = FileChooserDescriptorFactory
                .createSingleFileNoJarsDescriptor().withHideIgnored(false);
        public ProjectOrFileChooserDescriptor() {
            setTitle(IdeBundle.message("title.open.file.or.project"));
        }

        @Override
        public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
            return file.isDirectory() ? super.isFileVisible(file, showHiddenFiles) : myStandardDescriptor.isFileVisible(file, showHiddenFiles);
        }
        @Override
        public boolean isFileSelectable(VirtualFile file) {
            return file.isDirectory() ? super.isFileSelectable(file) : myStandardDescriptor.isFileSelectable(file);
        }
        @Override
        public boolean isChooseMultiple() {
            return false;
        }
    }
}
