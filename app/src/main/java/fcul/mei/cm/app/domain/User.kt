package fcul.mei.cm.app.domain

import java.util.UUID

data class User (
    val id: String = UUID.randomUUID().toString(),
    val district: Int,
    val role: String,
    val name: String,
    val status: String,
    val joinedAt: Long,
) {
    init {
        require(district in 1 until 13)
    }
}