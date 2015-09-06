package ru.kadei.diaryworkouts.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.threads.BackgroundLogic;
import ru.kadei.diaryworkouts.threads.Task;

public class MainActivity extends AppCompatActivity {

    BackgroundLogic bg;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bg = new BackgroundLogic(false);

        for (double val = 0.0; val < 100.0; val += 11.0) {
            Task t = new Task()
                    .setClient(this)
                    .setExecutedMethod("sqrt", Double.TYPE)
                    .setParameters(val)
                    .setCompleteMethod("resultSqrt")
                    .setFailMethod("failSqrt");

            bg.execute(t);
        }
        Log.d("TEST", "end onCreate()");
    }

    double sqrt(double val) {
        Log.d("TEST", "calculate sqrt for " + val);
        if(val * val > 600.0)
            throw new RuntimeException("value should not be better 600");
        return val * val;
    }

    void resultSqrt(double val) {
        Log.d("TEST", "result = " + val);
    }

    void failSqrt(Throwable t) {
        Log.e("TEST", t.getMessage());
    }

    private void taskWithoutException() throws InterruptedException {
        Thread.sleep(2000l);
        count++;
    }

    private void taskWithException() throws InterruptedException {
        Thread.sleep(2000l);
        count++;
        throw new RuntimeException("test exception");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
