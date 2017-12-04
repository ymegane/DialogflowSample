package com.github.ymegane.dialogflowsample.model

import ai.api.model.AIResponse
import com.stfalcon.chatkit.commons.models.IMessage
import java.util.*

data class RecognizedMessage(private val response: AIResponse) : IMessage {

    override fun getId(): String = response.id

    override fun getCreatedAt(): Date = response.timestamp

    override fun getUser(): Dialogflow = Dialogflow("ai", "Dialogflow")

    override fun getText(): String = response.result.fulfillment.speech
}