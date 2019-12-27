package com.production.wunner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Submit extends AppCompatActivity {

    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        Intent intent=new Intent();
        String station_id=intent.getStringExtra("station_id");
        reference=database.getReference(station_id);
        reference.setValue("Updated");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String valume =dataSnapshot.getValue(String.class);
                if (valume=="Update")
                {
                    SubmitScore();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(),"Failed to read value.",Toast.LENGTH_SHORT);
            }
        });
        Button buttonPost=findViewById(R.id.button_Post);
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sau khi xử lý xong.
                reference.setValue("Rated");
            }
        });
    }

    private void SubmitScore() {
        Toast.makeText(this.getBaseContext(),"Updated",Toast.LENGTH_SHORT);
        //get về xong push lên
    }
}
