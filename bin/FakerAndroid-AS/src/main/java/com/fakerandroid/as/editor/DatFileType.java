package com.fakerandroid.as.editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;

public class DatFileType implements FileType {
    public static final DatFileType INSTANCE = new DatFileType();
    public DatFileType() {
    }
    @Override
    public @NotNull String getName() {
        return "dat";
    }

    @Override
    public @NotNull
    @Nls(capitalization = Nls.Capitalization.Sentence) String getDescription() {
        return "dat";
    }

    @Override
    public @NotNull String getDefaultExtension() {
        return "dat";
    }

    @Override
    public @Nullable Icon getIcon() {
        return null;
    }

    @Override
    public boolean isBinary() {
        return true;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public @Nullable String getCharset(@NotNull VirtualFile virtualFile, byte [] bytes) {
        return null;
    }
}
