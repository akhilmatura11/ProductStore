package com.productstore.presentation.productList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.productstore.R
import com.productstore.data.localDataSource.ProductEntity

class ProductGridAdapter(
    val context: Context, private val listener: ProductClickListener
) : BaseAdapter() {
    private val productList: MutableList<ProductEntity> = mutableListOf()
    private var layoutInflater: LayoutInflater? = null
    private lateinit var productName: TextView
    private lateinit var productDescription: TextView
    private lateinit var productPrice: TextView

    override fun getCount(): Int {
        return productList.size
    }

    override fun getItem(position: Int): ProductEntity {
        return productList[position]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        val convertView = view ?: layoutInflater!!.inflate(R.layout.item_product, null)

        productName = convertView.findViewById(R.id.item_name)
        productDescription = convertView.findViewById(R.id.item_description)
        productPrice = convertView.findViewById(R.id.item_price)

        productName.text = productList[position].name
        productDescription.text = productList[position].description
        productPrice.text = productList[position].price.toString()

        convertView.findViewById<CardView>(R.id.item_card).setOnClickListener {
            listener.onProductClicked(productList[position])
        }

        return convertView
    }

    fun updateProducts(list: List<ProductEntity>) {
        productList.clear()
        productList.addAll(list)
        notifyDataSetChanged()
    }
}

interface ProductClickListener {
    fun onProductClicked(productEntity: ProductEntity)
}
