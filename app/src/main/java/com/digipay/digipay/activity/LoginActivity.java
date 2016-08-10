package com.digipay.digipay.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.digipay.digipay.R;
import com.digipay.digipay.functions.ConnectionDetector;
import com.digipay.digipay.functions.Constant;
import com.digipay.digipay.functions.SessionEmailLastLogin;
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

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private SessionManager mSession;
    private SessionEmailLastLogin emailLastLoginSession;
    private String pass, email;
    boolean isSessionEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        mSession = new SessionManager(getApplicationContext());
        emailLastLoginSession = new SessionEmailLastLogin(getApplicationContext());

        edtEmail = (EditText) findViewById(R.id.edtEmailLogin);
        edtPassword = (EditText) findViewById(R.id.edtPass);
        Button btnRegister = (Button) findViewById(R.id.btnDaftar);
        TextView tvForgotPass = (TextView) findViewById(R.id.tvLupaPass);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        isSessionEmail = false;

        String emailSession = emailLastLoginSession.getEmailLastLogin();
        if (emailSession != null) {
            edtEmail.setText(emailSession);
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationAndLogin();
            }
        });
    }

    private void validationAndLogin() {
        email = edtEmail.getText().toString();
        pass = edtPassword.getText().toString();

        Boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email tidak boleh kosong!");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()) {
            edtEmail.setError("Email salah:john@example.com");
            isValid = false;
        }
        if (TextUtils.isEmpty(pass)) {
            edtPassword.setError("Silahkan masukan Password!");
            isValid = false;
        }
        if (isValid) {
            login();
        }
    }

    private void login() {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent;
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            new doLogin().execute();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    private class doLogin extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        Boolean success = false;
        String id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
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
                HttpPost post = new HttpPost(Constant.WEB_URL + "login");
                obj.put("email", email);
                obj.put("password", pass);

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
                        if (responseBody != null) {

                            JSONObject objUser = new JSONObject(responseBody);
                            mSession.setUserId(objUser.getString("id"));
                            mSession.setEmail(objUser.getString("email"));
                            mSession.setName(objUser.getString("name"));
                            mSession.setToken(objUser.getString("token"));
                            mSession.setNoKtp(objUser.getString("no_ktp"));
                            mSession.setAlamat(objUser.getString("address"));
                            mSession.setNoHp(objUser.getString("phone"));
                            mSession.setPremium(objUser.getString("premium"));
                            mSession.setUId(objUser.getString("uid"));
                            mSession.setReferalId(objUser.getString("referral_id"));
                            mSession.setTotalBalance(String.valueOf(objUser.getInt("total_balance")));
                            mSession.setTotalBonus(String.valueOf(objUser.getInt("total_bonus")));
                            mSession.setTotalPoint(String.valueOf(objUser.getInt("total_point")));
                            mSession.setAvatar(objUser.getString("avatar_url"));
                            success = true;
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
                Toast.makeText(LoginActivity.this, "Terjadi kesalahan, Periksa kembali email atau password!",
                        Toast.LENGTH_SHORT).show();

            } else {
                if (email != null) {
                    if (isSessionEmail) {
                        emailLastLoginSession.setEmailLastLogin(email);
                    }
                }
                Intent i = new Intent(LoginActivity.this, MainMenuActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }

        }
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.cbIngatkanSaya:
                if (checked) {
                    isSessionEmail = true;
                } else {
                    isSessionEmail = false;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
