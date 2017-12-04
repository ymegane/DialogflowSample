package com.github.ymegane.dialogflowsample.model

import com.stfalcon.chatkit.commons.models.IUser

data class Dialogflow(private val id: String, private val name: String) : IUser {

    override fun getAvatar(): String = ""

    override fun getName(): String = name

    override fun getId(): String = id
}