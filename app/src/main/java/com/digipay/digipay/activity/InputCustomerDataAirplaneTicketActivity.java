package com.digipay.digipay.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digipay.digipay.R;
import com.digipay.digipay.adapter.DetailOrdersAirplaneTicketAdapter;
import com.digipay.digipay.adapter.InputCustomerDataAirplaneAdapter;
import com.digipay.digipay.functions.Constant;
import com.digipay.digipay.functions.SessionManager;
import com.digipay.digipay.functions.SessionSearchBook;
import com.digipay.digipay.models.BookParams;
import com.digipay.digipay.models.DetailOrdersAirplaneTicket;
import com.digipay.digipay.models.InputCustomerCount;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatConversionException;
import java.util.List;

public class InputCustomerDataAirplaneTicketActivity extends AppCompatActivity {

    private List<InputCustomerCount> inputCustomerCountList = new ArrayList<>();
    private List<InputCustomerCount> paxTypeCountsList = new ArrayList<>();
    private List<DetailOrdersAirplaneTicket> detailOrderList = new ArrayList<>();
    private DetailOrdersAirplaneTicketAdapter mAdapterDetail;
    private int day, month, year, jmlPenumpang = 0, dataIntentPenumpang, countPenumpangAdd;
    private SessionManager mSession;
    private SessionSearchBook mSessionSearchBook;
    private String errorMessage, phone, email, tittle, namaLengkap, jenisIdentitasView, birthdate;
    private EditText edtNamaPemesan, edtPhonePemesan, edtEmailPemesan;
    private TextView tvBirthdate, tvFromCity, tvToCity, tvFromReturn, tvToReturn,
            tvDateDepart, tvDateReturn, tvAirlineNameDepart, tvTypeDepart, tvTimeDepart;
    static final int DATE_DIALOG_ID = 999;
    private DatePicker dpResult;
    private HttpPost httpPost;
    private LinearLayout llDataPemesanan, llDetailPemesanan;
    private EditText edtFirstName, edtLastName, edtNomorIdentitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_customer_data_airplane_ticket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSession = new SessionManager(getApplicationContext());
        mSessionSearchBook = new SessionSearchBook(getApplicationContext());

