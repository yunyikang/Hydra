package com.example.yunyi.projecthydra;

/**
 * Created by Yun YI on 23/11/2016.
 */


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectionActivity extends AppCompatActivity {

    //get reference to database
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    //get reference to current user
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = firebaseAuth.getCurrentUser();


    ListView simpleList;
    ArrayList<Item> animalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        simpleList = (ListView) findViewById(R.id.simpleListView);

        animalList.add(new Item("Flowers", R.drawable.plant1));
        animalList.add(new Item("Bonsai", R.drawable.plant2));
        animalList.add(new Item("Small", R.drawable.plant3));
        animalList.add(new Item("Cactus", R.drawable.plant5));
        animalList.add(new Item("Herbs", R.drawable.plant6));
        animalList.add(new Item("Others", R.drawable.plant4));

        MyAdapter myAdapter = new MyAdapter(this, R.layout.selection_element_view, animalList);
        simpleList.setAdapter(myAdapter);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectionActivity.this);
                alertDialog.setTitle("Give it a name?");
                //alertDialog.setMessage("Enter Pass");

                final EditText input = new EditText(SelectionActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                String email = mUser.getEmail().toString();
                final String user = createUserName(email);
                final String type = animalList.get(i).animalName;

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //writeNewPost(user, animalList.get(i).animalName);
                                //Toast.makeText(SelectionActivity.this,"hello",Toast.LENGTH_SHORT).show();
                                final String name = input.getText().toString();

                                if (!mDatabase.child("user").child(user).equals(null)) {
                                    createPost(user, type, name);
                                } else {
                                    writeNewPost(user, type, name);
                                }
                                Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
                                //intent.putExtra("IMAGE_I_NEED", animalList.get(i).animalImage);
                                startActivity(intent);
                            }



                        });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //writeNewPost(user, animalList.get(i).animalName);
                                //Toast.makeText(SelectionActivity.this,"hello",Toast.LENGTH_SHORT).show();
                                final String name = input.getText().toString();

                                if (!mDatabase.child("user").child(user).equals(null)) {
                                    createPost(user, type, name);
                                } else {
                                    writeNewPost(user, type, name);
                                }
                                Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
                                //intent.putExtra("IMAGE_I_NEED", animalList.get(i).animalImage);
                                startActivity(intent);
                            }
                        });

                alertDialog.show();
            }

        });
    }



    private void createPost(String currentUser, String type, String name) {
        String s = mDatabase.child("user").child(currentUser).push().getKey();
        Plant p = new Plant(type, s);
        if (!name.equals(null)) p.setName(name);
        mDatabase.child("user").child(currentUser).child(s).setValue(p);
    }

    private void writeNewPost(String currentUser, String type, String name) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("user").child(currentUser).push().getKey();
        Plant plant = new Plant(type, key);
        Log.i(name, name);
        if (!name.equals(null)) plant.setName(name);
        Map<String, Object> postValues = plant.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/user/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    private String createUserName(String s) {
        if (s.contains("@")) {
            return s.split("@")[0];
        } else {
            return s;
        }
    }
}



