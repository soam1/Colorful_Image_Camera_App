package com.akashsoam.colorfulimageapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.SystemClock;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class SaveFile {
    public static File saveFile(Activity myActivity, Bitmap bitmap) throws IOException {
        String externalStorageState = Environment.getExternalStorageState();

        File myFile = null;
        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            File picturesDirectory = myActivity.getExternalFilesDir("ColorAppPictures");
            Date currDate = new Date();
            long elapsedTime = SystemClock.elapsedRealtime();
            String uniqueImageName = "/" + currDate + "_" + elapsedTime + ".png";

            myFile = new File(picturesDirectory + uniqueImageName);
            long remainingSpace = picturesDirectory.getFreeSpace();
            long requiredSpace = bitmap.getByteCount();
            if (requiredSpace * 1.8 < remainingSpace) {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(myFile);
                    boolean isImageSavedSuccessfully = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    if(isImageSavedSuccessfully)return myFile;
                    else throw new IOException("Image is not saved successfully to external storage");
                } catch (Exception e) {
                    throw new IOException("The operation of saving could not be successful");
                }
            } else {
                throw new IOException("Not enough space in external storage to save");
            }
        } else {
            throw new IOException("This device does not have an external storage");
        }
    }
}
