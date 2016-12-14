package com.example.yunyi.projecthydra;

/**
 * Created by Yun YI on 23/11/2016.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class SettingsActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = firebaseDatabase.getReference();

    private DatabaseReference mWaterRef;
    private DatabaseReference mSunRef;

    //get reference to current user
    private FirebaseUser mUser;


    private FirebaseAuth mAuth;

    private SeekBar seekBar1;
    private TextView textView1;
    private SeekBar seekBar2;
    private TextView textView2;
    private Button buttonSave;
    private EditText editText1;
    private EditText editText2;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();

        Bundle bd = intent.getExtras();

        if (bd != null) {
            id = (String) bd.get("idNum");
        }

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


//        mWaterRef = mDatabase.child("user").child(createUserName(mUser.toString()))
//                .child(id).child("waterNeeds");
//
//        mSunRef = mDatabase.child("user").child(createUserName(mUser.toString()))
//                .child(id).child("sunNeeds");

        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        editText1 = (EditText) findViewById(R.id.editWater);
        editText2 = (EditText) findViewById(R.id.editSun);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        mWaterRef = firebaseDatabase.getReference("user/" + createUserName(mUser.getEmail().toString())
                + "/" + id +  "/waterNeeds");
        mSunRef = firebaseDatabase.getReference("user/" + createUserName(mUser.getEmail().toString())
                + "/" + id +  "/sunNeeds");



        final DatabaseReference mWaterRef2 = firebaseDatabase.getReference("boxes/plant001/info/required_water");
        final DatabaseReference mSunRef2 = firebaseDatabase.getReference("boxes/plant001/info/required_light");

        mWaterRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                editText1.setText(dataSnapshot.getValue().toString());
//                seekBar1.setProgress(Integer.parseInt(dataSnapshot.getValue().toString()) % 10);
//                textView1.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mSunRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                editText2.setText(dataSnapshot.getValue().toString());
//                seekBar2.setProgress(Integer.parseInt(dataSnapshot.getValue().toString()) % 10);
//                textView2.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String waterLevel = editText1.getText().toString();
                String sunLevel = editText2.getText().toString();

                mWaterRef.setValue(waterLevel);
                mSunRef.setValue(sunLevel);

                mWaterRef2.setValue(waterLevel);
                mSunRef2.setValue(sunLevel);

//                Map<String, Object> updateWater = new HashMap<>();
//                updateWater.put("/user/" + id + "/waterNeeds", waterLevel);
//
//                mDatabase.updateChildren(updateWater);
//
//                Map<String, Object> updateSun = new HashMap<>();
//                updateWater.put("/user/" + id + "/sunNeeds", sunLevel);
//
//                mDatabase.updateChildren(updateSun);

//                String waterLevel = textView1.getText().toString();
//                String sunLevel = textView2.getText().toString();
//
//                mWaterRef.setValue(waterLevel);
//                mSunRef.setValue(sunLevel);
//
//                mWaterRef2.setValue(waterLevel);
//                mSunRef2.setValue(sunLevel);


                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);

//                Map<String, Object> postValues = plant.toMap();
//
//                Map<String, Object> childUpdates = new HashMap<>();
//                childUpdates.put("/user/" + key, postValues);
//
//                mDatabase.updateChildren(childUpdates);

            }
        });

        //image.setImageResource(imagenum);

         //Initialize the textview with '0'.
        //textView1.setText("Water value: " + seekBar1.getProgress());
//        seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//
//            int progressValue1 = 0;
//
//            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
//                progressValue1 = value;
//            }
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                //textView1.setText(progressValue1);
//                textView1.setText(progressValue1 * 10);
//
//            }
//        });
//
//        //textView2.setText("Sun value: " + seekBar2.getProgress());
//        seekBar2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//            int progressValue2 = 0;
//
//            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
//                progressValue2 = value;
//            }
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                //textView2.setText(progressValue2);
//                textView2.setText(progressValue2 * 10);
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {

        mAuth.signOut();
        Intent intent = new Intent(SettingsActivity.this, AuthActivity.class);
        startActivity(intent);
    }

    private String createUserName(String s) {
        if (s.contains("@")) {
            return s.split("@")[0];
        } else {
            return s;
        }
    }

}
