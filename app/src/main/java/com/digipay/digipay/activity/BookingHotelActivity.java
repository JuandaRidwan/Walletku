package com.digipay.digipay.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digipay.digipay.R;
import com.digipay.digipay.adapter.HotelsAdapter;
import com.digipay.digipay.functions.Constant;
import com.digipay.digipay.functions.SessionManager;
import com.digipay.digipay.models.Hotels;

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

public class BookingHotelActivity extends AppCompatActivity {
    private DatePicker dpResult;
    private TextView tvCheckIn, tvCheckOut;
    private boolean isCheckIn;
    private int day, year, month;
    private String checkIn, checkOut, city, errorMessage, room, adult, child;
    static final int DATE_DIALOG_ID = 999;
    private SessionManager mSession;
    private List<Hotels> hotelList = new ArrayList<>();
    private HotelsAdapter adapterHotels;
    private LinearLayout llPesanHotel;
    private RecyclerView recyclerViewHotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_hotel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSession = new SessionManager(getApplicationContext());

        recyclerViewHotel = (RecyclerView) findViewById(R.id.recycler_view_hotel);
        llPesanHotel = (LinearLayout) findViewById(R.id.llPesanHotel);
        ImageView imgCalendarCheckOut = (ImageView) findViewById(R.id.imgCalendarCheckOut);
        ImageView imgCalendarCheckIn = (ImageView) findViewById(R.id.imgCalendarCheckIn);
        FrameLayout frameSearch = (FrameLayout) findViewById(R.id.frameSearchHotel);
        final EditText edtCity = (EditText) findViewById(R.id.edtCity);
        Spinner spinAdult = (Spinner) findViewById(R.id.SpinnerAdult);
        Spinner spinChild = (Spinner) findViewById(R.id.SpinnerChild);
        Spinner spinRoom = (Spinner) findViewById(R.id.SpinnerRoom);
        tvCheckOut = (TextView) findViewById(R.id.tvCheckOut);
        dpResult = (DatePicker) findViewById(R.id.datePicker);
        tvCheckIn = (TextView) findViewById(R.id.tvCheckIn);
        isCheckIn = false;

        recyclerViewHotel.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewHotel.setLayoutManager(layoutManager);

        adapterHotels = new HotelsAdapter(getApplicationContext(), hotelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewHotel.setLayoutManager(mLayoutManager);
        recyclerViewHotel.setItemAnimator(new DefaultItemAnimator());
        recyclerViewHotel.setFocusable(false);
        recyclerViewHotel.setAdapter(adapterHotels);

        // set Current date
        setCurrentDateOnView();

        ArrayAdapter<CharSequence> AdapterCount = ArrayAdapter
                .createFromResource(getApplicationContext(), R.array.count_list,
                        R.layout.my_text_black);
        AdapterCount.setDropDownViewResource(R.layout.my_text_black);
        spinAdult.setAdapter(AdapterCount);
        spinChild.setAdapter(AdapterCount);
        spinRoom.setAdapter(AdapterCount);

        spinRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                room = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinAdult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                adult = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinChild.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                child = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imgCalendarCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckIn = true;
                showDialog(DATE_DIALOG_ID);
            }
        });

        imgCalendarCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckIn = false;
                showDialog(DATE_DIALOG_ID);
            }
        });

        frameSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city = edtCity.getText().toString();
                if (!city.equals("") && checkIn != null && checkOut != null) {
                    if (!room.equals("0") && !adult.equals("0")) {
                        new getHotelSearch().execute();
                    } else {
                        Toast.makeText(BookingHotelActivity.this, "Jumlah Kamar dan Adult tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BookingHotelActivity.this, "Silahkan lengkapi kolom-kolom di atas.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // display current date
    private void setCurrentDateOnView() {
        try {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            try {
                Date date = new Date(year - 1900, month, day);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = formatter.format(date);
                tvCheckIn.setText(currentDate);
                tvCheckOut.setText(currentDate);

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
                            year, month, day);
            }
        } catch (IllegalFormatConversionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
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
                if (isCheckIn) {
                    tvCheckIn.setText(nowDate);
                    checkIn = nowDate;
                } else {
                    tvCheckOut.setText(nowDate);
                    checkOut = nowDate;
                }

                dpResult.init(year, month, day, null);
            } catch (IllegalFormatConversionException e) {
                e.printStackTrace();
            }
        }
    };

    private class getHotelSearch extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BookingHotelActivity.this);
            pDialog.setMessage("Mohon Tunggu...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet;
            if (!child.equals("0")) {
                httpGet = new HttpGet(Constant.WEB_URL + "hotels/hotel_search" + "?city=" + city.replaceAll(" ", "")
                        + "&ci=" + checkIn + "&co=" + checkOut + "&room=" + room + "&adult=" + adult + "&child=" + child);
            } else {
                httpGet = new HttpGet(Constant.WEB_URL + "hotels/hotel_search" + "?city=" + city.replaceAll(" ", "")
                        + "&ci=" + checkIn + "&co=" + checkOut + "&room=" + room + "&adult=" + adult);
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
                                Hotels hotels = new Hotels();

                                mSession.setSessionIdHotel(objData.getString("session_id"));

                                JSONObject objSearchInfo = objData.getJSONObject("search_info");
                                hotels.setCity(objSearchInfo.getString("city"));
                                hotels.setCheckIn(objSearchInfo.getString("ci"));
                                hotels.setCheckOut(objSearchInfo.getString("co"));
                                hotels.setRoom(objSearchInfo.getString("room"));
                                hotels.setAdult(objSearchInfo.getString("adult"));
                                hotels.setChild(objSearchInfo.getString("child"));
                                hotels.setPageRef(objSearchInfo.getString("page_ref"));

                                JSONArray arrayResultData = objData.getJSONArray("result_data");
                                for (int i = 0; i < arrayResultData.length(); i++) {
                                    hotels = new Hotels();

                                    JSONObject objResultData = arrayResultData.getJSONObject(i);

                                    hotels.setHotelId(objResultData.getInt("hotelId"));
                                    hotels.setSource(objResultData.getString("source"));
                                    hotels.setStructureType(objResultData.getString("structureType"));
                                    hotels.setCsm(objResultData.getString("csm"));
                                    hotels.setName(objResultData.getString("name"));
                                    hotels.setCityName(objResultData.getString("cityName"));
                                    hotels.setAddress(objResultData.getString("address"));
                                    hotels.setPostalCode(objResultData.getString("postalCode"));
                                    hotels.setAirportCode(objResultData.getString("airportCode"));
                                    hotels.setCurrency(objResultData.getString("currency"));
                                    hotels.setPrice(objResultData.getString("price"));
                                    hotels.setThumbNailUrl(objResultData.getString("thumbNailUrl"));
                                    hotels.setLargeThumbnailURL(objResultData.getString("largeThumbnailURL"));
                                    hotels.setShortDescription(objResultData.getString("shortDescription"));
                                    hotels.setRoomInfo(objResultData.getString("room_info"));

                                    hotelList.add(hotels);
                                }
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
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (!success) {
                if (errorMessage != null) {
                    Toast.makeText(BookingHotelActivity.this, errorMessage,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookingHotelActivity.this, getString(R.string.tidak_dapat_terhubung_ke_server),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                llPesanHotel.setVisibility(View.GONE);
                recyclerViewHotel.setVisibility(View.VISIBLE);
                adapterHotels.notifyDataSetChanged();
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
