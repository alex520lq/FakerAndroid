package com.fakerandroid.decoder.pipeline;


import java.util.ArrayList;
import java.util.List;

public class TransformManager {
     TransformInvocation transformInvocation;
     public TransformManager(TransformInvocation transformInvocation){
          this.transformInvocation = transformInvocation;
     }
     private final List<TransformStream> streams = new ArrayList<>();

     private final List<Transform> transforms = new ArrayList<>();

     public void addStream(TransformStream stream) {
          streams.add(stream);
     }

     public List<TransformStream> getStreams() {
          return streams;
     }

     public void addTransform(Transform transform){
          transforms.add(transform);
     }
     public void action() {
        for (Transform transform:transforms){
             if(!transform.transform(transformInvocation)) {
                  transformInvocation.callBack("finish on "+transform.getClass().getName());
                  return;
             }
        }
     }
}
