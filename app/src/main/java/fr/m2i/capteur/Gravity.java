package fr.m2i.capteur;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by silve on 18/01/2018.
 */

public class Gravity extends Activity implements SensorEventListener {

    // Valeur courante de l'accéléromètre
    float x, y, z;

/*************************************************************************************/
/** Sensors and co *******************************************************************/
    /*************************************************************************************/

    private TextView accel;

    // Celui qui connait l'orientation de l'appareil
    private Display mDisplay;

    // Le sensor manager
    SensorManager sensorManager;

    // L'accéléromètre
    Sensor accelerometer;

    // La gravité
    Sensor gravity;

    // L'accéléromètre linéaire
    Sensor linearAcc;

/**************************************************************/
/** Sensors Type Constant *************************************/
    /**************************************************************/

    // Le capteur sélectionné
    private int sensorType;

    // L'accéléromètre
    private static final int ACCELE = 0;

    // La Gravité
    private static final int Gravity = 1;

    // L'accéléromètre linéaire
    private static final int LINEAR_ACCELE = 2;

/*****************************************/
/** Gestion du cycle de vie **************/
    /*****************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Construire l'IHM
        setContentView(R.layout.activity_main);
        // Gérer les capteurs
        // Instancier le SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Instancier l'accéléromètre
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Instancier la gravité
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        // Instancier l'accélération linéaire
        linearAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        // Et enfin instancier le display qui connaît l'orientation de l'appareil
        mDisplay = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        accel = findViewById(R.id.accel);
    }

    public void lumens (View v) {

        Intent i = new Intent(this, SensorLightTutoActivity.class);
        startActivity(i);
    }

    @Override
    protected void onPause() {
        // désenregistrer tous le monde
        sensorManager.unregisterListener(this, accelerometer);
        sensorManager.unregisterListener(this, gravity);
        sensorManager.unregisterListener(this, linearAcc);
        super.onPause();
    }

    @Override
    protected void onResume() {
    /* Ce qu'en dit Google&#160;:
     * «&#160; Ce n'est pas nécessaire d'avoir les évènements des capteurs à un rythme trop rapide.
     * En utilisant un rythme moins rapide (SENSOR_DELAY_UI), nous obtenons un filtre
     * automatique de bas-niveau qui "extrait" la gravité  de l'accélération.
     * Un autre bénéfice étant que l'on utilise moins d'énergie et de CPU.&#160;»
     */
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, linearAcc, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

/*********************************/
/** SensorEventListener **********/
    /*********************************/

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to do
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // update only when your are in the right case:
        if (((event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) && (sensorType == ACCELE))
                || ((event.sensor.getType() == Sensor.TYPE_GRAVITY) && (sensorType == Gravity))
                || ((event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) && (sensorType == LINEAR_ACCELE))) {
            // Corriger les valeurs x et y en fonction de l'orientation de l'appareil
            switch (mDisplay.getRotation()) {
                case Surface.ROTATION_0:
                    x = event.values[0];
                    y = event.values[1];
                    break;
                case Surface.ROTATION_90:
                    x = -event.values[1];
                    y = event.values[0];
                    break;
                case Surface.ROTATION_180:
                    x = -event.values[0];
                    y = -event.values[1];
                    break;
                case Surface.ROTATION_270:
                    x = event.values[1];
                    y = -event.values[0];
                    break;
            }
            // la valeur z
            z = event.values[2];
            // faire quelque chose
            accel.setText(String.valueOf("X : "+x+" - Y :"+y+" - Z : ")+z);
        }
    }
}