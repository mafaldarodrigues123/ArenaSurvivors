package fcul.mei.cm.app.domain

import java.util.UUID

data class User (
    val id: String = UUID.randomUUID().toString(),
    val district: Int,
    val name: String,
    val coordinates: Coordinates,
) {
    init {
        require(district in 1 until 13)
    }
}