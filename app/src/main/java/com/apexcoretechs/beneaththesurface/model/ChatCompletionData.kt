package com.apexcoretechs.beneaththesurface.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatCompletionData(
    val id: String,
    val `object`: String,
    val created: Int, // Using Int since `new NumberInt` indicates an integer value
    val model: String,
    val choices: List<Choice>,
    val usage: Usage,
    val system_fingerprint: String
) : IShop {
    companion object {
        const val getPath = "/chatCompletionData"
    }

    override val route: String
        get() = getPath
}

@Serializable
data class Choice(
    val index: Int,
    val message: Message,
    val logprobs: String?, // Assuming nullable String for logprobs based on its null value in JSON
    val finish_reason: String
)

/*@Serializable
data class Message(
    val role: String,
    val content: Content,  *//** changed to [Content] 20241207 *//*
    val refusal: String? // Assuming nullable String for refusal based on its null value in JSON
)*/

@Serializable
data class Message(
    val role: String,
    val content: String,
    val refusal: String? // Assuming nullable String for refusal based on its null value in JSON
)

/*@Serializable
data class Content(
    val eventTitle: String,
    val eventDescription: String,
    val eventDate: String
)*/

@Serializable
data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int,
    val prompt_tokens_details: TokenDetails?,
    val completion_tokens_details: TokenDetails?
)

@Serializable
data class TokenDetails(
    val cached_tokens: Int?,
    val audio_tokens: Int?,
    val reasoning_tokens: Int?,
    val accepted_prediction_tokens: Int?,
    val rejected_prediction_tokens: Int?
)
