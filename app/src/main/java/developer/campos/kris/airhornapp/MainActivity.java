package developer.campos.kris.airhornapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * An airhorn app. Designed for Cal Poly Mobile App Dev Club's booth at the 2016 Open House Club Fair.
 *
 * A listener is set whenever the application is active and watches for phone movement.
 *
 * @author Kris Campos
 * @version 1.0 - Initial Version - 15 April 2016
 */

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private MediaPlayer airhorn; // airhorn sound
    private Sensor senAccelerometer;
    private SensorManager sensorManager;

    // Used for determining when the device is shaking
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 6000;
    // Sensor Tutorial:
    // http://code.tutsplus.com/tutorials/using-the-accelerometer-on-android--mobile-22125

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize music player
        airhorn = MediaPlayer.create(this, R.raw.airhorn);

        // Initialize accelerometer controls
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // It is good practice to unregister the sensor when the application hibernates and register
        // it again once the application resumes.
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // It is good practice to unregister the sensor when the application hibernates and register
        // it again once the application resumes.
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Called when sensor values have changed.
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     * <p/>
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            // Sensors are VERY sensitive. This ensures method does not trigger constantly
            if((curTime - lastUpdate) > 100) {
                Log.i("Checking for shake", "");
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                // Used to detect start of shaking motion
                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                // Detects shake: Insert desired effect here.
                if(speed > SHAKE_THRESHOLD) {
                    /* INSERT DESIRED ACTION HERE */
                    Log.i("Playing airhorn...", "");
                    airhorn.start(); // play airhorn music
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    /**
     * Called when the accuracy of the registered sensor has changed.
     * <p/>
     * <p>See the SENSOR_STATUS_* constants in
     * {@link SensorManager SensorManager} for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
