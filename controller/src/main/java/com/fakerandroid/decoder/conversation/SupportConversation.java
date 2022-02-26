package com.fakerandroid.decoder.conversation;

import com.fakerandroid.decoder.api.AndroidProject;
import com.fakerandroid.decoder.api.Apk;
import com.fakerandroid.decoder.pipeline.Context;
import com.fakerandroid.decoder.pipeline.Conversation;
import com.fakerandroid.decoder.transforms.DexToJar;
import com.fakerandroid.decoder.transforms.DexToSmali;
import com.fakerandroid.decoder.transforms.Il2cppDumper;
import com.fakerandroid.decoder.transforms.ResourceProcesser;

public class SupportConversation extends Conversation {

    public SupportConversation(Context context) {
        super(context);
    }
    @Override
    public void converse() {
        Apk apk = context.getApks().iterator().next();
        AndroidProject androidProject = context.getAndroidProject();

        context.getTransformManager().addTransform(new ResourceProcesser(apk,androidProject));

        context.getTransformManager().addTransform(new DexToJar(apk,androidProject));

        context.getTransformManager().addTransform(new DexToSmali(apk,androidProject));

        context.getTransformManager().addTransform(new Il2cppDumper(apk,androidProject));
    }
}
