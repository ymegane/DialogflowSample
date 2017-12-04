package com.github.ymegane.dialogflowsample.model

import ai.api.model.AIResponse
import com.stfalcon.chatkit.commons.models.IMessage
import java.util.*

data class AuthorMessage(private val response: AIResponse) : IMessage {

    override fun getId(): String = "author${response.id}"

    override fun getCreatedAt(): Date = response.timestamp

    override fun getUser(): Author = Author("author", "me")

    override fun getText(): String = response.result.resolvedQuery
}