package com.example.yunyi.projecthydra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * TO DO LIST
 *
 *
 * Add image views for the plants
 * Obtain a reference to the current user after he signs in
 * Create an intent to the details activity
 */
public class MainActivity extends AppCompatActivity {

    TextView myPlant;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    //make a reference to current user node on firebase
    DatabaseReference mUser = mRootRef.child("Users").child("user0"); //not going to work now

    RecyclerView rv;
    CardView cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Plant, MyViewHolder> mAdapter =
                new FirebaseRecyclerAdapter<Plant, MyViewHolder>(
                        Plant.class,
                        R.layout.element_view,
                        MyViewHolder.class,
                        mUser
                ) {
                    @Override
                    protected void populateViewHolder
                            (MyViewHolder viewHolder, Plant p, int position) {
                        viewHolder.plantName.setText(p.getName());
                        viewHolder.plantPhoto.setText(p.getPhoto());
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
        TextView plantName, plantPhoto;
        RelativeLayout rl;

        public MyViewHolder(View v) {
            super(v);
            plantName = (TextView) v.findViewById(R.id.pN);
            plantPhoto = (TextView) v.findViewById(R.id.phN);
            rl = (RelativeLayout) v.findViewById(R.id.rl);
        }
    }

    //model for our plant data
    public static class Plant{

        String name;
        String photo;

        public Plant(){
        }

        public Plant(String in, String ph) {
            name = in;
            photo = ph;
        }

        public String getName(){
            return name;
        }
        public String getPhoto(){
            return photo;
        }

    }


    public void changeDetails(View v) {
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
    }

}


