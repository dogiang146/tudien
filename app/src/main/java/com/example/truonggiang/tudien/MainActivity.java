package com.example.truonggiang.tudien;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Database database;
    ListView lvWord;
    ArrayList<Word> arrWord;
    WordAdapter adapter;
    ImageButton btSearch, btDelete;
    EditText txtName, txtSearch, txtFrom, txtTo;
    Button btChose;
    TextToSpeech t1;
    ImageView imgVolume;
    ProgressBar prokbar;
    Random rd = new Random();
    Boolean run = false;
    int time = 2000;
    int i;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();

        arrWord = new ArrayList<>();

        adapter = new WordAdapter(this, R.layout.list_word, arrWord);
        lvWord.setAdapter(adapter);

        // tạo database
        database = new Database(this, "Tudien.sqlite", null, 1);
        // tạo bảng công việc
        database.QueryData("CREATE TABLE IF NOT EXISTS Library(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name NVARCHAR(200), Mean NVARCHAR(200), Example NVARCHAR(1000) )");
        // insert data to database


        // lấy dữ liệu từ database ra
        getDataWord();
//        Toast.makeText(this, ""+arrWord.size(), Toast.LENGTH_SHORT).show();

        // khi ấn vào button search
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = txtSearch.getText().toString();
                if (search.equals("")) {
                    getDataWord();
                } else {
                    SearchWord(search);
                    Toast.makeText(MainActivity.this, "検索終", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // khi ấn vào button delete search
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSearch.setText("");
            }
        });

        // chọn những từ mới nhất
        btChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = txtFrom.getText().toString();
                String to = txtTo.getText().toString();
                if(from.equals("") && to.equals("")){
                    getDataWord();
                }else {
                    if (from.equals("")) {
                        from = "" + 0;
                        SearchWordByChose(Integer.parseInt(from), Integer.parseInt(to));
                    }
                    if (to.equals("")) {
                        to = "" + 10000;
                        SearchWordByChose(Integer.parseInt(from), Integer.parseInt(to));
                    } else {
                        SearchWordByChose(Integer.parseInt(from), Integer.parseInt(to));
                        Toast.makeText(MainActivity.this, "検索終", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        // gọi texttospeech
        getTTP();

// khi ấn vào nghe tất cả

        imgVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.shuffle(arrWord);
                if (run == false) {
                    run = true;
                } else {
                    run = false;
                }
                if (arrWord.size() == 0) {
                    Toast.makeText(MainActivity.this, "Not data", Toast.LENGTH_SHORT).show();
                } else {
                    for (i = 0; i <= arrWord.size(); i++) {
                        prokbar.setMax(arrWord.size());
                        countDownTimer = new CountDownTimer(arrWord.size() * time + time, time) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if (run == true) {
                                    imgVolume.setImageResource(R.drawable.pause);
                                    i = prokbar.getProgress();
                                    if (i >= prokbar.getMax()) {
                                        i = 0;
                                    }
                                    prokbar.setProgress(i + 1);
                                    toSpeak(arrWord.get(i).getName().toString());
                                } else {
                                    imgVolume.setImageResource(R.drawable.volume);
                                }
                            }

                            @Override
                            public void onFinish() {
                                this.start();
                            }
                        };
                        countDownTimer.start();
                    }
                }
            }
        });

