package com.developer_rahul.imagedetection

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.jsoup.Jsoup

class SpeciesInfoFetcher {

    private val client = OkHttpClient()

    fun getSpeciesInfo(query: String, callback: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Search for matching titles using Wikipedia API
                val searchUrl =
                    "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=${query}&format=json"
                val request = Request.Builder().url(searchUrl).build()
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: ""
                val json = JSONObject(body)
                val searchArray = json.getJSONObject("query").getJSONArray("search")

                var validTitle: String? = null

                // Find the first valid article that is not a disambiguation page
                for (i in 0 until searchArray.length()) {
                    val title = searchArray.getJSONObject(i).getString("title")
                    val wikiUrl = "https://en.wikipedia.org/wiki/${title.replace(" ", "_")}"
                    val doc = Jsoup.connect(wikiUrl).get()

                    val isDisambiguation = doc.select(".mw-disambig").isNotEmpty()
                    if (!isDisambiguation) {
                        validTitle = title
                        break
                    }
                }

                if (validTitle == null) {
                    callback("No valid article found for '$query'.")
                    return@launch
                }

                // Get the actual article content
                val doc = Jsoup.connect("https://en.wikipedia.org/wiki/${validTitle.replace(" ", "_")}").get()
                val paragraphs = doc.select("p")
                    .map { it.text() }
                    .filter { it.isNotBlank() && !it.contains("may refer to") }
                    .take(3)
                    .joinToString("\n\n")

                callback(paragraphs)

            } catch (e: Exception) {
                callback("Error fetching species info: ${e.message}")
            }
        }
    }
}
