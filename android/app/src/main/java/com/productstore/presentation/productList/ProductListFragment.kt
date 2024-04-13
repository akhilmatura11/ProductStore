package com.productstore.presentation.productList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.productstore.R
import com.productstore.data.localDataSource.ProductEntity
import com.productstore.databinding.FragmentProductListBinding
import com.productstore.domain.ResponseStatus
import com.productstore.domain.UpdateType
import dagger.hilt.android.AndroidEntryPoint


const val ARG_PRODUCT_ENTITY = "productEntity"
const val ARG_UPDATE_TYPE = "updateType"

@AndroidEntryPoint
class ProductListFragment : Fragment(), ProductClickListener {
    private lateinit var binding: FragmentProductListBinding
    private val viewModel: ProductViewModel by activityViewModels()
    private var productList = mutableListOf<ProductEntity>()
    private val productGridAdapter by lazy {
        ProductGridAdapter(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeChanges()
    }

    private fun setupUI() {
        binding.productGridView.adapter = productGridAdapter
        binding.productAddButton.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable(ARG_PRODUCT_ENTITY, ProductEntity())
                putParcelable(ARG_UPDATE_TYPE, UpdateType.ADD_PRODUCT)
            }
            findNavController().navigate(
                R.id.action_productListFragment_to_addProductFragments,
                bundle
            )
        }
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(string: String?): Boolean {
                searchList(string.orEmpty().trim())
                return true
            }

        })
        binding.sortIcon.setOnClickListener {
            setupSpinner(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun observeChanges() {
        viewModel.fetchProducts()
        viewModel.responseStatus.observe(viewLifecycleOwner) {
            when (it) {
                ResponseStatus.Loading -> binding.progressBar.isVisible = true
                is ResponseStatus.Success<*> -> {
                    binding.progressBar.isVisible = false
                    if (it.data is List<*>) {
                        productList.clear()
                        productList.addAll(it.data as? List<ProductEntity> ?: emptyList())
                        if (productList.isNotEmpty()) {
                            productGridAdapter.updateProducts(
                                productList
                            )
                        } else {
                            binding.errorText.text = getString(R.string.empty_product_list)
                            binding.errorText.isVisible = true
                        }
                    }
                }

                is ResponseStatus.Error -> {
                    binding.progressBar.isVisible = false
                    binding.errorText.text = it.message
                    binding.errorText.isVisible = true
                }

                else -> {

                }
            }
        }
    }

    private fun searchList(searchString: String) {
        val list = productList.filter { it.name.contains(searchString, true) }
        if (list.isEmpty()) {
            productGridAdapter.updateProducts(emptyList())
        } else {
            productGridAdapter.updateProducts(list)
        }
    }


    private fun setupSpinner(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.sort_menu, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            viewModel.sortList(productList, menuItem.title.toString())
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    override fun onProductClicked(productEntity: ProductEntity) {
        val bundle = Bundle().apply {
            putParcelable(ARG_PRODUCT_ENTITY, productEntity)
        }
        findNavController().navigate(
            R.id.action_productListFragment_to_productDetailFragments,
            bundle
        )
    }
}
