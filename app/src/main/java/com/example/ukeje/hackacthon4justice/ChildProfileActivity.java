package com.example.ukeje.hackacthon4justice;

import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.HttpResponse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.net.wifi.WifiConfiguration.Status.strings;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class ChildProfileActivity extends AppCompatActivity {

    //UI Data
    private TextView nameView;
    private TextView genderView;
    private TextView addressView;
    private TextView parentView;
    private TextView dobView;
    private TextView parentNoView;

    private static final  String BASE_URL = "http://192.168.91.107:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile);


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        nameView = findViewById(R.id.name_text);
        genderView = findViewById(R.id.gender_text);
        addressView = findViewById(R.id.address_text);
        parentView = findViewById(R.id.parent_name);
        dobView = findViewById(R.id.dob);
        parentNoView = findViewById(R.id.parent_num);

        APIService service = RetrofitClient.getClient(BASE_URL).create(APIService.class);

        Call<ChildUser> call = service.getChildData();

        Call<ChildUser> callPar = service.getParentId();

        callPar.enqueue(new Callback<ChildUser>() {
            @Override
            public void onResponse(Call<ChildUser> call, Response<ChildUser> response) {

                parentNoView.setText(response.body().getContact());
            }

            @Override
            public void onFailure(Call<ChildUser> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();
            }
        });

        call.enqueue(new Callback<ChildUser>() {
            @Override
            public void onResponse(Call<ChildUser> call, Response<ChildUser> response) {

                nameView.setText(response.body().getName());
                genderView.setText(response.body().getGender());
                addressView.setText(response.body().getAddress());
                parentView.setText(response.body().getParent());
                dobView.setText(response.body().getDob());

            }

            @Override
            public void onFailure(Call<ChildUser> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();

            }
        });



//        Log.i("Check Response:", "REQUSET CHECK");
//        URL url;
//        HttpURLConnection urlConnection = null;
//
//        try{
//            Log.i("Check Response:", "REQUSET CHECK2");
//
//            url = new URL("http://192.168.91.107:8080/hackathon//login.php?fn=\"elijah\"&ln=\"okokon\"");
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//
//            int responseCode = urlConnection.getResponseCode();
//
//            InputStream inputStream = urlConnection.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String line = bufferedReader.readLine();
//            nameView.setText(line);
//
//            Log.i("CHECK RESPONSE: ",line );
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }


    }
}
