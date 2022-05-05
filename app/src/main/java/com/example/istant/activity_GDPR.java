package com.example.istant;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class activity_GDPR extends AppCompatActivity {

    private WebView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdpr);

        // load the webpage (hosted on GitHub) with the GDPR.
        view = findViewById(R.id.activitygdpr_webview);
        view.setWebViewClient(new WebViewClient());
        view.loadUrl("https://raw.githubusercontent.com/AlessandroConte/ISTANT/master/gdpr");

        // Definition of the Action Bar with the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("GDPR"); // actionbar's name
        }
    }

    // This function allows the back button located in the actionbar to make me return to the activity/fragment I was
    // visualizing before going in the settings activity
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(myIntent);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        Intent i=new Intent(activity_GDPR.this,RegistrationActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}