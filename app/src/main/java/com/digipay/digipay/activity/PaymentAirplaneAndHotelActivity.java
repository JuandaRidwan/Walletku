package com.digipay.digipay.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digipay.digipay.R;
import com.digipay.digipay.functions.Constant;
import com.digipay.digipay.functions.SessionManager;
import com.digipay.digipay.functions.SessionSearchBook;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PaymentAirplaneAndHotelActivity extends AppCompatActivity {
    private SessionManager mSession;
    private SessionSearchBook mSessionBook;
    private String errorMessage, pin, isHotelOrAirplane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_airplane_ticket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                isHotelOrAirplane = null;
            } else {
                isHotelOrAirplane = extras.getString("isHotelOrAirplane");
            }
        } else {
            isHotelOrAirplane = (String) savedInstanceState.getSerializable("isHotelOrAirplane");
        }

        mSession = new SessionManager(getApplicationContext());
        mSessionBook = new SessionSearchBook(getApplicationContext());

        LinearLayout llPaymentDepart = (LinearLayout) findViewById(R.id.llPaymentDepart);
        LinearLayout llPaymentReturn = (LinearLayout) findViewById(R.id.llPaymentReturn);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvBalance = (TextView) findViewById(R.id.tvBalance);
        TextView tvSubTotal = (TextView) findViewById(R.id.tvSubTotal);
        TextView tvTotalPayment = (TextView) findViewById(R.id.tvTotalPayment);
        TextView tvTittleDepart = (TextView) findViewById(R.id.tvTittleDepart);
        TextView tvPaymentDepart = (TextView) findViewById(R.id.tvPaymentDepart);
        TextView tvTittleReturn = (TextView) findViewById(R.id.tvTittleReturn);
        TextView tvPaymentReturn = (TextView) findViewById(R.id.tvPaymentReturn);
        TextView tvAdminFee = (TextView) findViewById(R.id.tvAdminFee);
        FrameLayout frameKembali = (FrameLayout) findViewById(R.id.frameKembali);
        FrameLayout frameBayar = (FrameLayout) findViewById(R.id.frameBayar);

        llPaymentReturn.setVisibility(View.GONE);

        tvName.setText("Hi " + mSession.getName());
        tvBalance.setText("Total Saldo Anda Rp." + mSession.getTotalBalance());

        if (isHotelOrAirplane != null && isHotelOrAirplane.equals("airplane")) {
            tvSubTotal.setText("Rp." + mSessionBook.getTotalPrice());
            tvTittleDepart.setText(mSessionBook.getFromDepart() + " - " + mSessionBook.getToDepart());
            tvPaymentDepart.setText("Rp." + mSessionBook.getTicketPrice());
            tvAdminFee.setText("Rp." + mSessionBook.getAdminFee());

            int totPrice = Integer.parseInt(mSessionBook.getTicketPrice());
            int totAdminFee = Integer.parseInt(mSessionBook.getAdminFee());
            int totPayment = totPrice + totAdminFee;
            tvTotalPayment.setText("Rp." + totPayment);
        } else {
            tvSubTotal.setText("Rp." + mSession.getTotalPrice());
            tvTittleDepart.setText("Total Harga Hotel: ");
            tvPaymentDepart.setText("Rp." + mSession.getTotalPrice());
            tvAdminFee.setText("Rp." + mSession.getAdminFeeHotel());

            int totPrice = Integer.parseInt(mSession.getTotalPrice());
            int totAdminFee = Integer.parseInt(mSession.getAdminFeeHotel());
            int totPayment = totPrice + totAdminFee;
            tvTotalPayment.setText("Rp." + totPayment);
        }

        frameKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        frameBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogPin();
            }
        });


    }

    private void alertDialogPin() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaymentAirplaneAndHotelActivity.this);
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage("Silahkan isi pin Anda");

        final EditText input = new EditText(PaymentAirplaneAndHotelActivity.this);
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
                            if (isHotelOrAirplane != null && isHotelOrAirplane.equals("airplane")) {
                                new getPaymentBookAirplane().execute();
                            } else {
                                new getPaymentBookHotel().execute();
                            }
                        } else {
                            Toast.makeText(PaymentAirplaneAndHotelActivity.this, "Silahkan isi pin Anda", Toast.LENGTH_SHORT).show();
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

    private class getPaymentBookAirplane extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PaymentAirplaneAndHotelActivity.this);
            pDialog.setMessage("Mohon Tunggu...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                HttpClient client = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(Constant.WEB_URL + "flights/issue?book_id=" + mSessionBook.getBookId()
                        + "&payment_type=deposit&pin=" + pin);

                // Add authorization header
                String auth = "Token token=" + mSession.getToken() + ",email=" + mSession.getEmail();
                httpPost.addHeader("Authorization", auth);
                // Set up the header types needed to properly transfer JSON
                httpPost.setHeader("Content-Type", "application/json");

                try {
                    HttpResponse response = client.execute(httpPost);
                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    if (statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            String responseBody = EntityUtils.toString(entity);
                            Log.e("RIDONE", "hasil == " + responseBody);
                            try {
                                JSONObject objData = new JSONObject(responseBody);
                                JSONObject objPayment = objData.getJSONObject("payment");
                                int id = objPayment.getInt("id");
                                int amount = objPayment.getInt("amount");
                                status = objPayment.getString("status");
                                String msisdn = objPayment.getString("msisdn");
                                String message = objPayment.getString("message");
                                String paymentType = objPayment.getString("payment_type");
                                String nominal = objPayment.getString("nominal");
                                String provider = objPayment.getString("provider");
                                String serialNumber = objPayment.getString("serial_number");
                                String date = objPayment.getString("date");
                                int totalBalance = objPayment.getInt("total_balance");
                                int totalBonus = objPayment.getInt("total_bonus");
                                int totalPoint = objPayment.getInt("total_point");

                                success = true;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            String responseBody = EntityUtils.toString(entity);
                            JSONObject objError = new JSONObject(responseBody);
                            JSONObject objErrorMsg = objError.getJSONObject("errors");
                            errorMessage = (objErrorMsg.getString("message"));
                            success = false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            } catch (Exception e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (!success) {
                if (errorMessage != null) {
                    Toast.makeText(PaymentAirplaneAndHotelActivity.this, errorMessage,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaymentAirplaneAndHotelActivity.this, getString(R.string.tidak_dapat_terhubung_ke_server),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                if (status != null) {
                    Toast.makeText(PaymentAirplaneAndHotelActivity.this, "Tiket pesawat " + status + " di booking",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaymentAirplaneAndHotelActivity.this, "Tiket pesawat berhasil di booking",
                            Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(PaymentAirplaneAndHotelActivity.this, MainMenuActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        }
    }

    private class getPaymentBookHotel extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PaymentAirplaneAndHotelActivity.this);
            pDialog.setMessage("Mohon Tunggu...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                HttpClient client = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(Constant.WEB_URL + "hotels/hotel_pay?session_id=" + mSession.getSessionIdHotelBook()
                        + "&room_code=" + mSession.getRoomCode() + "&pin=" + pin + "&guest_phone=" + mSession.getNoHp()
                        + "&guest_email=" + mSession.getEmail() + "&room_name_1=" + mSession.getHotelName().replaceAll(" ", ""));

                // Add authorization header
                String auth = "Token token=" + mSession.getToken() + ",email=" + mSession.getEmail();
                httpPost.addHeader("Authorization", auth);
                // Set up the header types needed to properly transfer JSON
                httpPost.setHeader("Content-Type", "application/json");

                try {
                    HttpResponse response = client.execute(httpPost);
                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    if (statusCode == 200) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            String responseBody = EntityUtils.toString(entity);
                            Log.e("RIDONE", "hasil == " + responseBody);
                            JSONObject objData = new JSONObject(responseBody);
                            String errorNo = (objData.getString("error_no"));
                            if (errorNo.equals("0")) {
                                try {
                                    status = objData.getString("book_status");
                                    success = true;

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            String responseBody = EntityUtils.toString(entity);
                            JSONObject objError = new JSONObject(responseBody);
                            JSONObject objErrorMsg = objError.getJSONObject("errors");
                            errorMessage = (objErrorMsg.getString("message"));
                            success = false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
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
                if (errorMessage != null) {
                    Toast.makeText(PaymentAirplaneAndHotelActivity.this, errorMessage,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaymentAirplaneAndHotelActivity.this, getString(R.string.tidak_dapat_terhubung_ke_server),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                if (status != null) {
                    Toast.makeText(PaymentAirplaneAndHotelActivity.this, "Hotel " + status + " di booking",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaymentAirplaneAndHotelActivity.this, "Hotel Berhasil di booking",
                            Toast.LENGTH_SHORT).show();
                }

                Intent i = new Intent(PaymentAirplaneAndHotelActivity.this, MainMenuActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
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
