package com.creative.share.apps.aqaar.activities_fragments.activity_sign_in.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.aqaar.R;
import com.creative.share.apps.aqaar.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.aqaar.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.aqaar.databinding.DialogAlertBinding;
import com.creative.share.apps.aqaar.databinding.FragmentCodeVerificationBinding;
import com.creative.share.apps.aqaar.models.UserModel;
import com.creative.share.apps.aqaar.preferences.Preferences;
import com.creative.share.apps.aqaar.remote.Api;
import com.creative.share.apps.aqaar.share.Common;
import com.creative.share.apps.aqaar.tags.Tags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Code_Verification extends Fragment {
    private static final String TAG = "DATA";

    private SignInActivity activity;
    private FragmentCodeVerificationBinding binding;
    private boolean canResend = true;
    private UserModel userModel;
    //    private int type;
    private CountDownTimer countDownTimer;
    private Preferences preferences;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String id;
    private String code;
    private FirebaseAuth mAuth;
    private ProgressDialog dialo;
    private String phone_code;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_code_verification, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    public static Fragment_Code_Verification newInstance(UserModel userModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, userModel);
        Fragment_Code_Verification fragment_code_verification = new Fragment_Code_Verification();
        fragment_code_verification.setArguments(bundle);
        return fragment_code_verification;
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            userModel = (UserModel) bundle.getSerializable(TAG);
            // type=bundle.getInt(TAG2);
        }

        activity = (SignInActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        binding.btnConfirm.setOnClickListener(v -> {
            preferences.create_update_userData(activity, userModel);
            preferences.createSession(activity, Tags.session_login);
            Intent intent = new Intent(activity, HomeActivity.class);
            startActivity(intent);
            activity.finish();
           // checkData();

        });

        binding.btnResend.setOnClickListener(v -> {

            Log.e("ddd", "ddd");
            if (canResend) {

                sendverficationcode(userModel.getUser_phone(), phone_code);
                startCounter();

            }
        });


        startCounter();

        authn();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userModel.getUser_phone_code().startsWith("00")) {
                    phone_code = userModel.getUser_phone_code().replaceFirst("00", "+");
                } else {
                    phone_code = userModel.getUser_phone_code();
                }
                sendverficationcode(userModel.getUser_phone(), phone_code);
            }
        }, 3);
        startCounter();

    }

    private void authn() {

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.e("id", s);
                id = s;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                Log.e("code",phoneAuthCredential.getSmsCode());
//phoneAuthCredential.getProvider();
                if (phoneAuthCredential.getSmsCode() != null) {
                    code = phoneAuthCredential.getSmsCode();
                    binding.edtCode.setText(code);
                  //  verfiycode(code);
                }


            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("llll", e.getMessage());
            }
        };

    }

    private void verfiycode(String code) {
        // Toast.makeText(register_activity,code,Toast.LENGTH_LONG).show();
        dialo = Common.createProgressDialog(activity, getString(R.string.wait));
        dialo.setCancelable(false);
        dialo.show();
        if (id != null) {

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, code);
            siginwithcredental(credential);
        }
    }

    private void siginwithcredental(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    dialo.dismiss();
                    preferences.create_update_userData(activity, userModel);
                    preferences.createSession(activity, Tags.session_login);
                    Intent intent = new Intent(activity, HomeActivity.class);
                    startActivity(intent);
                    activity.finish();


                } else {
                    Common.CreateDialogAlert(activity, activity.getResources().getString(R.string.failed));
                    try {
                        Log.e("Erorr", task.getResult().toString());

                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    private void sendverficationcode(String phone, String phone_code) {
        Log.e("kkk", phone_code + phone);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_code + phone, 59, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallbacks);

    }

    private void checkData() {
        String code = binding.edtCode.getText().toString().trim();
        if (!TextUtils.isEmpty(code)) {
            Common.CloseKeyBoard(activity, binding.edtCode);
            verfiycode(code);

        } else {
            binding.edtCode.setError(getString(R.string.field_req));
        }
    }


    private void startCounter() {
        countDownTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                canResend = false;

                int AllSeconds = (int) (millisUntilFinished / 1000);
                int seconds = AllSeconds % 60;


                binding.btnResend.setText("00:" + seconds);
            }

            @Override
            public void onFinish() {
                canResend = true;
                binding.btnResend.setText(activity.getResources().getString(R.string.resend));
            }
        }.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
