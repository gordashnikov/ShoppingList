package com.rustyrobot.shoppinglist.data

import com.rustyrobot.shoppinglist.domain.ShopItem
import com.rustyrobot.shoppinglist.domain.ShopListRepository

object ShopLIstRepositoryImpl : ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
    }

    override fun deleteItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int) = shopList.find { it.id == shopItemId }
        ?: throw RuntimeException("Element with id $shopItemId not found")


    override fun getShopList() = shopList.toList()
}