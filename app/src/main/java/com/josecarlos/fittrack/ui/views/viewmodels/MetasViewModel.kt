package com.josecarlos.fittrack.ui.views.viewmodels

import androidx.lifecycle.ViewModel
import com.josecarlos.fittrack.data.dataClases.Meta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class UIStateMetas(
    var createNewMeta:Boolean = false,
    var recharge:Int = 0,
    var showMetaDetail:Boolean = false,
    var idMetaDetail:Meta? = null,
    var isDiaria:Boolean = false
)

class MetasViewModel():ViewModel(){
    private val _uiState = MutableStateFlow<UIStateMetas>(UIStateMetas())
    val uiState: StateFlow<UIStateMetas> = _uiState

    fun showCreateNewMeta(){
        _uiState.value = _uiState.value.copy(createNewMeta = true)
    }

    fun hideCreateNewMeta(){
        _uiState.value = _uiState.value.copy(createNewMeta = false)
    }

    fun rechargeUI(){
        _uiState.value = _uiState.value.copy(recharge = _uiState.value.recharge+1)
    }
    fun hideShowMetaDetail(){
        _uiState.value = _uiState.value.copy(showMetaDetail = false)
    }

    fun showShowMetaDetail(id:Meta, isDiaria: Boolean){
        _uiState.value = _uiState.value.copy(showMetaDetail = true, idMetaDetail = id, isDiaria = isDiaria )

    }

}