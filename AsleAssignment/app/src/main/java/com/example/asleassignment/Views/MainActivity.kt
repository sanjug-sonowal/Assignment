package com.example.asleassignment.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.asleassignment.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var btn:Button;
    private lateinit var edtMobileNumber: EditText
    var mobileNumber:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val contButton = findViewById<Button>(R.id.btn_continue)
        contButton.setOnClickListener {

            // Find the reference to the EditText view
            edtMobileNumber = findViewById(R.id.edt_mobile_number)

            // Example usage: Retrieve the input value and log it
            val mobileNumber = edtMobileNumber.text.toString()
            println("Mobile number: $mobileNumber")

            // Create an instance of OkHttpClient
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()

            // Create a JSON object with the phone number
            val json = JSONObject()
            json.put("number", "+91$mobileNumber")

            // Create a RequestBody with the JSON payload
            val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

            // Create the Request object
            val request = Request.Builder()
                .url("https://app.aisle.co/V1/users/phone_number_login")
                .post(requestBody)
                .build()

            // Make the API call asynchronously
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Handle any error that occurred
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    // Handle the response from the server
                    val responseBody = response.body?.string()
                    println("ResponseApi$responseBody")

                    // Process the response as needed
                    if (response.isSuccessful) {
                        // Check the status value
                        val jsonObject = JSONObject(responseBody.toString())
                        val status = jsonObject.getBoolean("status")
                        if (status) {
                            changeActivity()
                            // Success response received and status is true, navigate to Screen 2
                            // Start your intent or navigate to the next screen here
                        } else {
                            // Status is false, handle the case accordingly
                            // Display an error message or perform any other required action
                        }
                    }
                }

            })
        }



//        btn = findViewById(R.id.btn_continue);
//
//        btn.setOnClickListener {
//
//        }


    }

    fun changeActivity() {
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("phoneNum", mobileNumber)
        startActivity(intent)
    }
}