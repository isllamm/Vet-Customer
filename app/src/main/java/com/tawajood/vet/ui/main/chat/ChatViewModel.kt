package com.tawajood.vet.ui.main.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawajood.vet.pojo.ChatResponse
import com.tawajood.vet.repository.Repository
import com.tawajood.vet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import handleResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel
@Inject
constructor(
    private val repository: Repository
) : ViewModel() {
    private val _getChat = MutableStateFlow<Resource<ChatResponse>>(Resource.Idle())
    val getChat = _getChat.asSharedFlow()

    private val _sendMessage = MutableStateFlow<Resource<Any>>(Resource.Idle())
    val sendMessage = _sendMessage.asSharedFlow()


    fun getChat(request_id: String, user_id: String) = viewModelScope.launch {
        try {
            _getChat.emit(Resource.Loading())
            val response = handleResponse(repository.getChat(request_id, user_id))
            if (response.status) {
                _getChat.emit(Resource.Success(response.data!!))
            } else {
                _getChat.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _getChat.emit(Resource.Error(message = e.message!!))
        }
    }

    fun sendMessage(request_id: String, user_id: String,message: String, message_type: String) = viewModelScope.launch {
        try {
            _sendMessage.emit(Resource.Loading())
            val response = handleResponse(repository.sendMessage(request_id,user_id,message, message_type))
            if (response.status) {
                _sendMessage.emit(Resource.Success(response.data!!))
            } else {
                _sendMessage.emit(Resource.Error(message = response.msg))
            }
        } catch (e: Exception) {
            _sendMessage.emit(Resource.Error(message = e.message!!))
        }
    }

}
