package com.application.freshfoodapp.ui.recipes.recipesdetail.exporttemplate;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.storage.StorageVolume;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageExporter extends FileExporter{

    @Override
    protected File getLocations(StorageVolume storageVolume, String fileName) {
        File imageFile = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            imageFile = new File(storageVolume.getDirectory().getPath() + "/Download/" + fileName + ".png");
        }
        return imageFile;
    }

    @Override
    protected boolean ExportFile(RecyclerView recyclerView, File fileOutput) {
        Bitmap bitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        recyclerView.draw(canvas);
        try {
            FileOutputStream outputStream = new FileOutputStream(fileOutput);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}