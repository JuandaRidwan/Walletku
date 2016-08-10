package com.digipay.digipay.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digipay.digipay.R;
import com.digipay.digipay.activity.BookingAirplaneTicketActivity;
import com.digipay.digipay.activity.BookingHotelActivity;
import com.digipay.digipay.activity.PinDanPasswordActivity;
import com.digipay.digipay.activity.TopupPlnActivity;
import com.digipay.digipay.activity.TopupPulsaActivity;
import com.digipay.digipay.functions.Constant;
import com.digipay.digipay.functions.SessionManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PayFragment extends Fragment {
    private SessionManager mSession;
    private String name, totalBalance;
    private boolean isFirstLogin;
    private TextView tvName, tvSaldo;

    public PayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pay, container, false);

        tvName = (TextView) v.findViewById(R.id.tvName);
        tvSaldo = (TextView) v.findViewById(R.id.tvSaldo);
        mSession = new SessionManager(getActivity());

        LinearLayout linearTopupPulsa = (LinearLayout) v.findViewById(R.id.linearTopupPulsa);
        LinearLayout linearBayarListrik = (LinearLayout) v.findViewById(R.id.linearBayarListrik);
        LinearLayout linearBayarHotel = (LinearLayout) v.findViewById(R.id.linearBayarKartuKredit);
        LinearLayout linearBayarTiketPesawat = (LinearLayout) v.findViewById(R.id.linearBayarPam);

        auotoLogout();

        linearTopupPulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TopupPulsaActivity.class);
                startActivity(i);
            }
        });

        linearBayarListrik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TopupPlnActivity.class);
                startActivity(i);
            }
        });

        linearBayarHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BookingHotelActivity.class);
                startActivity(i);
            }
        });

        linearBayarTiketPesawat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BookingAirplaneTicketActivity.class);
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        name = mSession.getName();
        totalBalance = mSession.getTotalBalance();
        if (totalBalance != null && name != null) {
            new getUserData().execute();
        }
    }

    private void auotoLogout() {
        try {
            // setCountDownTimer
            new CountDownTimer(60000 * 15, 1000) {

                public void onTick(long millisUntilFinished) {
                    Log.d("timerTick", "seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    Log.d("timerDone", "Done");
                    mSession.logout();
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alertDialogFirstLogin() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle("Ganti PIN");
        alertDialogBuilder
                .setMessage("Silahkan ganti PIN Anda terlebih dahulu, PIN lama Anda adalah 1234")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getActivity(), PinDanPasswordActivity.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class getUserData extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Mohon Tunggu...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Constant.WEB_URL + "users/" + mSession.getUserId());

            String auth = "Token token=" + mSession.getToken() + ",email=" + mSession.getEmail();
            httpGet.addHeader("Authorization", auth);

            // Set up the header types needed to properly transfer JSON
            httpGet.setHeader("Content-Type", "application/json");
            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String responseBody = EntityUtils.toString(entity);
                        Log.e("hasil", responseBody);
                        try {
                            JSONObject object = new JSONObject(responseBody);
                            JSONObject objUser = object.getJSONObject("user");
                            mSession.setUserId(objUser.getString("id"));
                            mSession.setEmail(objUser.getString("email"));
                            mSession.setName(objUser.getString("name"));
                            mSession.setAlamat(objUser.getString("address"));
                            mSession.setNoHp(objUser.getString("phone"));
                            mSession.setNoKtp(objUser.getString("no_ktp"));
                            mSession.setUId(objUser.getString("uid"));
                            mSession.setReferalId(objUser.getString("referral_id"));
                            mSession.setTotalBalance(objUser.getString("total_balance"));
                            mSession.setTotalBonus(objUser.getString("total_bonus"));
                            mSession.setTotalPoint(objUser.getString("total_point"));
                            mSession.setPremium(objUser.getString("premium"));
                            mSession.setAvatar(objUser.getString("avatar_url"));
                            isFirstLogin = Boolean.parseBoolean((objUser.getString("pin_empty")));
                            success = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    success = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                if (success) {
                    name = mSession.getName();
                    totalBalance = mSession.getTotalBalance();
                    tvName.setText("Hi " + name);
                    tvSaldo.setText("Total Saldo Anda Rp." + totalBalance);

                    if (isFirstLogin) {
                        alertDialogFirstLogin();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
