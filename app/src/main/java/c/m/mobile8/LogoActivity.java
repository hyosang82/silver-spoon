package c.m.mobile8;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.roger.match.library.MatchTextView;

public class LogoActivity extends AppCompatActivity {
    private MatchTextView mMatchTextViewMain;
    private MatchTextView mMatchTextViewSub;
    private boolean isFinishApp = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        mMatchTextViewMain = (MatchTextView)findViewById(R.id.matchTextViewMain);
        mMatchTextViewSub = (MatchTextView)findViewById(R.id.matchTextViewSub);
        mMatchTextViewMain.setInTime(0.7f);
        mMatchTextViewMain.setOutTime(0.7f);

        mMatchTextViewSub.setLight(false);
    }
    @Override
    public void onResume() {
        super.onResume();

        new CountDownTimer(4000, 2500) {
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (isFinishApp) {
                    finish();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        isFinishApp = true;
        super.onBackPressed();
    }

}
