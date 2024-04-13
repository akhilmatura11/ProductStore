package com.productstore.presentation.productDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.productstore.R
import com.productstore.data.localDataSource.ProductEntity
import com.productstore.databinding.FragmentProductDetailBinding
import com.productstore.domain.UpdateType
import com.productstore.presentation.productList.ARG_PRODUCT_ENTITY
import com.productstore.presentation.productList.ARG_UPDATE_TYPE

class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding
    private val args: ProductDetailFragmentArgs by navArgs()

    private var productEntity: ProductEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productEntity = args.productEntity
        binding.lifecycleOwner = viewLifecycleOwner
        binding.productEntity = productEntity
        binding.productEditButton.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable(ARG_PRODUCT_ENTITY, productEntity)
                putParcelable(ARG_UPDATE_TYPE, UpdateType.EDIT_PRODUCT)
            }
            findNavController().navigate(
                R.id.action_productListFragment_to_updateProductDetailFragments,
                bundle
            )
        }
    }
}
