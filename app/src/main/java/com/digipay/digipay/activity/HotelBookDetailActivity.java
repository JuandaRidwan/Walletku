package com.digipay.digipay.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digipay.digipay.R;
import com.digipay.digipay.adapter.HotelsAdapterDetail;
import com.digipay.digipay.functions.Constant;
import com.digipay.digipay.functions.SessionManager;
import com.digipay.digipay.models.HotelsDetail;
import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;
import java.util.List;

public class HotelBookDetailActivity extends AppCompatActivity {
    private String checkInInfo, checkOutInfo, hotelId, hotelName, hotelFacilities, roomFacilities,
            checkInInstructions, errorMessage;
    private SessionManager mSession;
    private TextView tvTglBooking, tvHotelName, tvHotelFacilities, tvRoomFacilities;
    private HotelsAdapterDetail adapterHotelsDetail;
    private List<HotelsDetail> hotelListDetail = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSession = new SessionManager(getApplicationContext());
        hotelId = mSession.getHotelId();

        ImageView imgHotelDetail = (ImageView) findViewById(R.id.imgHotelDetail);
        tvTglBooking = (TextView) findViewById(R.id.tvTglBooking);
        tvHotelName = (TextView) findViewById(R.id.tvHotelName);
        tvHotelFacilities = (TextView) findViewById(R.id.tvHotelFacilities);
        tvRoomFacilities = (TextView) findViewById(R.id.tvRoomFacilities);
        RecyclerView recyclerViewHotelDetail = (RecyclerView) findViewById(R.id.recycler_view_hotel_detail);
        Button btnSelectRoom = (Button) findViewById(R.id.btnSelectRoom);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels / 2;

        recyclerViewHotelDetail.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewHotelDetail.setLayoutManager(layoutManager);

        adapterHotelsDetail = new HotelsAdapterDetail(getApplicationContext(), hotelListDetail);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewHotelDetail.setLayoutManager(mLayoutManager);
        recyclerViewHotelDetail.setItemAnimator(new DefaultItemAnimator());
        recyclerViewHotelDetail.setFocusable(false);
        recyclerViewHotelDetail.setAdapter(adapterHotelsDetail);

        Picasso.with(HotelBookDetailActivity.this)
                .load(mSession.getHotelImgLarge())
                .placeholder(R.drawable.img_not_found)
                .resize(width, height)
                .into(imgHotelDetail);

        if (hotelId != null && !hotelId.equals("")) {
            new getHotelDetail().execute();
        }

        btnSelectRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSession.getRoomCodeDetail().equals("") && mSession.getRoomCodeDetail() != null) {
                    new selectHotelRoom().execute();
                } else {
                    Toast.makeText(HotelBookDetailActivity.this, "Silahkan pilih kamar tersedia!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private class getHotelDetail extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HotelBookDetailActivity.this);
            pDialog.setMessage("Mohon Tunggu...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Constant.WEB_URL + "hotels/hotel_detail" + "?session_id=" + mSession.getSessionIdHotel()
                    + "&hotelId=" + hotelId);

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
                        try {
                            JSONObject objData = new JSONObject(responseBody);
                            String errorNo = (objData.getString("error_no"));
                            if (errorNo.equals("0")) {
                                JSONObject objSearchInfo = objData.getJSONObject("search_info");
                                checkInInfo = objSearchInfo.getString("ci");
                                checkOutInfo = objSearchInfo.getString("co");
                                mSession.setSessionIdHotelDetail(objSearchInfo.getString("session_id"));

                                JSONObject objResultData = objData.getJSONObject("result_data");
                                hotelName = objResultData.getString("name");

                                JSONArray arrayAvailableRoom = objResultData.getJSONArray("available_room");
                                for (int i = 0; i < arrayAvailableRoom.length(); i++) {
                                    HotelsDetail hotelsDetail = new HotelsDetail();
                                    JSONObject objRoom = arrayAvailableRoom.getJSONObject(i);
                                    hotelsDetail.setBigImages(objRoom.getString("big_images"));
                                    hotelsDetail.setCancelDetail(objRoom.getString("cancelDetail"));
                                    hotelsDetail.setRoomCode(objRoom.getString("room_code"));
                                    hotelsDetail.setBasicPricePerNight(objRoom.getString("basic_price_per_night"));
                                    hotelsDetail.setSmallImages(objRoom.getString("small_images"));
                                    hotelsDetail.setRoomCapacity(objRoom.getString("room_capacity"));
                                    hotelsDetail.setRoomName(objRoom.getString("room_name"));
                                    hotelListDetail.add(hotelsDetail);
                                }

                                JSONObject objHotelInfo = objResultData.getJSONObject("hotel_info");
                                hotelFacilities = objHotelInfo.getString("hotel_facilities");
                                roomFacilities = objHotelInfo.getString("detail_facilities");
                                checkInInstructions = objHotelInfo.getString("checkInInstructions");

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
                    Toast.makeText(HotelBookDetailActivity.this, errorMessage,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HotelBookDetailActivity.this, getString(R.string.tidak_dapat_terhubung_ke_server),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                adapterHotelsDetail.notifyDataSetChanged();
                initView();
            }
        }
    }

    private void initView() {
        tvHotelName.setText(hotelName);
        tvTglBooking.setText("Tanggal Booking: " + checkInInfo + " - " + checkOutInfo + "\n");
        tvHotelFacilities.setText("Fasilitas Hotel: " + hotelFacilities + "\n");
        tvRoomFacilities.setText("Fasilitas Kamar: " + roomFacilities + "\n\nIntruksi: " + checkInInstructions);
    }

    private class selectHotelRoom extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        boolean success = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HotelBookDetailActivity.this);
            pDialog.setMessage("Mohon Tunggu...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Constant.WEB_URL + "hotels/select_room"
                    + "?session_id=" + mSession.getSessionIdHotelDetail() + "&room_code=" + mSession.getRoomCodeDetail());

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
                                mSession.setAdminFeeHotel(objData.getString("admin_fee"));
                                JSONObject objSearchInfo = objData.getJSONObject("search_info");
                                mSession.setSessionIdHotelBook(objSearchInfo.getString("session_id"));
                                mSession.setRoomCode(objSearchInfo.getString("room_code"));

                                JSONObject objRoomPrice = objData.getJSONObject("room_price");
                                mSession.setBasicPrice(objRoomPrice.getString("basic_price"));
                                mSession.setFeeNTax(objRoomPrice.getString("fee_and_tax"));
                                mSession.setTotalPrice(objRoomPrice.getString("total_price"));
                                mSession.setComission(objRoomPrice.getString("comission"));

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
                    Toast.makeText(HotelBookDetailActivity.this, errorMessage,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HotelBookDetailActivity.this, getString(R.string.tidak_dapat_terhubung_ke_server),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Intent i = new Intent(HotelBookDetailActivity.this, PaymentAirplaneAndHotelActivity.class);
                i.putExtra("isHotelOrAirplane", "hotel");
                startActivity(i);
                mSession.clearSharedPrevByKey("room_code_detail");
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
