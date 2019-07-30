package com.example.truonggiang.tudien;


import android.os.Bundle;
import android.app.Fragment;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class FragmentFlashcard extends android.support.v4.app.Fragment {

    ArrayList<Word> arr;
    Button btPrev, btNext;
    Button btText;
    int currentQues = 0;
    boolean mean = false;
    TextView tvCurrent, tvTotal;
    SeekBar sb;
    ImageButton img;
    TextToSpeech t1;

    public FragmentFlashcard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flashcard, container, false);

        Bundle extras = getArguments();
        arr = extras.getParcelableArrayList("key");
        Collections.shuffle(arr);

        btPrev = view.findViewById(R.id.btPre);
        btNext = view.findViewById(R.id.btNext);
        btText = view.findViewById(R.id.btText);
        tvCurrent = view.findViewById(R.id.current);
        tvTotal = view.findViewById(R.id.total);
        sb = view.findViewById(R.id.seekBar);
        img = view.findViewById(R.id.imageButton);
//        Toast.makeText(getActivity(), "" + arr.size(), Toast.LENGTH_SHORT).show();
        sb.setMax(arr.size());
        sb.setProgress(1);
        tvTotal.setText("" + arr.size());
        tvCurrent.setText("" + 1);
        LoadQuestion();


        getTTP();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = arr.get(currentQues).getName().toString();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arr.size() == 0) {
                    Toast.makeText(getActivity(), "データがない", Toast.LENGTH_SHORT).show();
                }
                if (currentQues >= arr.size() - 1) {
                    currentQues = 0;
                } else {
                    currentQues++;
                }
                int current = currentQues + 1;
                tvCurrent.setText("" + current);
                sb.setProgress(current + 1);
                LoadQuestion();
            }
        });

        btPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arr.size() == 0) {
                    Toast.makeText(getActivity(), "データがない", Toast.LENGTH_SHORT).show();
                } else {
                    if (currentQues <= 0) {
                        currentQues = arr.size() - 1;
                    } else {
                        currentQues--;
                    }
                    int current = currentQues + 1;
                    tvCurrent.setText("" + current);
                    sb.setProgress(current - 1);
                    LoadQuestion();
                }
            }
        });

        btText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arr.size() == 0) {
                    Toast.makeText(getActivity(), "データがない", Toast.LENGTH_SHORT).show();
                } else {
                    if (mean == false) {
                        btText.setText(arr.get(currentQues).getName().toString());
                        mean = true;
                    } else {
                        btText.setText(arr.get(currentQues).getMean().toString());
                        mean = false;
                    }
                }
            }
        });
        return view;

    }


    public void LoadQuestion() {
        if (arr.size() != 0) {
            if (mean == false) {
                btText.setText(arr.get(currentQues).getMean().toString());
            } else {
                btText.setText(arr.get(currentQues).getName().toString());
            }
        } else {
            Toast.makeText(getActivity(), "データがない", Toast.LENGTH_SHORT).show();
        }
    }
    public void getTTP() {
        t1 = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.JAPAN);
                }
            }
        });
    }
}
