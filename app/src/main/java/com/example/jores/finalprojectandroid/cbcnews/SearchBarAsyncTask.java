package com.example.jores.finalprojectandroid.cbcnews;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SearchBarAsyncTask extends AsyncTask<String,Integer,ArrayList<NewsStoryDTO> > {

    CBCNewsMain cbc;
    SQLiteDatabase database = NewsDatabaseHelper.getDatabase();
    Context ctx;
    String TAG = "SEARCH BAR ASYNC TASK";



    public SearchBarAsyncTask(Context ctx,CBCNewsMain cbc ){
        this.cbc = cbc;
        this.ctx=ctx;
    }

    @Override
    public ArrayList doInBackground(String...urls){
        ArrayList  arrayList = new ArrayList();
        String searchString = cbc.searchBar.getText().toString();
        Cursor cursor =null;
        if(cbc.breakingNews){
            cursor= database.rawQuery("SELECT * FROM NewsStories;", null );
        }
        else{
           cursor = database.rawQuery("SELECT * FROM SavedStories;", null );
        }

        while(cursor.moveToNext()) {
            String title = cursor.getString(0);

            if (title.toUpperCase().indexOf(searchString.toUpperCase()) !=-1){
                String imgSrc = cursor.getString(1);
                String imgFileName = cursor.getString(2);
                String description = cursor.getString(3);
                String link = cursor.getString(4);
                String date = cursor.getString(5);
                String author =cursor.getString(6);

                Log.d("IN title", title);
                Log.d("IN imgSrc", imgSrc);
                Log.d("IN imgFileName", imgFileName);
                Bitmap image = getBitmap(imgFileName, imgSrc);
                onProgressUpdate(new Integer[]{5});
                arrayList.add(new NewsStoryDTO(title,description,image, link, imgSrc, imgFileName, date, author));
            }
        }
        cursor.close();
        return arrayList;
    }

    @Override
    public void onProgressUpdate(Integer...i){

    }

    @Override
    public  void onPostExecute(ArrayList list){
        cbc.newsArrayList = list;
        cbc.newsAdapter.notifyDataSetChanged();
        cbc.pbar.setVisibility(View.INVISIBLE);

    }
    public Bitmap getBitmap(String imageStr, String imgUrl){
        Log.d("BITMAP" ,"IN BITMAP");

        Log.d("IMGURL", imgUrl);
        Bitmap image = null;
        if(fileExistence(imageStr)){
            Log.i(TAG, "Found file " + imageStr);
            FileInputStream fis = null;
            try {
                fis = ctx.openFileInput(imageStr);
            } catch (FileNotFoundException e) {
                Log.d("FILE NOT FOUND", "PROBLEM");
                e.printStackTrace();
            }
            image = BitmapFactory.decodeStream(fis);
            try {
                fis.close();
            }
            catch (IOException e){e.getLocalizedMessage();}
        }else {
            Log.i(TAG, "Did not find file.  Downloading " + imageStr);
            try {
                image = HttpUtils.getImage(imgUrl);
                FileOutputStream outputStream = ctx.openFileOutput(imageStr, Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("IOEXCEP", "PROBLEM");
                e.getLocalizedMessage();
            }
        }
        Log.d("BITMAP" ,"OUT BITMAP");
        return image;
    }

    public boolean fileExistence(String fname){
        File file = ctx.getFileStreamPath(fname);
        return file.exists();
    }



}
