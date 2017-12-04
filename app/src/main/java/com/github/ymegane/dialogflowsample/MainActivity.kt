package com.github.ymegane.dialogflowsample

import ai.api.model.AIResponse
import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.github.ymegane.dialogflowsample.model.RecognizedMessage
import com.github.ymegane.dialogflowsample.model.AuthorMessage
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions


@RuntimePermissions
class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    private val messageAdapter = MessagesListAdapter<IMessage>("author", ImageLoader { imageView, url ->
        // TODO
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initLayoutWithPermissionCheck()
    }

    override fun onPause() {
        super.onPause()

        viewModel.cancelListening()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    fun initLayout() {
        viewModel.isListening.observe(this, Observer<Boolean> {
            if (it == true) {
                buttonSpeech.text = "Stop"
            } else {
                buttonSpeech.text = "Speech"
            }
        })
        viewModel.response.observe(this, Observer<AIResponse> {
            it ?: return@Observer

            messageAdapter.addToStart(AuthorMessage(it), true)
            messageAdapter.addToStart(RecognizedMessage(it), true)
        })

        buttonSpeech.setOnClickListener {
            if (viewModel.isListening.value == true) {
                viewModel.stopListening()
            } else {
                viewModel.startListening()
            }
        }

        messageAdapter.setDateHeadersFormatter(viewModel)
        messagesList.setAdapter(messageAdapter)
    }

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    fun showDeniedForAudio() {
        Toast.makeText(this, "RECORD_AUDIO denied", Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO)
    fun showNeverAskForAudio() {
        Toast.makeText(this, "RECORD_AUDIO denied", Toast.LENGTH_SHORT).show()
    }
}
