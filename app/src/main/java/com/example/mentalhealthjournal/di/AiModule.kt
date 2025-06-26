package com.example.mentalhealthjournal.di

import android.app.Application
import com.example.mentalhealthjournal.service.AiService
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides @Singleton
    fun provideGenerativeModel(app: Application): GenerativeModel {
        FirebaseApp.initializeApp(app)
        val apiKey = "AIzaSyAVndEkj4KhW2qkQsr6riazTKWmaolK9zE"
        val aiLogic = Firebase.ai(backend = GenerativeBackend.vertexAI())
        return aiLogic.generativeModel("gemini-2.0-flash") // or gemini-1.5-flash
    }
    @Provides
    @Singleton
    fun provideAiService(generativeModel: GenerativeModel): AiService {
        return AiService()
    }

}
