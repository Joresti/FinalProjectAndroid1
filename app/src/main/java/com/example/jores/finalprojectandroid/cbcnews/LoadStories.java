package com.example.jores.finalprojectandroid.cbcnews;
import android.content.Context;
import android.database.Cursor;
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

public class LoadStories extends AsyncTask<Cursor, Integer, ArrayList<NewsStory> > {

    String TAG = "LOAD STORY";

    CBCNewsMain cbc;

    Context ctx;


    LoadStories(Context ctx, CBCNewsMain  cbc){
        this.ctx=ctx;

        this.cbc=cbc;
    }

    @Override
    public ArrayList doInBackground(Cursor...urls){
        ArrayList<NewsStory> arrayList = new ArrayList<>();

        Cursor cursor =urls[0];
        Log.d("LOAD", Integer.toString(cursor.getCount()));
        while(cursor.moveToNext()) {
            String title = cursor.getString(0);
            String imgSrc = cursor.getString(1);
            String imgFileName = cursor.getString(2);
            String description = cursor.getString(3);
            String link = cursor.getString(4);

            Log.d("IN title", title);
            Log.d("IN imgSrc", imgSrc);
            Log.d("IN imgFileName", imgFileName);
            Bitmap image = getBitmap(imgFileName, imgSrc);
            onProgressUpdate(new Integer[]{5});

            arrayList.add(new NewsStory(title,description,image, link, imgSrc, imgFileName));
        }
        cursor.close();
        return arrayList;

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

    @Override
    protected void onProgressUpdate(Integer...list){
        super.onProgressUpdate(list);
        cbc.pbar.setProgress(cbc.pbar.getProgress()+list[0]);

    }
    @Override
    protected void onPostExecute(ArrayList result){
        super.onPostExecute(result);
        cbc.newsArrayList = result;
        cbc.newsAdapter.notifyDataSetChanged();
        cbc.pbar.setVisibility(View.INVISIBLE);

    }



    public boolean fileExistence(String fname){
        File file = ctx.getFileStreamPath(fname);
        return file.exists();
    }

}