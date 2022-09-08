package com.rustyrobot.shoppinglist.data

import androidx.lifecycle.MutableLiveData
import com.rustyrobot.shoppinglist.domain.ShopItem
import com.rustyrobot.shoppinglist.domain.ShopListRepository
import kotlin.random.Random

object ShopLIstRepositoryImpl : ShopListRepository {

    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private val shopList = sortedSetOf<ShopItem>({ o1, o2 -> o1.id.compareTo(o2.id) })
    private var autoIncrementId = 0

    init {
        for (i in 0 until 100) {
            val item = ShopItem("Name $i", i, Random.nextBoolean())
            addShopItem(item)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun deleteItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int) = shopList.find { it.id == shopItemId }
        ?: throw RuntimeException("Element with id $shopItemId not found")


    override fun getShopList() = shopListLD

    private fun updateList() {
        shopListLD.value = shopList.toList()
    }

}