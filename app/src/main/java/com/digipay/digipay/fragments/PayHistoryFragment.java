package com.digipay.digipay.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.digipay.digipay.R;
import com.digipay.digipay.functions.ConnectionDetector;
import com.digipay.digipay.functions.Constant;
import com.digipay.digipay.adapter.PayHistoryAdapter;
import com.digipay.digipay.functions.SessionManager;
import com.digipay.digipay.models.PayHistory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PayHistoryFragment extends Fragment {
    private LinearLayoutManager lLayout;
    private SessionManager mSession;
    private String token, email, filterByMsisdn;
    private EditText edtSearch;
    private List<PayHistory> historyList = new ArrayList<PayHistory>();
    //    private List<PayHistory> rowListItem;
    private PayHistoryAdapter rcAdapter;
    private RecyclerView rView;
    private TextView tvDataTransaksi;

    public PayHistoryFragment() {
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
        View v = inflater.inflate(R.layout.fragment_pay_history, container, false);
        edtSearch = (EditText) v.findViewById(R.id.edtSearch);
        tvDataTransaksi = (TextView) v.findViewById(R.id.tvDataTransaksi);
        tvDataTransaksi.setVisibility(View.VISIBLE);
        mSession = new SessionManager(getActivity());
        token = mSession.getToken();
        email = mSession.getEmail();

//        rowListItem = historyList;
        lLayout = new LinearLayoutManager(getActivity());

        rView = (RecyclerView) v.findViewById(R.id.recycler_view);
        rView.setLayoutManager(lLayout);

        // getPayment History
        paymentHistory();

        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            filterByMsisdn = edtSearch.getText().toString();

                            boolean isValid = true;
                            if (TextUtils.isEmpty(filterByMsisdn)) {
                                edtSearch.setError("Silahkan masukan nomor yang ingin anda cari!");
                                isValid = false;
                            }
                            if (isValid) {
                              paymentHistoryFilter();
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        return v;
    }

    private void paymentHistory() {
        ConnectionDetector cd = new ConnectionDetector(getActivity());
        Boolean isInternetPresent;
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            new getPaymentHistory().execute();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    private class getPaymentHistory extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false, statusRespon = false;

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
            HttpGet httpGet = new HttpGet(Constant.WEB_URL + "payments");

            String auth = "Token token=" + token + ",email=" + mSession.getEmail();
            httpGet.addHeader("Authorization", auth);

            // Set up the header types needed to properly transfer JSON
            httpGet.setHeader("Content-Type", "application/json");
            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    statusRespon = true;
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String responseBody = EntityUtils.toString(entity);
                        Log.e("hasil", responseBody);
                        try {
                            historyList.clear();
                            JSONObject objPayments = new JSONObject(responseBody);
                            JSONArray arrayPayments = objPayments.getJSONArray("payments");
                            for (int i = 0; i < arrayPayments.length(); i++) {
                                JSONObject object = arrayPayments.getJSONObject(i);

                                PayHistory history = new PayHistory();
                                history.setId(Integer.toString(object.getInt("id")));
                                history.setAmount(Integer.toString(object.getInt("amount")));
                                history.setStatus(object.getString("status"));
                                history.setMsisdnHistory(object.getString("msisdn"));
                                history.setMessage(object.getString("message"));
                                history.setPaymentTipeHistory(object.getString("payment_type"));
                                history.setNominal(object.getString("nominal"));
                                history.setProvider(object.getString("provider"));
                                history.setSerialNumber(object.getString("serial_number"));
                                history.setDate(object.getString("date"));
                                history.setTotal_balance(Integer.toString(object.getInt("total_balance")));
                                history.setTotal_bonus(Integer.toString(object.getInt("total_bonus")));
                                history.setTotal_point(Integer.toString(object.getInt("total_point")));
                                historyList.add(history);
                            }
                            success = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("hasil", e.getMessage());
                            success = false;
                        }
                    }
                } else {
                    statusRespon = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("hasil", e.getMessage());
                success = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (!success) {
                tvDataTransaksi.setVisibility(View.VISIBLE);
                rView.setVisibility(View.GONE);
            } else {
                try {
                    tvDataTransaksi.setVisibility(View.GONE);
                    rView.setVisibility(View.VISIBLE);
                    rcAdapter = new PayHistoryAdapter(getActivity(), historyList);
                    rView.setAdapter(rcAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!statusRespon) {
                try {
                    Toast.makeText(getActivity(), "Terjadi kesalahana pada server. Silahkan coba beberapa saat lagi.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void paymentHistoryFilter() {
        ConnectionDetector cd = new ConnectionDetector(getActivity());
        Boolean isInternetPresent;
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            new getPaymentHistoryFilter().execute();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    private class getPaymentHistoryFilter extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        Boolean success = false;

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
            HttpGet httpGet = new HttpGet(Constant.WEB_URL + "payments" + "?msisdn=" + filterByMsisdn);

            String auth = "Token token=" + token + ",email=" + mSession.getEmail();
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
                            historyList.clear();
                            JSONObject objPayments = new JSONObject(responseBody);
                            JSONArray arrayPayments = objPayments.getJSONArray("payments");
                            for (int i = 0; i < arrayPayments.length(); i++) {
                                JSONObject object = arrayPayments.getJSONObject(i);

                                PayHistory history = new PayHistory();
                                history.setId(Integer.toString(object.getInt("id")));
                                history.setAmount(Integer.toString(object.getInt("amount")));
                                history.setStatus(object.getString("status"));
                                history.setMsisdnHistory(object.getString("msisdn"));
                                history.setMessage(object.getString("message"));
                                history.setPaymentTipeHistory(object.getString("payment_type"));
                                history.setNominal(object.getString("nominal"));
                                history.setProvider(object.getString("provider"));
                                history.setSerialNumber(object.getString("serial_number"));
                                history.setDate(object.getString("date"));
                                history.setTotal_balance(Integer.toString(object.getInt("total_balance")));
                                history.setTotal_bonus(Integer.toString(object.getInt("total_bonus")));
                                history.setTotal_point(Integer.toString(object.getInt("total_point")));
                                historyList.add(history);
                                Log.e("hasil2", historyList.get(i).getMessage());

                                success = true;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("error", e.getMessage());
                        }
                    }
                } else {
                    success = false;
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
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
                tvDataTransaksi.setVisibility(View.VISIBLE);
                rView.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Nomor tidak di temukan", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    tvDataTransaksi.setVisibility(View.GONE);
                    rView.setVisibility(View.VISIBLE);
                    rcAdapter = new PayHistoryAdapter(getActivity(), historyList);
                    rView.setAdapter(rcAdapter);

                    // hide keyboard function
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
