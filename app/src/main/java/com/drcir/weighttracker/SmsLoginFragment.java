package com.drcir.weighttracker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SmsLoginFragment extends Fragment {

    private static final String TAG = "SmsLoginFragment";

    EditText phoneNumber;
    EditText verificationCode;
    TextView verificationText;
    Button send;
    Button verify;
    Button resend_button;
    ImageView back;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private AccountManagementListener accountManagementListener;

    PhoneAuthProvider.ForceResendingToken mResendToken;
    String mVerificationId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            accountManagementListener = (AccountManagementListener) context;
        } catch (ClassCastException castException) {
        }
    }

    @Override
    public void onStart () {
        if(accountManagementListener.getVerificationFlag())
            send.performClick();
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations Instant verification and Auto-retrieval
                Log.d(TAG, "onVerificationCompleted:" + credential);
                Toast.makeText(getActivity(), getString(R.string.auto_verify), Toast.LENGTH_SHORT).show();
                accountManagementListener.setVerificationFlag(false);
                accountManagementListener.sms_login(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(getActivity(), "Invalid Phone Number Entered.", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(getActivity(), "Too many sms logins today.", Toast.LENGTH_SHORT).show();
                }
                flipVerificationVisibile(false);
                accountManagementListener.setVerificationFlag(false);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
                flipVerificationVisibile(true);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms_login, container, false);

        send = view.findViewById(R.id.send_button);
        verify = view.findViewById(R.id.verify_button);
        resend_button = view.findViewById(R.id.resend_button);
        phoneNumber = view.findViewById(R.id.phone_number);
        verificationCode = view.findViewById(R.id.verification_code);
        verificationText = view.findViewById(R.id.verification_text);
        back = view.findViewById(R.id.login_back);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        phoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(phoneNumber.getText().toString().trim().length()!= 0)
                        send.performClick();
                }
                return false;
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    send.setEnabled(false);
                } else {
                    send.setEnabled(true);
                }

                if(verify.getVisibility() == View.VISIBLE){
                    flipVerificationVisibile(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide keyboard at email_login
                Activity activity = getActivity();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = activity.getCurrentFocus();
                if (view == null) {
                    view = new View(activity);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                String number = phoneNumber.getText().toString();
                number = formatPhoneNumber(number);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        number,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        getActivity(),               // Activity (for callback binding)
                        mCallbacks);        // OnVerificationStateChangedCallbacks
                accountManagementListener.setVerificationFlag(true);
            }
        });

        verificationCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    send.performClick();
                    return true;
                }
                return false;
            }
        });

        verificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    verify.setEnabled(false);
                } else {
                    verify.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountManagementListener.sms_login(PhoneAuthProvider.getCredential(mVerificationId, verificationCode.getText().toString()));
            }
        });


        resend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneNumber.getText().toString();
                number = formatPhoneNumber(number);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        number,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        getActivity(),               // Activity (for callback binding),
                        mCallbacks);        // OnVerificationStateChangedCallbacks
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountManagementListener.back();
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    private String formatPhoneNumber(String number){
        number = number.replaceAll("[\\-\\s()]", "");
        number = number.trim();
        number = "+1" + number;
        return number;
    }

    public void flipVerificationVisibile(boolean visible){
        if(visible) {
            send.setVisibility(View.GONE);
            verificationCode.setVisibility(View.VISIBLE);
            verify.setVisibility(View.VISIBLE);
            resend_button.setVisibility(View.VISIBLE);
            verificationText.setVisibility(View.VISIBLE);
        }
        else{
            send.setVisibility(View.VISIBLE);
            verificationCode.setVisibility(View.GONE);
            verificationCode.setText(null);
            verify.setVisibility(View.GONE);
            resend_button.setVisibility(View.GONE);
            verificationText.setVisibility(View.GONE);
        }
    }

}