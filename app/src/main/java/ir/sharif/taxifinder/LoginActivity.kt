package ir.sharif.taxifinder

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.TextUtils
import android.view.View
import com.google.firebase.auth.FirebaseUser
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit
import com.google.firebase.auth.PhoneAuthProvider
import com.orhanobut.logger.Logger
import java.lang.Exception

//import com.google.firebase.quickstart.auth.R
//import kotlinx.android.synthetic.main.activity_phone_auth.buttonResend
//import kotlinx.android.synthetic.main.activity_phone_auth.buttonStartVerification
//import kotlinx.android.synthetic.main.activity_phone_auth.buttonVerifyPhone
//import kotlinx.android.synthetic.main.activity_phone_auth.detail
//import kotlinx.android.synthetic.main.activity_phone_auth.fieldPhoneNumber
//import kotlinx.android.synthetic.main.activity_phone_auth.fieldVerificationCode
//import kotlinx.android.synthetic.main.activity_phone_auth.phoneAuthFields
//import kotlinx.android.synthetic.main.activity_phone_auth.signOutButton
//import kotlinx.android.synthetic.main.activity_phone_auth.signedInButtons
//import kotlinx.android.synthetic.main.activity_phone_auth.status


class LoginActivity : BaseActivity() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private var verificationInProgress = false
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }

        // Assign click listeners
//        buttonStartVerification.setOnClickListener(this)
//        buttonVerifyPhone.setOnClickListener(this)
//        buttonResend.setOnClickListener(this)
//        signOutButton.setOnClickListener(this)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("fa")
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                // [START_EXCLUDE silent]
                verificationInProgress = false
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential)
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)
                // [START_EXCLUDE silent]
                verificationInProgress = false
                // [END_EXCLUDE]

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    phoneEditText.error = "شماره تلفن اشتباه است."
                    phoneNumberText.text = "لطفا شماره تلفن را به درستی وارد نمایید."
                    // [END_EXCLUDE]
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    phoneNumberText.text = "دفعات تکرار از حد خارج شده است!"
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED)
                // [END_EXCLUDE]
            }

            override fun onCodeSent(
                verificationId: String?,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId!!)

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token

                // [START_EXCLUDE]
                // Update UI
                updateUI(STATE_CODE_SENT)
                // [END_EXCLUDE]
            }

