package com.drcir.weighttracker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordFragment extends Fragment {

    APIInterface apiInterface;
    EditText useremail;
    ImageView back;
    Button sendPassword;
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
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        useremail = view.findViewById(R.id.reset_password_useremail);
        back = view.findViewById(R.id.reset_password_back);
        sendPassword = view.findViewById(R.id.send_password);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountManagementListener.back();
            }
        });

        sendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = useremail.getText().toString();
/*
                if(Utils.checkConnection(getActivity(), getString(R.string.no_connection_message_reset))) {
                    Call<JsonObject> call = apiInterface.resetPassword(email);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                accountManagementListener.swapFragment(new EmailLoginFragment(), true);
                                Toast.makeText(getActivity(), "Password Reset Sent", Toast.LENGTH_SHORT).show();
                            } else {
                                resetFailed();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            resetFailed();
                            call.cancel();
                        }
                    });
                }*/
            }
        });

    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public void resetFailed(){
        Toast.makeText(getActivity(), "Password Reset Failed", Toast.LENGTH_LONG).show();
    }
}