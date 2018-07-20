package cl.tectronix.fotocelda;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import cl.tectronix.fotocelda.thread.TuneThread;

public class TimeActivity extends AppCompatActivity implements View.OnClickListener{

    //private MusicIntentReceiver musicIntentReceiver;
    private AppCompatTextView lblTimer;
    private AppCompatButton btnStart, btnStop;
    boolean isOn = false;
    private int mili = 0, minute = 0, sec = 0;
    private Handler handler = new Handler();
    private TuneThread tuneThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        lblTimer = findViewById(R.id.lblTimer);

        //musicIntentReceiver = new MusicIntentReceiver();

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);

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

    /*@Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(musicIntentReceiver,intentFilter);

        super.onResume();
    }*/

    /*@Override
    protected void onPause() {
        unregisterReceiver(musicIntentReceiver);

        super.onPause();
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnStart:
                isOn = true;
                tuneThread.start();
                btnStart.setEnabled(false);
                break;
            case R.id.btnStop:
                isOn = false;
                tuneThread.stopTune();
                btnStart.setEnabled(true);
                break;
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
