package com.drcir.weighttracker;

import android.support.v4.app.Fragment;

public interface AccountManagementListener {
    public void swapFragment(Fragment fragment, boolean addToStack);
    public void login(String username, String userpass);
    public void back();
}
