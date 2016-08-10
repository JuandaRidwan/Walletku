package com.digipay.digipay.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.digipay.digipay.R;
import com.digipay.digipay.functions.ConnectionDetector;
import com.digipay.digipay.functions.Constant;
import com.digipay.digipay.functions.SessionManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class RegisterActivity extends AppCompatActivity {
    private SessionManager mSession;
    private EditText edtNama, edtEmail, edtAlamat, edtNoTlp, edtPassword,
            edtUlangiPassword, edtNoReferral;
    private TextView tvCekDaftar;
    private CheckBox cbCheckTerms;
    private RadioGroup radioUsersGroup;
    private RadioButton radioUsersButton;
    private String email, name, phone, referral_id, address, pass, confrim_pass, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        mSession = new SessionManager(getApplicationContext());

        radioUsersGroup = (RadioGroup) findViewById(R.id.radioUsers);
        RadioButton radioPremium = (RadioButton) findViewById(R.id.radioPremium);
        RadioButton radioReguler = (RadioButton) findViewById(R.id.radioReguler);
        edtNama = (EditText) findViewById(R.id.edtNama);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtAlamat = (EditText) findViewById(R.id.edtAlamat);
        edtNoTlp = (EditText) findViewById(R.id.edtNoTlp);
        edtPassword = (EditText) findViewById(R.id.edtPass);
        edtUlangiPassword = (EditText) findViewById(R.id.edtUlangPass);
        edtNoReferral = (EditText) findViewById(R.id.edtNoRefferal);
        cbCheckTerms = (CheckBox) findViewById(R.id.cbTerms);
        tvCekDaftar = (TextView) findViewById(R.id.text);
        Button btnDaftar = (Button) findViewById(R.id.btnDaftarReg);
        edtNoReferral.setVisibility(View.GONE);

        radioPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogPremium();
                edtNoReferral.setVisibility(View.VISIBLE);
            }
        });

        radioReguler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogReguler();
                edtNoReferral.setVisibility(View.GONE);
            }
        });

        cbCheckTerms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    tvCekDaftar.setText("true");
                } else {
                    tvCekDaftar.setText("false");
                }

            }
        });

        if (btnDaftar != null) {
            btnDaftar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cbCheckTerms.isChecked()) {
                        validationAndRegister();
                    } else {
                        Snackbar.make(v, "Anda belum menyetujui persyaratan!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
        }
    }

    private void validationAndRegister() {
        name = edtNama.getText().toString();
        email = edtEmail.getText().toString();
        address = edtAlamat.getText().toString();
        phone = edtNoTlp.getText().toString();
        pass = edtPassword.getText().toString();
        confrim_pass = edtUlangiPassword.getText().toString();
        referral_id = edtNoReferral.getText().toString();

        boolean isValid = true;
        if (TextUtils.isEmpty(name)) {
            edtNama.setError("Nama tidak boleh kosong!");
            isValid = false;
        }
        if (TextUtils.isEmpty(phone)) {
            edtNoTlp.setError("Nomor Telepon tidak boleh kosong!");
            isValid = false;
        }
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email tidak boleh kosong!");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()) {
            edtEmail.setError("Email salah:john@example.com");
            isValid = false;
        }
        if (TextUtils.isEmpty(address)) {
            edtAlamat.setError("Alamat tidak boleh kosong!");
            isValid = false;
        }
        if (TextUtils.isEmpty(pass)) {
            edtPassword.setError("Silahkan masukan password!");
            isValid = false;
        } else if (pass.length() < 8) {
            edtPassword.setError("Password kurang dari 8 karakter!");
            isValid = false;
        } else if (!pass.equals(confrim_pass)) {
            edtUlangiPassword.setError("Password tidak cocok!");
            isValid = false;
        }
        if (isValid) {
            alertDialogCheckRegister();
        }
    }

    private void alertDialogCheckRegister() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                RegisterActivity.this);
        alertDialogBuilder.setTitle("Cek Data");
        alertDialogBuilder
                .setMessage("Apakah yakin data sudah benar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int selectedId = radioUsersGroup.getCheckedRadioButtonId();
                        radioUsersButton = (RadioButton) findViewById(selectedId);
                        if (radioUsersButton.getText().equals("Premium")) {
                            register();
                        } else {
                            register();
                        }
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

    private void alertDialogReguler() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                RegisterActivity.this);
        alertDialogBuilder.setTitle("Tipe Reguler:");
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.alert_dialog_reguler))
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

    private void alertDialogPremium() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                RegisterActivity.this);
        alertDialogBuilder.setTitle("Tipe Premium User:");
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.alert_dialog_premium))
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

    private void register() {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent;
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            new doRegister().execute();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    private class doRegister extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        Boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
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
                HttpPost post = new HttpPost(Constant.WEB_URL + "users");
                obj.put("email", email);
                obj.put("name", name);
                obj.put("address", address);
                obj.put("phone", phone);
                obj.put("password", pass);
                obj.put("password_confirmation", confrim_pass);
                obj.put("referral_id", referral_id);
                obj.put("terms", "true");

                userObj.put("user", obj);

                StringEntity se = new StringEntity(userObj.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                response = client.execute(post);

                if (response != null) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String responseBody = EntityUtils.toString(entity);
                        Log.e("hasil", responseBody);
                        try {
                            if (responseBody != null) {
                                JSONObject jsonObj = new JSONObject(responseBody);
                                if (jsonObj.has("user")) {
                                    JSONObject objUser = jsonObj.getJSONObject("user");
                                    String id = objUser.getString("id");
                                    mSession.setEmail(objUser.getString("email"));
                                    mSession.setName(objUser.getString("name"));
                                    mSession.setAlamat(objUser.getString("address"));
                                    mSession.setNoHp(objUser.getString("phone"));
                                    mSession.setNoKtp(objUser.getString("no_ktp"));
                                    mSession.setUId(objUser.getString("uid"));
                                    mSession.setReferalId(objUser.getString("referral_id"));
                                    mSession.setTotalBalance(String.valueOf(objUser.getInt("total_balance")));
                                    mSession.setTotalBonus(String.valueOf(objUser.getInt("total_bonus")));
                                    mSession.setTotalPoint(String.valueOf(objUser.getInt("total_point")));
                                    mSession.setPremium(objUser.getString("premium"));
                                    mSession.setAvatar(objUser.getString("avatar_url"));
                                    success = true;
                                } else {
                                    JSONObject objError = jsonObj.getJSONObject("errors");
                                    message = objError.getString("message");
                                    success = false;
                                }
                            } else {
                                success = false;
                            }
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
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
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (!success) {
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_SHORT).show();
                mSession.setPremium("");
            } else {
                if (radioUsersButton.getText().equals("Premium")) {
                    Toast.makeText(getApplicationContext(), "Aktivasi kami kirim ke email anda, silahkan cek email!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Aktivasi kami kirim ke email anda, silahkan cek email!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        }
    }

}
