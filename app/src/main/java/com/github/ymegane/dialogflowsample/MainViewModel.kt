package com.github.ymegane.dialogflowsample

import ai.api.AIConfiguration
import ai.api.AIListener
import ai.api.android.AIService
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val aiService by lazy { initAiService() }

    val isListening = MutableLiveData<Boolean>()

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

    private fun initAiService(): AIService {
        val config = ai.api.android.AIConfiguration(BuildConfig.CLIENT_ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.Japanese,
                ai.api.android.AIConfiguration.RecognitionEngine.System)

        val aiService = AIService.getService(getApplication(), config)
        aiService.setListener(aiListener)
        return aiService
    }

    private val aiListener = object : AIListener {
        override fun onResult(result: ai.api.model.AIResponse) {
            Log.d("MainViewModel", "onResult ${result.result.fulfillment.speech}")
        }

        override fun onListeningStarted() {
            Log.d("MainViewModel", "onListeningStarted")
            isListening.postValue(true)
        }

        override fun onAudioLevel(level: Float) {
            Log.v("MainViewModel", "onAudioLevel")
        }

        override fun onError(error: ai.api.model.AIError) {
            Log.w("MainViewModel", "onError ${error.message}")
        }

        override fun onListeningCanceled() {
            Log.d("MainViewModel", "onListeningCanceled")
            isListening.postValue(false)
        }

        override fun onListeningFinished() {
            Log.d("MainViewModel", "onListeningFinished")
            isListening.postValue(false)
        }

    }
}