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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


    DatabaseReference mRef;
    //make a reference to current user node on firebase
    DatabaseReference mUser; //not going to work now
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;


    private RecyclerView rv;

    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = (Button) findViewById(R.id.buttonAddPlant);

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

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

        user = FirebaseAuth.getInstance().getCurrentUser();


        String s = user.getEmail().toString();
        String user = createUserName(s);

        mRef = FirebaseDatabase.getInstance().getReference();
        //make a reference to current user node on firebase
        mUser = mRef.child("user").child(user);
        Log.d("user now is", user);

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
                        ArrayList<String> l = new ArrayList<>();
                        l.add("Flowers");
                        l.add("Bonsai");
                        l.add("Small");
                        l.add("Cactus");
                        l.add("Herbs");
                        l.add("Others");

                        int index = l.indexOf(p.getType());
                        viewHolder.plantPhoto.setImageResource(R.drawable.plant1 + index);


                        if (p.name.equals(null)) {
                            viewHolder.plantName.setText(p.getType());
                        }
                        else viewHolder.plantName.setText(p.getName());



                        Log.i(p.getName() + "R.drawable is", String.valueOf(R.drawable.plant1));
                        Log.i(p.getName() + "R.drawable is", String.valueOf(R.drawable.plant2));

                        viewHolder.rl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            //make intent to details activity
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(),
                                        "Hello",Toast.LENGTH_SHORT).show();
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
        RelativeLayout rl;

        public MyViewHolder(View v) {
            super(v);
            plantName = (TextView) v.findViewById(R.id.name);
            plantPhoto = (ImageView) v.findViewById(R.id.photo);
            rl = (RelativeLayout) v.findViewById(R.id.rl);
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
        private int waterNeeds;
        private int sunNeeds;

        public Plantt(String name, String type, String id, int water, int sun) {
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

        public int getWaterNeeds() {
            return waterNeeds;
        }

        public void setWaterNeeds(int waterNeeds) {
            this.waterNeeds = waterNeeds;
        }

        public int getSunNeeds() {
            return sunNeeds;
        }

        public void setSunNeeds(int sunNeeds) {
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

    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



}


