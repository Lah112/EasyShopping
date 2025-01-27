package com.example.buynow.data  // Ensure this matches your package structure

object CardType {
    fun detect(cardNumber: String): String {
        return when {
            cardNumber.startsWith("4") -> "Visa"
            cardNumber.startsWith("5") -> "MasterCard"
            cardNumber.startsWith("34") || cardNumber.startsWith("37") -> "American Express"
            cardNumber.startsWith("6") -> "Discover"
            else -> "Unknown"
        }
    }
}
