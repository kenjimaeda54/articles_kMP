package com.example.articleskmp.util.data

data class DataOrException<T, Exception,Boolean> (
    val data: T? = null,
    val exception: Exception? = null,
    val isLoading: Boolean
)