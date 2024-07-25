package com.example.articleskmp.util.data

data class DataOrException<T, TException : Exception,Boolean> (
    val data: T? = null,
    val exception: Exception? = null,
    val isLoading: Boolean
)