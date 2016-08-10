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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.digipay.digipay.R;
import com.digipay.digipay.functions.ConnectionDetector;
import com.digipay.digipay.functions.Constant;
import com.digipay.digipay.functions.SessionManager;
import com.digipay.digipay.models.TopupWallet;

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

public class TopupWalletActivity extends AppCompatActivity {
    SessionManager mSession;
    private String jmlTransfer, token, email, tipePembayaran, namaBankSaya, pemilikRek, noRekSaya, namaBankTujuan, noRekTujuan, namaPenerima;
    private EditText edtPemilikRek, edtNoRek, edtNamaBank, edtJmlTransfer, edtNoRekTujuan, edtNamaPenerima;
    private RadioButton radioBankBca, radioBankMandiri;
    private LinearLayout linearButtonPembayaran, linearBank, linearCreditCard, linearDetailBankBca,
            linearDetailBankMandiri, linearDataKonfirmasi, linearBtnKonfirmasi;
    private FrameLayout frameRedPilih, frameGreyPilih, frameRedData, frameGreyData, frameRedKofirmasi, frameGreyKonfirmasi, frameKirim;
    private View viewYellowPilih, viewGreyPilih, viewYellowData, viewGreyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSession = new SessionManager(getApplicationContext());
        token = mSession.getToken();
        email = mSession.getEmail();

        edtPemilikRek = (EditText) findViewById(R.id.edtPemilikRek);
        edtNoRek = (EditText) findViewById(R.id.edtNoRek);
        edtNamaBank = (EditText) findViewById(R.id.edtNamaBank);
        edtJmlTransfer = (EditText) findViewById(R.id.edtJmlTransfer);
        edtNoRekTujuan = (EditText) findViewById(R.id.edtNoRekTujuan);
        edtNamaPenerima = (EditText) findViewById(R.id.edtNamaPenerima);
        radioBankBca = (RadioButton) findViewById(R.id.radioBca);
        radioBankMandiri = (RadioButton) findViewById(R.id.radioMandiri);

        linearButtonPembayaran = (LinearLayout) findViewById(R.id.linearButtonPembayaran);
        linearBank = (LinearLayout) findViewById(R.id.linearTransferBank);
        linearCreditCard = (LinearLayout) findViewById(R.id.linearCreditCard);
        linearDetailBankBca = (LinearLayout) findViewById(R.id.linearDetailBankBca);
        linearDetailBankMandiri = (LinearLayout) findViewById(R.id.linearDetailBankMandiri);
        linearDataKonfirmasi = (LinearLayout) findViewById(R.id.linearDataKonfirmasi);
        linearBtnKonfirmasi = (LinearLayout) findViewById(R.id.linearBtnKonfirmasi);

        frameRedPilih = (FrameLayout) findViewById(R.id.frameRedPilih);
        frameGreyPilih = (FrameLayout) findViewById(R.id.frameGreyPilih);
        frameRedData = (FrameLayout) findViewById(R.id.frameRedData);
        frameGreyData = (FrameLayout) findViewById(R.id.frameGreyData);
        frameRedKofirmasi = (FrameLayout) findViewById(R.id.frameRedKonfirmasi);
        frameGreyKonfirmasi = (FrameLayout) findViewById(R.id.frameGreyKonfirmasi);
        frameKirim = (FrameLayout) findViewById(R.id.frameKirim);

        viewYellowPilih = (View) findViewById(R.id.viewYellowPilih);
        viewGreyPilih = (View) findViewById(R.id.viewGreyPilih);
        viewYellowData = (View) findViewById(R.id.viewYellowData);
        viewGreyData = (View) findViewById(R.id.viewGreyData);

        Button btnTransferBank = (Button) findViewById(R.id.btnTranfBank);
        Button btnCreditCard = (Button) findViewById(R.id.btnCreditCard);
        final Button btnBackKonfrim = (Button) findViewById(R.id.btnBackKonfrim);
        final Button btnLanjutKonfrim = (Button) findViewById(R.id.btnLanjutKonfrim);
        ImageView imgBca = (ImageView) findViewById(R.id.imgBca);
        ImageView imgMandiri = (ImageView) findViewById(R.id.imgMandiri);

        final TextView tvDepositKet1 = (TextView) findViewById(R.id.tvDepositKet1);
        final TextView tvDepositKet2 = (TextView) findViewById(R.id.tvDepositKet2);

        // first condition
        tipePembayaran = "bank";
        frameRedPilih.setVisibility(View.VISIBLE);
        frameGreyPilih.setVisibility(View.GONE);
        viewGreyPilih.setVisibility(View.VISIBLE);
        linearButtonPembayaran.setVisibility(View.VISIBLE);
        linearBank.setVisibility(View.GONE);
        tvDepositKet1.setVisibility(View.VISIBLE);

