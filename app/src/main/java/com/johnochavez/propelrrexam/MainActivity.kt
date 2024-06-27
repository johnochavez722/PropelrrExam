package com.johnochavez.propelrrexam

import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.johnochavez.propelrrexam.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var age: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edtFullName.addTextChangedListener { s ->
            val regex = "^[a-zA-Z.,\\s]*$".toRegex()
            if (s.isNullOrEmpty() || !regex.matches(s.toString())) {
                binding.layoutName.error = "Full name must only contain comma and period."
                return@addTextChangedListener
            }
            binding.layoutName.isErrorEnabled = false
        }

        binding.edtEmailAdd.addTextChangedListener { s ->
            if (s.isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                binding.layoutEmail.error = "Must enter your valid email address."
                return@addTextChangedListener
            }
            binding.layoutEmail.isErrorEnabled = false
        }

        binding.edtMobileNo.addTextChangedListener { s ->
            val regex = "^(09|\\+639)\\d{9}\$".toRegex()
            if (s.isNullOrEmpty() || !regex.matches(s)) {
                binding.layoutMobileNo.error = "Must enter correct mobile no."
                return@addTextChangedListener
            }
            binding.layoutMobileNo.isErrorEnabled = false
        }

        binding.ageSelectionPicker.setOnDateChangedListener(object :
            DatePicker.OnDateChangedListener {
            override fun onDateChanged(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                val year = binding.ageSelectionPicker.year
                val month = binding.ageSelectionPicker.month
                val day = binding.ageSelectionPicker.dayOfMonth
                age = getAge(year, month, day)
                binding.txtAge.text = String.format(
                    getString(R.string.age),
                    age
                )
            }
        })

        binding.btnSave.setOnClickListener {
            if (binding.edtFullName.text.toString().isEmpty() ||
                binding.edtEmailAdd.text.toString().isEmpty() ||
                binding.edtMobileNo.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "Please complete your information.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!binding.layoutName.isErrorEnabled && !binding.layoutEmail.isErrorEnabled
                && !binding.layoutMobileNo.isErrorEnabled && age >= 18
            ) {

                val api = ApiClient.getInstance().create(ApiInterface::class.java)
                // launching a new coroutine
                GlobalScope.launch {
                    val result = api.getResponse()
                    if (result != null){
                        val body = result.body()
                        runOnUiThread {
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("System Message")
                                .setMessage(body?.message)
                                .setPositiveButton(
                                    "Close"
                                ) { dialog, which ->
                                    dialog.dismiss()
                                }
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please complete your information.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getAge(year: Int, month: Int, day: Int): Int {
        val dateOfBirth = Calendar.getInstance()
        val today = Calendar.getInstance()
        dateOfBirth[year, month] = day
        var age = today[Calendar.YEAR] - dateOfBirth[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dateOfBirth[Calendar.DAY_OF_YEAR]) {
            age--
        }
        return age
    }
}