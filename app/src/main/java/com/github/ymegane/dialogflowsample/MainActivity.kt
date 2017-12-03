package com.github.ymegane.dialogflowsample

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions


@RuntimePermissions
class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

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
                buttonSpeach.text = "Stop"
            } else {
                buttonSpeach.text = "Speach"
            }
        })

        buttonSpeach.setOnClickListener {
            if (viewModel.isListening.value == true) {
                viewModel.stopListening()
            } else {
                viewModel.startListening()
            }
        }
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
