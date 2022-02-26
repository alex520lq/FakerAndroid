/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fakerandroid.as.editor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.impl.ArchiveHandler;
import com.intellij.openapi.vfs.newvfs.ArchiveFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DatFileSystem extends ArchiveFileSystem {
  public static final String PROTOCOL = "dat";
  public static DatFileSystem getInstance() {
    return (DatFileSystem) VirtualFileManager.getInstance().getFileSystem(PROTOCOL);
  }

  @Override
  protected @NotNull String extractLocalPath(@NotNull String s) {
    return null;
  }

  @Override
  protected @NotNull String composeRootPath(@NotNull String s) {
    return null;
  }

  @Override
  protected @NotNull ArchiveHandler getHandler(@NotNull VirtualFile virtualFile) {
    return null;
  }

  @Override
  protected @NotNull String extractRootPath(@NotNull String s) {
    return null;
  }

  @Override
  public @Nullable VirtualFile findFileByPathIfCached(@NotNull String s) {
    return null;
  }

  @Override
  public @NotNull String getProtocol() {
    return null;
  }

  @Override
  public @Nullable VirtualFile findFileByPath(@NotNull String s) {
    return null;
  }

  @Override
  public void refresh(boolean b) {
  }

  @Override
  public @Nullable VirtualFile refreshAndFindFileByPath(@NotNull String s) {
    return null;
  }
}
