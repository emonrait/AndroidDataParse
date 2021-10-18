package com.example.androidapidataparse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    lateinit var requestQueue: RequestQueue
    private lateinit var search: Button
    private lateinit var userinput: EditText
    private lateinit var name: TextView
    private lateinit var plot: TextView
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        search = findViewById(R.id.search)
        userinput = findViewById(R.id.userinput)
        name = findViewById(R.id.name)
        plot = findViewById(R.id.plot)
        image = findViewById(R.id.image)

        val appnetwork = BasicNetwork(HurlStack())
        val appcache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap
        requestQueue = RequestQueue(appcache, appnetwork).apply {
            start()
        }

        search.setOnClickListener {
            var input = userinput.text.toString()
            fetchData(input)
        }
    }

    fun fetchData(input: String) {
        val url = "http://www.omdbapi.com/?t=${input}&apikey=b6f52680"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                if (response.get("Response") == "False") {
                    name.text = "Incorrect detail"
                } else {
                    Glide.with(this).load(response.getString("Poster")).into(image)
                    plot.text = response.getString("Plot")
                    name.text =
                        response.getString("Title") + "\n\n" + "Writer: " + response.getString("Writer")
                }
            },
            { error ->
                Log.d("vol", error.toString())
            }
        )

        requestQueue.add(jsonObjectRequest)
    }
}