package com.example.truonggiang.tudien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class WordAdapter extends BaseAdapter {
    // để từ class này sang class kia gọi được thì phải dùng đúng tên của class đó
    private MainActivity context;
    private int layout;
    private List<Word> wordList;

    public WordAdapter() {
    }

    public WordAdapter(MainActivity context, int layout, List<Word> wordList) {
        this.context = context;
        this.layout = layout;
        this.wordList = wordList;
    }

    @Override
    public int getCount() {
        return wordList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView txtName;
        TextView txtMean;
        ImageView imgDeltete, imgUpdate, imgVolume;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtMean = (TextView) convertView.findViewById(R.id.txtMean);

            holder.imgDeltete = (ImageView) convertView.findViewById(R.id.btDelete);
            holder.imgUpdate = (ImageView) convertView.findViewById(R.id.btEdit);
            holder.imgVolume = (ImageView) convertView.findViewById(R.id.btVolume);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Word word = wordList.get(position);

        holder.txtName.setText(word.getName());
        holder.txtMean.setText(word.getMean());

        // gọi sự kiện khi ấn click hình X
        holder.imgDeltete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.Delete(word.getName(), word.getId());
            }
        });
        // gọi sự kiện khi ấn click hình bút chì
        holder.imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.Update(word.getId(), word.getName(), word.getMean(),word.getExample());
            }
        });

        // gọi sự kiện khi ấn click hình volume
        holder.imgVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = holder.txtName.getText().toString();
                context.Volume(holder.imgVolume, text);
            }
        });
        return convertView;

    }

}
