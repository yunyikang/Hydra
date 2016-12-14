package com.example.yunyi.projecthydra;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * TO DO LIST
 *
 *
 * Add image views for the plants
 * Obtain a reference to the current user after he signs in
 * Create an intent to the details activity
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    FirebaseDatabase firebaseDatabase;


    DatabaseReference mRef;
    //make a reference to current user node on firebase
    DatabaseReference mUser; //not going to work now
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private int tracker;


    private RecyclerView rv;
    private TextView textAlert;

    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = (Button) findViewById(R.id.buttonAddPlant);

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        textAlert = (TextView) findViewById(R.id.redalert);

        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAut" +
                            "hStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
                // [START_EXCLUDE]
                //updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]

        //user = FirebaseAuth.getInstance().getCurrentUser();

//
//        String s = user.getEmail().toString();
//        String user = createUserName(s);
//
//        mRef = FirebaseDatabase.getInstance().getReference();
//        //make a reference to current user node on firebase
//        mUser = mRef.child("user").child(user);
//        Log.d("user now is", user);

        user = FirebaseAuth.getInstance().getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mAlertRef = firebaseDatabase.getReference("boxes/plant001/info/water_needrefill");

        mAlertRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tracker = Integer.parseInt(dataSnapshot.getValue().toString());
                if (tracker == 1) {
                    textAlert.setVisibility(View.VISIBLE);
                }
                else textAlert.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectionActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        String s = user.getEmail().toString();
        String user = createUserName(s);


        mRef = firebaseDatabase.getReference();
        //make a reference to current user node on firebase
        mUser = mRef.child("user").child(user);

        Log.d("user now is", user);


        FirebaseRecyclerAdapter<Plantt, MyViewHolder> mAdapter =
                new FirebaseRecyclerAdapter<Plantt, MyViewHolder>(
                        Plantt.class,
                        R.layout.main_element_view,
                        MyViewHolder.class,
                        mUser
                ) {
                    @Override
                    protected void populateViewHolder
                            (MyViewHolder viewHolder, Plantt p, int position) {

                        final String namee = p.getName();
                        final String id = p.getId();
                        ArrayList<String> l = new ArrayList<>();
                        l.add("Flower");
                        l.add("Bonsai");
                        //l.add("Small");
                        l.add("Cactus");
                        //l.add("Herbs");
                        l.add("Others");

                        int index = l.indexOf(p.getType());
                        viewHolder.plantPhoto.setImageResource(R.drawable.aflower + index);


                        if (p.name.equals(null)) {
                            viewHolder.plantName.setText(p.getType());
                        }
                        else viewHolder.plantName.setText(p.getName());



                        Log.i(p.getName() + "R.drawable is", String.valueOf(R.drawable.plant1));
                        Log.i(p.getName() + "R.drawable is", String.valueOf(R.drawable.plant2));

                        viewHolder.ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            //make intent to details activity
                            public void onClick(View view) {

                                if (namee.equals("Potty")) {
                                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                                    intent.putExtra("idNum", id);
                                    startActivity(intent);
                                }
                                else {
                                    //Toast.makeText(getApplicationContext(),
                                   //         "Proceed to Details Page",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                };

        rv.setAdapter(mAdapter);
    }

    //class for the elements within recycler class
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView plantName;
        ImageView plantPhoto;
        LinearLayout ll;
        TextView alert;

        public MyViewHolder(View v) {
            super(v);
            plantName = (TextView) v.findViewById(R.id.name);
            plantPhoto = (ImageView) v.findViewById(R.id.photo);
            ll = (LinearLayout) v.findViewById(R.id.ll);
            alert = (TextView) v.findViewById(R.id.alert);
        }
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

    //model for our plant data
    public static class Plantt {

        private String type;
        private String name;
        private String id;
        private String waterNeeds;
        private String sunNeeds;

        public Plantt(String name, String type, String id, String water, String sun) {
            this.type = type;
            this.waterNeeds = water;
            sunNeeds = sun;
            this.name = name;
            this.id = id;
        }
        public Plantt(String n) {

        }
        public Plantt(String name, String id) {
            this.name = name;
            this.id = id;
            this.name = name;
        }

        public Plantt() {

        }

        public String getWaterNeeds() {
            return waterNeeds;
        }

        public void setWaterNeeds(String waterNeeds) {
            this.waterNeeds = waterNeeds;
        }

        public String getSunNeeds() {
            return sunNeeds;
        }

        public void setSunNeeds(String sunNeeds) {
            this.sunNeeds = sunNeeds;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public  String getType() { return type; }

        public void setType(String type) { this.type = type; }

        public String getId() { return id; }

        public void setId(String id) { this.id = id; }
    }


    private String createUserName(String s) {
        if (s.contains("@")) {
            return s.split("@")[0];
        } else {
            return s;
        }
    }

    private void signOut() {

        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(intent);
    }






}