// sự kiện listview khi giữ lâu
        lvWord.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.inflate(R.menu.menu_popup);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.item_edit) {
                            Update(arrWord.get(position).getId(), arrWord.get(position).getName(), arrWord.get(position).getMean(), arrWord.get(position).getExample());
                        }
                        if (item.getItemId() == R.id.item_delete) {
                            Delete(arrWord.get(position).getName(), arrWord.get(position).getId());
                        }
                        if (item.getItemId() == R.id.item_example) {
                            View(arrWord.get(position).getName(), arrWord.get(position).getMean(), arrWord.get(position).getExample());
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }

    // chọn ngôn ngữ để nói tts
    public void getTTP() {
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.JAPAN);
                }
            }
        });
    }

    // lấy toàn bộ dữ liệu của list
    public void getDataWord() {
        Cursor dataWord = database.GetData("SELECT * FROM Library");
        arrWord.clear();
        while (dataWord.moveToNext()) {
            int id = dataWord.getInt(0);
            String name = dataWord.getString(1);
            String mean = dataWord.getString(2);
            String example = dataWord.getString(3);
            arrWord.add(new Word(id, name, mean, example));
        }
        adapter.notifyDataSetChanged();
    }


    private void SearchWord(String search) {
        Cursor dataWord = database.GetData("select * from Library Where Name Like '%" + search.trim() + "%' OR Mean Like '%" + search.trim() + "%'");
        arrWord.clear();
        while (dataWord.moveToNext()) {
            int id = dataWord.getInt(0);
            String name = dataWord.getString(1);
            String mean = dataWord.getString(2);
            String example = dataWord.getString(3);
            arrWord.add(new Word(id, name, mean, example));
        }
        adapter.notifyDataSetChanged();
    }

    private void SearchWordByChose(int from, int to) {
        Cursor dataWord = database.GetData("select * from Library order by id asc limit " + from + "," + to + "");
        arrWord.clear();
        while (dataWord.moveToNext()) {
            int id = dataWord.getInt(0);
            String name = dataWord.getString(1);
            String mean = dataWord.getString(2);
            String example = dataWord.getString(3);
            arrWord.add(new Word(id, name, mean, example));
        }
        adapter.notifyDataSetChanged();
    }

    public void Anhxa() {
        lvWord = findViewById(R.id.lvWord);
        btSearch = findViewById(R.id.btSearch);
        txtSearch = findViewById(R.id.txtSearch);
        imgVolume = findViewById(R.id.imgVolume);
        prokbar = findViewById(R.id.proBar);
        txtFrom = findViewById(R.id.edFrom);
        txtTo = findViewById(R.id.edTo);
        btChose = findViewById(R.id.btChose);
        btDelete = findViewById(R.id.btDelete);
    }

    // tạo nút menu add công việc
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_word, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // khi mà ấn vào nút add công việc
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuAdd) {
            DialogAddword();
        }
        if (item.getItemId() == R.id.menuLearn) {
            DialogLearnword();
        }
        return super.onOptionsItemSelected(item);
    }

    // click menu learn
    public void DialogLearnword() {
        Intent intent = new Intent(MainActivity.this, learn_word.class);
        intent.putExtra("list", arrWord);
        startActivity(intent);
    }

    // gọi dialog để add thêm từ vựng
    public void DialogAddword() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addword);

        final EditText edName = dialog.findViewById(R.id.txtTu);
        final EditText edMean = dialog.findViewById(R.id.txtImi);
        final EditText edExample = dialog.findViewById(R.id.txtRei);
        Button btnThem = dialog.findViewById(R.id.btThem);
        Button btnHuy = dialog.findViewById(R.id.btHuy);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString();
                String mean = edMean.getText().toString();
                String example = edExample.getText().toString();
                if (example == null) {
                    edExample.setText("");
                }
                if (name.equals("")) {
                    Toast.makeText(MainActivity.this, " 言葉を入力しなければならない !!!", Toast.LENGTH_SHORT).show();
                } else if (mean.equals("")) {
                    Toast.makeText(MainActivity.this, "　意味を入力しなければならない !!!", Toast.LENGTH_SHORT).show();
                } else {
                    database.QueryData("insert into Library values(null,'" + name + "','" + mean + "','" + example + "')");
                    dialog.dismiss();
                    getDataWord();
                }
                Toast.makeText(MainActivity.this, " 追加終 !!!", Toast.LENGTH_SHORT).show();

            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //delete word
    public void Delete(final String name, final int id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("この字 " + name + " を消除 したいですか。 ");
        dialog.setPositiveButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("delete from Library Where id = '" + id + "'");
                Toast.makeText(MainActivity.this, "消除しました。", Toast.LENGTH_SHORT).show();
                getDataWord();
            }
        });
        dialog.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //Update word
    public void Update(final int id, final String name, final String mean, final String example) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_updateword);

        final EditText edName = dialog.findViewById(R.id.txtudKotoba);
        final EditText edMean = dialog.findViewById(R.id.txtudImi);
        final EditText edExample = dialog.findViewById(R.id.txtRei);

        Button btCapNhat = dialog.findViewById(R.id.btCapNhat);
        Button btHuy = dialog.findViewById(R.id.btHuy);

        edName.setText(name);
        edMean.setText(mean);
        edExample.setText(example);
        btCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString();
                String mean = edMean.getText().toString();
                String example = edExample.getText().toString();
                database.QueryData("update Library set Name='" + name + "', Mean='" + mean + "', Example='" + example + "' Where id='" + id + "'");
                Toast.makeText(MainActivity.this, "データを更新させました!!! ", Toast.LENGTH_SHORT).show();
//                getDataWord();
                dialog.dismiss();
            }
        });
        btHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    // dialog example
    public void View(String name, String mean, String example) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_example);

        TextView edName = dialog.findViewById(R.id.txtTu);
        TextView edMean = dialog.findViewById(R.id.txtYnghia);
        TextView edExample = dialog.findViewById(R.id.txtCau);

        edName.setText(name);
        edMean.setText(mean);
        edExample.setText(example);


        dialog.show();
    }


    public void toSpeak(String text) {
        t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    //volume
    public void Volume(ImageView b1, final String toSpeak) {
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                toSpeak(toSpeak);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void pause() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}
