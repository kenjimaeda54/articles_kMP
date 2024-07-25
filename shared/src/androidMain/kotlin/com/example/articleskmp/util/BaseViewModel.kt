package com.example.articleskmp.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope


//precisa ser mesmo diretorio que criou no common ou seja viewModel
actual  open class BaseViewModel: ViewModel() {

    actual val scope: CoroutineScope = viewModelScope
}