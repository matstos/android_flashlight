package com.matstos.flashlight;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    private Camera camera;
    ImageButton flashlightImage;
    private boolean isFlashlightOn;
    Parameters params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // on/off image
        flashlightImage = (ImageButton) findViewById(R.id.flashlightSwitch);
        boolean isCameraFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isCameraFlash) {
            showNoCameraAlert();
        }else {
            camera = Camera.open();
            params = camera.getParameters();
        }
        flashlightImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (isFlashlightOn) {
                    setFlashlighOff();
                } else {
                    setFlaslightOn();
                }
            }
        });
    }



    private void showNoCameraAlert(){
        new AlertDialog.Builder(this)
                .setTitle("Error: No camera flash!")
                .setMessage("Sorry, your device does not support camera flash :/")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        finish(); //This closes the app
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        return;

    }

    private void setFlaslightOn(){
        params = camera.getParameters();
        params.setFlashMode(Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();
        isFlashlightOn = true;
        flashlightImage.setImageResource(R.drawable.btn_on);
    }
    private void setFlashlighOff(){
        params.setFlashMode(Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        isFlashlightOn = false;
        flashlightImage.setImageResource(R.drawable.btn_off);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null){
            camera.release();
            camera = null;
        }
    }


}
