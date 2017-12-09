package com.navjot.deepak.manpreet.pdfsociety.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navjot.deepak.manpreet.pdfsociety.Models.Feedback;
import com.navjot.deepak.manpreet.pdfsociety.R;
import java.util.HashMap;
import java.util.Map;

public class FeedbackNotifyService extends Service {

    int notificationCount=0;
    public static int serviceStartCount =0;
    int listenerCount=0;
    FirebaseUser fuser;
    ValueEventListener FeedbackListener;
    DatabaseReference FeedbackReference;

    @Override
    public void onCreate() {
        super.onCreate();

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

    private void init(){
        FeedbackReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.DB_Feedbacks));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i("Pdfsociety", "Service Started");

            init();

            listenForFeedback();
            return START_STICKY;
    }

    private void listenForFeedback(){
        FeedbackListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Pdfsociety", "Feedback onDataChange");
                Feedback feedback;

                if (listenerCount == 0){
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        feedback = postSnapShot.getValue(Feedback.class);

                        getSeenByAdminsMap(feedback);
                    }
                    listenerCount = 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Pdfsociety", "Feedback onCancelled "+databaseError.getMessage());
            }
        };

        FeedbackReference.addValueEventListener(FeedbackListener);
    }

    void getSeenByAdminsMap(final Feedback fb){
        Log.d("Pdfsociety", "getSeenByAdminsMap");
        FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.DB_Feedbacks))
                .child(fb.getUid())
                .child(getString(R.string.DB_SeenByAdmins))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String,Object> map = (Map<String,Object>) dataSnapshot.getValue();
                        checkSeen(fb, map);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void checkSeen(Feedback fb, Map<String, Object> adminMap){
        Log.d("Pdfsociety", "checkSeen");

        listenerCount = 0;

        if (adminMap != null){
            Boolean value = (Boolean) adminMap.get(fuser.getUid());
            if (!(value)){
                showNotification(fb);
            }
        }

    }

    public String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void showNotification(Feedback feedback) {
        Log.d("Pdfsociety", "showNotification");

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder builder = new Notification.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.a))
                .setSmallIcon(R.drawable.a)
                .setSound(uri)
                .setContentTitle(usernameFromEmail(feedback.getEmail()))
                .setContentText("Rating "+feedback.getRating())
                .setStyle(
                        new Notification.BigTextStyle()
                        .bigText(feedback.getComment())
                        .setBigContentTitle(usernameFromEmail(feedback.getEmail()))
                        .setSummaryText("Rating "+feedback.getRating())
                );

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationCount,builder.build());
        notificationCount++;
        putSeenTrue(feedback);
    }

    private void putSeenTrue(Feedback feedback){
        Log.d("Pdfsociety","putSeenTrue method");

//        FirebaseDatabase.getInstance().getReference()
//                .child(getString(R.string.DB_Feedbacks))
//                .child(feedback.getUid())
//                .child(getString(R.string.DB_SeenByAdmins))
//                .child(fuser.getUid())
//                .setValue(true);
        Map<String,Object> map = new HashMap<>();
        map.put(fuser.getUid(), true);

        FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.DB_Feedbacks))
                .child(feedback.getUid())
                .child(getString(R.string.DB_SeenByAdmins))
                .updateChildren(map);

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
