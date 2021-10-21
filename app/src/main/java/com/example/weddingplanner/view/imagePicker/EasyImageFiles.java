package com.example.weddingplanner.view.imagePicker;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/* access modifiers changed from: package-private */
public class EasyImageFiles implements Constants {
    EasyImageFiles() {
    }


    public static String getFolderName(Context context) {
        return EasyImage.configuration(context).getFolderName();
    }

    private static File tempImageDirectory(Context context) {
        File file = new File(context.getCacheDir(), Constants.DEFAULT_FOLDER_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private static void writeToFile(InputStream inputStream, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.close();
                    inputStream.close();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void copyFile(File file, File file2) throws IOException {
        writeToFile(new FileInputStream(file), file2);
    }

    static void copyFilesInSeparateThread(final Context context, final List<File> list) {
        new Thread(new Runnable() {
            /* class com.selfmentor.myweddingplanner.imagePicker.EasyImageFiles.AnonymousClass1 */

            public void run() {
                ArrayList arrayList = new ArrayList();
                int i = 1;
                for (File file : list) {
                    File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), EasyImageFiles.getFolderName(context));
                    if (!file2.exists()) {
                        file2.mkdirs();
                    }
                    String[] split = file.getName().split("\\.");
                    File file3 = new File(file2, String.format("IMG_%s_%d.%s", new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()), Integer.valueOf(i), "." + split[split.length - 1]));
                    try {
                        file3.createNewFile();
                        EasyImageFiles.copyFile(file, file3);
                        arrayList.add(file3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
                EasyImageFiles.scanCopiedImages(context, arrayList);
            }
        }).run();
    }

    static List<File> singleFileList(File file) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(file);
        return arrayList;
    }

    static void scanCopiedImages(Context context, List<File> list) {
        String[] strArr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strArr[i] = list.get(i).toString();
        }
        MediaScannerConnection.scanFile(context, strArr, null, new MediaScannerConnection.OnScanCompletedListener() {
            /* class com.selfmentor.myweddingplanner.imagePicker.EasyImageFiles.AnonymousClass2 */

            public void onScanCompleted(String str, Uri uri) {
                String simpleName = getClass().getSimpleName();
                Log.d(simpleName, "Scanned " + str + ":");
                String simpleName2 = getClass().getSimpleName();
                Log.d(simpleName2, "-> uri=" + uri);
            }
        });
    }

    static File pickedExistingPicture(Context context, Uri uri) throws IOException {
        InputStream openInputStream = context.getContentResolver().openInputStream(uri);
        File tempImageDirectory = tempImageDirectory(context);
        File file = new File(tempImageDirectory, UUID.randomUUID().toString() + "." + getMimeType(context, uri));
        file.createNewFile();
        writeToFile(openInputStream, file);
        return file;
    }

    static File getCameraPicturesLocation(Context context) throws IOException {
        return File.createTempFile(UUID.randomUUID().toString(), ".jpg", tempImageDirectory(context));
    }

    static File getCameraVideoLocation(Context context) throws IOException {
        return File.createTempFile(UUID.randomUUID().toString(), ".mp4", tempImageDirectory(context));
    }

    private static String getMimeType(Context context, Uri uri) {
        if (uri.getScheme().equals("content")) {
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(context.getContentResolver().getType(uri));
        }
        return MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
    }

    static Uri getUriToFile(Context context, File file) {
        String packageName = context.getApplicationContext().getPackageName();
        return FileProvider.getUriForFile(context, packageName + ".provider", file);
    }
}
