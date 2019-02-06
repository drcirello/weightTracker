package com.drcir.weighttracker;

import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public interface AccountManagementListener {
    public void swapFragment(Fragment fragment, boolean addToStack);
    public void login(String username, String userpass);
    public void back();
    public FirebaseAuth getFirebaseAuth();
    public void setFirebaseUser(FirebaseUser user);

}
