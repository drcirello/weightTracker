package com.drcir.weighttracker;

import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;

public interface AccountManagementListener {
    public void swapFragment(Fragment fragment, boolean addToStack);
    public void email_login(String username, String userpass);
    public void sms_login(PhoneAuthCredential credential);
    public void back();
    public FirebaseAuth getFirebaseAuth();
    public void setFirebaseUser(FirebaseUser user);
    public boolean getVerificationFlag();
    public void setVerificationFlag(boolean verifying);

}
