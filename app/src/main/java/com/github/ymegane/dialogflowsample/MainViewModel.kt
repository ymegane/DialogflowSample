package com.github.ymegane.dialogflowsample

import ai.api.AIConfiguration
import ai.api.AIListener
import ai.api.android.AIService
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var aiService: AIService

    val isListening = MutableLiveData<Boolean>()

    init {
        initAiConfig()
    }

    override fun onCleared() {
        super.onCleared()

        aiService.cancel()
    }

    fun startListening() {
        aiService.startListening()
    }

    fun stopListening() {
        aiService.stopListening()
    }

    fun cancelListening() {
        aiService.cancel()
    }

    private fun initAiConfig() {
        val config = ai.api.android.AIConfiguration(BuildConfig.CLIENT_ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.Japanese,
                ai.api.android.AIConfiguration.RecognitionEngine.System)

        aiService = AIService.getService(getApplication(), config)
        aiService.setListener(aiListener)
    }

    private val aiListener = object : AIListener {
        override fun onResult(result: ai.api.model.AIResponse) {
            Log.v("MainViewModel", "onResult ${result.result.source}")
        }

        override fun onListeningStarted() {
            Log.v("MainViewModel", "onListeningStarted")
            isListening.postValue(true)
        }

        override fun onAudioLevel(level: Float) {
            Log.v("MainViewModel", "onAudioLevel")
        }

        override fun onError(error: ai.api.model.AIError?) {
            Log.v("MainViewModel", "onError")
        }

        override fun onListeningCanceled() {
            Log.v("MainViewModel", "onListeningCanceled")
            isListening.postValue(false)
        }

        override fun onListeningFinished() {
            Log.v("MainViewModel", "onListeningFinished")
            isListening.postValue(false)
        }

    }
}