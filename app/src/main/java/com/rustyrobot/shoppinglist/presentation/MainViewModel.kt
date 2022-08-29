package com.rustyrobot.shoppinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rustyrobot.shoppinglist.data.ShopLIstRepositoryImpl
import com.rustyrobot.shoppinglist.domain.DeleteItemUseCase
import com.rustyrobot.shoppinglist.domain.EditShopItemUseCase
import com.rustyrobot.shoppinglist.domain.GetShopListUseCase
import com.rustyrobot.shoppinglist.domain.ShopItem

class MainViewModel : ViewModel() {
    private val repository = ShopLIstRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteItemUseCase = DeleteItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = MutableLiveData<List<ShopItem>>()

    fun getShopList() {
        val list = getShopListUseCase.getShopList()
        shopList.value = list
    }

    fun deleteItem(shopItem: ShopItem) {
        deleteItemUseCase.deleteItem(shopItem)
        getShopList()
    }

    fun editShopItem(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
    }

}