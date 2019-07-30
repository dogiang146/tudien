package com.example.truonggiang.tudien;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentFilltext extends Fragment {
    TextView txtName;
    EditText edMean,edNumber;
    Button btPrev, btFinish, btNext,btOk;
    ArrayList<Word> arr;
    String[] arrlist;
    ImageButton imgChange;
    int currentQues = 0;
    int userPoint = 0;


    boolean change = false;

    public FragmentFilltext() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filltext, container, false);
        change = false;

        txtName = view.findViewById(R.id.txtText);
        edMean = view.findViewById(R.id.edMean);
        btPrev = view.findViewById(R.id.buttonPrev);
        btFinish = view.findViewById(R.id.buttonEnd);
        btNext = view.findViewById(R.id.buttonNext);
        imgChange = view.findViewById(R.id.imgChange);

        Bundle extras = getArguments();
        arr = extras.getParcelableArrayList("key");
        Collections.shuffle(arr);

        arrlist = new String[arr.size()];
        LoadQuestion();
        imgChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change == false) {
                    change = true;
                    LoadQuestion();
                } else {
                    change = false;
                    LoadQuestion();
                }
            }
        });

        btPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arr.size() == 0) {
                    Toast.makeText(getActivity(), "データがない", Toast.LENGTH_SHORT).show();
                }
                addText(currentQues, arrlist);
                if (currentQues <= 0) {
                    currentQues = arr.size() - 1;
                } else {
                    currentQues--;
                }
                LoadQuestion();
            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addText(currentQues, arrlist);
                if (arr.size() == 0) {
                    Toast.makeText(getActivity(), "データがない", Toast.LENGTH_SHORT).show();
                }
                if (currentQues >= arr.size() - 1) {
                    currentQues = 0;
                } else {
                    currentQues++;
                }
                LoadQuestion();
            }
        });

        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addText(currentQues, arrlist);
                if (arr.size() == 0) {
                    Toast.makeText(getActivity(), "データがない", Toast.LENGTH_SHORT).show();
                }
                if (change == false) {
                    for (int i = 0; i < arrlist.length; i++) {
                        if (arrlist[i] == null) {
                            arrlist[i] = "";
                        }
                        if (arrlist[i].trim().equalsIgnoreCase(arr.get(i).getMean().trim())) {
                            userPoint++;
                        }
                    }
                } else {
                    for (int i = 0; i < arrlist.length; i++) {
                        if (arrlist[i] == null) {
                            arrlist[i] = "";
                        }
                        if (arrlist[i].trim().equalsIgnoreCase(arr.get(i).getName().trim())) {
                            userPoint++;
                        }
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Result");
                builder.setMessage("Your result: " + userPoint + "/" + arr.size());
                AlertDialog dialog = builder.create();
                dialog.show();
                resetApp();
            }
        });


        return view;
    }

    public void resetApp() {
        userPoint = 0;
        for (int i = 0; i < arr.size(); i++) {
            arrlist[i] = null;
        }
        currentQues = 0;
        Collections.shuffle(arr);
        LoadQuestion();
    }

    public void LoadQuestion() {
        if (arr.size() != 0) {
            if (change == false) {
                txtName.setText(currentQues + 1 + ". " + arr.get(currentQues).getName().toString());
                if (arrlist[currentQues] == null) {
                    edMean.setText("");
                } else {
                    edMean.setText(arrlist[currentQues]);
                }
            } else {
                txtName.setText(currentQues + 1 + ". " + arr.get(currentQues).getMean().toString());
                if (arrlist[currentQues] == null) {
                    edMean.setText("");
                } else {
                    edMean.setText(arrlist[currentQues]);
                }
            }
        } else {
            Toast.makeText(getActivity(), "データがない", Toast.LENGTH_SHORT).show();
        }
    }

    public void addText(int currentQues, String[] arrlist) {
        if (arr.size() == 0) {
            Toast.makeText(getActivity(), "データがない", Toast.LENGTH_SHORT).show();
        } else {
            if (edMean.getText() != null)
                arrlist[currentQues] = edMean.getText().toString();
            else {
                arrlist[currentQues] = "";
            }
        }
    }
}
