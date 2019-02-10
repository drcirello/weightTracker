package com.drcir.weighttracker;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AccountFragment extends Fragment {

    Button email_login;
    Button sms_login;
    Button createAccount;
    private AccountManagementListener accountManagementListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            accountManagementListener = (AccountManagementListener) context;
        } catch (ClassCastException castException) {
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        createAccount = view.findViewById(R.id.createAccount);
        email_login = view.findViewById(R.id.email_login);
        sms_login = view.findViewById(R.id.sms_login);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountManagementListener.swapFragment(new EmailLoginFragment(), true);
            }
        });

        sms_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountManagementListener.swapFragment(new SmsLoginFragment(), true);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountManagementListener.swapFragment(new CreateAccountFragment(), true);
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
    }
}