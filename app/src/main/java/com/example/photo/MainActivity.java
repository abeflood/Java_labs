package com.example.photo;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;




public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;
    static final int CAMERA_REQUEST_CODE = 1;
    private String currentPhotoPath = null;
    private int currentPhotoIndex = 0;
    private ArrayList<String> photoGallery;
    public Date minDate = new Date(Long.MIN_VALUE);
    public Date maxDate = new Date(Long.MAX_VALUE);
    public TextView timestamp;
    public LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    public EditText caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Acquire a reference to the system Location Manager

        LocationListener locationListener = new  LocationListener() {
             public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }
            // Register the listener with the Location Manager to receive location updates
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timestamp = (TextView) findViewById(R.id.TimeStamp);
        caption = findViewById(R.id.Caption);
        Button btnLeft = (Button)findViewById(R.id.btnLeft);
        Button btnCaption = (Button)findViewById(R.id.btnCaption);
        Button btnRight = (Button)findViewById(R.id.btnRight);
        Button btnFilter = (Button)findViewById(R.id.btnFilter);
        btnLeft.setOnClickListener(this);
        btnCaption.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnFilter.setOnClickListener(filterListener);

        //Date minDate = new Date(Long.MIN_VALUE);
        //Date maxDate = new Date(Long.MAX_VALUE);
        photoGallery = populateGallery(minDate, maxDate,"");


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



    private ArrayList<String> populateGallery(Date minDate, Date maxDate,String keywords) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photo/files/Pictures");
        photoGallery = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : fList) {
                if (((minDate == null && maxDate == null) || (f.lastModified() >= minDate.getTime()
                            && f.lastModified() <= maxDate.getTime())
                ) && (keywords == "" || f.getPath().contains(keywords)))
                    photoGallery.add(f.getPath());
            }
        }
        currentPhotoIndex = 0;
        currentPhotoPath = photoGallery.get(currentPhotoIndex);
        displayPhoto(currentPhotoPath);
        String[] split_str = currentPhotoPath.split("_");
        timestamp.setText(split_str[1]);
        caption.setText(split_str[3]);
        caption.invalidate();
        timestamp.invalidate();
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

    public void onClick( View v) {

        //Date minDate = new Date(Long.MIN_VALUE);
        //Date maxDate = new Date(Long.MAX_VALUE);
        if (photoGallery.size() != 0 ) {
            switch (v.getId()) {
                case R.id.btnCaption:
                    File source =new File(currentPhotoPath);
                    String[] split_str2 = currentPhotoPath.split("_");
                    String fin = split_str2[0]+"_"+split_str2[1]+"_"+split_str2[2]+"_"+caption.getText()+"_"+split_str2[4];
                    File destination =new File(fin);
                    source.renameTo(destination);
                    photoGallery = populateGallery(minDate, maxDate,"");
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

            currentPhotoPath = photoGallery.get(currentPhotoIndex);
            String[] split_str = currentPhotoPath.split("_");
            timestamp.setText(split_str[1]);
            caption.setText(split_str[3]);
            caption.invalidate();
            timestamp.invalidate();
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
                Date startTimestamp , endTimestamp;
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
                currentPhotoIndex = 0;
                photoGallery = populateGallery(startTimestamp, endTimestamp, keywords);
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
            photoGallery = populateGallery(new Date(Long.MIN_VALUE), new Date(), "");
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_NoCaption_";
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", dir );
        currentPhotoPath = image.getAbsolutePath();
        Log.d("createImageFile", currentPhotoPath);
        return image;


    }

}