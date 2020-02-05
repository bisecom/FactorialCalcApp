package com.e.progressbar;
    import android.app.Activity;
    import android.os.Bundle;
    import android.os.Handler;
    import android.os.Message;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ProgressBar;
    import android.widget.TextView;

public class MainActivity extends Activity {

    ProgressBar myProgressBar;
    TextView textView, resultTextView;
    Button startBtn;
    EditText getNumber;
    int dataToCalculate = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.progressBarData);
        resultTextView = findViewById(R.id.resultData);
        myProgressBar = findViewById(R.id.progressBarHorizontal);
        getNumber = findViewById(R.id.numberET);
        startBtn = findViewById(R.id.buttonStart);

        startBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                try {
                    dataToCalculate = Integer.parseInt(getNumber.getText().toString());
                } catch (Throwable t) {
                }
                if (dataToCalculate != 0) {
                    resultTextView.setText("");
                    Runnable myThread = new CustomRunnable(dataToCalculate);
                    startBtn.setEnabled(false);
                    new Thread(myThread).start();

                }
            }
        });
    }

    public class CustomRunnable implements Runnable {
        private int numberToCalculate;
        CustomRunnable(int number) {
            this.numberToCalculate = number;
        }

        @Override
        public void run() {
            try {
                long result = 1;
                int progressBarData = 1;
                PassData data = new PassData();
                Message msg;
                for (int factor = 2; factor <= numberToCalculate; factor++) {
                    result *= factor;
                    progressBarData = factor * 100 / numberToCalculate;
                    data.progressBarData = progressBarData;
                    msg = Message.obtain();
                    msg.obj = data;
                    myHandle.sendMessage(msg);
                    Thread.sleep(200);
                }
                msg = Message.obtain();
                data.factorialResult = result;
                msg.obj = data;
                myHandle.sendMessage(msg);
            } catch (Throwable t) {
            }
        }
    }

    Handler myHandle = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            PassData outObj = (PassData) msg.obj;
            myProgressBar.setProgress(outObj.progressBarData);
            textView.setText(String.format("%d %%", outObj.progressBarData));
            if(outObj.progressBarData > 99) {
                resultTextView.setText(String.valueOf(outObj.factorialResult));
                startBtn.setEnabled(true);
            }
            return false;
        }
    });
}
