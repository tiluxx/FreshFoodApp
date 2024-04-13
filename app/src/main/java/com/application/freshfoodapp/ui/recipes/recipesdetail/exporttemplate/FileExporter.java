package com.application.freshfoodapp.ui.recipes.recipesdetail.exporttemplate;

import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.STORAGE_SERVICE;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public abstract class FileExporter {
    public final boolean export(FragmentActivity activity , RecyclerView recyclerView, String fileName) {
        // Get the Request Permissions
        RequestPermissions(activity);

        // Handle storing the export file
        StorageVolume storageVolume = getStorageVolumes(activity);

        // Handle file name & locations for each type
        File fileOutput = getLocations(storageVolume, fileName);

        // Export file PDF or Image
        boolean exportFile = ExportFile(recyclerView, fileOutput);

        return exportFile;
    }

    private void RequestPermissions(FragmentActivity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{READ_MEDIA_IMAGES, WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
    }

    private StorageVolume getStorageVolumes(FragmentActivity activity) {
        StorageManager storageManager = (StorageManager) activity.getSystemService(STORAGE_SERVICE);
        StorageVolume storageVolume = storageManager.getStorageVolumes().get(0);
        return storageVolume;
    }

    protected abstract File getLocations(StorageVolume storageVolume, String fileName);
    protected abstract boolean ExportFile(RecyclerView recyclerView, File fileOutput);
}