package com.jun.akbonara;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class LoadImage extends AsyncTask<String, String, Bitmap> {

    ProgressDialog pDialog;
    Context context;
    String VideoID;
    Bitmap mBitmap;

    public LoadImage(Context context,String VideoID) {
        this.context = context;
        this.VideoID = VideoID;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        pDialog = new ProgressDialog(context);
//        pDialog.setMessage("이미지 로딩중입니다...");
//        pDialog.show();
    }

    protected Bitmap doInBackground(String... args) {
        try {
            mBitmap = BitmapFactory
                    .decodeStream((InputStream) new URL(args[0])
                            .getContent());

            ImageSaver imageSaver = new ImageSaver(context);
            imageSaver.setFileName(VideoID).save(mBitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBitmap;
    }

    protected void onPostExecute(Bitmap image) {

//        if (image != null) {
//            pDialog.dismiss();
//
//        } else {
//            pDialog.dismiss();
//            Toast.makeText(context, "이미지가 존재하지 않습니다.",
//                    Toast.LENGTH_SHORT).show();
//
//        }
    }
}