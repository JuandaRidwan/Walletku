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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digipay.digipay.R;
import com.digipay.digipay.adapter.BookingAirplaneAdapterDepart;
import com.digipay.digipay.adapter.BookingAirplaneAdapterReturn;
import com.digipay.digipay.functions.Constant;
import com.digipay.digipay.functions.SessionManager;
import com.digipay.digipay.functions.SessionSearchBook;
import com.digipay.digipay.models.Airlines;
import com.digipay.digipay.models.BookingAirplane;
import com.digipay.digipay.models.Departure;
import com.digipay.digipay.models.Destination;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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

public class BookingAirplaneTicketActivity extends AppCompatActivity {
    private LinearLayout llTiketPesawat, llHasilPencarian, llItemSelectedDepart, llItemSelectedReturn;
    private DatePicker dpResult;
    private TextView tvDepart, tvReturn, tvFrom, tvFromReturn, tvTo, tvToReturn, tvSchedule, tvScheduleReturn;
    private SessionManager mSession;
    private SessionSearchBook mSessionSearchBook;
    private boolean isDepart, isReturn, isDepartGone;
    private int infant, child, adult, day, month, year, jmlPenumpang;
    private String airlinesId, roundTrip, departureCode, destinationCode, errorMessage, departureDate, arrivalDate;
    private Spinner spinAirlines, spinDeparture, spinDestination;
    static final int DATE_DIALOG_ID = 999;
    private TextView tvAirplaneName, tvBookingPayment, tvBookingTime, tvBookingClass, tvBookingTimeTransit, tvDirectFlag;
    private TextView tvAirplaneNameReturn, tvBookingPaymentReturn, tvBookingTimeReturn, tvBookingClassReturn,
            tvBookingTimeTransitReturn, tvDirectFlagReturn;

    private ArrayList<String> airLinesList = new ArrayList<>();
    private ArrayList<String> departureList = new ArrayList<>();
    private ArrayList<Departure> departureListModel = new ArrayList<>();
    private ArrayList<Destination> destinationListModel = new ArrayList<>();
    private ArrayList<String> destinationList = new ArrayList<>();
    private ArrayList<Airlines> airlinesListAll = new ArrayList<>();
    private List<BookingAirplane> bookingListDepart = new ArrayList<>();
    private List<BookingAirplane> bookingListReturn = new ArrayList<>();
    private BookingAirplaneAdapterDepart mAdapterDepart;
    private BookingAirplaneAdapterReturn mAdapterReturn;
    private LinearLayout llReturn;
    private RecyclerView recyclerViewDepart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_airplane_ticket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSession = new SessionManager(getApplicationContext());
        mSessionSearchBook = new SessionSearchBook(getApplicationContext());

        recyclerViewDepart = (RecyclerView) findViewById(R.id.recycler_view_depart);
        final RecyclerView recyclerViewReturn = (RecyclerView) findViewById(R.id.recycler_view_return);
        llTiketPesawat = (LinearLayout) findViewById(R.id.llTiketPesawat);
        llHasilPencarian = (LinearLayout) findViewById(R.id.llHasilPencarian);
        llItemSelectedDepart = (LinearLayout) findViewById(R.id.llItemSelected);
        llItemSelectedReturn = (LinearLayout) findViewById(R.id.llItemSelectedReturn);
        llReturn = (LinearLayout) findViewById(R.id.llReturn);
        FrameLayout frameSearch = (FrameLayout) findViewById(R.id.frameSearchTiketPesawat);
        RadioButton radioReturn = (RadioButton) findViewById(R.id.radioReturn);
        RadioButton radioOneWay = (RadioButton) findViewById(R.id.radioOneWay);
        ImageView imgCalendarDepart = (ImageView) findViewById(R.id.imgCalendarDepart);
        ImageView imgCalendarReturn = (ImageView) findViewById(R.id.imgCalendarReturn);
        spinAirlines = (Spinner) findViewById(R.id.SpinnerAirlines);
        spinDeparture = (Spinner) findViewById(R.id.SpinnerDeparture);
        spinDestination = (Spinner) findViewById(R.id.SpinnerDestination);
        Spinner spinJmlAdult = (Spinner) findViewById(R.id.SpinnerJmlAdult);
        Spinner spinJmlChild = (Spinner) findViewById(R.id.SpinnerJmlChild);
        Spinner spinJmlInfant = (Spinner) findViewById(R.id.SpinnerJmlInfant);
        final LinearLayout llDateReturn = (LinearLayout) findViewById(R.id.llDateReturn);
        final LinearLayout llTextDateReturn = (LinearLayout) findViewById(R.id.llTextDateReturn);
        tvDepart = (TextView) findViewById(R.id.tvDepart);
        tvReturn = (TextView) findViewById(R.id.tvReturn);
        tvFrom = (TextView) findViewById(R.id.tvFrom);
        tvFromReturn = (TextView) findViewById(R.id.tvFromReturn);
        tvTo = (TextView) findViewById(R.id.tvTo);
        tvToReturn = (TextView) findViewById(R.id.tvToReturn);
        tvSchedule = (TextView) findViewById(R.id.tvSchedule);
        tvScheduleReturn = (TextView) findViewById(R.id.tvScheduleReturn);
        dpResult = (DatePicker) findViewById(R.id.datePicker);
        Button btnPencarianUlang = (Button) findViewById(R.id.btnPencarianUlang);
        FrameLayout frameLanjut = (FrameLayout) findViewById(R.id.frameLanjut);

