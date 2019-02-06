package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class Main extends AppCompatActivity implements AccountManagementListener {

    FragmentManager fragmentManager;
    APIInterface apiInterface;
    private FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                            Log.d(this.getClass().getName(), "signInWithEmail:success");
                            mCurrentUser = mAuth.getCurrentUser();
                            performLogin();
                        } else {
                            Log.w(this.getClass().getName(), "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    public void swapFragment(Fragment fragment, boolean addToStack){
         FragmentTransaction transaction = fragmentManager.beginTransaction().replace(R.id.fragment_container_main, fragment);
         if(addToStack)
            transaction.addToBackStack(null);
         transaction.commit();
    }

    public void login(String username, String userpass){
        requestLogin(username, userpass);
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
}
