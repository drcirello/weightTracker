package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    //converts to Month DD, YYYY format
    public static String formatDate(long unixDate){
        Date date = new java.util.Date(unixDate);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        return sdf.format(date);
    }

    public static String formatSelectedDate(long unixDate){
        Date date = new java.util.Date(unixDate);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEE, MMMM dd yyyy", Locale.US);
        return sdf.format(date);
    }

    public static DecimalFormat getDecimalFormat(){
        return new DecimalFormat("0");
    }

    public static boolean checkConnection(Context context, String failedMessage) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            connected = true;
        if(!connected) {
            Toast.makeText(context, failedMessage, Toast.LENGTH_LONG).show();
            Log.i("CONNECTION CHECK: ", "No connection");
        }
        return connected;
    }


    public static void logout(final Context context, final Intent intent){
        FirebaseAuth.getInstance().signOut();
        context.startActivity(intent);
    }

    public static boolean isEmailValid(Context context, CharSequence email) {
        boolean valid =  android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if(!valid) {
            Toast.makeText(context, context.getString(R.string.invalid_email), Toast.LENGTH_LONG).show();
            Log.i("CONNECTION CHECK: ", "No connection");
        }
        return valid;
    }

}
