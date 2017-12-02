package com.navjot.deepak.manpreet.pdfsociety.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.SoundPool;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navjot.deepak.manpreet.pdfsociety.Activities.HomeActivity;
import com.navjot.deepak.manpreet.pdfsociety.Models.Feedback;
import com.navjot.deepak.manpreet.pdfsociety.Models.User;
import com.navjot.deepak.manpreet.pdfsociety.R;

import java.util.ArrayList;

public class FeedbackNotifyService extends Service {

    int i=0;
    Feedback feedback;
    FirebaseUser fuser;
    ArrayList DataList;
    ArrayList SeenFBList = new ArrayList();
    ArrayList NotifyList = new ArrayList();

    @Override
    public void onCreate() {
        super.onCreate();
        HomeActivity.i++;
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(
            !(
                fuser.getEmail().toString().equals("navjotsingh9633@gmail.com") ||
                fuser.getEmail().toString().equals("mannis720@gmail.com") ||
                fuser.getEmail().toString().equals("dpruthi16@gmail.com")
            )
        )
        {
            Log.d("Pdfsociety", "stopSelf");
            stopForeground(true);
            stopSelf();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i("Pdfsociety", "Service Started");
            listenForFeedback();
            return START_STICKY;
    }

    private void listenForFeedback(){
        FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.DB_Feedbacks))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Pdfsociety", "Feedback onDataChange");
                        DataList = new ArrayList();
                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                            feedback =  postSnapShot.getValue(Feedback.class);
                            DataList.add(feedback);
                        }

                        getSeenFeedbackList();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("Pdfsociety", "Feedback onCancelled "+databaseError.getMessage());
                    }
                });
    }

    private void getSeenFeedbackList(){
        FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.DB_Seen_Feedbacks))
                .child(fuser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                            Feedback seenFeedBack =  postSnapShot.getValue(Feedback.class);
                            SeenFBList.add(seenFeedBack);
                        }
                        generateUpdatedList();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void generateUpdatedList(){
        NotifyList.addAll(DataList);
        NotifyList.removeAll(SeenFBList);

        for (Object obj: NotifyList){
            Feedback fb = (Feedback)obj;
            Log.d("Pdfsociety", "notifylist: "+fb);
        }

        for (Object list:NotifyList) {
            Feedback feedback = (Feedback) list;
            showNotification(feedback);
        }
        DataList.clear();
        SeenFBList.clear();
        NotifyList.clear();
    }

    public String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void showNotification(Feedback feedback) {
        Notification.Builder builder = new Notification.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.a))
                .setSmallIcon(R.drawable.a)
                .setContentTitle(usernameFromEmail(feedback.getEmail()))
                .setContentText(feedback.getRating()+"\n"+feedback.getComment());
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(i,builder.build());
        i++;
        addToSeenFeedbacks(feedback);
    }

    private void addToSeenFeedbacks(Feedback feedback){
        FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.DB_Seen_Feedbacks))
                .child(fuser.getUid())
                .child(feedback.getUid())
                .setValue(feedback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Pdfsociety", "Feedback Service Destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
