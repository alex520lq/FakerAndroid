package com.fakerandroid.as;

import com.fakerandroid.decoder.api.Transfer;
import com.fakerandroid.decoder.pipeline.TransformInvocation;
import com.intellij.ide.JavaUiBundle;
import com.intellij.ide.impl.ProjectUtil;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserDialog;
import com.intellij.openapi.progress.BackgroundTaskQueue;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.luhuiguo.chinese.ChineseUtils;
import com.luhuiguo.chinese.pinyin.PinyinFormat;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.io.File;

public class ProjectPathDialog extends DialogWrapper {
    private JPanel contentPane;
    private TextFieldWithBrowseButton fileFied;
    VirtualFile apk;
    Project project;
    protected ProjectPathDialog(@Nullable Project project, boolean canBeParent, VirtualFile apk) {
        super(project, canBeParent);
        this.apk = apk;
        this.project = project;
        init();
        contentPane.setSize(300,100);
        final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        descriptor.putUserData(FileChooserDialog.PREFER_LAST_OVER_TO_SELECT, Boolean.TRUE);
        fileFied.addBrowseFolderListener(JavaUiBundle.message("module.paths.output.title"),
                JavaUiBundle.message("module.paths.output.title"), null,
                descriptor);

        File originalFile = new File(apk.getPath());
        File projectFile = new File(originalFile.getParent(), ChineseUtils.toPinyin(originalFile.getName().replace(".apk",""), PinyinFormat.TONELESS_PINYIN_FORMAT).replace(" ","-"));
        String outDir = projectFile.getAbsolutePath();
        fileFied.setText(outDir);

    }
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    @Override
    protected JComponent createNorthPanel() {
        return contentPane;
    }

    @Override
    public JComponent getContentPanel() {
        return contentPane;
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
        return null;
    }

    @Override
    protected void dispose() {
        super.dispose();
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
                        BackgroundTaskQueue backgroundTaskQueue = new BackgroundTaskQueue(project, "FackerAndroid is decoder running ");//.run();
                FakerAndroidTask smailPackerThread= new FakerAndroidTask(project,apk,"faking a apk file to AndroidStudio project");
                FakerAndroidProgressIndicator backgroundableProcessIndicator = new FakerAndroidProgressIndicator(smailPackerThread);
                backgroundTaskQueue.run(smailPackerThread, ModalityState.NON_MODAL,backgroundableProcessIndicator);
    }

    class FakerAndroidTask extends Task.Backgroundable {
        VirtualFile apk;
        String outDir;
        Project project;
        public FakerAndroidTask(@Nullable Project project, VirtualFile apk,@Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String title) {
            super(project, title);
            this.apk = apk;
            File originalFile = new File(apk.getPath());
            File projectFile = new File(originalFile.getParent(), ChineseUtils.toPinyin(originalFile.getName().replace(".apk",""), PinyinFormat.TONELESS_PINYIN_FORMAT).replace(" ","-"));
            outDir = projectFile.getAbsolutePath();
            this.project = project;
        }
        @Override
        public boolean shouldStartInBackground() {
            return false;
        }

        @Override
        public void run(@NotNull ProgressIndicator progressIndicator) {
            //TODO  you need a FakerAndroid.jar for dependency(add the last version of FakerAndroid.jar to the lib folder)
            new Transfer(apk.getPath(), outDir, new TransformInvocation() {
                @Override
                public void callBack(String msg) {
                    progressIndicator.setText2(msg);
                }
            }).translate();
        }
        @Override
        public void onFinished() {
            super.onFinished();
            ProjectUtil.openOrImport(new File(outDir).getPath(),project,false);
            System.out.println("finished~~~");
        }
        @Override
        public void onThrowable(@NotNull Throwable error) {
            super.onThrowable(error);
        }
    }
}
