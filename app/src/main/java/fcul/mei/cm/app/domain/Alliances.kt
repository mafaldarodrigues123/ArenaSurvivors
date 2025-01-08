package fcul.mei.cm.app.domain

import java.sql.Timestamp


data class Alliances(
    val chatName: String = "",
    val creationTime: Long = System.currentTimeMillis(),
    val description: String = "",
    val owner: String = "",
)