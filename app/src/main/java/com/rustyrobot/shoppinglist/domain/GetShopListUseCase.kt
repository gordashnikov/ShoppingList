package com.rustyrobot.shoppinglist.domain

class GetShopListUseCase(private val shopListRepository: ShopListRepository) {

    fun getShopList() = shopListRepository.getShopList()
}