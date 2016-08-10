package com.digipay.digipay.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.digipay.digipay.R;
import com.digipay.digipay.fragments.TabFragment;
import com.digipay.digipay.functions.SessionManager;

public class MainMenuActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private SessionManager mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_baru);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSession = new SessionManager(getApplicationContext());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.shitstuff);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

//                if (menuItem.getItemId() == R.id.nav_item_home) {
//                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
//                }
                if (menuItem.getItemId() == R.id.nav_item_profile) {
                    Intent i = new Intent(getApplicationContext(), ProfilesActivity.class);
                    startActivity(i);
                }

                if (menuItem.getItemId() == R.id.nav_item_pinpass) {
                    Intent i = new Intent(getApplicationContext(), PinDanPasswordActivity.class);
                    startActivity(i);
                }
//
                if (menuItem.getItemId() == R.id.nav_item_top_wallet) {
                    Intent i = new Intent(getApplicationContext(), TopupWalletActivity.class);
                    startActivity(i);
                }

//                if (menuItem.getItemId() == R.id.nav_item_trans_pembayaran) {
//                    Intent i = new Intent(getApplicationContext(), ConfirmationPaymentActivity.class);
//                    startActivity(i);
//                }

                if (menuItem.getItemId() == R.id.nav_item_call_us) {
                    Intent i = new Intent(getApplicationContext(), CallUsActivity.class);
                    startActivity(i);
                }

                if (menuItem.getItemId() == R.id.nav_item_logout) {
                    alertDialogLogout();
                }

                return false;
            }

        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }

    private void alertDialogLogout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainMenuActivity.this);
        alertDialogBuilder.setTitle("Keluar");
        alertDialogBuilder
                .setMessage("Apakah yakin anda ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mSession.logout();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
