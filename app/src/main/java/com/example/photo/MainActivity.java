package com.example.photo;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;
    static final int CAMERA_REQUEST_CODE = 1;
    public String currentPhotoPath = null;
    private int currentPhotoIndex = 0;
    public ArrayList<String> photoGallery;
    public int min_long = -5000;
    public int max_long = 5000;
    public int min_lat = -5000;
    public int max_lat = 5000;
    public Date minDate = new Date(Long.MIN_VALUE);
    public Date maxDate = new Date(Long.MAX_VALUE);
    public TextView timestamp;
    public FusedLocationProviderClient fusedLocationClient;
    public EditText caption;
    public String slat = "test";
    public String slong;
    public TextView lat_text;
    public TextView long_text;
    public DownloadWebPageTask task = new DownloadWebPageTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermission();
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {

                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            int latitude = (int) location.getLatitude();
                            int longitude = (int) location.getLongitude();
                            slat = latitude + "";
                            slong = longitude + "";
                        }
                    }
                });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        https:
//www.w3schools.com/html/html_images.asp
        timestamp = (TextView) findViewById(R.id.TimeStamp);
        lat_text = (TextView) findViewById(R.id.Lat_Text);
        long_text = (TextView) findViewById(R.id.Long_Text);
        caption = findViewById(R.id.Caption);
        Button btnLeft = (Button) findViewById(R.id.btnLeft);
        Button btnCaption = (Button) findViewById(R.id.btnCaption);
        Button btnRight = (Button) findViewById(R.id.btnRight);
        Button btnFilter = (Button) findViewById(R.id.btnFilter);
        btnLeft.setOnClickListener(this);
        btnCaption.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnFilter.setOnClickListener(filterListener);

        //Date minDate = new Date(Long.MIN_VALUE);
        //Date maxDate = new Date(Long.MAX_VALUE);
        photoGallery = populateGallery(minDate, maxDate, "", min_long, max_long, min_lat, max_lat);


        Log.d("onCreate, size", Integer.toString(photoGallery.size()));
        if (photoGallery.size() > 0)
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
        if (photoGallery.size() != 0) {
            String[] split_str = currentPhotoPath.split("_");
            timestamp.setText(split_str[1]);
            caption.setText(split_str[3]);
            caption.invalidate();
            timestamp.invalidate();
            displayPhoto(currentPhotoPath);
        }
    }

    private View.OnClickListener filterListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
        }
    };


    private ArrayList<String> populateGallery(Date minDate, Date maxDate, String keywords,
                                              int longmin, int longmax, int latmin, int latmax) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photo/files/Pictures");
        photoGallery = new ArrayList<String>();
        File ret;
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : fList) {
                ret = fileChecker.checkFile(f, minDate, maxDate, keywords,
                        longmin, longmax, latmin, latmax);
                if (ret != null)
                    photoGallery.add(f.getPath());
            }
        }
        currentPhotoIndex = 0;
        if (photoGallery.size() != 0) {
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
            displayPhoto(currentPhotoPath);
            String[] split_str = currentPhotoPath.split("_");
            timestamp.setText(split_str[1]);
            caption.setText(split_str[3]);
            lat_text.setText(split_str[4]);
            long_text.setText(split_str[5]);
            caption.invalidate();
            timestamp.invalidate();
        }
        return photoGallery;
    }

    private void displayPhoto(String path) {
        ImageView iv = (ImageView) findViewById(R.id.ivMain);
        iv.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void onClick(View v) {

        //Date minDate = new Date(Long.MIN_VALUE);
        //Date maxDate = new Date(Long.MAX_VALUE);
        if (photoGallery.size() != 0) {
            switch (v.getId()) {
                case R.id.btnUpload:
                    task.execute(new String[] { "http://localhost:9091/PhotoServer/sends" });
                case R.id.btnCaption:
                    File source = new File(currentPhotoPath);
                    String[] split_str2 = currentPhotoPath.split("_");
                    String fin = split_str2[0] + "_" + split_str2[1] + "_" + split_str2[2] + "_" + caption.getText() + "_" + split_str2[4] + "_" + split_str2[5] + "_" + split_str2[6];
                    File destination = new File(fin);
                    source.renameTo(destination);
                    photoGallery = photoGallery = populateGallery(minDate, maxDate, "", min_long, max_long, min_lat, max_lat);

                    break;
                case R.id.btnLeft:
                    --currentPhotoIndex;
                    break;
                case R.id.btnRight:
                    ++currentPhotoIndex;
                    break;
                default:
                    break;
            }
            if (currentPhotoIndex < 0)
                currentPhotoIndex = 0;
            if (currentPhotoIndex >= photoGallery.size())
                currentPhotoIndex = photoGallery.size() - 1;
            if (photoGallery.size() != 0) {
                currentPhotoPath = photoGallery.get(currentPhotoIndex);
                String[] split_str = currentPhotoPath.split("_");
                timestamp.setText(split_str[1]);
                caption.setText(split_str[3]);
                lat_text.setText(split_str[4]);
                long_text.setText(split_str[5]);
                caption.invalidate();
                timestamp.invalidate();
            }
            Log.d("phpotoleft, size", Integer.toString(photoGallery.size()));
            Log.d("photoleft, index", Integer.toString(currentPhotoIndex));
            displayPhoto(currentPhotoPath);

        }
    }


    //    public void goToSettings(View v) {
//        Intent i = new Intent(this, SettingsActivity.class);
//        startActivity(i);
//    }
//
//    public void goToDisplay(String x) {
//        Intent i = new Intent(this, DisplayActivity.class);
//        i.putExtra("DISPLAY_TEXT", x);
//        startActivity(i);
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DateFormat format = new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
                Date startTimestamp, endTimestamp;
                try {
                    String from = (String) data.getStringExtra("STARTTIMESTAMP");
                    String to = (String) data.getStringExtra("ENDTIMESTAMP");
                    startTimestamp = format.parse(from);
                    endTimestamp = format.parse(to);
                } catch (Exception ex) {
                    startTimestamp = null;
                    endTimestamp = null;
                }
                String keywords = (String) data.getStringExtra("KEYWORDS");
                int LongMin = Integer.parseInt(data.getStringExtra("LONGMIN"));
                int LongMax = Integer.parseInt(data.getStringExtra("LONGMAX"));
                ;
                int LatMin = Integer.parseInt(data.getStringExtra("LATMIN"));
                ;
                int LatMax = Integer.parseInt(data.getStringExtra("LATMAX"));
                ;
                currentPhotoIndex = 0;
                photoGallery = populateGallery(startTimestamp, endTimestamp, keywords, LongMin, LongMax, LatMin, LatMax);
                if (photoGallery.size() == 0) {
                    displayPhoto(null);
                } else {
                    displayPhoto(photoGallery.get(currentPhotoIndex));
                }
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            ImageView mImageView = (ImageView) findViewById(R.id.ivMain);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath));
            photoGallery = populateGallery(minDate, maxDate, "", min_long, max_long, min_lat, max_lat);
        }

    }

    public void takePicture(View v) {


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("FileCreation", "Failed");
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.photo.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermission();
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {

                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            int latitude = (int) location.getLatitude();
                            int longitude = (int) location.getLongitude();
                            slat = latitude + "";
                            slong = longitude + "";
                        }
                    }
                });
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_NoCaption_" + slat + "_" + slong + "_";
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", dir);
        currentPhotoPath = image.getAbsolutePath();
        Log.d("createImageFile", currentPhotoPath);
        return image;


    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    public void shareImage(View v) {
        Uri imagePath = Uri.parse(currentPhotoPath);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imagePath);
        startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
    }
    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
                Bitmap bm = BitmapFactory.decodeFile(currentPhotoPath);
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                byte[] data = bos.toByteArray();
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost(urls[0]);
                ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");
                // File file= new File("/mnt/sdcard/forest.png");
                // FileBody bin = new FileBody(file);
                MultipartEntity reqEntity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("uploaded", bab);
                reqEntity.addPart("photoCaption", new StringBody("sfsdfsdf"));
                postRequest.setEntity(reqEntity);
                HttpResponse response = httpClient.execute(postRequest);
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();

                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }
                System.out.println("Response: " + s);
            } catch (Exception e) {
                // handle exception here
                Log.e(e.getClass().getName(), e.getMessage());
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }
}