//            override fun onCodeAutoRetrievalTimeOut(p0: String?) {
//                Logger.e("onCodeAutoRetrievalTimeOut " + p0)
//            }
        }
        // [END phone_auth_callbacks]

        submitPhoneButton.setOnClickListener {
            if (!validatePhoneNumber()) {
                return@setOnClickListener
            }
            startPhoneNumberVerification(phoneEditText.text.toString())

//            PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "+989021112782", // Phone number to verify
//                60, // Timeout duration
//                TimeUnit.SECONDS, // Unit of timeout
//                this, // Activity (for callback binding)
//                callbacks
//            ) // OnVerificationStateChangedCallbacks
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
        }


        submitCodeButton.setOnClickListener {
            val code = verificationCode.text.toString()
            if (TextUtils.isEmpty(code) || code.length != 6) {
                verificationCode.error = "فرمت نادرست است."
                return@setOnClickListener
            }

            verifyPhoneNumberWithCode(storedVerificationId, code)
        }


        resendButton.setOnClickListener {
            try {
                resendVerificationCode(phoneEditText.text.toString(), resendToken)
            }
            catch (e: Exception){
                Toast.makeText(this, "کدی برای شما ارسال نشده است.", Toast.LENGTH_LONG).show()
            }
        }


        editPhoneNumberButton.setOnClickListener {
            getVerificationCode.visibility = View.GONE
            getPhoneNumber.visibility = View.VISIBLE
        }

    }

    private fun tryToConnect() {
        if (isOnline()) {
            // Check if user is signed in (non-null) and update UI accordingly.
            val currentUser = auth.currentUser
            updateUI(currentUser)

            // [START_EXCLUDE]
            if (verificationInProgress && validatePhoneNumber()) {
                startPhoneNumberVerification(phoneEditText.text.toString())
            }
            // [END_EXCLUDE]
        } else {
            showNetworkAlert()
        }
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        tryToConnect()
    }
    // [END on_start_check_user]

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, verificationInProgress)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        verificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS)
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        Progressbar.visibility = View.VISIBLE
//        getPhoneNumber.visibility = View.GONE
//        getVerificationCode.visibility = View.VISIBLE
        val text = "کد فعال سازی به شماره $phoneNumber ارسال شد."
        verificationText.text = text
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+98" + phoneNumber.substring(1), // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks
        ) // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
        verificationInProgress = true
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }

    // [START resend_verification]
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val text = "کد فعال سازی مجددا به شماره $phoneNumber ارسال شد."
        verificationText.text = text
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+98" + phoneNumber.substring(1), // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks, // OnVerificationStateChangedCallbacks
            token
        ) // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    // [START_EXCLUDE]
                    updateUI(STATE_SIGN_IN_SUCCESS, user)
                    // [END_EXCLUDE]
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        // [START_EXCLUDE silent]
                        verificationCode.error = "کد نادرست است."
                        Toast.makeText(this, "کد نادرست است.", Toast.LENGTH_LONG).show()
                        // [END_EXCLUDE]
                    }
                    // [START_EXCLUDE silent]
                    // Update UI
                    updateUI(STATE_SIGN_IN_FAILED)
                    // [END_EXCLUDE]
                }
            }
    }
    // [END sign_in_with_phone]

    private fun signOut() {
        auth.signOut()
        updateUI(STATE_INITIALIZED)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            updateUI(STATE_SIGN_IN_SUCCESS, user)
        } else {
            updateUI(STATE_INITIALIZED)
        }
    }

    private fun updateUI(uiState: Int, cred: PhoneAuthCredential) {
        updateUI(uiState, null, cred)
    }

    private fun updateUI(
        uiState: Int,
        user: FirebaseUser? = auth.currentUser,
        cred: PhoneAuthCredential? = null
    ) {
        when (uiState) {
            STATE_INITIALIZED -> {
                getPhoneNumber.visibility = View.VISIBLE
                getVerificationCode.visibility = View.GONE
                phoneNumberText.text = ""
                verificationText.text = ""
                Progressbar.visibility = View.GONE
                // Initialized state, show only the phone number field and start button
//                enableViews(buttonStartVerification, fieldPhoneNumber)
//                disableViews(buttonVerifyPhone, buttonResend, fieldVerificationCode)
//                detail.text = null
            }
            STATE_CODE_SENT -> {
                getPhoneNumber.visibility = View.GONE
                getVerificationCode.visibility = View.VISIBLE
                val phoneNo = phoneEditText.text.toString()
                val text = "کد فعال سازی به شماره $phoneNo ارسال شد."
                Toast.makeText(this, text, Toast.LENGTH_LONG).show()
                // Code sent state, show the verification field, the
//                enableViews(buttonVerifyPhone, buttonResend, fieldPhoneNumber, fieldVerificationCode)
//                disableViews(buttonStartVerification)
//                detail.setText(R.string.status_code_sent)
            }
            STATE_VERIFY_FAILED -> {
                getPhoneNumber.visibility = View.VISIBLE
                getVerificationCode.visibility = View.GONE
                verificationText.text = ""
                Progressbar.visibility = View.GONE
                val phoneNo = phoneEditText.text.toString()
                val text = "ارسال کد به شماره $phoneNo با خطا مواجه شد."
                phoneNumberText.text = text
                Toast.makeText(this, text, Toast.LENGTH_LONG).show()

                // Verification has failed, show all options
//                enableViews(
//                    buttonStartVerification, buttonVerifyPhone, buttonResend, fieldPhoneNumber,
//                    fieldVerificationCode
//                )
//                detail.setText(R.string.status_verification_failed)
            }
            STATE_VERIFY_SUCCESS -> {
                // Verification has succeeded, proceed to firebase sign in
//                disableViews(
//                    buttonStartVerification, buttonVerifyPhone, buttonResend, fieldPhoneNumber,
//                    fieldVerificationCode
//                )
//                detail.setText(R.string.status_verification_succeeded)

                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.smsCode != null) {
//                        fieldVerificationCode.setText(cred.smsCode)
                    } else {
//                        fieldVerificationCode.setText(R.string.instant_validation)
                    }
                }
            }
            STATE_SIGN_IN_FAILED -> {
//                Toast.makeText(this, "STATE_SIGN_IN_FAILED", Toast.LENGTH_LONG).show()
            }
            STATE_SIGN_IN_SUCCESS -> {
//                Toast.makeText(this, "STATE_SIGN_IN_SUCCESS", Toast.LENGTH_LONG).show()
            }
        } // Np-op, handled by sign-in check

        if (user == null) {
            // Signed out
//            Toast.makeText(this, "شما خارج شدید.", Toast.LENGTH_LONG).show()
//            phoneAuthFields.visibility = View.VISIBLE
//            signedInButtons.visibility = View.GONE

//            status.setText(R.string.signed_out)
        } else {
            // Signed in
            Toast.makeText(this, "شما وارد شدید.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
//            phoneAuthFields.visibility = View.GONE
//            signedInButtons.visibility = View.VISIBLE

//            enableViews(fieldPhoneNumber, fieldVerificationCode)
//            fieldPhoneNumber.text = null
//            fieldVerificationCode.text = null

//            status.setText(R.string.signed_in)
//            detail.text = getString(R.string.firebase_status_fmt, user.uid)
        }
    }

    private fun validatePhoneNumber(): Boolean {
        val phoneNumber = phoneEditText.text.toString()
        if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.length == 11 && phoneNumber.startsWith("09")) {
            return true
        }

        phoneEditText.error = "شماره تلفن نادرست است."
        phoneNumberText.text = "لطفا شماره تلفن را به درستی وارد نمایید."
        return false

    }

    private fun enableViews(vararg views: View) {
        for (v in views) {
            v.isEnabled = true
        }
    }

    private fun showNetworkAlert() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("No internet connection.")
            .setCancelable(false)
            .setNegativeButton("try again for connect") { _, _ ->
                tryToConnect()
            }.setNeutralButton("exit programme") { _, _ ->
                finish()
            }


        val alert = dialogBuilder.create()
        alert.setTitle("Network Manager")
        alert.show()
    }

    private fun isOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    private fun disableViews(vararg views: View) {
        for (v in views) {
            v.isEnabled = false
        }
    }


    companion object {
        private const val TAG = "PhoneAuthActivity"
        private const val KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress"
        private const val STATE_INITIALIZED = 1
        private const val STATE_VERIFY_FAILED = 3
        private const val STATE_VERIFY_SUCCESS = 4
        private const val STATE_CODE_SENT = 2
        private const val STATE_SIGN_IN_FAILED = 5
        private const val STATE_SIGN_IN_SUCCESS = 6
    }
}

