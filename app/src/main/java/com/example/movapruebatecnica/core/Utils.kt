package com.example.movapruebatecnica.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedDate(): String {
    if (this == 0L) return "N/A"
    val date = Date(this)
    val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return formatter.format(date)
}