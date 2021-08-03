package com.summit.summitproject.prebuilt.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object ConversionHelper {
    private val gson by lazy { Gson() }

    private val type: Type by lazy { object : TypeToken<List<Transaction?>?>() {}.type }

    fun encodeTransactionsToJson(transactions: List<Transaction>): String = gson.toJson(transactions)

    fun decodeJsonToTransactions(json: String): List<Transaction> = gson.fromJson(json, type)
}