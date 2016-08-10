package com.digipay.digipay.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digipay.digipay.R;
import com.digipay.digipay.functions.ConnectionDetector;
import com.digipay.digipay.functions.Constant;
import com.digipay.digipay.functions.SessionManager;
import com.digipay.digipay.models.Nominal;
import com.digipay.digipay.models.Provider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class TopupPulsaActivity extends AppCompatActivity {
    private Spinner SpinnerProvider, SpinnerNominal, SpinnerStatus;
    private EditText edtNohp;
    private TextView tvPrice;
    private SessionManager mSession;
    private int id_nominals, idProvider;
    private ArrayList<String> nominalList = new ArrayList<>();
    private ArrayList<Nominal> nominalListNew = new ArrayList<>();
    private ArrayList<String> providerList = new ArrayList<>();
    private ArrayList<Provider> providerListAll = new ArrayList<>();
    private String message, pin, status, no_hp;
    private ArrayAdapter<String> nominalAdapter;
    private ArrayAdapter<CharSequence> statusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_pulsa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSession = new SessionManager(getApplicationContext());

        SpinnerProvider = (Spinner) findViewById(R.id.SpinnerProvider);
        SpinnerNominal = (Spinner) findViewById(R.id.SpinnerNominal);
        SpinnerStatus = (Spinner) findViewById(R.id.SpinnerStatus);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        TextView tvSatuanUnit = (TextView) findViewById(R.id.tvSatuanUnit);
        edtNohp = (EditText) findViewById(R.id.edtNoHp);
        Button btnSend = (Button) findViewById(R.id.btnKirim);
        tvSatuanUnit.setVisibility(View.GONE);

        new getProvider().execute();

        statusAdapter = ArrayAdapter
                .createFromResource(getApplicationContext(), R.array.status_list,
                        R.layout.my_text_black);
        statusAdapter.setDropDownViewResource(R.layout.my_text_black);


        SpinnerProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
//                provider = String.valueOf(parent.getItemAtPosition(position));
                Provider provider = providerListAll.get(position);
                idProvider = provider.getIdProvider();

                nominalListNew = new ArrayList<Nominal>();
                nominalListNew = provider.getNominalList();
                nominalList = new ArrayList<String>();
                for (Nominal nominal : nominalListNew) {
                    nominalList.add(nominal.getAmount());
                }

                nominalAdapter = new ArrayAdapter<String>(TopupPulsaActivity.this, android.R.layout.simple_spinner_item, nominalList);
                nominalAdapter.setDropDownViewResource(R.layout.my_text_black);
                SpinnerNominal.setAdapter(nominalAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        SpinnerNominal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Nominal nominal = nominalListNew.get(position);
                id_nominals = nominal.getIdNominal();
                tvPrice.setText("Rp" + nominal.getPrice() + ",00");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        SpinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String statusData = String.valueOf(parent.getItemAtPosition(position));
                if (statusData.equals("real")) {
                    status = "";
                }
                if (statusData.equals("success")) {
                    status = "success";
                }
                if (statusData.equals("failed")) {
                    status = "failed";
                }
                if (statusData.equals("pending")) {
                    status = "pending";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        if (btnSend != null) {
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogCheckKirim();
                }
            });
        }
    }

    private void alertDialogCheckKirim() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                TopupPulsaActivity.this);
        alertDialogBuilder.setTitle("Cek Data");
        alertDialogBuilder
                .setMessage("Apakah yakin data sudah benar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        no_hp = edtNohp.getText().toString();
                        if (!no_hp.equals("")) {
                            alertDialogPin();
                        } else {
                            Toast.makeText(TopupPulsaActivity.this, "Silahkan isi nomor Handphone", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class getProvider extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TopupPulsaActivity.this);
            pDialog.setMessage("Mohon Tunggu...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

//            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Constant.WEB_URL + "providers" + "?payment_type=topup");

            // Add authorization header
//            httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials("user", "password"), "UTF-8", false));
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
                            JSONObject objProvider = new JSONObject(responseBody);
                            JSONArray arrayProvider = objProvider.getJSONArray("providers");
                            for (int i = 0; i < arrayProvider.length(); i++) {
                                JSONObject object = arrayProvider.getJSONObject(i);
                                Provider provider = new Provider();

                                int id = object.getInt("id");
                                String name_provider = object.getString("name");
                                providerList.add(name_provider);

                                provider.setIdProvider(id);
                                provider.setNamaProvider(name_provider);

                                JSONArray arrayNominals = object.getJSONArray("nominals");
                                ArrayList<Nominal> nominalListAll = new ArrayList<>();
                                for (int j = 0; j < arrayNominals.length(); j++) {
                                    JSONObject objNominal = arrayNominals.getJSONObject(j);

                                    Nominal nominal = new Nominal();
                                    nominal.setIdNominal(objNominal.getInt("id"));
                                    nominal.setAmount(objNominal.getString("name"));
                                    nominal.setAlias(objNominal.getString("alias"));
                                    nominal.setPrice(objNominal.getString("price"));

                                    nominalListAll.add(nominal);

                                }

                                provider.setNominalList(nominalListAll);
                                providerListAll.add(provider);

                                success = true;

                            }
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
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (!success) {
                Toast.makeText(TopupPulsaActivity.this, "Tidak dapat terhubung ke server, silahkan periksa koneksi internet Anda.",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Creating provider adapter
                tvPrice.setVisibility(View.VISIBLE);
                ArrayAdapter<String> providerAdapter = new ArrayAdapter<String>(TopupPulsaActivity.this, android.R.layout.simple_spinner_item, providerList);
                providerAdapter.setDropDownViewResource(R.layout.my_text_black);
                // set Spinner adapter provider
                SpinnerProvider.setAdapter(providerAdapter);
                // set Spinner adapter status
                SpinnerStatus.setAdapter(statusAdapter);
            }

        }
    }

    private void alertDialogPin() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TopupPulsaActivity.this);
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage("Silahkan isi pin anda");

        final EditText input = new EditText(TopupPulsaActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setPadding(10, 10, 10, 10);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_wallet);

        alertDialog.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        pin = input.getText().toString();
                        if (!pin.equals("")) {
                            topupPulsa();
                        } else {
                            Toast.makeText(TopupPulsaActivity.this, "Silahkan isi pin Anda", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        alertDialog.setNegativeButton("TIDAK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    private void topupPulsa() {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent;
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            new doTopupPulsa().execute();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    private class doTopupPulsa extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false;
        boolean nothingRespon = false;
        String msisdn, payment_type;
        int id_payment, amount_payment;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TopupPulsaActivity.this);
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
                HttpPost post = new HttpPost(Constant.WEB_URL + "payments");
                obj.put("provider_id", Integer.toString(idProvider));
                obj.put("nominal_id", Integer.toString(id_nominals));
                obj.put("msisdn", no_hp);
                obj.put("payment_type", "topup");
                obj.put("pin", pin);
                obj.put("test", status); // failed, success, pending, "" = real

                userObj.put("payment", obj);
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
                        Log.e("hasil", responseBody);
                        try {
                            if (responseBody != null) {
                                JSONObject objData = new JSONObject(responseBody);
                                if (objData.has("payment")) {
                                    JSONObject objPayment = objData.getJSONObject("payment");
                                    id_payment = objPayment.getInt("id");
                                    amount_payment = objPayment.getInt("amount");
                                    status = objPayment.getString("status");
                                    msisdn = objPayment.getString("msisdn");
                                    message = objPayment.getString("message");
                                    payment_type = objPayment.getString("payment_type");
                                    mSession.setTotalBalance(String.valueOf(objPayment.getInt("total_balance")));
                                    mSession.setTotalBonus(String.valueOf(objPayment.getInt("total_bonus")));
                                    mSession.setTotalPoint(String.valueOf(objPayment.getInt("total_point")));
                                    success = true;
                                } else {
                                    JSONObject objError = objData.getJSONObject("errors");
                                    message = objError.getString("message");
                                    success = false;
                                }
                            } else {
                                success = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        success = false;
                    }
                } else {
                    nothingRespon = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                nothingRespon = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                if (nothingRespon) {
                    message = "Tidak dapat terhubung ke server. Mohon mencoba kembali.";
                    alertDialogMessage();
                } else if (!success) {
                    if (message.equals("PIN incorrect")) {
                        message = "PIN Anda Salah.";
                        alertDialogMessage();
                    }
                    no_hp = "";
                } else if (status.equals("success")) {
                    message = "Transaksi berhasil.";
                    alertDialogMessage();
                } else if (status.equals("failed")) {
                    message = "Transaksi gagal.";
                    alertDialogMessage();
                } else {
                    alertDialogMessage();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(TopupPulsaActivity.this, "Gagal koneksi ke server, mohon mencoba kembali", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void alertDialogMessage() {
        String title;
        if (message.equals("")) {
            Log.d("masuk", "message kosong");
            message = "Transaksi gagal.";
        }
        if (status != null) {
            title = status;
        } else {
            title = "WalletKu";
        }

        final AlertDialog.Builder dialogMessage = new AlertDialog.Builder(
                TopupPulsaActivity.this);
        dialogMessage.setTitle(title);
        dialogMessage
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = dialogMessage.create();
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