/**
class LoginActivity : BaseActivity() {

private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContentView(R.layout.activity_login)

submitButton.bold()
phoneNumberLabel.bold()

callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

override fun onVerificationCompleted(credential: PhoneAuthCredential) {
// This callback will be invoked in two situations:
// 1 - Instant verification. In some cases the phone number can be instantly
//     verified without needing to send or enter a verification code.
// 2 - Auto-retrieval. On some devices Google Play services can automatically
//     detect the incoming verification SMS and perform verification without
//     user action.
// [START_EXCLUDE silent]
//                verificationInProgress = false
// [END_EXCLUDE]
Logger.e("onVerificationCompleted")
// [START_EXCLUDE silent]
// Update the UI and attempt sign in with the phone credential
//                updateUI(STATE_VERIFY_SUCCESS, credential)
// [END_EXCLUDE]
//                signInWithPhoneAuthCredential(credential)
}

override fun onVerificationFailed(e: FirebaseException) {
// This callback is invoked in an invalid request for verification is made,
// for instance if the the phone number format is not valid.
// [START_EXCLUDE silent]
//                verificationInProgress = false
// [END_EXCLUDE]
Logger.e("onVerificationFailed " + e.toString())

if (e is FirebaseAuthInvalidCredentialsException) {
// Invalid request
// [START_EXCLUDE]
//                    fieldPhoneNumber.error = "Invalid phone number."
// [END_EXCLUDE]
} else if (e is FirebaseTooManyRequestsException) {
// The SMS quota for the project has been exceeded
// [START_EXCLUDE]
//                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
//                        Snackbar.LENGTH_SHORT).show()
// [END_EXCLUDE]
}

// Show a message and update the UI
// [START_EXCLUDE]
//                updateUI(STATE_VERIFY_FAILED)
// [END_EXCLUDE]
}

override fun onCodeSent(
verificationId: String?,
token: PhoneAuthProvider.ForceResendingToken
) {

Logger.e("onCodeSent " + verificationId)
// The SMS verification code has been sent to the provided phone number, we
// now need to ask the user to enter the code and then construct a credential
// by combining the code with a verification ID.

// Save verification ID and resending token so we can use them later
//                storedVerificationId = verificationId
//                resendToken = token

// [START_EXCLUDE]
// Update UI
//                updateUI(STATE_CODE_SENT)
// [END_EXCLUDE]
}

override fun onCodeAutoRetrievalTimeOut(p0: String?) {
Logger.e("onCodeAutoRetrievalTimeOut " + p0)
}
}


submitButton.setOnClickListener {
val auth = FirebaseAuth.getInstance()
auth.setLanguageCode("fa")
PhoneAuthProvider.getInstance().verifyPhoneNumber(
"+989021112782", // Phone number to verify
60, // Timeout duration
TimeUnit.SECONDS, // Unit of timeout
this, // Activity (for callback binding)
callbacks
) // OnVerificationStateChangedCallbacks
startActivity(Intent(this, MainActivity::class.java))
finish()
val phone = phoneEditText.text
if (phone.startsWith("9") || phone.startsWith("09")) {
//                callLoginWebservice(phone.toString())
} else {
Toast.makeText(this, "شماره موبایل اشتباه است", Toast.LENGTH_LONG).show()
}
}
}

private fun callLoginWebservice(phone: String) {
thread(true) {
try {
val response = WebserviceHelper.login(phone)
if (response.code == 200) {
startActivity(Intent(this, MainActivity::class.java))
finish()
} else {
toast(response.message)
}
} catch (e: Exception) {
toastNoNetwork()
}
}
}
}
 */