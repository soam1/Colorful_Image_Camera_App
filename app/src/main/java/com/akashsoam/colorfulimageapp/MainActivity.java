package com.akashsoam.colorfulimageapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
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

import java.io.File;

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

    private Button btnShare;

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

        btnShare = findViewById(R.id.btnShare);

        btnSaveThePicture.setVisibility(View.INVISIBLE);
        btnShare.setVisibility(View.INVISIBLE);
        redSeekBar.setVisibility(View.INVISIBLE);
        blueSeekBar.setVisibility(View.INVISIBLE);
        greenSeekBar.setVisibility(View.INVISIBLE);

        txtRedColorValue.setVisibility(View.INVISIBLE);
        txtBlueColorValue.setVisibility(View.INVISIBLE);
        txtGreenColorValue.setVisibility(View.INVISIBLE);


        btnTakeThePicture.setOnClickListener(MainActivity.this);
        btnSaveThePicture.setOnClickListener(MainActivity.this);
        btnShare.setOnClickListener(MainActivity.this);


        ColorizationHandler colorizationHandler = new ColorizationHandler();

        redSeekBar.setOnSeekBarChangeListener(colorizationHandler);
        greenSeekBar.setOnSeekBarChangeListener(colorizationHandler);
        blueSeekBar.setOnSeekBarChangeListener(colorizationHandler);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSavePicture) {
            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                try {
                    SaveFile.saveFile(MainActivity.this, bitmap);
                    Toast.makeText(this, "Image is successfully saved to external storage", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 23);
            }
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
        } else if (id == R.id.btnShare) {
            try {
                File myPicFile = SaveFile.saveFile(MainActivity.this, bitmap);
                Uri myUri = Uri.fromFile(myPicFile);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "This picture is sent from Image app i created myself!");
                shareIntent.putExtra(Intent.EXTRA_STREAM, myUri);
                startActivity(Intent.createChooser(shareIntent, "Lets share your picture with others!"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Toast.makeText(MainActivity.this, "OnActivityResult is called", Toast.LENGTH_LONG).show();
        if (requestCode == CAMERA_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            btnSaveThePicture.setVisibility(View.VISIBLE);
            btnShare.setVisibility(View.VISIBLE);
            redSeekBar.setVisibility(View.VISIBLE);
            blueSeekBar.setVisibility(View.VISIBLE);
            greenSeekBar.setVisibility(View.VISIBLE);

            txtRedColorValue.setVisibility(View.VISIBLE);
            txtBlueColorValue.setVisibility(View.VISIBLE);
            txtGreenColorValue.setVisibility(View.VISIBLE);

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
                }
                if (seekBar == blueSeekBar) {
                    colorful.setBlueColorValue(progress / 100.0f);
                    blueSeekBar.setProgress((int) (100 * (colorful.getBlueColorValue())));
                    txtBlueColorValue.setText(colorful.getBlueColorValue() + "");
                }
                if (seekBar == greenSeekBar) {
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