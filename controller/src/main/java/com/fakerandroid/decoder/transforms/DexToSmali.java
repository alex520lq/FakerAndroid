package com.fakerandroid.decoder.transforms;
import com.fakerandroid.decoder.api.AndroidProject;
import com.fakerandroid.decoder.smali.DexToSmaliException;
import com.fakerandroid.decoder.smali.SmaliDecoder;
import com.fakerandroid.decoder.api.Apk;
import com.fakerandroid.decoder.pipeline.Transform;
import com.fakerandroid.decoder.pipeline.TransformInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class DexToSmali extends Transform {

    public DexToSmali(Apk apk, AndroidProject androidProject) {
        super(apk, androidProject);
    }
    @Override
    public boolean transform(TransformInvocation transformInvocation) {
        transformInvocation.callBack("Translating dexes to smali files....");
        List<String> names = (List<String>) androidProject.getIntermediate(AndroidProject.INTERMEDIATE_DEX_NAMES);
        AndroidProject.ManifestInfo s = (AndroidProject.ManifestInfo) androidProject.getIntermediate(AndroidProject.INTERMEDIATE_MANIFESTINFO);
        for (String name:names) {
            try {
                SmaliDecoder.decode(apk.getApkFile(),androidProject.getSmali(),name,true,Integer.valueOf(s.getMinSdkVersion()));
            } catch (DexToSmaliException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
