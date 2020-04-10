package com.example.coronafightauthorities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class typeselect extends AppCompatActivity {

    ArrayList<Map> data = new ArrayList<>();
    LinearLayout linearLayout;
    ScrollView scrollView;
    LinearLayout linLayout;
    ArrayList<String> typeSet = new ArrayList<>();
    Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typeselect);
        linLayout = findViewById(R.id.layout);
        scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.drawable.auth_background);
        scrollView.addView(linearLayout);
        TextView test = new TextView(this);
        test.setText("Details..");
        test.setTextSize(20);
        test.setGravity(Gravity.CENTER);
        linLayout.addView(scrollView);
        linearLayout.addView(test);


        FirebaseDatabase.getInstance().getReference().child(MainActivity.state).child(MainActivity.district).child(MainActivity.city)
                .child(MainActivity.area).child(MainActivity.type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnaptemp : dataSnapshot.getChildren()) {

                    for (DataSnapshot datasnap : datasnaptemp.getChildren()) {

                        map = (Map<String, String>) datasnap.getValue();
                        String temp = map.toString();
                        String[] temps = temp.substring(1, temp.length() - 1).split(",");
                        LinearLayout templayout = new LinearLayout(typeselect.this);
                        templayout.setOrientation(LinearLayout.VERTICAL);
                        templayout.setPadding(10, 10, 10, 10);
                        templayout.setBackgroundResource(android.R.color.black);
                        for (int i = 0; i < temps.length; i++) {
                            String[] phone = temps[i].split("=");
                            TextView test = new TextView(typeselect.this);
                            test.setText(phone[0] + "=" + phone[1]);
                            test.setPadding(10, 5, 10, 5);
                            if (phone[0].equals(" Phone")) {
                                test.setOnClickListener(v -> {
                                    Intent call = new Intent(Intent.ACTION_DIAL);
                                    call.setData(Uri.parse("tel:" + "+919493928707"));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                                            startActivity(call);
                                    }
                                });
                            }
                            test.setTextSize(20);
                            test.setBackgroundResource(R.color.textview);
                            templayout.addView(test);
                        }
                        TextView tv = new TextView(typeselect.this);
                        tv.setText("                    ");
                        linearLayout.addView(templayout);
                        linearLayout.addView(tv);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        if(linearLayout.getChildCount()==1)
        {
            TextView textView=new TextView(this);
            textView.setText("No records yet..");
            textView.setTextSize(30);
            textView.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);
        }

    }
}
