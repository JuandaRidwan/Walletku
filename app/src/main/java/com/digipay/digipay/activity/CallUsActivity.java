package com.digipay.digipay.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import org.json.JSONException;
import org.json.JSONObject;

public class CallUsActivity extends AppCompatActivity {
    private SessionManager mSession;
    private EditText edtMessage;
    private TextView tvDivision;
    private String fieldDivision, fieldMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSession = new SessionManager(getApplicationContext());
        tvDivision = (TextView) findViewById(R.id.edtDivision);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        ImageView imgCall = (ImageView) findViewById(R.id.imgCall);
        ImageView imgCall1 = (ImageView) findViewById(R.id.imgCall1);
        ImageView imgCall2 = (ImageView) findViewById(R.id.imgCall2);
        FrameLayout frameSend = (FrameLayout) findViewById(R.id.frameKirim);
        fieldDivision = "Customer Service";

        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // note: string "tel", jgn di ganti atau di hapus, itu key utk get intent dr contact nya
                    String noHp = "02185903636";
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", noHp, null));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        imgCall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // note: string "tel", jgn di ganti atau di hapus, itu key utk get intent dr contact nya
                    String noHp1 = "081574402443";
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", noHp1, null));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        imgCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // note: string "tel", jgn di ganti atau di hapus, itu key utk get intent dr contact nya
                    String noHp2 = "081284991001";
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", noHp2, null));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        frameSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldDivision = tvDivision.getText().toString();
                fieldMessage = edtMessage.getText().toString();
                boolean isValid = true;
                if (TextUtils.isEmpty(fieldDivision)) {
                    tvDivision.setError("Silahkan masukkan bagian yang ingi di hubungi!");
                    isValid = false;
                }
                if (TextUtils.isEmpty(fieldMessage)) {
                    edtMessage.setError("Silahkan masukkan pesan anda!");
                    isValid = false;
                }
                if (isValid) {
                    callUs();
                }
            }
        });
    }

    private void callUs() {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent;
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            new doCallUs().execute();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    private class doCallUs extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        Boolean success = false;
        int id, userId;
        String division, message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CallUsActivity.this);
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
                HttpPost post = new HttpPost(Constant.WEB_URL + "contacts");
                obj.put("division", fieldDivision);
                obj.put("message", fieldMessage);

                userObj.put("contact", obj);
                String auth = "Token token=" + mSession.getToken() + ", email=" + mSession.getEmail();

                StringEntity se = new StringEntity(userObj.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                post.addHeader("Authorization", auth);
                response = client.execute(post);

                if (response != null) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String responseBody = EntityUtils.toString(entity);
                        Log.d("hasil", responseBody);
                        try {
                            if (responseBody != null) {
                                JSONObject objContact = new JSONObject(responseBody);
                                JSONObject objRespon = objContact.getJSONObject("contact");

                                id = objRespon.getInt("id");
                                userId = objRespon.getInt("user_id");
                                division = objRespon.getString("division");
                                message = objRespon.getString("message");

                                success = true;
                            } else {
                                success = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                Toast.makeText(CallUsActivity.this, "Terjadi kesalahan, Mohon mencoba kembali!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CallUsActivity.this, "Terimakasih untuk masukkannya, pesan Anda akan kami sampaikan kepada "
                                + division,
                        Toast.LENGTH_LONG).show();
                tvDivision.setText("");
                edtMessage.setText("");
            }

        }
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