        // item selected initial depart
        tvAirplaneName = (TextView) findViewById(R.id.tvAirplaneName);
        tvBookingPayment = (TextView) findViewById(R.id.tvBookingPayment);
        tvBookingTime = (TextView) findViewById(R.id.tvBookingTime);
        tvBookingClass = (TextView) findViewById(R.id.tvBookingClass);
        tvBookingTimeTransit = (TextView) findViewById(R.id.tvBookingTimeTransit);
        tvDirectFlag = (TextView) findViewById(R.id.tvDirectFlag);
        Button btnUnselected = (Button) findViewById(R.id.btnSelected);

        // item selected initial return
        tvAirplaneNameReturn = (TextView) findViewById(R.id.tvAirplaneNameReturn);
        tvBookingPaymentReturn = (TextView) findViewById(R.id.tvBookingPaymentReturn);
        tvBookingTimeReturn = (TextView) findViewById(R.id.tvBookingTimeReturn);
        tvBookingClassReturn = (TextView) findViewById(R.id.tvBookingClassReturn);
        tvBookingTimeTransitReturn = (TextView) findViewById(R.id.tvBookingTimeTransitReturn);
        tvDirectFlagReturn = (TextView) findViewById(R.id.tvDirectFlagReturn);
        Button btnUnselectedReturn = (Button) findViewById(R.id.btnSelectedReturn);

        isDepart = false;

        // set Current date
        setCurrentDateOnView();

        // getListAirlines
        new getAirlines().execute();

        ArrayAdapter<CharSequence> jmlAdultAdapter = ArrayAdapter
                .createFromResource(getApplicationContext(), R.array.adult_list,
                        R.layout.my_text_black);
        jmlAdultAdapter.setDropDownViewResource(R.layout.my_text_black);
        spinJmlAdult.setAdapter(jmlAdultAdapter);

        ArrayAdapter<CharSequence> jmlChildAdapter = ArrayAdapter
                .createFromResource(getApplicationContext(), R.array.count_list,
                        R.layout.my_text_black);
        jmlChildAdapter.setDropDownViewResource(R.layout.my_text_black);
        spinJmlChild.setAdapter(jmlChildAdapter);

        ArrayAdapter<CharSequence> jmlInfantdAdapter = ArrayAdapter
                .createFromResource(getApplicationContext(), R.array.count_list,
                        R.layout.my_text_black);
        jmlInfantdAdapter.setDropDownViewResource(R.layout.my_text_black);
        spinJmlInfant.setAdapter(jmlInfantdAdapter);

