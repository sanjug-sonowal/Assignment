package com.example.asleassignment.Views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.asleassignment.R
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class NotesFragment : Fragment() {

    private var value: String? = null

    private lateinit var imageView0: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView
    private lateinit var textView0: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_notes, container, false)


        // Retrieve the passed value
        value = arguments?.getString("key") // Replace "key" with the actual key used in the activity
        println("Notes fragment value: $value")
        // Use the value as needed

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView0 = view.findViewById(R.id.imageView)
        imageView2 = view.findViewById(R.id.notes_image2)
        imageView3 = view.findViewById(R.id.notes_image3)
        textView0 = view.findViewById<TextView>(R.id.textmain1)
        textView2 = view.findViewById<TextView>(R.id.text_image2)
        textView3 = view.findViewById<TextView>(R.id.text_image3)


        // Call the function to make the API call
        makeNotesApiCall()


    }

    private fun makeNotesApiCall() {
        val client = OkHttpClient()
        val url = "https://app.aisle.co/V1/users/test_profile_list"

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", value!!)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle the failure scenario
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                println("mobibox$responseBody")

                // Handle the response here
                if (response.isSuccessful && responseBody != null) {
                    // Process the response
                    activity?.runOnUiThread {
                        // Update UI or perform any other action with the response
                        val jsonObject = JSONObject(responseBody)
                        val firstName = jsonObject.getJSONObject("invites")
                            .getJSONArray("profiles")
                            .getJSONObject(0)
                            .getJSONObject("general_information")
                            .getString("first_name")

                        // Extracting Mayank's age
                        val mayankAge = jsonObject.getJSONObject("invites")
                            .getJSONArray("profiles")
                            .getJSONObject(0)
                            .getJSONObject("general_information")
                            .getInt("age")

                        // Extracting the first photo link
                        val photosArray = jsonObject.getJSONObject("invites")
                            .getJSONArray("profiles")
                            .getJSONObject(0)
                            .getJSONArray("photos")

                        val firstPhotoLink = photosArray.getJSONObject(0)
                            .getString("photo")

                        Picasso.get().load(firstPhotoLink).into(imageView0)
                        textView0.text = firstName + ", " + mayankAge


                        val avatarLink1 = jsonObject.getJSONObject("likes").getJSONArray("profiles").getJSONObject(0).getString("avatar")
                        val avatarName1 = jsonObject.getJSONObject("likes").getJSONArray("profiles").getJSONObject(0).getString("first_name")
                        val avatarLink2 = jsonObject.getJSONObject("likes").getJSONArray("profiles").getJSONObject(1).getString("avatar")
                        val avatarName2 = jsonObject.getJSONObject("likes").getJSONArray("profiles").getJSONObject(1).getString("first_name")
                        Picasso.get().load(avatarLink1).into(imageView2)
                        textView2.text = avatarName1
                        Picasso.get().load(avatarLink2).into(imageView3)
                        textView3.text = avatarName2

                    }
                } else {
                    // Handle the unsuccessful response
                    println("Error: ${response.code}")
                }
            }
        })
    }
}