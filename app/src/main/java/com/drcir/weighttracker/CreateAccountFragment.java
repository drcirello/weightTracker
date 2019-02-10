package com.drcir.weighttracker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountFragment extends Fragment {

    EditText userEmail;
    EditText userPass;
    Button createAccount;
    LinearLayout phoneCreateSwap;
    ImageView back;
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
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        userEmail = view.findViewById(R.id.create_account_useremail);
        userPass = view.findViewById(R.id.create_account_userpass);
        createAccount = view.findViewById(R.id.create_account_button);
        phoneCreateSwap = view.findViewById(R.id.phone_create_swap);
        back = view.findViewById(R.id.create_account_back);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    userPass.requestFocus();
                }
                return false;
            }
        });

        userEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0 || userPass.getText().toString().trim().length()== 0){
                    createAccount.setEnabled(false);
                } else {
                    createAccount.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        userPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(userPass.getText().toString().trim().length()!= 0 && userEmail.getText().toString().trim().length()!=0)
                        createAccount.performClick();
                }
                return false;
            }
        });

        userPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0 || userEmail.getText().toString().trim().length()== 0){
                    createAccount.setEnabled(false);
                } else {
                    createAccount.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountManagementListener.back();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final String email = userEmail.getText().toString().trim();
            final String password = userPass.getText().toString().trim();
            final FirebaseAuth mAuth = accountManagementListener.getFirebaseAuth();
            if(Utils.checkConnection(getActivity(), getString(R.string.no_connection_message_create_account)) && Utils.isEmailValid(getActivity(), email)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("CreateAccount", "createUserWithEmail:success");
                            accountManagementListener.setFirebaseUser(mAuth.getCurrentUser());
                            createOnServer(email, password);
                        } else {
                            Log.w("CreateAccount", "createUserWithEmail:failure", task.getException());
                            createFailed(getString(R.string.account_created_failed));
                        }
                        }
                    });
            }
            }
        });

        phoneCreateSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountManagementListener.swapFragment(new SmsLoginFragment(), true);
            }
        });

    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public void createOnServer(final String email, final String password){
        FirebaseUser user = accountManagementListener.getFirebaseAuth().getCurrentUser();
        user.getIdToken(true)
            .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult().getToken();
                    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                    Call<Void> call = apiInterface.createUserFirebase(token);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                accountManagementListener.email_login(email, password);
                                Toast.makeText(getActivity(), "ACCOUNT CREATED", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            call.cancel();
                            removeFromFirebase();
                            createFailed(getString(R.string.account_created_failed));
                        }
                    });
                }
                else{
                    removeFromFirebase();
                    createFailed(getString(R.string.account_created_failed));
                }
                }
            });
    }

    //If server create fails remove from firebase so email is not tagged used.
    public void removeFromFirebase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete()
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("CreateAccount", "User account deleted due to server failure.");
                }
                }
            });
    }

    public void createFailed(String message){
        Toast.makeText(getActivity(),  message, Toast.LENGTH_LONG).show();
    }
}