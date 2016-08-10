package com.digipay.digipay.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.digipay.digipay.R;
import com.digipay.digipay.functions.Constant;
import com.digipay.digipay.functions.SessionManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilesActivity extends AppCompatActivity {
    private CircleImageView imgProfileCircle;
    private EditText edtNamaProfile, edtNoKtp, edtNoHp, edtAlamat;
    private ProgressBar progressBar;
    private String pathPhotoBase64;
    private SessionManager mSession;
    private String mNama, mNoKtp, mEdtNoHp, mAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSession = new SessionManager(getApplicationContext());

        imgProfileCircle = (CircleImageView) findViewById(R.id.imgProfileCircle);
        edtNamaProfile = (EditText) findViewById(R.id.edtNamaProfile);
        edtNoKtp = (EditText) findViewById(R.id.edtNoKtp);
        edtNoHp = (EditText) findViewById(R.id.edtNoHp);
        edtAlamat = (EditText) findViewById(R.id.edtAlamat);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button btnSimpan = (Button) findViewById(R.id.btnSimpan);

        // ngambil data user dari sessionManager
        String imageProfile = mSession.getAvatar();
        Log.d("picture", mSession.getAvatar());
        if (imageProfile != null) {
            progressBar.setVisibility(View.VISIBLE);
            Picasso.with(ProfilesActivity.this)
                    .load("http://188.166.236.85" + imageProfile)
                    .placeholder(R.drawable.ic_profile_yellow)
                    .into(imgProfileCircle, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }

        String noKtp = mSession.getNoKtp();
        String nama = mSession.getName();
        String noHp = mSession.getNoHp();
        String alamat = mSession.getAlamat();
        edtAlamat.setText(alamat);
        edtNamaProfile.setText(nama);
        if (!noKtp.equals("null") && !noHp.equals("null")) {
            edtNoKtp.setText(noKtp);
            edtNoHp.setText(noHp);
        } else {
            edtNoKtp.setText("");
            edtNoHp.setText("");
        }

        imgProfileCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationAndEditProfile();
            }
        });
    }


    private void selectImage() {
        final CharSequence[] options = {"Gallery", "Batal"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilesActivity.this);
        builder.setTitle("Ambil Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImageUri;

            if (requestCode == 2) {
                try {
                    selectedImageUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImageUri,
                            filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);

                    // encode image
                    Bitmap bm = BitmapFactory.decodeFile(picturePath);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    byte[] path = baos.toByteArray();

                    String pathGallery = Base64.encodeToString(path, Base64.DEFAULT);
                    pathPhotoBase64 = "data:image/jpg;base64," + pathGallery;

                    cursor.close();
                    Log.d("picturePath", picturePath);
                    imgProfileCircle.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                } catch (OutOfMemoryError e) {
                    Toast.makeText(ProfilesActivity.this, "Ukuran gambar terlalu besar.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void validationAndEditProfile() {
        mNama = edtNamaProfile.getText().toString();
        mNoKtp = edtNoKtp.getText().toString();
        mEdtNoHp = edtNoHp.getText().toString();
        mAlamat = edtAlamat.getText().toString();
        Boolean isValid = true;
        if (TextUtils.isEmpty(mNama)) {
            edtNamaProfile.setError("Nama tidak boleh kosong!");
            isValid = false;
        }
        if (TextUtils.isEmpty(mNoKtp)) {
            edtNoKtp.setError("Silahkan masukan nomor KTP!");
            isValid = false;
        }
        if (TextUtils.isEmpty(mEdtNoHp)) {
            edtNoHp.setError("Silahkan masukan nomor Handphone!");
            isValid = false;
        }
        if (TextUtils.isEmpty(mAlamat)) {
            edtAlamat.setError("Silahkan masukan Alamat!");
            isValid = false;
        }
        if (isValid) {
            new doEditProfile().execute();
        }
    }

    private class doEditProfile extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;
        Boolean success = false;
        String id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfilesActivity.this);
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
            if (pathPhotoBase64 == null) {
                pathPhotoBase64 = "";
            } else {
                Log.d("path", pathPhotoBase64);
            }

            try {
                HttpPut post = new HttpPut(Constant.WEB_URL + "users/" + mSession.getUserId());
                obj.put("name", mNama);
                obj.put("address", mAlamat);
                obj.put("phone", mEdtNoHp);
                obj.put("avatar", pathPhotoBase64);
                obj.put("no_ktp", mNoKtp);
                obj.put("pin", "");
                obj.put("referral_id", "");

                userObj.put("user", obj);
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
                        if (responseBody != null) {
                            JSONObject object = new JSONObject(responseBody);
                            JSONObject objUser = object.getJSONObject("user");
                            mSession.setUserId(objUser.getString("id"));
                            mSession.setEmail(objUser.getString("email"));
                            mSession.setName(objUser.getString("name"));
                            mSession.setAlamat(objUser.getString("address"));
                            mSession.setNoHp(objUser.getString("phone"));
                            mSession.setNoKtp(objUser.getString("no_ktp"));
                            mSession.setUId(objUser.getString("uid"));
                            mSession.setReferalId(objUser.getString("referral_id"));
                            mSession.setTotalBalance(objUser.getString("total_balance"));
                            mSession.setTotalBonus(objUser.getString("total_bonus"));
                            mSession.setTotalPoint(objUser.getString("total_point"));
                            mSession.setPremium(objUser.getString("premium"));
                            mSession.setAvatar(objUser.getString("avatar_url"));
                            success = true;
                        } else {
                            success = false;
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
                Toast.makeText(ProfilesActivity.this, "Gagal update profile. Mohon mencoba kembali",
                        Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                Picasso.with(ProfilesActivity.this)
                        .load("http://188.166.236.85" + mSession.getAvatar())
                        .placeholder(R.drawable.ic_profile_yellow)
                        .into(imgProfileCircle, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
//                finish();
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
