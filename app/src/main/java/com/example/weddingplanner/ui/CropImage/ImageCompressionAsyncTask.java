package com.example.weddingplanner.ui.CropImage;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


import com.example.weddingplanner.ui.model.ConstantData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {
    Context context;
    ImageListener imageCopy;
    String initialFilePath = "";
    String path = "";

    public ImageCompressionAsyncTask(Context context2, String str, ImageListener imageListener) {
        this.context = context2;
        this.initialFilePath = str;
        this.imageCopy = imageListener;
    }


    public String doInBackground(String... strArr) {
        try {
            return compressImage(this.initialFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String compressImage(String str) {
        Bitmap bitmap;
        int i;
        int i2;
        try {
            int attributeInt = new ExifInterface(str).getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + attributeInt);
            Matrix matrix = new Matrix();
            if (attributeInt == 6) {
                matrix.postRotate(90.0f);
                Log.d("EXIF", "Exif: " + attributeInt);
            } else if (attributeInt == 3) {
                matrix.postRotate(180.0f);
                Log.d("EXIF", "Exif: " + attributeInt);
            } else if (attributeInt == 8) {
                matrix.postRotate(270.0f);
                Log.d("EXIF", "Exif: " + attributeInt);
            }
            String realPathFromURI = getRealPathFromURI(str);
            new BitmapFactory.Options().inJustDecodeBounds = true;
            Bitmap decodeFile = BitmapFactory.decodeFile(realPathFromURI);
            int height = decodeFile.getHeight();
            int width = decodeFile.getWidth();
            float f = (float) (width / height);
            Log.i("compressImage", "compressImage: " + height + "----" + width);
            float f2 = (float) height;
            if (f2 <= 816.0f) {
                if (((float) width) <= 612.0f) {
                    bitmap = decodeFile;
                    Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    File profilePicStoreParent = ConstantData.profilePicStoreParent(this.context);
                    FileOutputStream fileOutputStream = new FileOutputStream(profilePicStoreParent);
                    createBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                    fileOutputStream.close();
                    return profilePicStoreParent.getAbsolutePath();
                }
            }
            if (f < 0.75f) {
                i2 = (int) ((816.0f / f2) * ((float) width));
                i = (int) 816.0f;
            } else {
                i = f > 0.75f ? (int) ((612.0f / ((float) width)) * f2) : (int) 816.0f;
                i2 = (int) 612.0f;
            }
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(decodeFile, i2, i, true);
            decodeFile.recycle();
            bitmap = createScaledBitmap;
            Bitmap createBitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            try {
                File profilePicStoreParent2 = ConstantData.profilePicStoreParent(this.context);
                FileOutputStream fileOutputStream2 = new FileOutputStream(profilePicStoreParent2);
                createBitmap2.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream2);
                fileOutputStream2.close();
                return profilePicStoreParent2.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }


    public void onPostExecute(String str) {
        super.onPostExecute(String.valueOf((Object) str));
        this.imageCopy.onImageCopy(str);
    }

    private String getRealPathFromURI(String str) {
        Uri parse = Uri.parse(str);
        Cursor query = this.context.getContentResolver().query(parse, null, null, null, null);
        if (query == null) {
            return parse.getPath();
        }
        query.moveToFirst();
        return query.getString(query.getColumnIndex("_data"));
    }
}
