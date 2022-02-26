package com.fakerandroid.as;

import com.intellij.openapi.progress.Task;
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator;
public class FakerAndroidProgressIndicator extends BackgroundableProcessIndicator {
    public FakerAndroidProgressIndicator(Task.Backgroundable task) {
        super(task);
    }
}
