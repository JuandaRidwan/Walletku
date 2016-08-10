package com.digipay.digipay.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digipay.digipay.R;
import com.digipay.digipay.functions.ConnectionDetector;
import com.digipay.digipay.functions.Constant;
import com.digipay.digipay.functions.SessionManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class PinDanPasswordActivity extends AppCompatActivity {
    private LinearLayout llGantiPassword, llGantiPin;
    private EditText edtPassLama, edtPassBaru, edtUlangiPass, edtPinLama, edtPinBaru, edtUlangiPin;
    private TextView tvOptGantiPass, tvOptGantiPin;
    private SessionManager mSession;
    private String passLama, passBaru, ulangiPass, pinLama, pinBaru, ulangiPin, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_dan_pssword);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSession = new SessionManager(getApplicationContext());

        llGantiPassword = (LinearLayout) findViewById(R.id.llGantiPassword);
        llGantiPin = (LinearLayout) findViewById(R.id.llGantiPin);
        edtPassLama = (EditText) findViewById(R.id.edtPassLama);
        edtPassBaru = (EditText) findViewById(R.id.edtPassBaru);
        edtUlangiPass = (EditText) findViewById(R.id.edtUlangiPassBaru);
        edtPinLama = (EditText) findViewById(R.id.edtPinLama);
        edtPinBaru = (EditText) findViewById(R.id.edtPinBaru);
        edtUlangiPin = (EditText) findViewById(R.id.edtUlangiPinBaru);
        tvOptGantiPin = (TextView) findViewById(R.id.tvOptGantiPin);
        tvOptGantiPass = (TextView) findViewById(R.id.tvOptGantiPass);
        Button btnSimpanPass = (Button) findViewById(R.id.btnGantiPass);
        Button btnSimpanPin = (Button) findViewById(R.id.btnGantiPin);

        tvOptGantiPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llGantiPassword.setVisibility(View.GONE);
                llGantiPin.setVisibility(View.VISIBLE);
                tvOptGantiPin.setVisibility(View.GONE);
                tvOptGantiPass.setVisibility(View.VISIBLE);
            }
        });

        tvOptGantiPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llGantiPassword.setVisibility(View.VISIBLE);
                llGantiPin.setVisibility(View.GONE);
                tvOptGantiPin.setVisibility(View.VISIBLE);
                tvOptGantiPass.setVisibility(View.GONE);
            }
        });

        btnSimpanPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinLama = edtPinLama.getText().toString();
                pinBaru = edtPinBaru.getText().toString();
                ulangiPin = edtUlangiPin.getText().toString();
                Boolean isValid = true;
                if (TextUtils.isEmpty(pinLama)) {
                    edtPinLama.setError("Silahkan masukan pin lama anda!");
                    isValid = false;
                }
                if (TextUtils.isEmpty(pinBaru)) {
                    edtPinBaru.setError("Silahkan masukan pin baru anda!");
                    isValid = false;
                } else if (!pinBaru.equals(ulangiPin)) {
                    edtUlangiPin.setError("Pin tidak cocok!");
                    isValid = false;
                }
                if (isValid) {
                    changePin();
                }
            }
        });

        btnSimpanPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passLama = edtPassLama.getText().toString();
                passBaru = edtPassBaru.getText().toString();
                ulangiPass = edtUlangiPass.getText().toString();
                Boolean isValid = true;
                if (TextUtils.isEmpty(passLama)) {
                    edtPassLama.setError("Silahkan masukan password lama anda!");
                    isValid = false;
                }
                if (TextUtils.isEmpty(passBaru)) {
                    edtPassBaru.setError("Silahkan masukan password baru anda!");
                    isValid = false;
                } else if (passBaru.length() < 8) {
                    edtPassBaru.setError("Password kurang dari 8 karakter!");
                    isValid = false;
                } else if (!passBaru.equals(ulangiPass)) {
                    edtUlangiPass.setError("Password tidak cocok!");
                    isValid = false;
                }
                if (isValid) {
                    changePassword();
                }
            }
        });
    }

    private void changePassword() {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent;
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            new doChangePassword().execute();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    private class doChangePassword extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        Boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PinDanPasswordActivity.this);
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
            HttpResponse response;
            JSONObject userObj = new JSONObject();
            JSONObject obj = new JSONObject();

            try {
                HttpPut put = new HttpPut(Constant.WEB_URL + "users/" + mSession.getUserId());
                obj.put("old_password", passLama);
                obj.put("password", passBaru);
                obj.put("password_confirmation", ulangiPass);

                userObj.put("user", obj);
                String auth = "Token token=" + mSession.getToken() + ", email=" + mSession.getEmail();

                StringEntity se = new StringEntity(userObj.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                put.setEntity(se);
                put.addHeader("Authorization", auth);
                response = client.execute(put);

                if (response != null) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String responseBody = EntityUtils.toString(entity);
                        Log.e("hasil", responseBody);
                        if (responseBody != null) {
                            JSONObject object = new JSONObject(responseBody);
                            if (object.has("user")) {
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
                                success = true;
                            } else {
                                JSONObject objError = object.getJSONObject("errors");
                                message = objError.getString("message");
                                success = false;
                            }
                        } else {
                            success = false;
                        }
                    } else {
                        success = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (!success) {
                Toast.makeText(PinDanPasswordActivity.this, message,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PinDanPasswordActivity.this, "Password berhasil di ubah.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    private void changePin() {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent;
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            new doChangePin().execute();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    private class doChangePin extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PinDanPasswordActivity.this);
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
            HttpResponse response;
            JSONObject userObj = new JSONObject();
            JSONObject obj = new JSONObject();

            try {
                HttpPut put = new HttpPut(Constant.WEB_URL + "users/" + mSession.getUserId());
                obj.put("old_pin", pinLama);
                obj.put("pin", pinBaru);
                obj.put("pin_confirmation", ulangiPin);

                userObj.put("user", obj);
                String auth = "Token token=" + mSession.getToken() + ", email=" + mSession.getEmail();

                StringEntity se = new StringEntity(userObj.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                put.setEntity(se);
                put.addHeader("Authorization", auth);
                response = client.execute(put);

                if (response != null) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String responseBody = EntityUtils.toString(entity);
                        Log.e("hasil", responseBody);
                        if (responseBody != null) {
                            JSONObject object = new JSONObject(responseBody);
                            if (object.has("user")) {
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
                                success = true;
                            } else {
                                JSONObject objError = object.getJSONObject("errors");
                                message = objError.getString("message");
                                success = false;
                            }
                        } else {
                            success = false;
                        }
                    } else {
                        success = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (!success) {
                alertDialogPin();
            } else {
                Toast.makeText(PinDanPasswordActivity.this, "PIN berhasil di ubah.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void alertDialogPin() {
        if (message == null) {
            message = "PIN Anda Salah";
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PinDanPasswordActivity.this);
        alertDialogBuilder.setTitle("Terjadi Kesalahan");
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
