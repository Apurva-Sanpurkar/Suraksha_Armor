package com.example.suraksha;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseSovereignty {
    public static void registerFile(String fileId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("files");
        // Set initial state to active
        ref.child(fileId).child("status").setValue("active");
    }
}