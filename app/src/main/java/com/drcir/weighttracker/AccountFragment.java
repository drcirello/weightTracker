package com.drcir.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AccountFragment extends Fragment {

    String TOS_URL = "https://www.weighttrackerapp.net/terms/";
    String PRIVACY_URL = "https://www.weighttrackerapp.net/privacy/";

    Button email_login;
    Button sms_login;
    Button createAccount;
    TextView tos;
    TextView privacy;
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
        tos = view.findViewById(R.id.tos);
        privacy = view.findViewById(R.id.privacy);
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

        tos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(TOS_URL));
                startActivity(intent);
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(PRIVACY_URL));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
    }
}