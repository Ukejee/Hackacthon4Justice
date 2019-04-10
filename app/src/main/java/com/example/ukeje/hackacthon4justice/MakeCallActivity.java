package com.example.ukeje.hackacthon4justice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakeCallActivity extends AppCompatActivity {

    private static final  String BASE_URL = "http://192.168.91.107:8080/";
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_call);

        btn = findViewById(R.id.btn_1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCLickBtn();
            }
        });


    }

    public void onCLickBtn(){

        APIService service = RetrofitClient.getClient(BASE_URL).create(APIService.class);

       // Call<ChildUser> call = service.getChildData();

        Call<ChildUser> callPar = service.getEmergencyContact();

        callPar.enqueue(new Callback<ChildUser>() {
            @Override
            public void onResponse(Call<ChildUser> call, Response<ChildUser> response) {

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(response.body().getContact1()));

               // intent.putExtra("Emergence number", response.body().getContact1());
                if (ActivityCompat.checkSelfPermission(MakeCallActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ChildUser> call, Throwable t) {

                Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();
            }
        });

    }
}
