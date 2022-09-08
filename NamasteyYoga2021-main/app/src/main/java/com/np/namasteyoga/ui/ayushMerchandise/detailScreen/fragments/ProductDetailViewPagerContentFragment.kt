package com.np.namasteyoga.ui.ayushMerchandise.detailScreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.response.ayushSubCategoryModels.ProductImage
import kotlinx.android.synthetic.main.view_pager_content.*

class ProductDetailViewPagerContentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.product_detail_item_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(DATA_SHARE) }?.apply {
            (getParcelable(DATA_SHARE) as ProductImage?)?.run {
                    image.load(product_image?: CommonString.Empty) {
                        crossfade(true)
                        placeholder(R.drawable.ic_place_holder)
                    }
            }
        }
    }




    companion object {
        private const val DATA_SHARE = "data_share"
        fun newInstance(productImage: ProductImage):ProductDetailViewPagerContentFragment{
            val instance = ProductDetailViewPagerContentFragment()
            instance.arguments = Bundle().apply {
                putParcelable(DATA_SHARE,productImage)
            }
           return instance
        }
    }
}