        radioBankBca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bcaDetailBank();
            }
        });

        radioBankMandiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mandiriDetailBank();
            }
        });

        btnTransferBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearBank.setVisibility(View.VISIBLE);
                linearCreditCard.setVisibility(View.GONE);
                tvDepositKet1.setVisibility(View.GONE);
                tvDepositKet2.setVisibility(View.VISIBLE);
                tipePembayaran = "bank";
            }
        });

        btnCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearBank.setVisibility(View.GONE);
                linearCreditCard.setVisibility(View.VISIBLE);
                tipePembayaran = "kartu kredit";
                tvDepositKet1.setVisibility(View.GONE);
                tvDepositKet2.setVisibility(View.VISIBLE);
            }
        });

        btnBackKonfrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameRedPilih.setVisibility(View.VISIBLE);
                frameGreyPilih.setVisibility(View.GONE);
                frameRedData.setVisibility(View.GONE);
                frameGreyData.setVisibility(View.VISIBLE);
                viewGreyPilih.setVisibility(View.VISIBLE);
                viewYellowPilih.setVisibility(View.GONE);
                linearButtonPembayaran.setVisibility(View.VISIBLE);
                linearBank.setVisibility(View.VISIBLE);
                linearDetailBankBca.setVisibility(View.GONE);
                linearDetailBankMandiri.setVisibility(View.GONE);
                linearBtnKonfirmasi.setVisibility(View.GONE);

            }
        });

        imgBca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bcaDetailBank();
            }
        });

        imgMandiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mandiriDetailBank();
            }
        });


        btnLanjutKonfrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameRedKofirmasi.setVisibility(View.VISIBLE);
                frameGreyKonfirmasi.setVisibility(View.GONE);

                viewGreyData.setVisibility(View.GONE);
                viewYellowData.setVisibility(View.VISIBLE);

                linearDataKonfirmasi.setVisibility(View.VISIBLE);
                linearBank.setVisibility(View.GONE);
                linearCreditCard.setVisibility(View.GONE);
                linearDetailBankBca.setVisibility(View.GONE);
                linearDetailBankMandiri.setVisibility(View.GONE);
                linearButtonPembayaran.setVisibility(View.GONE);

                linearBtnKonfirmasi.setVisibility(View.GONE);
            }
        });

        frameKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pemilikRek = edtPemilikRek.getText().toString();
                noRekSaya = edtNoRek.getText().toString();
                namaBankTujuan = edtNamaBank.getText().toString();
                noRekTujuan = edtNoRekTujuan.getText().toString();
                namaPenerima = edtNamaPenerima.getText().toString();
                jmlTransfer = edtJmlTransfer.getText().toString();
                boolean isValid = true;
                if (TextUtils.isEmpty(pemilikRek)) {
                    edtPemilikRek.setError("Silahkan isi nama anda!");
                    isValid = false;
                }
                if (TextUtils.isEmpty(noRekSaya)) {
                    edtNoRek.setError("Silahkan isi no rekening anda!");
                    isValid = false;
                }
                if (TextUtils.isEmpty(namaBankTujuan)) {
                    edtNamaBank.setError("Silahkan isi nama bank tujuan!");
                    isValid = false;
                }
                if (TextUtils.isEmpty(noRekTujuan)) {
                    edtNoRekTujuan.setError("Silahkan isi no rekening tujuan!");
                    isValid = false;
                }
                if (TextUtils.isEmpty(namaPenerima)) {
                    edtNamaPenerima.setError("Silahkan isi nama penerima!");
                    isValid = false;
                }
                if (TextUtils.isEmpty(jmlTransfer)) {
                    edtJmlTransfer.setError("Silahkan isi jumlan transfer!");
                    isValid = false;
                }
                if (isValid)
                    alertDialogCheckTransfer();
            }
        });

    }

    private void bcaDetailBank() {
        radioBankBca.setChecked(true);
        radioBankMandiri.setChecked(false);
        linearBank.setVisibility(View.GONE);
        linearButtonPembayaran.setVisibility(View.GONE);
        linearCreditCard.setVisibility(View.GONE);
        linearDetailBankBca.setVisibility(View.VISIBLE);
        linearDetailBankMandiri.setVisibility(View.GONE);

        frameRedData.setVisibility(View.VISIBLE);
        frameGreyData.setVisibility(View.GONE);

        viewYellowPilih.setVisibility(View.VISIBLE);
        viewYellowData.setVisibility(View.GONE);
        viewGreyData.setVisibility(View.VISIBLE);
        viewGreyPilih.setVisibility(View.GONE);

        linearBtnKonfirmasi.setVisibility(View.VISIBLE);

        namaBankSaya = "BCA";
        edtNamaBank.setText("BCA");
        edtNoRekTujuan.setText("4910050180");
        edtNamaPenerima.setText("Dian Hariani");
    }

    private void mandiriDetailBank() {
        radioBankBca.setChecked(false);
        radioBankMandiri.setChecked(true);

        linearBank.setVisibility(View.GONE);
        linearButtonPembayaran.setVisibility(View.GONE);
        linearCreditCard.setVisibility(View.GONE);
        linearDetailBankBca.setVisibility(View.GONE);
        linearDetailBankMandiri.setVisibility(View.VISIBLE);

        frameRedData.setVisibility(View.VISIBLE);
        frameGreyData.setVisibility(View.GONE);

        viewYellowPilih.setVisibility(View.VISIBLE);
        viewYellowData.setVisibility(View.GONE);
        viewGreyData.setVisibility(View.VISIBLE);
        viewGreyPilih.setVisibility(View.GONE);

        linearBtnKonfirmasi.setVisibility(View.VISIBLE);

        namaBankSaya = "Mandiri";
        edtNamaBank.setText("MANDIRI");
        edtNoRekTujuan.setText("1250003238888");
        edtNamaPenerima.setText("Dian Hariani");
    }

    private void alertDialogCheckTransfer() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                TopupWalletActivity.this);
        alertDialogBuilder.setTitle("Cek Data");
        alertDialogBuilder
                .setMessage("Apakah yakin data sudah benar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        transferConfirmation();
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

    private void transferConfirmation() {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent;
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            new doTransferConfirmation().execute();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_connection),
                    Toast.LENGTH_LONG).show();
        }
    }


    private class doTransferConfirmation extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        Boolean success = false;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TopupWalletActivity.this);
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
                HttpPost post = new HttpPost(Constant.WEB_URL + "deposits");
                obj.put("source_type", tipePembayaran);
                obj.put("from_source_name", namaBankSaya);
                obj.put("from_account_name", pemilikRek);
                obj.put("from_account_number", noRekSaya);
                obj.put("to_source_name", namaBankTujuan);
                obj.put("to_account_name", namaPenerima);
                obj.put("to_account_number", noRekTujuan);
                obj.put("amount", Integer.parseInt(jmlTransfer));
                obj.put("image", "");
                obj.put("note", "Baru isi saldo");

                userObj.put("deposit", obj);
                String auth = "Token token=" + token + ", email=" + email;

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
                                JSONObject objRespon = new JSONObject(responseBody);
                                JSONObject objTc = objRespon.getJSONObject("deposit");

                                TopupWallet wallet = new TopupWallet();
                                wallet.setId(objTc.getInt("id"));
                                wallet.setUserId(objTc.getString("user_id"));
                                wallet.setSourceType(objTc.getString("source_type"));
                                wallet.setFromSourceName(objTc.getString("from_source_name"));
                                wallet.setFromAccountName(objTc.getString("from_account_name"));
                                wallet.setFromAccountNumber(objTc.getString("from_account_number"));
                                wallet.setToSourceName(objTc.getString("to_source_name"));
                                wallet.setToAccountName(objTc.getString("to_account_name"));
                                wallet.setToAccountName(objTc.getString("to_account_number"));
                                wallet.setAmount(objTc.getString("amount"));
                                status = objTc.getString("status");
                                wallet.setStatus(status);

                                success = true;
                            } else {
                                success = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error", e.getMessage());
                        }
                    } else {
                        success = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("error", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (!success) {
                Toast.makeText(TopupWalletActivity.this, "Gagal isi saldo", Toast.LENGTH_SHORT).show();
            } else {
                alertDialogSuccessTopup();
            }
        }
    }

    private void alertDialogSuccessTopup() {
        final AlertDialog.Builder dialogMessage = new AlertDialog.Builder(
                TopupWalletActivity.this);
        dialogMessage.setTitle("Topup Anda berhasil");
        dialogMessage
                .setMessage("Harap segera transfer, saldo akan segera kami proses.\n" +
                        "Silahkan cek di My Wallet untuk melihat saldo yang direquest. Jika nominal saldo Anda masih 0, \n" +
                        "Silahkan pilih salah satu menu di pojok kiri atas, kemudian kembali lagi ke My Wallet. Jika masih 0, \n" +
                        "lakukan hal tersebut berulang - ulang.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
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
