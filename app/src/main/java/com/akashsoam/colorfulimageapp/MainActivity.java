package com.akashsoam.colorfulimageapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTakeThePicture;
    private Button btnSaveThePicture;
    private ImageView imgPhoto;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;

    private TextView txtRedColorValue;
    private TextView txtBlueColorValue;
    private TextView txtGreenColorValue;
    private final static int CAMERA_RC = 11;
    private static final int CAMERA_IMAGE_REQUEST_CODE = 1011;

    private Bitmap bitmap;

    private Colorful colorful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSaveThePicture = findViewById(R.id.btnSavePicture);
        btnTakeThePicture = findViewById(R.id.btnTakePicture);
        imgPhoto = findViewById(R.id.imgPhoto);
        redSeekBar = findViewById(R.id.redColorSeekBar);
        greenSeekBar = findViewById(R.id.greenColorSeekBar);
        blueSeekBar = findViewById(R.id.blueColorSeekBar);
        txtGreenColorValue = findViewById(R.id.txtGreenColorValue);
        txtBlueColorValue = findViewById(R.id.txtBlueColorValue);
        txtRedColorValue = findViewById(R.id.txtRedColorValue);

        btnTakeThePicture.setOnClickListener(MainActivity.this);
        btnSaveThePicture.setOnClickListener(MainActivity.this);


        ColorizationHandler colorizationHandler = new ColorizationHandler();

        redSeekBar.setOnSeekBarChangeListener(colorizationHandler);
        greenSeekBar.setOnSeekBarChangeListener(colorizationHandler);
        blueSeekBar.setOnSeekBarChangeListener(colorizationHandler);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSavePicture) {

        } else if (id == R.id.btnTakePicture) {
            int permissionResult = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
            if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                PackageManager packageManager = getPackageManager();
                if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_IMAGE_REQUEST_CODE);
                } else {
                    Toast.makeText(this, "Your device does not have a working camera", Toast.LENGTH_SHORT).show();
                }
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_RC);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(MainActivity.this, "OnActivityResult is called", Toast.LENGTH_LONG).show();
        if (requestCode == CAMERA_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");
            colorful = new Colorful(bitmap, 0.0f, 0.0f, 0.0f);
            imgPhoto.setImageBitmap(bitmap);
        }
    }

    private class ColorizationHandler implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                if (seekBar == redSeekBar) {
                    colorful.setRedColorValue(progress / 100.0f);
                    redSeekBar.setProgress((int) (100 * (colorful.getRedColorValue())));
                    txtRedColorValue.setText(colorful.getRedColorValue() + "");
                } if (seekBar == blueSeekBar) {
                    colorful.setBlueColorValue(progress / 100.0f);
                    blueSeekBar.setProgress((int) (100 * (colorful.getBlueColorValue())));
                    txtBlueColorValue.setText(colorful.getBlueColorValue() + "");
                } if (seekBar == greenSeekBar) {
                    colorful.setGreenColorValue(progress / 100.0f);
                    greenSeekBar.setProgress((int) (100 * (colorful.getGreenColorValue())));
                    txtGreenColorValue.setText(colorful.getGreenColorValue() + "");
                }
                bitmap = colorful.returnTheColorizedBitmap();
                imgPhoto.setImageBitmap(bitmap);
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

}