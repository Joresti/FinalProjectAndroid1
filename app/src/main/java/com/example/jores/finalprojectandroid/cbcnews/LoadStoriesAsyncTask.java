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

/**
 * Class to load stories from the Database
 */
public class LoadStoriesAsyncTask extends AsyncTask<Cursor, Integer, ArrayList<NewsStoryDTO> > {

    String TAG = "LOAD STORY";

    CBCNewsMain cbc;

    Context ctx;

    /**
     * Constructor
     * @param ctx Context
     * @param cbc CBCNewsMain
     */

    LoadStoriesAsyncTask(Context ctx, CBCNewsMain  cbc){
        this.ctx=ctx;

        this.cbc=cbc;
    }

    /**
     * Overriding the doInBackground.  Adding functionality:
     * - Querying database for NewsStories
     * - Looping through the resulting Cursor
     * - Constructing an ArrayList with each database object
     * - Replacing the ArrayList in CBCNewsMain with the newly constructed one
     *
     * @param params Array of Cursor objects
     * @return ArrayList - An ArrayList of NewsStoryDTOs
     */

    @Override
    public ArrayList doInBackground(Cursor...params){
        ArrayList<NewsStoryDTO> arrayList = new ArrayList<>();

        Cursor cursor =params[0];
        Log.d("LOAD", Integer.toString(cursor.getCount()));
        while(cursor.moveToNext()) {
            String title = cursor.getString(0);
            String imgSrc = cursor.getString(1);
            String imgFileName = cursor.getString(2);
            String description = cursor.getString(3);
            String link = cursor.getString(4);
            String date = cursor.getString(5);
            String author = cursor.getString(6);

            Log.d("IN title", title);
            Log.d("IN imgSrc", imgSrc);
            Log.d("IN imgFileName", imgFileName);
            Bitmap image = getBitmap(imgFileName, imgSrc);
            onProgressUpdate(new Integer[]{5});

            arrayList.add(new NewsStoryDTO(title,description,image, link, imgSrc, imgFileName, date, author));
        }
        cursor.close();
        return arrayList;

    }

    /**
     * Function to handle bitmap objects for each NewsStoryDTO in the new ArrayList
     * @param imageStr String representing the filename
     * @param imgUrl String representing the url address
     * @return Bitmap - resulting bitmap object -to be saved in a NewsStoryDTO object
     */

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

    /**
     * Updates progressbar when called
     * @param list Integer Array - number to update progresbar with
     */
    @Override
    protected void onProgressUpdate(Integer...list){
        super.onProgressUpdate(list);
        cbc.pbar.setProgress(cbc.pbar.getProgress()+list[0]);

    }

    /**
     * Replaces ArrayList in CBCNewsMain with the one constructed in this class
     * @param result ArrayList
     */
    @Override
    protected void onPostExecute(ArrayList result){

        cbc.newsArrayList = result;
        cbc.newsAdapter.notifyDataSetChanged();
        cbc.pbar.setVisibility(View.INVISIBLE);

    }

    /**
     * Function to help determine if we already have an image file of the same name stored.
     * @param fname String filename
     * @return boolean
     */
    public boolean fileExistence(String fname){
        File file = ctx.getFileStreamPath(fname);
        return file.exists();
    }

}