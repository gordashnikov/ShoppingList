package com.rustyrobot.shoppinglist.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rustyrobot.shoppinglist.R
import com.rustyrobot.shoppinglist.databinding.FragmentShopItemBinding
import com.rustyrobot.shoppinglist.domain.ShopItem.Companion.UNDEFINED_ID

class ShopItemFragment : Fragment() {

    private var _binding: FragmentShopItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ShopItemViewModel
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        val view = binding.root

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        addTextChangeListener()
        launchRightMode()
        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            binding.tilCount.error = message
        }

        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            binding.tilName.error = message
        }

        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun addTextChangeListener() {
        binding.edName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.edCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) throw RuntimeException("Parameter screen mode is absent")
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) throw RuntimeException("Unknown screen mode: $mode")
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Parameter shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, UNDEFINED_ID)
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            binding.edName.setText(it.name)
            binding.edCount.setText(it.count.toString())
        }
        binding.buttonSave.setOnClickListener {
            viewModel.editShopItem(
                binding.edName.text?.toString(),
                binding.edCount.text?.toString()
            )
        }

    }

    private fun launchAddMode() {
        binding.buttonSave.setOnClickListener {
            viewModel.addShopItem(binding.edName.text?.toString(), binding.edCount.text?.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem() = ShopItemFragment().apply {
            arguments = Bundle().apply {
                putString(SCREEN_MODE, MODE_ADD)
            }
        }

        fun newInstanceEditItem(shopItemId: Int) = ShopItemFragment().apply {
            arguments = Bundle().apply {
                putString(SCREEN_MODE, MODE_EDIT)
                putInt(SHOP_ITEM_ID, shopItemId)
            }
        }
    }
}