        int infantIntent, childIntent, adultIntent;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                dataIntentPenumpang = 0;
                adultIntent = 0;
                childIntent = 0;
                infantIntent = 0;
            } else {
                dataIntentPenumpang = Integer.parseInt(extras.getString("jmlPenumpang"));
                adultIntent = Integer.parseInt(extras.getString("adult"));
                childIntent = Integer.parseInt(extras.getString("child"));
                infantIntent = Integer.parseInt(extras.getString("infant"));
            }
        } else {
            dataIntentPenumpang = Integer.parseInt((String) savedInstanceState.getSerializable("jmlPenumpang"));
            adultIntent = Integer.parseInt((String) savedInstanceState.getSerializable("adult"));
            childIntent = Integer.parseInt((String) savedInstanceState.getSerializable("child"));
            infantIntent = Integer.parseInt((String) savedInstanceState.getSerializable("infant"));
        }

        if (adultIntent != 0) {
            for (int i = 0; i <= adultIntent; i++) {
                InputCustomerCount adultCount = new InputCustomerCount();
                adultCount.setPaxType("adult");
                paxTypeCountsList.add(adultCount);
            }
        }

        if (childIntent != 0) {
            for (int i = 0; i <= childIntent; i++) {
                InputCustomerCount adultCount = new InputCustomerCount();
                adultCount.setPaxType("child");
                paxTypeCountsList.add(adultCount);
            }
        }

        if (infantIntent != 0) {
            for (int i = 0; i <= infantIntent; i++) {
                InputCustomerCount adultCount = new InputCustomerCount();
                adultCount.setPaxType("infant");
                paxTypeCountsList.add(adultCount);
            }
        }

        edtNamaPemesan = (EditText) findViewById(R.id.edtNama);
        edtPhonePemesan = (EditText) findViewById(R.id.edtNoHp);
        edtEmailPemesan = (EditText) findViewById(R.id.edtEmail);
        tvBirthdate = (TextView) findViewById(R.id.tvBirthdate);
        dpResult = (DatePicker) findViewById(R.id.datePicker);

        llDataPemesanan = (LinearLayout) findViewById(R.id.llDataPemesanan);
        FrameLayout frameKembali = (FrameLayout) findViewById(R.id.frameKembali);
        FrameLayout frameLanjut = (FrameLayout) findViewById(R.id.frameLanjut);
        LinearLayout llTglLahir = (LinearLayout) findViewById(R.id.llTglLahir);
        Button btnPencarianUlang = (Button) findViewById(R.id.btnPencarianUlang);
        Spinner spinTittle = (Spinner) findViewById(R.id.spinnerTittle);
        Spinner spinJenisIdentitas = (Spinner) findViewById(R.id.spinnerJenisIdentitas);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final TextView tvIsiDataPenumpang = (TextView) findViewById(R.id.tvIsiDataPenumpang);
        edtFirstName = (EditText) findViewById(R.id.edtFirstName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtNomorIdentitas = (EditText) findViewById(R.id.edtNomorIdentitas);
        final Button btnSelanjutnya = (Button) findViewById(R.id.btnSelanjutnya);
        final LinearLayout llDataPenumpang = (LinearLayout) findViewById(R.id.llDataPenumpang);

        // initital detail pemesanan
        llDetailPemesanan = (LinearLayout) findViewById(R.id.llDetailPemsanan);
        tvFromCity = (TextView) findViewById(R.id.tvFrom);
        tvFromReturn = (TextView) findViewById(R.id.tvFromReturn);
        tvToCity = (TextView) findViewById(R.id.tvTo);
        tvAirlineNameDepart = (TextView) findViewById(R.id.tvAirlineNameDepart);
        tvToReturn = (TextView) findViewById(R.id.tvToReturn);
        tvDateDepart = (TextView) findViewById(R.id.tvDateDepart);
        tvTypeDepart = (TextView) findViewById(R.id.tvTypeDepart);
        tvTimeDepart = (TextView) findViewById(R.id.tvTimeDepart);
        tvDateReturn = (TextView) findViewById(R.id.tvDateReturn);
        FrameLayout frameKembaliDetail = (FrameLayout) findViewById(R.id.frameKembaliDetail);
        FrameLayout frameLanjutDetail = (FrameLayout) findViewById(R.id.frameLanjutDetail);
        RecyclerView recyclerViewDetail = (RecyclerView) findViewById(R.id.recycler_view_detail);

        ArrayAdapter<CharSequence> tittleAdapter = ArrayAdapter
                .createFromResource(getApplicationContext(), R.array.search_tittle_list,
                        R.layout.my_text_black);
        tittleAdapter.setDropDownViewResource(R.layout.my_text_black);
        spinTittle.setAdapter(tittleAdapter);

        ArrayAdapter<CharSequence> jenisIdentitasAdapter = ArrayAdapter
                .createFromResource(getApplicationContext(), R.array.jenis_identitas_lis,
                        R.layout.my_text_black);
        jenisIdentitasAdapter.setDropDownViewResource(R.layout.my_text_black);
        spinJenisIdentitas.setAdapter(jenisIdentitasAdapter);

        final InputCustomerDataAirplaneAdapter mAdapter = new InputCustomerDataAirplaneAdapter(this, inputCustomerCountList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // init adapter n recycler detail
        mAdapterDetail = new DetailOrdersAirplaneTicketAdapter(this, detailOrderList);
        RecyclerView.LayoutManager mLayoutManagerDetail = new LinearLayoutManager(getApplicationContext());
        recyclerViewDetail.setLayoutManager(mLayoutManagerDetail);
        recyclerViewDetail.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDetail.setAdapter(mAdapterDetail);

        countPenumpangAdd = dataIntentPenumpang + 1;

        // set Current date
        setCurrentDateOnView();

        spinTittle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                tittle = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinJenisIdentitas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                jenisIdentitasView = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnPencarianUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputCustomerDataAirplaneTicketActivity.this, BookingAirplaneTicketActivity.class);
                startActivity(i);
                finish();

            }
        });

        btnSelanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jmlPenumpang < dataIntentPenumpang && tittle != null
                        && !edtFirstName.getText().toString().equals("") && !edtLastName.getText().toString().equals("")
                        && jenisIdentitasView != null && !edtNomorIdentitas.getText().toString().equals("") && birthdate != null) {

                    jmlPenumpang += 1;

                    InputCustomerCount customer = new InputCustomerCount();
                    customer.setCount(jmlPenumpang);
                    customer.setTittle(tittle);
                    customer.setFirtsName(edtFirstName.getText().toString());
                    customer.setLastName(edtLastName.getText().toString());
                    customer.setJenisIdentitas(jenisIdentitasView);
                    customer.setNomorIdentitas(edtNomorIdentitas.getText().toString());
                    customer.setBirthdate(birthdate);

                    mSessionSearchBook.setFirstnameOpt(customer.getFirtsName());
                    mSessionSearchBook.setLastnameOpt(customer.getLastName());
                    mSessionSearchBook.setNoIdentitasOpt(customer.getNomorIdentitas());

                    inputCustomerCountList.add(customer);

                    tvIsiDataPenumpang.setText("Penumpang " + jmlPenumpang);
                    edtFirstName.setText("");
                    edtLastName.setText("");
                    edtNomorIdentitas.setText("");

                    if (jmlPenumpang == dataIntentPenumpang) {
                        llDataPenumpang.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();

                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(InputCustomerDataAirplaneTicketActivity.this, "Silahkan isi Data Penumpang!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        frameKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        frameKembaliDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        frameLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jmlPenumpang == dataIntentPenumpang) {
                    validationAndSearchBook();
                } else {
                    Toast.makeText(InputCustomerDataAirplaneTicketActivity.this, "Anda belum menyelesaikan pengisian data penumpang", Toast.LENGTH_SHORT).show();
                }
            }
        });

        frameLanjutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputCustomerDataAirplaneTicketActivity.this, PaymentAirplaneAndHotelActivity.class);
                i.putExtra("isHotelOrAirplane", "airplane");
                startActivity(i);
            }
        });
    }

    private void validationAndSearchBook() {
        namaLengkap = edtNamaPemesan.getText().toString();
        phone = edtPhonePemesan.getText().toString();
        email = edtEmailPemesan.getText().toString();

        Boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            edtEmailPemesan.setError("Email tidak boleh kosong!");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()) {
            edtEmailPemesan.setError("Email salah:john@example.com");
            isValid = false;
        }
        if (TextUtils.isEmpty(phone)) {
            edtPhonePemesan.setError("Silahkan masukan Nomor Telepon!");
            isValid = false;
        }
        if (TextUtils.isEmpty(namaLengkap)) {
            edtNamaPemesan.setError("Silahkan masukan Nama!");
            isValid = false;
        }
        if (isValid) {
            new getSearchBook().execute();
        }
    }

    private class getSearchBook extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InputCustomerDataAirplaneTicketActivity.this);
            pDialog.setMessage("Mohon Tunggu...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                HttpClient client = new DefaultHttpClient();

                httpPostParams();

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
                            try {
                                JSONObject objData = new JSONObject(responseBody);
                                String errorNo = (objData.getString("error_no"));
                                if (errorNo.equals("0")) {
                                    DetailOrdersAirplaneTicket detailOrder = new DetailOrdersAirplaneTicket();

                                    JSONObject objSearchInfo = objData.getJSONObject("search_info");
                                    mSessionSearchBook.setAirline(objSearchInfo.getString("airline"));
                                    mSessionSearchBook.setRoundtrip(objSearchInfo.getString("roundtrip"));
                                    mSessionSearchBook.setFromBook(objSearchInfo.getString("from"));
                                    mSessionSearchBook.setToBook(objSearchInfo.getString("to"));
                                    mSessionSearchBook.setDepart(objSearchInfo.getString("depart"));
                                    mSessionSearchBook.setReturnBook(objSearchInfo.getString("return"));
                                    mSessionSearchBook.setAdult(objSearchInfo.getString("adult"));
                                    mSessionSearchBook.setChild(objSearchInfo.getString("child"));
                                    mSessionSearchBook.setInfant(objSearchInfo.getString("infant"));

                                    if (objData.has("book_id")) {
                                        mSessionSearchBook.setBookId(objData.getString("book_id"));
                                        mSessionSearchBook.setAdminFee(String.valueOf(objData.getInt("admin_fee")));
                                    }

                                    if (objData.has("passenger")) {
                                        JSONArray arrayPassenger = objData.getJSONArray("passenger");
                                        for (int i = 0; i < arrayPassenger.length(); i++) {
                                            detailOrder = new DetailOrdersAirplaneTicket();

                                            JSONObject objPassenger = arrayPassenger.getJSONObject(i);

                                            detailOrder.setPsgrType(objPassenger.getString("psgr_type"));
                                            detailOrder.setTittle(objPassenger.getString("title"));
                                            detailOrder.setFirstName(objPassenger.getString("first_name"));
                                            detailOrder.setLastName(objPassenger.getString("last_name"));
                                            detailOrder.setIdPsgr(objPassenger.getString("id_psgr"));
                                            detailOrder.setBirthDate(objPassenger.getString("birthdate"));
                                            detailOrder.setPaspor(objPassenger.getString("paspor"));
                                            detailOrder.setExpirePaspor(objPassenger.getString("expire_paspor"));
                                            detailOrder.setCountryPaspor(objPassenger.getString("country_paspor"));
                                            detailOrder.setParent(objPassenger.getString("parent"));

                                            detailOrderList.add(detailOrder);

                                        }

                                        mAdapterDetail.notifyDataSetChanged();
                                    }

                                    if (objData.has("customer")) {
                                        JSONObject objCustomer = objData.getJSONObject("customer");
                                        detailOrder.setCusName(objCustomer.getString("cust_name"));
                                        detailOrder.setCusPhone(objCustomer.getString("cust_phone"));
                                        detailOrder.setCusEmail(objCustomer.getString("cust_email"));

                                    }

                                    if (objData.has("depart")) {
                                        JSONObject objDepart = objData.getJSONObject("depart");
                                        JSONObject objDetailInfo = objDepart.getJSONObject("detail_info");

                                        detailOrder.setAirlineId(objDetailInfo.getString("airline_id"));
                                        detailOrder.setAirlineName(objDetailInfo.getString("airline_name"));
                                        detailOrder.setType(objDetailInfo.getString("type"));
                                        detailOrder.setFrom(objDetailInfo.getString("from"));
                                        detailOrder.setVia(objDetailInfo.getString("via"));
                                        detailOrder.setTo(objDetailInfo.getString("to"));
                                        detailOrder.setDate(objDetailInfo.getString("date"));
                                        detailOrder.setFno(objDetailInfo.getString("fno"));
                                        detailOrder.setEtd(objDetailInfo.getString("etd"));
                                        detailOrder.setEta(objDetailInfo.getString("eta"));
                                        detailOrder.setClassDepart(objDetailInfo.getString("class"));
                                        detailOrder.setClassName(objDetailInfo.getString("class_name"));

                                        mSessionSearchBook.setAirlineNameDepart(detailOrder.getAirlineName());
                                        mSessionSearchBook.setFromDepart(detailOrder.getFrom());
                                        mSessionSearchBook.setToDepart(detailOrder.getTo());
                                        mSessionSearchBook.setTypeDepart(detailOrder.getType());
                                        mSessionSearchBook.setDateDepart(detailOrder.getDate());
                                        mSessionSearchBook.setEtaDepart(detailOrder.getEta());
                                        mSessionSearchBook.setEtdDepart(detailOrder.getEtd());
                                    }

                                    if (objData.has("price")) {
                                        JSONObject objPrice = objData.getJSONObject("price");
                                        mSessionSearchBook.setTickerPrice(String.valueOf(objPrice.getInt("ticket_price")));
                                        mSessionSearchBook.setAeroComisi(String.valueOf(objPrice.getInt("aero_comisi")));
                                        mSessionSearchBook.setCabangComisi(String.valueOf(objPrice.getInt("cabang_comisi")));
                                        mSessionSearchBook.setAgentComisi(String.valueOf(objPrice.getInt("agent_comisi")));
                                        mSessionSearchBook.setAgentPrice(String.valueOf(objPrice.getInt("agent_price")));
                                        mSessionSearchBook.setAgentMargin(String.valueOf(objPrice.getInt("agent_margin")));
                                        mSessionSearchBook.setAsuransi(objPrice.getString("asuransi"));
                                        mSessionSearchBook.setComisiAsuransi(objPrice.getString("comisi_asuransi"));
                                        mSessionSearchBook.setTotalPrice(String.valueOf(objPrice.getInt("total_price")));
                                    }
                                    success = true;
                                } else {
                                    errorMessage = (objData.getString("error_msg"));
                                    success = false;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("RIDONE", "ERROR WAN UY1 == " + e.getMessage());
                            }
                        }
                    } else {
                        success = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("RIDONE", "ERROR WAN UY2 == " + e.getMessage());
                }
                return null;
            } catch (Exception e) {
                Log.e("RIDONE", "ERROR WAN == " + e.getMessage());
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
                    Toast.makeText(InputCustomerDataAirplaneTicketActivity.this, errorMessage,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InputCustomerDataAirplaneTicketActivity.this, "Terjadi kesalahan pada server. Mohon mencoba kembali.",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                llDataPemesanan.setVisibility(View.GONE);
                llDetailPemesanan.setVisibility(View.VISIBLE);

                initDetailOrderView();

            }
        }
    }

    private void httpPostParams() {
        String customURLString = "";
        ArrayList<InputCustomerCount> customerCountList = new ArrayList<>();
        ArrayList<InputCustomerCount> paxTypeCountList = new ArrayList<>();
        if (dataIntentPenumpang > 1) {
            for (int i = 1; i < countPenumpangAdd; i++) {
                for (InputCustomerCount customer : inputCustomerCountList) {
                    customerCountList.add(customer);
                }

                for (InputCustomerCount paxType : paxTypeCountsList) {
                    paxTypeCountList.add(paxType);
                }

                BookParams params = new BookParams();

                params.setPaxType("&pax_type_" + i + "=" + paxTypeCountList.get(i).getPaxType());
                params.setTittle("&title_" + i + "=" + customerCountList.get(i).getTittle());
                params.setFirstName("&first_name_" + i + "=" + customerCountList.get(i).getFirtsName().replaceAll(" ", ""));
                params.setLastName("&last_name_" + i + "=" + customerCountList.get(i).getLastName().replaceAll(" ", ""));
                params.setIdNo("&id_no_" + i + "=" + customerCountList.get(i).getNomorIdentitas().replaceAll(" ", ""));
                params.setBirthDate("&birthdate_" + i + "=" + customerCountList.get(i).getBirthdate());

                // bikin lagi string baru buat nampung semua param yang diperluin
                String cacheString = params.getPaxType() + params.getTittle() + params.getFirstName() + params.getLastName() + params.getIdNo() + params.getBirthDate();

                // terus masukin string nya ke string kosong yang dibikin di atas tadi, pastikan string nya ngeloop
                customURLString = customURLString + cacheString;

            }
            Log.e("RIDONE", "CUSTOM STRING = " + customURLString);

            httpPost = new HttpPost(Constant.WEB_URL + "flights/search_book?session_id=" +
                    mSession.getSessionIdSearchDetail() +
                    "&cust_name=" + namaLengkap.replaceAll(" ", "") +
                    "&cust_phone=" + phone.replaceAll(" ", "") +
                    "&cust_email=" + email.replaceAll(" ", "") + customURLString);
        } else {
            httpPost = new HttpPost(Constant.WEB_URL + "flights/search_book?session_id=" +
                    mSession.getSessionIdSearchDetail().replaceAll(" ", "") +
                    "&cust_name=" + namaLengkap.replaceAll(" ", "") +
                    "&cust_phone=" + phone.replaceAll(" ", "") +
                    "&cust_email=" + email.replaceAll(" ", "") +
                    "&pax_type_1=adult" + "&title_1=" + tittle +
                    "&first_name_1=" + mSessionSearchBook.getFirstNameOpt().replaceAll(" ", "")
                    + "&last_name_1=" + mSessionSearchBook.getLastnameOpt().replaceAll(" ", "")
                    + "&id_no_1=" + mSessionSearchBook.getNoIdentitasOpt().replaceAll(" ", "")
                    + "&birthdate_1=" + birthdate);

        }
    }

    private void initDetailOrderView() {
        tvFromCity.setText(mSessionSearchBook.getCityDepart() + " - ");
        tvToCity.setText(mSessionSearchBook.getCityDestination() + "(Depart)");

        tvToReturn.setText(mSessionSearchBook.getCityDestination() + " - ");
        tvFromReturn.setText(mSessionSearchBook.getCityDepart() + "(Return)");

        tvAirlineNameDepart.setText(mSessionSearchBook.getAirlineNameDepart());
        tvDateDepart.setText(mSessionSearchBook.getDateDepart() + ", " + dataIntentPenumpang + " Penumpang");
        tvTypeDepart.setText(mSessionSearchBook.getTypeDepart());
        tvTimeDepart.setText(mSessionSearchBook.getEtaDepart() + " - " + mSessionSearchBook.getEtdDepart());
        tvDateReturn.setText(mSessionSearchBook.getDepart());
    }

    // display current date
    public void setCurrentDateOnView() {
        try {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            try {
                Date date = new Date(year - 1926, month, day);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = formatter.format(date);
                tvBirthdate.setText(currentDate);

                dpResult.init(year, month, day, null);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (IllegalFormatConversionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        try {
            switch (id) {
                case DATE_DIALOG_ID:
                    // set date picker as current date
                    return new DatePickerDialog(this, datePickerListener,
                            year - 26, month, day);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            try {
                year = selectedYear;
                month = selectedMonth;
                day = selectedDay;

                Date date = new Date(year - 1900, month, day);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String nowDate = formatter.format(date);
                tvBirthdate.setText(nowDate);
                birthdate = nowDate;
                // set selected date into datepicker also
                dpResult.init(year, month, day, null);
            } catch (IllegalFormatConversionException e) {
                e.printStackTrace();
                Log.e("DatePickerDialogFix", "IllegalFormatConversionException Fixed! ", e);
            }
        }
    };

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