        // depart
        mAdapterDepart = new BookingAirplaneAdapterDepart(this, bookingListDepart, recyclerViewDepart, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewDepart.setLayoutManager(mLayoutManager);
        recyclerViewDepart.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDepart.setFocusable(false);
        recyclerViewDepart.setAdapter(mAdapterDepart);

        //return
        mAdapterReturn = new BookingAirplaneAdapterReturn(this, bookingListReturn, recyclerViewReturn, this);
        RecyclerView.LayoutManager mLayoutManagerReturn = new LinearLayoutManager(getApplicationContext());
        recyclerViewReturn.setLayoutManager(mLayoutManagerReturn);
        recyclerViewReturn.setItemAnimator(new DefaultItemAnimator());
        recyclerViewReturn.setFocusable(false);
        recyclerViewReturn.setAdapter(mAdapterReturn);

        // first time selected radio button
        roundTrip = "oneway";
        llDateReturn.setVisibility(View.GONE);
        llTextDateReturn.setVisibility(View.GONE);

        radioOneWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llDateReturn.setVisibility(View.GONE);
                llTextDateReturn.setVisibility(View.GONE);
                roundTrip = "oneway";
            }
        });

        radioReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llDateReturn.setVisibility(View.VISIBLE);
                llTextDateReturn.setVisibility(View.VISIBLE);
                roundTrip = "return";
            }
        });

        spinAirlines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                departureList.clear();
                Airlines airlines = airlinesListAll.get(position);
                airlinesId = airlines.getAirlinesId();
                if (airlinesId != null) {
                    departureListModel = new ArrayList<Departure>();
                    new getDepartureAirport().execute();
                } else {
                    Toast.makeText(BookingAirplaneTicketActivity.this, "Silahkan pilih Maskapai penerbangan kepercayaan Anda!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinDeparture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                destinationList.clear();
                Departure departure = departureListModel.get(position);
                departureCode = departure.getAirPortCode();
                String city = departure.getAirPortCity();
                mSessionSearchBook.setCityDepart(city);
                tvFrom.setText(city + " - ");
                tvToReturn.setText(city + "(return)");

                if (departureCode != null) {
                    destinationListModel = new ArrayList<>();
                    new getArrivalAirport().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Destination destination = destinationListModel.get(position);
                destinationCode = destination.getAirPortCode();
                String city = destination.getAirPortCity();
                mSessionSearchBook.setCityDestination(city);
                tvTo.setText(city + "(depart)");
                tvFromReturn.setText(city + " - ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinJmlAdult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String statusData = String.valueOf(parent.getItemAtPosition(position));
                adult = Integer.parseInt(statusData);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinJmlChild.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String statusData = String.valueOf(parent.getItemAtPosition(position));
                child = Integer.parseInt(statusData);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinJmlInfant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String statusData = String.valueOf(parent.getItemAtPosition(position));
                infant = Integer.parseInt(statusData);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        imgCalendarDepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDepart = true;
                showDialog(DATE_DIALOG_ID);
            }
        });

        imgCalendarReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDepart = false;
                showDialog(DATE_DIALOG_ID);
            }
        });

        btnUnselected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewDepart.setVisibility(View.VISIBLE);
                llItemSelectedDepart.setVisibility(View.GONE);
                mSession.clearSharedPrevByKey("fno_1");
                mSession.clearSharedPrevByKey("class_1");
            }
        });

        btnUnselectedReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewReturn.setVisibility(View.VISIBLE);
                llItemSelectedReturn.setVisibility(View.GONE);
                mSession.clearSharedPrevByKey("fno_2");
                mSession.clearSharedPrevByKey("class_2");
            }
        });
        btnPencarianUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pencarianUlang();
            }
        });

        frameSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roundTrip != null && departureCode != null && destinationCode != null && departureDate != null
                        && !departureDate.equals("")) {
                    new searchAirlinesTicket().execute();
                } else {
                    Toast.makeText(BookingAirplaneTicketActivity.this, "Silahkan lengkapi kolong-kolom di atas", Toast.LENGTH_SHORT).show();
                }
            }
        });

        frameLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSession.getSessionId() != null && mSession.getFno1() != null && !mSession.getFno1().equals("")
                        && mSession.getClass1() != null && !mSession.getClass1().equals("") && !isReturn) {
                    new getSearchDetail().execute();
                } else if (mSession.getSessionId() != null && mSession.getFno1() != null && !mSession.getFno1().equals("")
                        && mSession.getClass1() != null && !mSession.getClass1().equals("") && mSession.getFno2() != null
                        && !mSession.getFno2().equals("") && mSession.getClass2() != null && !mSession.getClass2().equals("") && isReturn) {
                    new getSearchDetail().execute();
                } else {
                    Toast.makeText(BookingAirplaneTicketActivity.this, "Silahakan pilih salah satu Depart dan Return", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void pencarianUlang() {
        bookingListDepart.clear();
        bookingListReturn.clear();
        llTiketPesawat.setVisibility(View.VISIBLE);
        llHasilPencarian.setVisibility(View.GONE);
    }

    // display current date
    public void setCurrentDateOnView() {
        try {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            try {
                Date date = new Date(year - 1900, month, day);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = formatter.format(date);
                tvDepart.setText(currentDate);
                tvReturn.setText(currentDate);

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
                    return new DatePickerDialog(this, datePickerListener,
                            year, month, day);
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
                if (isDepart) {
                    tvDepart.setText(nowDate);
                    departureDate = nowDate;

                } else {
                    tvReturn.setText(nowDate);
                    arrivalDate = nowDate;

                }
                dpResult.init(year, month, day, null);
            } catch (IllegalFormatConversionException e) {
                e.printStackTrace();
                Log.e("DatePickerDialogFix", "IllegalFormatConversionException Fixed! ", e);
            }
        }
    };

    private class getAirlines extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BookingAirplaneTicketActivity.this);
            pDialog.setMessage("Mohon Tunggu...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Constant.WEB_URL + "flights/get_airlines");

            // Add authorization header
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
                            JSONArray arrayProvider = objProvider.getJSONArray("airlines_data");
                            for (int i = 0; i < arrayProvider.length(); i++) {
                                JSONObject object = arrayProvider.getJSONObject(i);
                                Airlines airlines = new Airlines();
                                airlines.setAirlinesId(object.getString("airlines_id"));
                                airlines.setAirlinesName(object.getString("airlines_name"));
                                airlines.setAirlinesCode(object.getString("airlines_code"));
                                airlines.setAirlinesType(object.getString("airlines_type"));
                                airlines.setConnectionStatus(object.getString("connection_status"));

                                airLinesList.add(airlines.getAirlinesName());
                                airlinesListAll.add(airlines);

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
                Toast.makeText(BookingAirplaneTicketActivity.this, getString(R.string.tidak_dapat_terhubung_ke_server),
                        Toast.LENGTH_SHORT).show();
            } else {
                ArrayAdapter<String> spinDepartureAdapter = new ArrayAdapter<String>(BookingAirplaneTicketActivity.this, android.R.layout.simple_spinner_item, airLinesList);
                spinDepartureAdapter.setDropDownViewResource(R.layout.my_text_black);
                // set Spinner adapter departure
                spinAirlines.setAdapter(spinDepartureAdapter);
            }
        }
    }

    private class getDepartureAirport extends AsyncTask<Void, Void, Void> {
        boolean success = false;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BookingAirplaneTicketActivity.this);
            pDialog.setMessage("Sedang mengambil data Departure...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Constant.WEB_URL + "flights/get_departure_airport" + "?airline=" + airlinesId);

            // Add authorization header
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
                            JSONArray arrayProvider = objProvider.getJSONArray("departure_airport");
                            for (int i = 0; i < arrayProvider.length(); i++) {
                                JSONObject object = arrayProvider.getJSONObject(i);
                                Departure departure = new Departure();
                                departure.setAirPortCode(object.getString("airport_code"));
                                departure.setAirPortCity(object.getString("airport_city"));
                                departureList.add(departure.getAirPortCity());
                                departureListModel.add(departure);

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
                Toast.makeText(BookingAirplaneTicketActivity.this, getString(R.string.tidak_dapat_terhubung_ke_server),
                        Toast.LENGTH_SHORT).show();
            } else {
                ArrayAdapter<String> spinDepartureAdapter = new ArrayAdapter<String>(BookingAirplaneTicketActivity.this, android.R.layout.simple_spinner_item, departureList);
                spinDepartureAdapter.setDropDownViewResource(R.layout.my_text_black);
                // set Spinner adapter departure
                spinDeparture.setAdapter(spinDepartureAdapter);
            }
        }
    }

    private class getArrivalAirport extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BookingAirplaneTicketActivity.this);
            pDialog.setMessage("Sedang mengambil data Destination...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Constant.WEB_URL + "flights/get_arrival_airport" + "?airline=" + airlinesId + "&departure=" + departureCode);

            // Add authorization header
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
                            JSONArray arrayProvider = objProvider.getJSONArray("arrival_airport");
                            for (int i = 0; i < arrayProvider.length(); i++) {
                                JSONObject object = arrayProvider.getJSONObject(i);
                                Destination destinations = new Destination();
                                destinations.setAirPortCode(object.getString("airport_code"));
                                destinations.setAirPortCity(object.getString("airport_city"));
                                destinationList.add(destinations.getAirPortCity());
                                destinationListModel.add(destinations);

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
                Toast.makeText(BookingAirplaneTicketActivity.this, "Terjadi kesalahan pada server. Mohon mencoba kembali.",
                        Toast.LENGTH_SHORT).show();
            } else {
                ArrayAdapter<String> spinDestinationAdapter = new ArrayAdapter<String>(BookingAirplaneTicketActivity.this, android.R.layout.simple_spinner_item, destinationList);
                spinDestinationAdapter.setDropDownViewResource(R.layout.my_text_black);
                // set Spinner adapter spinDestinationAdapter
                spinDestination.setAdapter(spinDestinationAdapter);
            }
        }
    }

    private class searchAirlinesTicket extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BookingAirplaneTicketActivity.this);
            pDialog.setMessage("Mohon Tunggu...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet;
            if (arrivalDate == null || arrivalDate.equals("")) {
                httpGet = new HttpGet(Constant.WEB_URL + "flights/search" + "?airline=" +
                        airlinesId + "&roundtrip=" + roundTrip + "&from=" + departureCode + "&to=" + destinationCode
                        + "&depart=" + departureDate + "&adult=" + adult + "&child=" + child);
            } else if (child == 0) {
                httpGet = new HttpGet(Constant.WEB_URL + "flights/search" + "?airline=" +
                        airlinesId + "&roundtrip=" + roundTrip + "&from=" + departureCode + "&to=" + destinationCode
                        + "&depart=" + departureDate + "&return=" + arrivalDate + "&adult=" + adult);
            } else {
                httpGet = new HttpGet(Constant.WEB_URL + "flights/search" + "?airline=" +
                        airlinesId + "&roundtrip=" + roundTrip + "&from=" + departureCode + "&to=" + destinationCode
                        + "&depart=" + departureDate + "&return=" + arrivalDate + "&adult=" + adult
                        + "&child=" + child + "&infant=" + infant);
            }
            // Add authorization header
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
                        Log.e("RIDONE", responseBody);
                        try {
                            JSONObject objData = new JSONObject(responseBody);
                            String errorNo = (objData.getString("error_no"));
                            if (errorNo.equals("0")) {
                                BookingAirplane booking = new BookingAirplane();

                                mSession.setSessionId(objData.getString("session_id"));

                                JSONObject objSearchInfo = objData.getJSONObject("search_info");

                                booking.setRoundTrip(objSearchInfo.getString("roundtrip"));
                                booking.setFrom(objSearchInfo.getString("from"));
                                booking.setTo(objSearchInfo.getString("to"));
                                booking.setDepart(objSearchInfo.getString("depart"));
                                booking.setDepartReturn(objSearchInfo.getString("return"));
                                booking.setAdult(objSearchInfo.getString("adult"));
                                booking.setChild(objSearchInfo.getString("child"));
                                booking.setInfant(objSearchInfo.getString("infant"));

                                JSONObject objAirlinesDetail = objData.getJSONObject("airlines_detail");
                                booking.setAirlineId(objAirlinesDetail.getString("airlines_id"));
                                booking.setAirlineCode(objAirlinesDetail.getString("airlines_code"));
                                booking.setAirlineName(objAirlinesDetail.getString("airlines_name"));

                                JSONObject objSchedule = objData.getJSONObject("schedule");

                                if (objSchedule.has("depart")) {
                                    JSONArray arrayDepart = objSchedule.getJSONArray("depart");
                                    if (arrayDepart != null && arrayDepart.length() > 0) {
                                        for (int i = 0; i < arrayDepart.length(); i++) {
                                            JSONObject objDepart = arrayDepart.getJSONObject(i);
                                            if (objDepart.has("connecting_flight")) {
                                                JSONArray arrayConnectingFlight = objDepart.getJSONArray("connecting_flight");
                                                if (arrayConnectingFlight != null && arrayConnectingFlight.length() > 0) {
                                                    for (int j = 0; j < arrayConnectingFlight.length(); j++) {
                                                        JSONObject objConnectingFlight = arrayConnectingFlight.getJSONObject(j);
                                                        JSONArray arrayClass = objConnectingFlight.getJSONArray("class");
                                                        for (int k = 0; k < arrayClass.length(); k++) {

                                                            booking = new BookingAirplane();

                                                            booking.setFromConnecting(objConnectingFlight.getString("from"));
                                                            booking.setToConnecting(objConnectingFlight.getString("to"));
                                                            booking.setDateConnecting(objConnectingFlight.getString("date"));
                                                            booking.setFnoConnecting(objConnectingFlight.getString("fno"));
                                                            booking.setEtdConnecting(objConnectingFlight.getString("etd"));
                                                            booking.setEtaConnecting(objConnectingFlight.getString("eta"));
                                                            booking.setViaConnecting(objConnectingFlight.getString("via"));

                                                            JSONObject objClass = arrayClass.getJSONObject(k);

                                                            booking.setClassName(objClass.getString("class_name"));
                                                            booking.setClassTitle(objClass.getString("class"));
                                                            booking.setClassPrice(objClass.getString("price"));
                                                            booking.setClassSeat(objClass.getString("seat"));
                                                            booking.setClassValue(objClass.getString("value"));

                                                            bookingListDepart.add(booking);
                                                        }
                                                    }
                                                }
                                            }

                                            if (objDepart.has("class")) {
                                                JSONArray arrayClass = objDepart.getJSONArray("class");
                                                for (int l = 0; l < arrayClass.length(); l++) {
                                                    booking = new BookingAirplane();

                                                    booking.setAirlineIdDepart(objDepart.getString("airline_id"));
                                                    booking.setAirlineNameDepart(objDepart.getString("airline_name"));
                                                    booking.setTypeSchedule(objDepart.getString("type"));
                                                    booking.setFromDepart(objDepart.getString("from"));
                                                    booking.setToDepart(objDepart.getString("to"));
                                                    booking.setDateDepart(objDepart.getString("date"));
                                                    booking.setFnoDepart(objDepart.getString("fno"));
                                                    booking.setEtdDepart(objDepart.getString("etd"));
                                                    booking.setEtaDepart(objDepart.getString("eta"));
                                                    booking.setViaDepart(objDepart.getString("via"));

                                                    JSONObject objClass = arrayClass.getJSONObject(l);

                                                    booking.setClassIdNext(objClass.getString("class_id"));
                                                    booking.setClassNameNext(objClass.getString("class_name"));
                                                    booking.setClassNext(objClass.getString("class"));
                                                    booking.setClassPriceNext(objClass.getString("price"));
                                                    booking.setClassSeatNext(objClass.getString("seat"));
                                                    booking.setClassValueNext(objClass.getString("value"));

                                                    bookingListDepart.add(booking);

                                                }
                                            }
                                        }
                                    } else {
                                        isDepartGone = true;
                                    }
                                }

                                if (objSchedule.has("return")) {
                                    JSONArray arrayReturn = objSchedule.getJSONArray("return");
                                    isReturn = true;
                                    for (int m = 0; m < arrayReturn.length(); m++) {
                                        JSONObject objReturn = arrayReturn.getJSONObject(m);
                                        if (objReturn.has("connecting_flight")) {
                                            JSONArray arrayConnectingFlightReturn = objReturn.getJSONArray("connecting_flight");
                                            if (arrayConnectingFlightReturn != null && arrayConnectingFlightReturn.length() > 0) {
                                                for (int n = 0; n < arrayConnectingFlightReturn.length(); n++) {
                                                    JSONObject objConnectingFlightReturn = arrayConnectingFlightReturn.getJSONObject(n);

                                                    if (objConnectingFlightReturn.has("class")) {
                                                        JSONArray arrayClassReturn = objConnectingFlightReturn.getJSONArray("class");
                                                        for (int o = 0; o < arrayClassReturn.length(); o++) {

                                                            booking = new BookingAirplane();

                                                            booking.setFromConnectingReturn(objConnectingFlightReturn.getString("from"));
                                                            booking.setToConnectingReturn(objConnectingFlightReturn.getString("to"));
                                                            booking.setDateConnectingReturn(objConnectingFlightReturn.getString("date"));
                                                            booking.setFnoConnectingReturn(objConnectingFlightReturn.getString("fno"));
                                                            booking.setEtdConnectingReturn(objConnectingFlightReturn.getString("etd"));
                                                            booking.setEtaConnectingReturn(objConnectingFlightReturn.getString("eta"));
                                                            if (objConnectingFlightReturn.has("via"))
                                                                booking.setViaConnectingReturn(objConnectingFlightReturn.getString("via"));

                                                            JSONObject objClassReturn = arrayClassReturn.getJSONObject(o);

                                                            booking.setClassNameReturn(objClassReturn.getString("class_name"));
                                                            if (objClassReturn.has("class")) {
                                                                booking.setClassReturn(objClassReturn.getString("class"));
                                                            }
                                                            booking.setClassPriceReturn(objClassReturn.getString("price"));
                                                            booking.setClassSeatReturn(objClassReturn.getString("seat"));
                                                            booking.setClassValueReturn(objClassReturn.getString("value"));

                                                            bookingListReturn.add(booking);
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if (objReturn.has("class")) {
                                            JSONArray arrayClassReturnNext = objReturn.getJSONArray("class");
                                            for (int p = 0; p < arrayClassReturnNext.length(); p++) {

                                                booking = new BookingAirplane();

                                                booking.setAirlineIdReturn(objReturn.getString("airline_id"));
                                                booking.setAirlineNameReturn(objReturn.getString("airline_name"));
                                                booking.setTypeReturn(objReturn.getString("type"));
                                                booking.setFromReturn(objReturn.getString("from"));
                                                booking.setToReturn(objReturn.getString("to"));
                                                booking.setDateReturn(objReturn.getString("date"));
                                                booking.setFnoReturn(objReturn.getString("fno"));
                                                booking.setEtdReturn(objReturn.getString("etd"));
                                                booking.setEtaReturn(objReturn.getString("eta"));
                                                if (objReturn.has("via"))
                                                    booking.setViaReturn(objReturn.getString("via"));

                                                JSONObject objClass = arrayClassReturnNext.getJSONObject(p);

                                                booking.setClassIdReturnNext(objClass.getString("class_id"));
                                                booking.setClassNameReturnNext(objClass.getString("class_name"));
                                                if (objClass.has("class")) {
                                                    booking.setClassReturnNext(objClass.getString("class"));
                                                }
                                                booking.setClassPriceReturnNext(objClass.getString("price"));
                                                booking.setClassSeatReturnNext(objClass.getString("seat"));
                                                booking.setClassValueReturnNext(objClass.getString("value"));

                                                bookingListReturn.add(booking);
                                            }
                                        }
                                    }
                                } else {
                                    isReturn = false;
                                }

                                success = true;
                            } else {
                                errorMessage = (objData.getString("error_msg"));
                                success = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("RIDONE", "ERROR " + e.getMessage());
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if (pDialog.isShowing())
                    pDialog.dismiss();

                if (!success) {
                    if (errorMessage != null) {
                        Toast.makeText(BookingAirplaneTicketActivity.this, errorMessage,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BookingAirplaneTicketActivity.this, "Tidak dapat terhubung ke server, silahkan periksa Internet koneksi Anda!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    llTiketPesawat.setVisibility(View.GONE);
                    llHasilPencarian.setVisibility(View.VISIBLE);

                    if (isReturn) {
                        llReturn.setVisibility(View.VISIBLE);
                    } else {
                        llReturn.setVisibility(View.GONE);
                    }

                    if (isDepartGone) {
                        recyclerViewDepart.setVisibility(View.GONE);
                        tvSchedule.setVisibility(View.GONE);
                        tvFrom.setVisibility(View.GONE);
                        tvTo.setVisibility(View.GONE);
                    }

                    jmlPenumpang = adult + child + infant;
                    tvSchedule.setText("Tanggal: " + departureDate + ", " + jmlPenumpang + " Penumpang");
                    tvScheduleReturn.setText("Tanggal: " + arrivalDate + ", " + jmlPenumpang + " Penumpang");
                    mSession.setJmlPenumpang(Integer.toString(jmlPenumpang));

                    // set to adapter recycler view
                    if (mAdapterReturn != null || mAdapterDepart != null) {
                        mAdapterDepart.notifyDataSetChanged();
                        mAdapterReturn.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private class getSearchDetail extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BookingAirplaneTicketActivity.this);
            pDialog.setMessage("Mohon Tunggu...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet httpGet;
                if (isReturn) {
                    httpGet = new HttpGet(Constant.WEB_URL + "flights/search_detail?" + "session_id=" + mSession.getSessionId()
                            + "&flightno_1=" + mSession.getFno1().replaceAll(" ", "%20") + "&class_id_1=" + mSession.getClass1()
                            + "&flightno_2=" + mSession.getFno1().replaceAll(" ", "%20") + "&class_id_2=" + mSession.getClass2());
                } else {
                    httpGet = new HttpGet(Constant.WEB_URL + "flights/search_detail?" + "session_id=" + mSession.getSessionId()
                            + "&flightno_1=" + mSession.getFno1().replaceAll(" ", "%20") + "&class_id_1=" + mSession.getClass1());
                }
                // Add authorization header
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
                            Log.e("RIDONE", "hasil == " + responseBody);
                            try {
                                JSONObject objData = new JSONObject(responseBody);
                                String errorNo = (objData.getString("error_no"));
                                if (errorNo.equals("0")) {
                                    mSession.setSessionIdSearchDetail(objData.getString("session_id"));
                                    success = true;
                                } else {
                                    errorMessage = (objData.getString("error_msg"));
                                    success = false;
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
                    Toast.makeText(BookingAirplaneTicketActivity.this, errorMessage,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookingAirplaneTicketActivity.this, "Terjadi kesalahan pada server. Mohon mencoba kembali.",
                            Toast.LENGTH_SHORT).show();
                }

                clearSessionByKey();

                pencarianUlang();

            } else {
                clearSessionByKey();

                Intent i = new Intent(BookingAirplaneTicketActivity.this, InputCustomerDataAirplaneTicketActivity.class);
                i.putExtra("jmlPenumpang", Integer.toString(jmlPenumpang));
                i.putExtra("adult", Integer.toString(adult));
                i.putExtra("child", Integer.toString(child));
                i.putExtra("infant", Integer.toString(infant));
                startActivity(i);
            }
        }
    }

    private void clearSessionByKey() {
        mSession.clearSharedPrevByKey("session_id");
        mSession.clearSharedPrevByKey("fno_1");
        mSession.clearSharedPrevByKey("fno_2");
        mSession.clearSharedPrevByKey("class_1");
        mSession.clearSharedPrevByKey("class_2");
    }

    public void selectedItemDepart(String airplaneName, String bookingPayment, String etd, String eta, String from, String to,
                                   String bookingClass, String bookingTimeTransit, String directFlight) {
        llItemSelectedDepart.setVisibility(View.VISIBLE);

        tvAirplaneName.setText(airplaneName);
        tvBookingPayment.setText("Rp." + bookingPayment);
        tvBookingTime.setText(etd + " - " + eta + "(" + from + " - " + to + ")");
        tvBookingClass.setText("Class " + bookingClass);
        tvBookingTimeTransit.setText(bookingTimeTransit);
        tvDirectFlag.setText(directFlight);
    }

    public void selectedItemReturn(String airplaneName, String bookingPayment, String etd, String eta, String from, String to,
                                   String bookingClass, String bookingTimeTransit, String directFlight) {
        llItemSelectedReturn.setVisibility(View.VISIBLE);

        tvAirplaneNameReturn.setText(airplaneName);
        tvBookingPaymentReturn.setText("Rp." + bookingPayment);
        tvBookingTimeReturn.setText(etd + " - " + eta + "(" + from + " - " + to + ")");

        if (bookingClass != null && !bookingClass.equals("")) {
            tvBookingClassReturn.setText("Class " + bookingClass);
        }

        tvBookingTimeTransitReturn.setText(bookingTimeTransit);
        tvDirectFlagReturn.setText(directFlight);
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
