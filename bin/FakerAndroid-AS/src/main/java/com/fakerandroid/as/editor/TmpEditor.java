package com.fakerandroid.as.editor;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.compiled.ClassFileDecompilers;
import org.jetbrains.annotations.NotNull;

public class TmpEditor extends ClassFileDecompilers.Light {
    @Override
    public @NotNull CharSequence getText(@NotNull VirtualFile virtualFile) throws CannotDecompileException {
        return null;
    }

    @Override
    public boolean accepts(@NotNull VirtualFile virtualFile) {
        return false;
    }
}
