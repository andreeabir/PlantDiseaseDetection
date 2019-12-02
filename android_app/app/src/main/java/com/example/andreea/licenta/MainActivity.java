package com.example.andreea.licenta;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
//import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    private static final String IMAGE_DIRECTORY = "/andreea";
    ImageView imageToUpload;
    File mPhotoFile = null;
    ImageButton bUploadImage;
    Button bRun;
    EditText serverResponse;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private final OkHttpClient client = new OkHttpClient();
    private String message;
    final Context context = this;
    Bitmap bitmap_to_send = null;
    Button bSwitchActivity = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_layout);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
//                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP| ActionBar.DISPLAY_USE_LOGO);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.var4);
        requestMultiplePermissions();

        bUploadImage = (ImageButton) findViewById(R.id.imageButton);
//        bSwitchActivity = (Button) findViewById(R.id.button);
//        bRun = (Button) findViewById(R.id.Run);
//        serverResponse = (EditText) findViewById(R.id.serverResponse);
//        bSwitchActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeActivity();
//            }
//        });

        bUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
//        bUploadImage.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                showPictureDialog();
//            }
//        });

//        bRun.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (bitmap_to_send == null) {
//                    Toast.makeText(MainActivity.this, "Please upload a photo!", Toast.LENGTH_SHORT).show();
//                } else {
//                    new Thread(new Runnable() {
//
//                        public void run() {
////                            uploadFile(mPhotoFile.getAbsolutePath());
//                            uploadFile(bitmap_to_send);
//                        }
//                    }).start();
//                }
//                }
//        });

    }

    public void changeActivity(){
        Intent myIntent = new Intent(getBaseContext(),  SecondActivity.class);
        myIntent.putExtra("value1", "Barcelona");
        startActivity(myIntent);
    }

//    public void uploadFile(Bitmap bitmap_to_send) {
//
////        BitmapFactory.Options options = new BitmapFactory.Options();
////        options.inSampleSize = 4;
////        options.inPreferredConfig = Bitmap.Config.RGB_565;
////        Bitmap bm = BitmapFactory.decodeFile(sourceFileUri, options);
//        Bitmap croppedBitmap = getResizedBitmap(bitmap_to_send, 256);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//
//        byte[] byteImage_photo = baos.toByteArray();
//        //generate base64 string of image
//        String encodedImage = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
//        String data = sendData(encodedImage);
////        Toast.makeText(MainActivity.this, "Image was sent to the server", Toast.LENGTH_SHORT).show();
//
//        Log.d("data", data);
//
//    JSONArray arr = null;
//        try {
//            arr = new JSONArray(data);
//            JSONObject jObj = arr.getJSONObject(0);
//            message = jObj.getString("phrase");
//            if (message.equals("")) {
//                message = "Hi 1! there was a problem getting the information, please try again";
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            message = "Hi 2! there was a problem getting the information, please try again";
//        }
//
//        Log.d("TAG", message);
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(context);
//                builder1.setTitle("Your plant");
//                builder1.setMessage(message);
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton(
//                        "Ok",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
////                                this.getSupportFragmentManager().popBackStack();
//                            }
//                        });
//                android.support.v7.app.AlertDialog alert11 = builder1.create();
//                alert11.show();
//            }
//        });
//
//    }
//
//    public String sendData(String image) {
//        try {
//            RequestBody formBody = new FormBody.Builder()
//                    .add("image", image)
//                    .build();
//
//            Request request = new Request.Builder()
//                    .url(getResources().getString(R.string.server_url))
//                    .post(formBody)
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            return response.body().string();
//        } catch (IOException e) {
//            return "Error: " + e.getMessage();
//        }
//    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"
        };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;

                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY_PHOTO);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.RESULT_CANCELED) {
                return;
        }

        if(requestCode == REQUEST_GALLERY_PHOTO) {
            if (data != null){
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    bitmap_to_send = bitmap;
//                    String path = saveImage(bitmap);
//                    Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap_to_send.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] byteImage_photo = baos.toByteArray();

                    Intent myIntent = new Intent(getBaseContext(),  SecondActivity.class);
                    myIntent.putExtra("value1", byteImage_photo);
                    startActivity(myIntent);

//                    imageToUpload.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO){
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            bitmap_to_send = thumbnail;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap_to_send.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteImage_photo = baos.toByteArray();
            Intent myIntent = new Intent(getBaseContext(),  SecondActivity.class);
            myIntent.putExtra("value1", byteImage_photo);
            startActivity(myIntent);

//            imageToUpload.setImageBitmap(bitmap);
//            bitmap_to_send = thumbnail;
//            imageToUpload.setImageBitmap(thumbnail);
            saveImage(thumbnail);
//            Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }

    }

    public String saveImage(Bitmap myBitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Bitmap processed_bitmap = getResizedBitmap(myBitmap, 256);
        processed_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            mPhotoFile = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            mPhotoFile.createNewFile();
            FileOutputStream fo = new FileOutputStream(mPhotoFile);
            fo.write(bytes.toByteArray());
            android.media.MediaScannerConnection.scanFile(this,
                    new String[]{mPhotoFile.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved:: ---&gt;" + mPhotoFile.getAbsolutePath());
        } catch (IOException e1){
            e1.printStackTrace();
        }
        return "";
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


//    private class UploadImage extends AsyncTask<Void, Void, Void>{
//        Bitmap image;
//        String name;
//
//        public UploadImage(Bitmap image, String name){
//            this.image = image;
//            this.name = name;
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
//            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
//            String encodedImage = Base64.encodeToString(byteArray.toByteArray(), Base64.DEFAULT);
//            ArrayList<NameValuePair> dataTosend = new ArrayList<>();
//            dataTosend.add(new BasicNameValuePair("image", encodedImage));
//            dataTosend.add(new BasicnameValuepair("name", name));
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid){
//            super.onPostExecute(aVoid);
//        }
//    }
//
//    private HttpParams getHttpRespParams(){
//        HttpParams httpRequestParams = new BasicHttpParams();
//        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
//        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
//        return httpRequestParams;
//    }
}
