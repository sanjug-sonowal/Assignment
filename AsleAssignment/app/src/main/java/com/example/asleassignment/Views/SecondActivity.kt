package com.example.asleassignment.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.asleassignment.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class SecondActivity : AppCompatActivity() {

    private lateinit var otp: EditText
    private var phoneNum: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Retrieve the Intent that started this activity
        val intent = intent

        // Retrieve the "authToken" extra from the Intent
        phoneNum = intent.getStringExtra("authToken")
        println("london$phoneNum")

        val button = findViewById<Button>(R.id.buttonContinue)
        button.setOnClickListener {

            // Find the reference to the EditText view
            otp = findViewById(R.id.edt_mobile_number)

                makeOTPAPICall()
            }

        }

    private fun makeOTPAPICall() {

        val otpNumber = otp.text.toString()

        val client = OkHttpClient()

        // Request body
        val requestBody = FormBody.Builder()
            .add("number", "+91$phoneNum")
            .add("otp", otpNumber)
            .build()

        // Create the request
        val request = Request.Builder()
            .url("https://app.aisle.co/V1/users/verify_otp")
            .post(requestBody)
            .build()

        // Make the API call asynchronously
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle API call failure
                e.printStackTrace()
                // TODO: Handle the failure scenario
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle API call success
                val responseBody = response.body?.string()

                // Check the response code
                if (response.isSuccessful) {
                    // Parse the response and extract the auth token
                    val jsonObject = JSONObject(responseBody)
                    val authToken = jsonObject.getString("token")
                    println("canada$authToken")
                    newActivity(authToken)
                    // Proceed to Screen 3 passing the authToken
                    // TODO: Start Screen 3 and pass the authToken to it
                } else {
                    // Handle the API error response
                    // TODO: Handle the error scenario
                }
            }
        })
    }

    // Function to parse the auth token from the API response
    fun parseAuthToken(responseBody: String?): String {
        // TODO: Implement the parsing logic based on the API response format
        // Here, assuming the response body is in JSON format:
        // val jsonObject = JSONObject(responseBody)
        // val authToken = jsonObject.getString("auth_token")
        // return authToken

        // Placeholder return statement
        return ""
    }

    private fun newActivity(authToken: String) {

        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("authToken", authToken)
        startActivity(intent)

//        val notesFragment = NotesFragment()
//        val bundle = Bundle()
//        bundle.putString("AuthToken", authToken)
//        notesFragment.arguments = bundle
//
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.contentFrame, notesFragment)
//            .commit()

    }
}