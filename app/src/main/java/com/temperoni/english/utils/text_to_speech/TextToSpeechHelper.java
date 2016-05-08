package com.temperoni.english.utils.text_to_speech;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

/**
 * Created by COCO on 06/03/2016.
 */
public class TextToSpeechHelper implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private Context context;
    private TextToSpeechListener listener;
    private String text;

    public TextToSpeechHelper(Context context, final TextToSpeechListener listener){

        this.context = context;
        this.listener = listener;
        textToSpeech = new TextToSpeech(context, this);
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                listener.onCompleted();
            }

            @Override
            public void onError(String utteranceId) {

            }
        });
        text = "";

    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS)
            listener.onInitSucceeded();
        else listener.onInitFailed();
    }

    public void speak(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "");
        }
        else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

    }

    public void shutdown(){ textToSpeech.shutdown(); }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSpeaking(){ return textToSpeech.isSpeaking(); }

}
