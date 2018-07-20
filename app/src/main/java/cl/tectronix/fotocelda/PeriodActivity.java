package cl.tectronix.fotocelda;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

import cl.tectronix.fotocelda.thread.TuneThread;

public class PeriodActivity extends AppCompatActivity implements View.OnClickListener{

    private boolean isOn = false;
    private int mili = 0, minute = 0, sec = 0;
    private Handler handler = new Handler();
    private TuneThread tuneThread;
    private AppCompatTextView lblTimer;
    private AppCompatButton btnStart,btnStop,btnLap;
    private EditText mEtLaps; //laps text view
    private ScrollView mSvLaps; //scroll view which wraps the et_laps

    //keep track of how many times btn_lap was clicked
    int mLapCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);

        mEtLaps = findViewById(R.id.et_laps);
        mEtLaps.setEnabled(false); //prevent the et_laps to be editable

        mSvLaps = findViewById(R.id.sv_lap);

        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnLap = findViewById(R.id.btnLap);

        lblTimer = findViewById(R.id.lblTimer);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnLap.setOnClickListener(this);

        tuneThread = new TuneThread();

        Thread chrono = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isOn) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mili++;
                        if (mili == 999) {
                            sec++;
                            mili = 0;
                        }
                        if (sec == 59) {
                            minute++;
                            sec = 0;
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String m, s, mi;
                                if (mili < 10) {
                                    m = "00" + mili;
                                } else if (mili < 100) {
                                    m = "0" + mili;
                                } else {
                                    m = "" + mili;
                                }
                                if (sec < 10) {
                                    s = "0" + sec;
                                } else {
                                    s = "" + sec;
                                }
                                if (minute < 10) {
                                    mi = "0" + minute;
                                } else {
                                    mi = "" + minute;
                                }

                                lblTimer.setText(mi + ":" + s + ":" + m);
                            }
                        });
                    }
                }
            }
        });

        chrono.start();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnStart:
                isOn = true;
                tuneThread.start();
                btnStart.setEnabled(false);
                btnLap.setEnabled(true);
                break;
            case R.id.btnStop:
                isOn = false;
                tuneThread.stopTune();
                btnStart.setEnabled(true);
                btnLap.setEnabled(false);
                break;
            case R.id.btnLap:
                mEtLaps.append("LAP " + String.valueOf(mLapCounter++)
                        + "   " + lblTimer.getText() + "\n");

                //scroll to the bottom of et_laps
                mSvLaps.post(new Runnable() {
                    @Override
                    public void run() {
                        mSvLaps.smoothScrollTo(0, mEtLaps.getBottom());
                    }
                });
        }
    }

    @Override
    public void onBackPressed() {
        if (tuneThread != null){
            tuneThread.stopTune();
        }

        super.onBackPressed();
    }

}
