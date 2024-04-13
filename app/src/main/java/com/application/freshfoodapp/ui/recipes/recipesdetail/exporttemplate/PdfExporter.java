package com.application.freshfoodapp.ui.recipes.recipesdetail.exporttemplate;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.os.Build;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firestore.v1.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfExporter extends FileExporter {

    @Override
    protected File getLocations(StorageVolume storageVolume, String fileName) {
        File filePDFOutput = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            filePDFOutput = new File(storageVolume.getDirectory().getPath() + "/Download/" + fileName + ".pdf");
        }
        return filePDFOutput;
    }

    @Override
    protected boolean ExportFile(RecyclerView recyclerView, File fileOutput) {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        recyclerView.draw(page.getCanvas());
        pdfDocument.finishPage(page);
        try {
            pdfDocument.writeTo(new FileOutputStream(fileOutput));
            pdfDocument.close();
            return true;
        } catch (IOException e) {
            pdfDocument.close();
            return false;
        }
    }
}