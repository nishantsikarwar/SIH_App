package com.example.nishant.sih_app;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {

   private SurfaceView cam_scan;
  private   BarcodeDetector barcodeDetector;
  private CameraSource cameraSource ;
   final  int REQUEST_CODE=100;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    try {
                        cameraSource.start(cam_scan.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        cam_scan= (SurfaceView)findViewById(R.id.cam_scan);
        barcodeDetector=new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        if(!barcodeDetector.isOperational()){
            Toast.makeText(getApplicationContext(),"SORRY_COULDN'T_DECECTED",Toast.LENGTH_SHORT).show();
            this.finish();
        }

        cameraSource=new CameraSource.Builder(this,barcodeDetector).setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedFps(24).setAutoFocusEnabled(true).setRequestedPreviewSize(1920,1024).build();
        cam_scan.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                if(ActivityCompat.checkSelfPermission(ScanActivity.this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ScanActivity.this,new String[]{Manifest.permission.CAMERA},REQUEST_CODE );
                     return;
                }

                try {

                      cameraSource.start(cam_scan.getHolder());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
               cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qr_codes = detections.getDetectedItems();
                if (qr_codes.size() != 0) {
                    Intent intent =new Intent();
                    intent.putExtra("barcode",qr_codes.valueAt(0));
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }


}
