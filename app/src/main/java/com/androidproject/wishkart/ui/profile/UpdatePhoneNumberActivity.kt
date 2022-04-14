package com.androidproject.wishkart.ui.profile

import android.app.ProgressDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.androidproject.wishkart.R
import com.androidproject.wishkart.auth.PHONE_NUMBER
import com.androidproject.wishkart.databinding.ActivityUpdatePhoneNumberBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

const val PHONE_NUMBER = "phoneNumber"

class UpdatePhoneNumberActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var updatePhoneNumberBinding: ActivityUpdatePhoneNumberBinding
    private lateinit var auth: FirebaseAuth
    private var phoneNumber: String? = null
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var verificationId: String? = null
    private var reSendToken: PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var progressDialog: ProgressDialog
    private var counterDown: CountDownTimer? = null
    private var timeLeft: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updatePhoneNumberBinding = ActivityUpdatePhoneNumberBinding.inflate(layoutInflater)
        setContentView(updatePhoneNumberBinding.root)
        setSupportActionBar(updatePhoneNumberBinding.toolbarUpdatePhoneNumber)
        phoneNumber = intent.getStringExtra(PHONE_NUMBER).toString()
        auth = FirebaseAuth.getInstance()

        initView()
        startVerify()

        updatePhoneNumberBinding.verification.setOnClickListener {
            verification()
            updatePhoneNumber()
        }

        updatePhoneNumberBinding.reSend.setOnClickListener {
            reSend()
        }
    }

    private fun updatePhoneNumber() {
        val code = updatePhoneNumberBinding.sentCode.text.toString()
        val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // Update Mobile Number...
        auth.currentUser!!.reauthenticate(phoneAuthCredential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val phoneAuthCredentialNew =
                        PhoneAuthProvider.getCredential(verificationId!!, "666666")
                    auth.currentUser!!.updatePhoneNumber(phoneAuthCredentialNew)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun reSend() {
        if (reSendToken != null) {
            resendVerificationCode(phoneNumber.toString(), reSendToken!!)
            showTimer()
            progressDialog = createProgressDialog("Sending a verification code")
            progressDialog.show()
        } else {
            Toast.makeText(
                this,
                "Sorry, You Can't request new code now, Please wait...",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun verification() {
        // try to enter the code by yourself to handle the case
        // if user enter another sim card used in another phone ...
        val code = updatePhoneNumberBinding.sentCode.text.toString()
        if (code.isNotEmpty() && !verificationId.isNullOrEmpty()) {
            progressDialog = createProgressDialog("Please wait...")
            progressDialog.show()
            val credential =
                PhoneAuthProvider.getCredential(verificationId!!, code)
            signInWithPhoneAuthCredential(credential)
        }
    }

    private fun startVerify() {
        startPhoneNumberVerification(phoneNumber!!)
        showTimer()
        progressDialog = createProgressDialog("Sending a verification code")
        progressDialog.show()
    }

    private fun initView() {
        updatePhoneNumberBinding.verify.text = getString(R.string.verify_number, phoneNumber)

        // init fire base verify Phone number callback
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                if (::progressDialog.isInitialized) {
                    progressDialog.dismiss()
                }
                val smsMessageSent = credential.smsCode
                if (!smsMessageSent.isNullOrBlank()) {
                    updatePhoneNumberBinding.sentCode.setText(smsMessageSent)
                }
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                if (::progressDialog.isInitialized) {
                    progressDialog.dismiss()
                }
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.e("Exception:", "FirebaseAuthInvalidCredentialsException", e)
                    Log.e("=========:", "FirebaseAuthInvalidCredentialsException" + e.message)

                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.e("Exception:", "FirebaseTooManyRequestsException", e)
                }
                Toast.makeText(
                    this@UpdatePhoneNumberActivity,
                    "Something went wrong, please try again!!!",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) { //for low level version which doesn't do auto verification save the verification code and the token
                progressDialog.dismiss()
                updatePhoneNumberBinding.counter.isVisible = false
                // Save verification ID and resending token so we can use them later
                // Log.e("onCodeSent==", "onCodeSent:$verificationId")
                this@UpdatePhoneNumberActivity.verificationId = verificationId
                reSendToken = token
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (::progressDialog.isInitialized) {
                        progressDialog.dismiss()
                    }
                } else {
                    if (::progressDialog.isInitialized) {
                        progressDialog.dismiss()
                    }
                    Toast.makeText(
                        this,
                        "Something went wrong, please try again!!!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    // This method will send a code to a given phone number as an SMS
    private fun startPhoneNumberVerification(phoneNumber: String) {
        val option = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(option)
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        mResendToken: PhoneAuthProvider.ForceResendingToken
    ) {
        val option = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .setForceResendingToken(mResendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(option)
    }

    private fun showTimer() {
        updatePhoneNumberBinding.reSend.isEnabled = false
        counterDown = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                updatePhoneNumberBinding.counter.isVisible = true
                updatePhoneNumberBinding.counter.text = getString(
                    R.string.seconds_remaining, millisUntilFinished / 1000
                )
            }

            override fun onFinish() {
                updatePhoneNumberBinding.reSend.isEnabled = true
                updatePhoneNumberBinding.counter.isVisible = false
            }
        }.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("timeLeft", timeLeft)
        outState.putString(PHONE_NUMBER, phoneNumber)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (counterDown != null) {
            counterDown!!.cancel()
        }
    }

    private fun createProgressDialog(message: String): ProgressDialog {
        return ProgressDialog(this).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setMessage(message)
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Can't go back.", Toast.LENGTH_SHORT).show()
        finish()
    }
}