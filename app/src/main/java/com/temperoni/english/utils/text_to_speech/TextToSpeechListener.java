package com.temperoni.english.utils.text_to_speech;

/**
 * Created by COCO on 06/03/2016.
 */
public interface TextToSpeechListener {

    public void onInitSucceeded();
    public void onInitFailed();
    public void onCompleted();

}
