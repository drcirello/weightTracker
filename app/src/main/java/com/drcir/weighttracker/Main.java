package com.drcir.weighttracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends AppCompatActivity implements AccountManagementListener {

    private static final String TAG = Main.class.getName();

    FragmentManager fragmentManager;
    APIInterface apiInterface;
    private FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    boolean verificationInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verificationInProgress = false;
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        fragmentManager = getSupportFragmentManager();
        apiInterface = APIClient.getClient().create(APIInterface.class);

        if(mCurrentUser != null && Utils.checkConnection(this, getString(R.string.no_connection_message)))
            validateToken();
        else
            loginRequired();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putBoolean("verifying", verificationInProgress);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onResume() {
        mAuth = FirebaseAuth.getInstance();
        super.onResume();
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        verificationInProgress = bundle.getBoolean("verifying");
        super.onRestoreInstanceState(bundle);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    public void loginRequired(){
        setContentView(R.layout.activity_main);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_main, new AccountFragment());
        fragmentTransaction.commit();
    }

    public void validateToken(){
        mCurrentUser.getIdToken(true)
            .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        performLogin();
                    } else {
                        loginRequired();
                    }
                }
            });
    }

    public void performLogin(){
        Intent i = new Intent(Main.this, Base_Activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Main.this.startActivity(i);
        finish();
    }

    public void requestLogin(String useremail, String userpass){
        if(Utils.checkConnection(this, getString(R.string.no_connection_message))){
            mAuth.signInWithEmailAndPassword(useremail, userpass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            mCurrentUser = mAuth.getCurrentUser();
                            performLogin();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    public void requestSMSLogin(final PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            mCurrentUser = mAuth.getCurrentUser();
                            createOnServer(credential);
                            performLogin();
                        } else {
                            // Sign in failed
                            Crashlytics.logException(new Exception("Instant Verification failure: " + task.getException().getMessage()));
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Invalid Verification Code", Toast.LENGTH_SHORT).show();
                            mAuth = FirebaseAuth.getInstance();
                        }
                    }
                });
    }

    public void swapFragment(Fragment fragment, boolean addToStack){
         FragmentTransaction transaction = fragmentManager.beginTransaction().replace(R.id.fragment_container_main, fragment);
         if(addToStack)
            transaction.addToBackStack(null);
         transaction.commit();
    }

    public void createOnServer(final PhoneAuthCredential credential){
        mCurrentUser.getIdToken(true)
            .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        String token = task.getResult().getToken();
                        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                        Call<Void> call = apiInterface.createUserFirebase(token);
                        //Login regardless of create status for consistency with email
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {}
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                call.cancel();
                            }
                        });
                    }
                }
            });
    }

    public void email_login(String username, String userpass){
        requestLogin(username, userpass);
    }

    public void sms_login(PhoneAuthCredential credential){
        requestSMSLogin(credential);
    }

    public void back(){onBackPressed();}

    @Override
    public FirebaseAuth getFirebaseAuth(){
        return mAuth;
    }

    @Override
    public void setFirebaseUser(FirebaseUser user){
        mCurrentUser = user;
    }

    @Override
    public void setVerificationFlag(boolean verifying){
        verificationInProgress = verifying;
    }

    @Override
    public boolean getVerificationFlag(){ return verificationInProgress; }
}
