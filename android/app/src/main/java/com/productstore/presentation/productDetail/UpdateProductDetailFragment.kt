package com.productstore.presentation.productDetail

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.productstore.R
import com.productstore.data.localDataSource.ProductEntity
import com.productstore.databinding.FragmentUpdateProductDetailBinding
import com.productstore.domain.ResponseStatus
import com.productstore.domain.UpdateType
import com.productstore.presentation.productList.ProductViewModel
import java.io.InputStream


class UpdateProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentUpdateProductDetailBinding
    private val viewModel: ProductViewModel by activityViewModels()
    private val args: UpdateProductDetailFragmentArgs by navArgs()

    private lateinit var productEntity: ProductEntity
    private var updateType: UpdateType = UpdateType.ADD_PRODUCT
    private var imageData: String = ""
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val iStream: InputStream? = requireActivity().contentResolver
                    .openInputStream(uri)
                binding.productImage.setImageBitmap(BitmapFactory.decodeStream(iStream))
                imageData = viewModel.getBytes(iStream).toString(Charsets.UTF_8)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateType = args.updateType
        productEntity = args.productEntity
        setupUI()
        observeChanges()
    }

    private fun setupUI() = with(binding) {
        lifecycleOwner = viewLifecycleOwner
        productEntity = this@UpdateProductDetailFragment.productEntity
        if (updateType == UpdateType.ADD_PRODUCT) {
            productTitle.text = getString(R.string.action_add_product)
            productChooseImage.text = getString(R.string.action_add_image)
            productUpdateButton.text = getString(R.string.action_add)
        } else {
            productTitle.text = getString(R.string.action_update_product)
            productChooseImage.text = getString(R.string.action_choose_image)
            productUpdateButton.text = getString(R.string.action_update)
        }

        binding.productChooseImage.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly).build()
            )
        }

        productUpdateButton.setOnClickListener {
            val newProduct = ProductEntity(
                -1,
                productName.text.toString(),
                productDescription.text.toString(),
                imageData,
                productPrice.text.toString().replace("S$", "").trim().toDouble(),
                productCategory.text.toString()
            )
            updateProduct(newProduct)
        }
    }

    private fun observeChanges() {
        viewModel.resetStatus()
        viewModel.updateStatus.observe(viewLifecycleOwner) {
            when (it) {
                ResponseStatus.Loading -> {
                    binding.productUpdateButton.isEnabled = false
                    binding.progressBar.isVisible = true
                }

                is ResponseStatus.Success<*> -> {
                    binding.progressBar.isVisible = false
                    findNavController().popBackStack()
                }

                is ResponseStatus.Error -> {
                    binding.productUpdateButton.isEnabled = true
                    binding.progressBar.isVisible = false
                    showSnackBar(it.message)
                }

                else -> {

                }
            }
        }
    }

    private fun updateProduct(newProduct: ProductEntity) {
        if (viewModel.validateData(productEntity, newProduct, updateType)) {
            binding.productUpdateButton.isEnabled = false
            viewModel.updateProduct(newProduct)
        } else {
            showSnackBar(
                if (updateType == UpdateType.ADD_PRODUCT)
                    getString(R.string.all_fields_mandatory)
                else
                    getString(R.string.no_change_in_data)
            )
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.productUpdateButton.rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}
