package com.example.jores.finalprojectandroid.cbcnews;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jores.finalprojectandroid.R;

/**
 * Class extends fragement to display news story, provides options for saving & deleting story
 */

public class ArticleFragment extends Fragment {

    public boolean tablet = true;
    public NewsStoryDTO article;
    public boolean saved;

    NewsDatabaseHelper newsDatabaseHelper;
    SQLiteDatabase database;

    protected final static String TITLE = "title";
    protected final static String IMG_SRC = "imgSrc";
    protected final static String IMG_FILE_NAME = "imgFileName";
    protected final static String DESCRIPTION = "description";
    protected final static String LINK = "link";
    protected final static String DATE = "date";
    protected final static String AUTHOR = "author";

    public ArticleFragment() {
    }

    /**
     * Overriding onCreateView - finding the views in the fragment seting their content & onClickhandlers
     * @param inflater object used to inflate layout
     * @param container
     * @param savedInstanceState arguments
     * @return View - returns the constructed fragment for displaying
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View screen = inflater.inflate(R.layout.article_fragment, container, false);

        Bundle bundle = this.getArguments();
        ImageView imgView = screen.findViewById(R.id.frag_img);
        TextView title = screen.findViewById(R.id.frag_title);
        TextView des = screen.findViewById(R.id.frag_des);
        Button link = screen.findViewById(R.id.frag_link);
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
        link.setText("Read full article");
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

            deleteBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.INVISIBLE);



        } else {
            saveBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);

        }
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues cv = new ContentValues();
                cv.put(TITLE, article.getTitle());
                cv.put(IMG_SRC, article.getImgSrc());
                cv.put(IMG_FILE_NAME, article.getImageFileName());
                cv.put(DESCRIPTION, article.getDescription());
                cv.put(LINK, article.getLink());
                cv.put(DATE, article.getPubDate());
                Log.d("SAVED", article.getPubDate());
                cv.put(AUTHOR, article.getAuthor());
                Cursor c = database.rawQuery("SELECT * FROM SavedStories WHERE TITLE = ?", new String[]{article.getTitle()}, null);
                if (c.getCount() == 0) {
                    database.insert("SavedStories", null, cv);
                    Toast.makeText(getContext(), "Saving story!", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getContext(), "Saving story!", Toast.LENGTH_SHORT).show();
                saveBtn.setVisibility(View.INVISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);




            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                final View newView = inflater.inflate(R.layout.cbc_custom_dialog, null);

                builder.setView(newView);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.execSQL("DELETE FROM SavedStories WHERE TITLE = ?;", new String[]{article.getTitle()});
                        deleteBtn.setVisibility(View.INVISIBLE);

                        deleteBtn.setVisibility(View.INVISIBLE);
                        saveBtn.setVisibility(View.VISIBLE);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog1 = builder.create();
                dialog1.show();

                Log.d("FRAG", article.toString());
                Log.d("FRAG", article.getImgSrc());

            }
        });


        return screen;
    }

}