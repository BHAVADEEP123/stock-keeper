package com.stockkeeper.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import com.stockkeeper.app.databinding.ActivityFingerprintAuthBinding
import com.stockkeeper.app.ui.MainActivity
import com.stockkeeper.app.utils.BiometricUtils

class FingerprintAuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFingerprintAuthBinding
    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFingerprintAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if biometric is available
        if (!BiometricUtils.isBiometricAvailable(this)) {
            Toast.makeText(
                this,
                "Biometric authentication is not available",
                Toast.LENGTH_SHORT
            ).show()
            // Optionally, allow bypass with PIN or password
            proceedToMain()
            return
        }

        setupBiometricAuth()
        launchFingerprint()
    }

    private fun setupBiometricAuth() {
        biometricPrompt = BiometricUtils.createBiometricPrompt(
            this,
            onSuccess = { onAuthenticationSuccess() },
            onError = { error -> onAuthenticationError(error) },
            onFailed = { onAuthenticationFailed() }
        )
    }

    private fun launchFingerprint() {
        val promptInfo = BiometricUtils.getFingerprintPromptInfo()
        biometricPrompt.authenticate(promptInfo)
    }

    private fun onAuthenticationSuccess() {
        Toast.makeText(this, "Authentication successful!", Toast.LENGTH_SHORT).show()
        proceedToMain()
    }

    private fun onAuthenticationError(error: String) {
        Toast.makeText(this, "Authentication error: $error", Toast.LENGTH_SHORT).show()
    }

    private fun onAuthenticationFailed() {
        Toast.makeText(this, "Authentication failed. Try again.", Toast.LENGTH_SHORT).show()
    }

    private fun proceedToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
