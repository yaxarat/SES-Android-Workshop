package com.summit.summitproject.prebuilt.model

/**
 * Represents a simple user account information -- containing the following:
 * @param name name of the account holder.
 * @param cardLastFour last 4 digits of the user's credit card.
 * @param transactions list of transaction details.
 * @see Transaction
 */
data class AccountInfo(
    val name: String,
    val cardLastFour: String,
    val transactions: List<Transaction>
) {
    override fun toString(): String {
        return "name = $name, card last 4 digits: $cardLastFour" +
                "\ntransactions: $transactions"
    }
}
