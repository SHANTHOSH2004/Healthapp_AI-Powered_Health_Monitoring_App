package com.example.heaalthapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;

public class VoiceInputHelper {
    private static final int SPEECH_REQUEST_CODE = 0;

    public static void startVoiceInput(Activity activity) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your health metrics...");

        try {
            activity.startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(activity, "Sorry, your device doesn't support speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static String processVoiceInput(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            return result.get(0);
        }
        return null;
    }
} 