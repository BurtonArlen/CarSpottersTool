//package com.example.guest.carspotterstool.adapters;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//
//import com.example.guest.carspotterstool.Constants;
//import com.example.guest.carspotterstool.models.PhotoContribution;
//import com.example.guest.carspotterstool.models.User;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//import butterknife.ButterKnife;
//
///**
// * Created by Guest on 6/19/17.
// */
//
//public class FirebaseUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//    private static final int MAX_WIDTH = 200;
//    private static final int MAX_HEIGHT = 200;
//    View mView;
//    Context mContext;
//
//    public FirebaseUserViewHolder(View itemView){
//        super(itemView);
//        ButterKnife.bind(this, itemView);
//        mView = itemView;
//        mContext = itemView.getContext();
//        itemView.setOnClickListener(this);
//    }
//    public void bindUser(User user){
////        set user info here
//        Log.d(user.getName() ,"This Exists");
//    }
//    public void onClick(View v){
//        final ArrayList<PhotoContribution> userContributions = new ArrayList<>();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = user.getUid();
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS).child(Constants.FIREBASE_CHILD_USERS).child(uid);
//        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    userContributions.add(snapshot.getValue(PhotoContribution.class));
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                databaseError.getMessage();
//            }
//        });
//    }
//}
