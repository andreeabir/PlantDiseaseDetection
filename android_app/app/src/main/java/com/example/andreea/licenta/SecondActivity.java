package com.example.andreea.licenta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SecondActivity extends AppCompatActivity {

    CheckBox checkWeather;
    ImageButton bUploadImage;
    boolean useWeather;
    Bitmap bmp = null;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private final OkHttpClient client = new OkHttpClient();
    private String message;
    private String message_plant;
    private String message_disease;
    private String message_treatment;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.var4);
        checkWeather = (CheckBox)findViewById(R.id.checkBox);
        bUploadImage = (ImageButton)findViewById(R.id.uploadImage);
        byte[] byteImage_photo = getIntent().getByteArrayExtra("value1");
//                getStringExtra("value1");
        ImageView imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        bmp = BitmapFactory.decodeByteArray(byteImage_photo, 0, byteImage_photo.length, options);
        imageToUpload.setImageBitmap(bmp);
//                setText(savedExtra);
        checkWeather.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                useWeather = ((CheckBox) v).isChecked();

            }
        });

        bUploadImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                uploadFile(bmp);
            }
        });

        bUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bmp == null) {
                    Toast.makeText(getApplicationContext(), "Please upload a photo!", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {

                        public void run() {
//                            uploadFile(mPhotoFile.getAbsolutePath());
                            uploadFile(bmp);
                        }
                    }).start();
                }
            }
        });

    }

    public void uploadFile(Bitmap bitmap_to_send) {

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 4;
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
//        Bitmap bm = BitmapFactory.decodeFile(sourceFileUri, options);
        Bitmap croppedBitmap = getResizedBitmap(bitmap_to_send, 256);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] byteImage_photo = baos.toByteArray();
        //generate base64 string of image
        String encodedImage = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
        String data = sendData(encodedImage);
//        Toast.makeText(MainActivity.this, "Image was sent to the server", Toast.LENGTH_SHORT).show();

        Log.d("data", data);

        JSONArray arr = null;
        try {
            arr = new JSONArray(data);
            JSONObject jObj = arr.getJSONObject(0);
            message_plant = jObj.getString("plant");
            message_disease = jObj.getString("disease");
            message_treatment = jObj.getString("treatment");
//            if (message.equals("")) {
//                message = "Hi 1! there was a problem getting the information, please try again";
//            }
        } catch (JSONException e) {
            e.printStackTrace();
            message = "Hi 2! there was a problem getting the information, please try again";
        }

        Intent myIntent = new Intent(getBaseContext(),  ThirdActivity.class);
        myIntent.putExtra("value_plant", message_plant);
        myIntent.putExtra("value_disease", message_disease);
        myIntent.putExtra("value_treatment", message_treatment);
        myIntent.putExtra("use_weather", useWeather);
        startActivity(myIntent);

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

    }

    public String sendData(String image) {
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("image", image)
                    .build();

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.server_url))
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

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

}
