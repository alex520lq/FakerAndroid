package com.fakerandroid.as.editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBSplitter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.beans.PropertyChangeListener;
public class DatEditor extends UserDataHolderBase implements FileEditor {
  private JBSplitter mySplitter;
  private DatViewPanel myApkViewPanel;
  private VirtualFile myBaseFile;
  public DatEditor(@NotNull Project project, @NotNull VirtualFile baseFile) {
    myBaseFile = baseFile;
    mySplitter = new JBSplitter(true, "android.apk.xxviewer", 0.62f);
    mySplitter.setName("apkViwerContainer");
    mySplitter.setFocusCycleRoot(true);
    mySplitter.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
    mySplitter.setSecondComponent(new JPanel());
    myApkViewPanel = new DatViewPanel();
    mySplitter.setFirstComponent(myApkViewPanel.getContainer());
  }
  @Override
  public @NotNull JComponent getComponent() {
    return mySplitter;
  }

  @Override
  public @Nullable JComponent getPreferredFocusedComponent() {
    return myApkViewPanel.getPreferredFocusedComponent();
  }

  @Override
  public @NotNull String getName() {
    return myBaseFile.getName();
  }

  @Override
  public void setState(@NotNull FileEditorState fileEditorState) {
  }

  @Override
  public boolean isModified() {
    return false;
  }

  @Override
  public boolean isValid() {
    return myBaseFile.isValid();
  }

  @Override
  public void addPropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {
  }

  @Override
  public void removePropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {
  }

  @Override
  public @Nullable FileEditorLocation getCurrentLocation() {
    return null;
  }

  @Override
  public void dispose() {
  }
}
