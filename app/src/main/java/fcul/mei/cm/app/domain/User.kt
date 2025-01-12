package fcul.mei.cm.app.domain

import java.util.UUID

data class User (
    val id: String = UUID.randomUUID().toString(),
    val district: Int = 0,
    val role: String = "participant",
    val name: String = "",
    val status: String = "none",
    val joinedAt: Long = 0,
) {
    init {
        require(district in 0 until 13)
    }
}