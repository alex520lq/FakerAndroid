/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fakerandroid.as;

import com.google.common.collect.Maps;
import com.intellij.ide.actions.OpenProjectFileChooserDescriptor;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.IconUtil;
import java.util.Map;
import javax.swing.Icon;
/**
 * OpenProjectFileChooserDescriptorWithAsyncIcon is a customized open project file chooser with an
 * icon cache and async icon loading. The icon is chosen by a file type (extension, file, or
 * directory) by {@link IconUtil#getIcon(VirtualFile, int, Project)} then asynchronously be updated
 * to {@link OpenProjectFileChooserDescriptor#getIcon(VirtualFile)}.
 * <p>This class is a workaround solution for the issue b/37099520. Once the issue is addressed in
 * the upstream (IntelliJ open API), this class can be removed.
 */
public class OpenProjectFileChooserDescriptorWithAsyncIcon extends OpenProjectFileChooserDescriptor implements Disposable {

  private final Map<VirtualFile, Icon> myIconCache = Maps.newConcurrentMap();

  public OpenProjectFileChooserDescriptorWithAsyncIcon() {
    super(true);
  }

  @Override
  public void dispose() {
    myIconCache.clear();
  }
}
