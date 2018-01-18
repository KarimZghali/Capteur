package fr.m2i.capteur;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by silve on 18/01/2018.
 */

public class SensorLightTutoActivity extends Activity implements SensorEventListener{

    /*********************************************************************/
/** Attribut du capteur***********************************************/
    /*********************************************************************/

    private TextView txtLumens;

    // Valeur courante de la lumière
    float l;

    // Le sensor manager
    SensorManager sensorManager;

    // Le capteur de lumière
    Sensor light;

/***************************************************************/
/** Gestion du cycle de vie ************************************/
    /***************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // construire l'IHM
        setContentView(R.layout.activity_main);

        // Instancier le SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Instancier le capteur de lumière
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        txtLumens = findViewById(R.id.lumens);


    }

    public void gravite (View v) {
        Intent y = new Intent(this, Gravity.class);
        startActivity(y);
    }

    @Override
    protected void onPause() {
        // désenregistrer notre écoute du capteur
        sensorManager.unregisterListener(this, light);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // enregistrer notre écoute du capteur
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }

/*****************************************************************/
/** SensorEventListener ******************************************/
/*****************************************************************/

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Faîtes quelque chose ou pas&#8230;
        Toast.makeText(this, "Test",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Mettre à jour uniquement dans le cas de notre capteur
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            // La valeur de la lumière
           txtLumens.setText(String.valueOf(l));
            l = event.values[0];
            // faire autre chose&#8230;
            txtLumens.setText(String.valueOf(l));
        }
    }
}