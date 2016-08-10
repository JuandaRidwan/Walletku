package com.digipay.digipay.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {
    private SessionManager mSession;
    private EditText edtEmailLupaPass;
    private String email, user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSession = new SessionManager(getApplicationContext());

        edtEmailLupaPass = (EditText) findViewById(R.id.edtMail);
        Button btnKirim = (Button) findViewById(R.id.btnKirim);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationAndForgotPass();
            }
        });

    }

    private void validationAndForgotPass() {
        email = edtEmailLupaPass.getText().toString();
        boolean isValid = true;

        if (TextUtils.isEmpty(email)) {
            edtEmailLupaPass.setError("Email tidak boleh kosong!");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()) {
            edtEmailLupaPass.setError("Email salah:john@example.com");
            isValid = false;
        }
        if (isValid) {
            forgotPass();
        }
    }

    private void forgotPass() {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent;
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            new doForgotPass().execute();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    private class doForgotPass extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        Boolean success = false;
        String id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForgotPasswordActivity.this);
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
                HttpPost post = new HttpPost(Constant.WEB_URL + "password_resets");
                obj.put("email", email);

                userObj.put("password_reset", obj);

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
                            if (!responseBody.equals(" ")) {
                                JSONObject objUser = new JSONObject(responseBody);
                                user_id = objUser.getString("id");
                                mSession.setEmail(objUser.getString("email"));
                                mSession.setName(objUser.getString("name"));
                                mSession.setToken(objUser.getString("token"));
                                mSession.setNoKtp(objUser.getString("no_ktp"));
                                mSession.setAlamat(objUser.getString("address"));
                                mSession.setNoHp(objUser.getString("phone"));
                                success = true;
                            } else {
                                success = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ForgotPasswordActivity.this, "TerjaDi kesalahan pada server, mohon mencoba kembali", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Terjadi kesalahan, Periksa kembali email! ",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Berhasil merubah password, silahkan cek email anda!",
                        Toast.LENGTH_LONG).show();
                Log.d("UserId", user_id);
                finish();
            }
        }
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
