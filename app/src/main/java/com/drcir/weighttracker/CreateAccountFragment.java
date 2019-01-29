package com.drcir.weighttracker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountFragment extends Fragment {

    EditText userEmail;
    EditText userPass;
    Button createAccount;
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
                accountManagementListener.swapFragment(new AccountFragment());
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = userEmail.getText().toString();
                final String password = userPass.getText().toString();

                if(Utils.isEmailValid(email)) {
                    final APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
                    if (Utils.checkConnection(getActivity(), getString(R.string.no_connection_message_create_account))) {
                        Call<JsonObject> call = apiInterface.createUser(email, email, password, password);
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    accountManagementListener.login(email, password);
                                } else {
                                    try {
                                        String message;
                                        JSONObject errorMessage = new JSONObject(response.errorBody().string());
                                        if(errorMessage.has("email"))
                                            message = errorMessage.getJSONArray("email").getString(0);
                                        else if(errorMessage.has("password1"))
                                            message = errorMessage.getJSONArray("password1").getString(0);
                                        else
                                            message = getString(R.string.account_created_failed);
                                        createFailed(message);
                                    } catch (Exception e) {
                                        createFailed(getString(R.string.account_created_failed));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                createFailed(getString(R.string.account_created_failed));
                                call.cancel();
                            }
                        });
                    }
                }
                else{
                    createFailed(getString(R.string.invalid_email));
                }
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public void createFailed(String message){
        Toast.makeText(getActivity(),  message, Toast.LENGTH_LONG).show();
    }
}