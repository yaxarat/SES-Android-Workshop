package com.summit.summitproject.prebuilt.model

/**
 * Represents a simple credit card transaction -- containing the merchant and the amount.
 *
 * @property merchant String value representing the merchant name.
 * @property amount String value representing the amount for that transaction.
 */
data class Transaction(
    val merchant: String,
    val amount: String,
) {
    override fun toString(): String = "Merchant: $merchant, Amount: $amount"
}