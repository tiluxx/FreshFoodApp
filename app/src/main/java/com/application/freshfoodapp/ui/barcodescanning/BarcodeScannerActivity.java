package com.application.freshfoodapp.ui.barcodescanning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BarcodeScannerActivity extends AppCompatActivity {
    public static final int RC_CAMERA_PERMISSION = 1001;
    public static final String RES_BARCODE = "BARCODE_RETRIEVED";
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ProcessCameraProvider cameraProvider;
    private CameraSelector cameraSelector;
    private Preview previewUseCase;
    private ImageAnalysis analysisUseCase;

    PreviewView previewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        previewView = findViewById(R.id.previewView);

        setUpCamera();
    }

    private void setUpCamera() {
        int lensFacing = CameraSelector.LENS_FACING_BACK;
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();

        cameraProviderFuture = ProcessCameraProvider.getInstance(BarcodeScannerActivity.this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {
                            android.Manifest.permission.CAMERA
                    }, RC_CAMERA_PERMISSION);
                } else {
                    bindCameraUseCases(cameraProvider);
                }
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(BarcodeScannerActivity.this));
    }

    private void bindCameraUseCases(ProcessCameraProvider cameraProvider) {
        bindImagePreview(cameraProvider);
        bindImageAnalyse(cameraProvider);
    }

    private void bindImagePreview(ProcessCameraProvider cameraProvider) {
        previewUseCase = new Preview.Builder()
                .build();

        previewUseCase.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, previewUseCase);
    }

    private void bindImageAnalyse(ProcessCameraProvider cameraProvider) {
        analysisUseCase =
                new ImageAnalysis.Builder()
                        // enable the following line if RGBA output is needed.
                        //.setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        ExecutorService cameraExecutor = Executors.newSingleThreadExecutor();
        BarcodeScanner barcodeScanner = BarcodeScanning.getClient();

        analysisUseCase.setAnalyzer(cameraExecutor, imageProxy -> processImageProxy(barcodeScanner, imageProxy));

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, analysisUseCase, previewUseCase);
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void processImageProxy(BarcodeScanner barcodeScanner, ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            barcodeScanner.process(image)
                    .addOnSuccessListener(barcodes -> {
                        String rawValue = null;
                        for (Barcode barcode: barcodes) {
                            rawValue = barcode.getRawValue();
                        }
                        Intent resIntent = new Intent();
                        resIntent.putExtra(RES_BARCODE, rawValue);
                        if (rawValue != null) {
                            setResult(RESULT_OK, resIntent);
                        } else {
                            setResult(RESULT_CANCELED);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Task failed with an exception
                        // ...
                    })
                    .addOnCompleteListener(task -> imageProxy.close());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BarcodeScannerActivity.RC_CAMERA_PERMISSION) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Log.i("ERROR", "no camera permission");
            } else {
                setUpCamera();
            }
        }
    }
}