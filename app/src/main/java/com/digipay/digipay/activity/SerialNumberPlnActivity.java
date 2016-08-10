package com.digipay.digipay.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.digipay.digipay.R;

public class SerialNumberPlnActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_number_pln);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("Message");

        TextView tvSnPln = (TextView) findViewById(R.id.tvSnPln);

        if (message != null) {
            tvSnPln.setText(message);
        } else {
            tvSnPln.setText("Maaf terjadi kesalahan, mohon mencoba kembali");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (doubleBackToExitPressedOnce) {
                    this.finish();
                    return false;
                }
                twiceTapBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        twiceTapBack();

    }

    private void twiceTapBack() {
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pastikan anda sudah mengisi SN(Serial Number) ke KWH meter!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}
