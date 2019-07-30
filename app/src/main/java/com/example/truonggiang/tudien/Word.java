package com.example.truonggiang.tudien;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Word implements Serializable, Parcelable {
    private int id;
    private String name;
    private String mean;
    private String example;

    public Word(int id, String name, String mean, String example) {
        this.id = id;
        this.name = name;
        this.mean = mean;
        this.example = example;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMean() {
        return mean;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }


    protected Word(Parcel in) {
        id = in.readInt();
        name = in.readString();
        mean = in.readString();
        example = in.readString();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(mean);
        dest.writeString(example);
    }
}
