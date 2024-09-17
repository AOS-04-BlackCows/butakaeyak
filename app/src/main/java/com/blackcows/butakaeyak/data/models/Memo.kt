package com.blackcows.butakaeyak.data.models

import com.google.gson.annotations.Expose
import java.time.LocalDate

data class Memo(
    @Expose(serialize = false)
    val id: String,
    val userId: String,
    val groupId: String,
    val content: String,
    val createdAt: LocalDate,
    val updatedAt: LocalDate
) {
    fun toRequest()
        = MemoRequest(
            userId, groupId, content, createdAt.toString(), updatedAt.toString()
        )
}

data class MemoRequest(
    val userId: String,
    val groupId: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String
)
data class MemoResponse(
    val id: String,
    val userId: String,
    val groupId: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String
)
