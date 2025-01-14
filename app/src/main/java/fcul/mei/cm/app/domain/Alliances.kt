package fcul.mei.cm.app.domain

data class Alliances(
    val chatName: String = "",
    val creationTime: Long = System.currentTimeMillis(),
    val description: String = "",
    val owner: String = "",
)