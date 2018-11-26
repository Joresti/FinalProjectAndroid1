package com.example.jores.finalprojectandroid.cbcnews;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jores.finalprojectandroid.R;

public class ArticleFragment extends Fragment {

    public boolean tablet = true;
    public NewsStory article;
    public boolean saved;

    NewsDatabaseHelper newsDatabaseHelper;
    SQLiteDatabase database;

    protected final static String TITLE = "title";
    protected final static String IMG_SRC = "imgSrc";
    protected final static String IMG_FILE_NAME = "imgFileName";
    protected final static String DESCRIPTION = "description";
    protected final static String LINK = "link";

    public ArticleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View screen = inflater.inflate(R.layout.article_fragment, container, false);

        Bundle bundle = this.getArguments();
        ImageView imgView = screen.findViewById(R.id.frag_img);
        TextView title = screen.findViewById(R.id.frag_title);
        TextView des = screen.findViewById(R.id.frag_des);
        TextView link = screen.findViewById(R.id.frag_link);
        final Button saveBtn = screen.findViewById(R.id.saveStory);
        final Button deleteBtn = screen.findViewById(R.id.deleteStory);

        Intent i = getActivity().getIntent();
        article = i.getParcelableExtra("Story");

        imgView.setImageBitmap(article.getImage());

        newsDatabaseHelper = NewsDatabaseHelper.getInstance(getActivity());
        database = newsDatabaseHelper.getWritableDatabase();

        Cursor c = database.rawQuery("SELECT * FROM SavedStories WHERE TITLE = ?", new String[]{article.getTitle()}, null);
        Log.d("FRAG COUNT", Integer.toString(c.getCount()));

        if (c.getCount() != 0) {
            saved = true;
        } else {
            saved = false;
        }

        title.setText(article.getTitle());
        des.setText(article.getDescription());
        link.setText(article.getLink());
        Log.d("ARTICLE FRAG", article.getLink());
        link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(article.getLink()));
                startActivity(i);

            }
        });


        if (saved) {
            Log.d("FRAG SAVE", "LOG");
            saveBtn.setEnabled(false);
            saveBtn.setText("ALREADY SAVED");
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    database.execSQL("DELETE FROM SavedStories WHERE TITLE = ?;", new String[]{article.getTitle()});
                    deleteBtn.setVisibility(View.INVISIBLE);
                    saveBtn.setEnabled(true);
                    saveBtn.setText("SAVE STORY");
                }
            });

        } else {
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("FRAG", article.toString());
                    Log.d("FRAG", article.getImgSrc());

                    ContentValues cv = new ContentValues();
                    cv.put(TITLE, article.getTitle());
                    cv.put(IMG_SRC, article.getImgSrc());
                    cv.put(IMG_FILE_NAME, article.getImageFileName());
                    cv.put(DESCRIPTION, article.getDescription());
                    cv.put(LINK, article.getLink());
                    Cursor c = database.rawQuery("SELECT * FROM SavedStories WHERE TITLE = ?", new String[]{article.getTitle()}, null);
                    if (c.getCount() == 0) {
                        database.insert("SavedStories", null, cv);
                    }
                    saveBtn.setEnabled(false);
                    saveBtn.setText("ALREADY SAVED");

                }
            });
        }


        return screen;
    }

}