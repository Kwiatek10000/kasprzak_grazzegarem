package pl.edu.kasprzak.grazzegarem;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GraActivity extends AppCompatActivity {

    boolean runningClock = false;
    int counter = 100;

    private static final String PREFERENCES_NAME = "GraZZegarem_preferences";
    private SharedPreferences preferences;

    private Runnable worker;
    private Button action;
    private TextView clock;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gra);

        preferences = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);
        action = (Button)findViewById(R.id.action);
        clock = (TextView)findViewById(R.id.clock);
        clock.setText("" + counter);

        worker = new Runnable() {
            @Override
            public void run() {
                Log.d("LICZNIK", "DZIAŁAM " + counter);
                clock.setText("" + counter);
                --counter;
                if (runningClock) {
                    handler.postDelayed(worker, 1);
                }
            }
        };
        handler = new Handler();
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!runningClock) {
                    counter = 100;
                    runningClock = true;
                    handler.postDelayed(worker, 1);
                } else {
                    runningClock = false;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putBoolean("runningClock", runningClock);
        preferencesEditor.putInt("counter", counter);
        preferencesEditor.commit();
        runningClock = false;

        Log.d("CYKL_ZYCIA", "ONPAUSE");
    }

    @Override
    protected void onResume() {
        super.onResume();

        runningClock = preferences.getBoolean("runningClock", runningClock);
        counter = preferences.getInt("counter", counter) + 1;
        handler.postDelayed(worker, 1);

        Log.d("CYKL_ZYCIA", "ONRESUME");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("CYKL_ZYCIA", "ONSTART");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("CYKL_ZYCIA", "ONSTOP");
    }
}
