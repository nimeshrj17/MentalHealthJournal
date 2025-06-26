package com.example.mentalhealthjournal.service

import com.example.mentalhealthjournal.domain.model.AiSupport
import com.google.firebase.ai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.create
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class AiService(){


    private val client = OkHttpClient()


    suspend fun generateSupport(title: String, content: String): AiSupport =
        withContext(Dispatchers.IO) {
            val prompt = """
                You're a kind and thoughtful mental health assistant.
                A user wrote this journal entry:

                Title: "$title"
                Content: "$content"

                1. Provide a gentle, empathetic response to help them feel better.
                2. Suggest two short, actionable mental health tips.

                Respond in strict JSON format:
                {
                  "response": "your message",
                  "suggestions": ["tip 1", "tip 2"]
                }
            """.trimIndent()

            val json = JSONObject().apply {
                put("contents", JSONArray().put(
                    JSONObject().put("parts", JSONArray().put(
                        JSONObject().put("text", prompt)
                    ))
                ))
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=AIzaSyAVndEkj4KhW2qkQsr6riazTKWmaolK9zE")
                .addHeader("Content-Type", "application/json")
                .post(create("application/json".toMediaTypeOrNull(), json.toString()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    if (response.code == 429) throw IOException("Rate limit exceeded.")
                    throw IOException("Unexpected response: ${response.code} ${response.message}")
                }

                val body = response.body?.string() ?: throw IOException("Empty response body")
                val contentText = JSONObject(body)
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")

                val parsed = JSONObject(contentText)
                val responseText = parsed.getString("response")
                val suggestionsJson = parsed.getJSONArray("suggestions")
                val suggestions = List(suggestionsJson.length()) { i -> suggestionsJson.getString(i) }

                return@withContext AiSupport(response = responseText, suggestions = suggestions)
            }
        }

    suspend fun analyzeSentiment(content: String): String =
        withContext(Dispatchers.IO) {
            val prompt = """
                Analyze the emotional sentiment of the following journal entry. Respond with a line how is the writer feeling".

                Entry: "$content"
            """.trimIndent()

            val json = JSONObject().apply {
                put("contents", JSONArray().put(
                    JSONObject().put("parts", JSONArray().put(
                        JSONObject().put("text", prompt)
                    ))
                ))
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=AIzaSyAVndEkj4KhW2qkQsr6riazTKWmaolK9zE")
                .addHeader("Content-Type", "application/json")
                .post(create("application/json".toMediaTypeOrNull(), json.toString()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    if (response.code == 429) throw IOException("Rate limit exceeded.")
                    throw IOException("Unexpected response: ${response.code} ${response.message}")
                }

                val body = response.body?.string() ?: throw IOException("Empty response body")
                return@withContext JSONObject(body)
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
                    .trim()
            }
        }
}
