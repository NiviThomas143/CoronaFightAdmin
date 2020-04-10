package com.example.coronafightauthorities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static String state;
    static String district;
    static String city;
    static String area;
    static String type;
    static ArrayList<String> areas;
    static ArrayList<String> users;
    static ArrayList<String> types;
    ArrayAdapter adapter;
    SharedPreferences shared;
    ListView listView;
    PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        areas = new ArrayList<>();
        users = new ArrayList<>();
        types = new ArrayList<>();
        listView = findViewById(R.id.areas_listView);
        shared = getSharedPreferences("login", MODE_PRIVATE);
        state = shared.getString("state", "");
        district = shared.getString("district", "");
        city = shared.getString("city", "");

        FirebaseDatabase.getInstance().getReference().child(state).child(district).child(city).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                areas.add(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    types.add(snapshot.getKey());
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        users.add(snap.getKey());
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, areas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                area = parent.getItemAtPosition(position).toString();
                popupMenu = new PopupMenu(MainActivity.this, listView);
                popupMenu.getMenuInflater().inflate(R.menu.types, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        type = item.toString();
                        startActivity(new Intent(MainActivity.this, typeselect.class));
                        return true;
                    }
                });

            }
        });

    }
}
