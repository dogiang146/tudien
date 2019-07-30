package com.example.truonggiang.tudien;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentMultichoice extends Fragment {
    TextView txtText, txtResult;
    ArrayList<Word> arr;
    ArrayList<String> choice;
    int currentQues = 0;
    int userPoint = 0;
    int[] arrlist;
    boolean isQuesLoad = true;
    RadioGroup radioGroup;
    RadioButton rd1, rd2, rd3, rd4;
    Button btPrev, btNext;
    ImageButton img;
    TextToSpeech t1;

    public FragmentMultichoice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multichoice, container, false);

        txtText = view.findViewById(R.id.txtText);
        radioGroup = view.findViewById(R.id.radioGroup);
        btPrev = view.findViewById(R.id.buttonPrev);
        btNext = view.findViewById(R.id.buttonNext);
        txtResult = view.findViewById(R.id.txtResult);
        img = view.findViewById(R.id.imgBt);

        rd1 = view.findViewById(R.id.radioButton1);
        rd2 = view.findViewById(R.id.radioButton2);
        rd3 = view.findViewById(R.id.radioButton3);
        rd4 = view.findViewById(R.id.radioButton4);

        Bundle extras = getArguments();
        arr = extras.getParcelableArrayList("key");
        Collections.shuffle(arr);

        choice = new ArrayList<String>();
        arrlist = new int[arr.size()];

        LoadQuestion();

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arr.size() == 0) {
                    Toast.makeText(getActivity(), "データがない", Toast.LENGTH_SHORT).show();
                } else {
                    if (rd1.isChecked() && rd1.getText().equals(arr.get(currentQues).getMean())) {
                        userPoint++;
                        txtResult.setText("点: " + userPoint);
                    } else if (rd2.isChecked() && rd2.getText().equals(arr.get(currentQues).getMean())) {
                        userPoint++;
                        txtResult.setText("点: " + userPoint);

                    } else if (rd3.isChecked() && rd3.getText().equals(arr.get(currentQues).getMean())) {
                        userPoint++;
                        txtResult.setText("点: " + userPoint);
                    } else if (rd4.isChecked() && rd4.getText().equals(arr.get(currentQues).getMean())) {
                        userPoint++;
                        txtResult.setText("点: " + userPoint);
                    }
                    if (currentQues >= arr.size() - 1) {
                        currentQues = 0;
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("結果");
                        builder.setMessage("君の点: " + userPoint + "/" + arr.size());
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        userPoint = 0;
                        txtResult.setText("" + userPoint);
                    } else {
                        currentQues++;
                    }
                    LoadQuestion();
                }
            }
        });
        getTTP();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = arr.get(currentQues).getName().toString();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        return view;
    }

    private void LoadQuestion() {
        if (arr.size() == 0) {
            Toast.makeText(getActivity(), "データがない", Toast.LENGTH_SHORT).show();
        } else {
            txtText.setText(currentQues + 1 + ". " + arr.get(currentQues).getName());
            String answer = arr.get(currentQues).getMean();
            choice.add(answer);

            Random rd = new Random();
            int number;
            for (int i = 0; i < 3; i++) {
                number = rd.nextInt(arr.size());
                choice.add(arr.get(number).getMean());
            }
            Collections.shuffle(choice);

            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                int j = 0;
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setText(choice.get(j).toString());
                choice.remove(j);
                if (arrlist[currentQues] == i) {
                    radioButton.setChecked(true);
                    arrlist[currentQues] = i;
                }
            }
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

