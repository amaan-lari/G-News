package com.example.g_news

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.g_news.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
private lateinit var mAdapter: NewsListAdapter

class MainActivity : AppCompatActivity(), NewsItemClicked {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recyclerView.layoutManager= LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter(this)
        binding.recyclerView.adapter = mAdapter
    }

    private fun fetchData() {

        val url = " https://api.worldnewsapi.com/search-news?api-key=d141e8d093f240b1bd0f8ab8d1a5bc73&source-countries=in"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                val jsonArray = it.getJSONArray("news")
                val newsArray = ArrayList<News>()
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val news = News(
                        jsonObject.getString("title"),
                        jsonObject.getString("author"),
                        jsonObject.getString("url"),
                        jsonObject.getString("image")
                    )
                    newsArray.add(news)
                }

                mAdapter.updateNews(newsArray)
            },
            {

            },

        )


        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabIntent = builder.build()
        customTabIntent.launchUrl(this, Uri.parse(item.url))
    }
}