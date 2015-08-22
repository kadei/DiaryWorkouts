package ru.kadei.diaryworkouts.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Field;
import java.util.ArrayList;

import ru.kadei.diaryworkouts.R;

public class MainActivity extends AppCompatActivity {

    class T {

        int integer;
        String str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final T t = new T();
        t.integer = 10;
        t.str = "hello";

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field f : fields) {
            String name = f.getName();
            Class typeClass = f.getType();
            try {
                if(typeClass == int.class) {
                    Log.d("TEST", "integer value = ");
                    int val = (int) f.get(t);
                    Log.d("TEST", String.valueOf(val));
                }
                else if(typeClass == String.class) {
                    Log.d("TEST", "String");
                    String str = (String) f.get(t);
                    Log.d("TEST", str);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
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
