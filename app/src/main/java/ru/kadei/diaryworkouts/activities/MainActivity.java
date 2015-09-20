package ru.kadei.diaryworkouts.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.fragments.Navigator;
import ru.kadei.diaryworkouts.managers.ResourceManager;

import static android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP;
import static android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM;

public class MainActivity extends AppCompatActivity {

    private Navigator navigator;
    private ResourceManager resourceManager;

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TEST", "onActivityResult");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applyToolBar();

        resourceManager = new ResourceManager(this);

        navigator = new Navigator(this);
        navigator.openMainFragment();
    }

    private void applyToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayOptions(DISPLAY_SHOW_CUSTOM | DISPLAY_HOME_AS_UP);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navigator.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        navigator.configurationChange(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TEST", "getTypeface method called " + resourceManager.getCounterRequestFont() + " times");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
