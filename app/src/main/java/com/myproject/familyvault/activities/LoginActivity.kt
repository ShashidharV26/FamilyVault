package com.myproject.familyvault.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myproject.familyvault.R
import java.util.concurrent.Executor


class LoginActivity : AppCompatActivity() {

    private lateinit var messageText : TextView
    private lateinit var logInBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        messageText = findViewById(R.id.msgtext)
        logInBtn = findViewById(R.id.login)

        val biometricManager : BiometricManager = androidx.biometric.BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                messageText.text = "You can use the fingerprint sensor to login"
                messageText.setTextColor(Color.parseColor("#fafafa"))
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                messageText.text = "This device doesnot have a fingerprint sensor"
                logInBtn.visibility = View.GONE
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                messageText.text = "The biometric sensor is currently unavailable"
                logInBtn.visibility = View.GONE
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                messageText.text = "Your device doesn't have fingerprint saved,please check your security settings"
                logInBtn.visibility = View.GONE
            }

            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                TODO()
            }

            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                TODO()
            }

            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                TODO()
            }
        }

        val executor : Executor = ContextCompat.getMainExecutor(this)



        val biometricPrompt: BiometricPrompt = BiometricPrompt(
            this@LoginActivity, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }

                // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext, "Login Success", Toast.LENGTH_SHORT).show()
                    logInBtn.text = "Login Successful"
                    val intent = Intent(this@LoginActivity,MainActivity::class.java)
                   startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })


        val promptInfo = PromptInfo.Builder().setTitle("FamilyVault")
            .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel")
            .build()
        logInBtn.setOnClickListener(View.OnClickListener {
            biometricPrompt.authenticate(
                promptInfo
            )
        })
    }
}