package com.np.namasteyoga.ui.ayushMerchandise.detailScreen.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.np.namasteyoga.datasource.response.ayushSubCategoryModels.ProductImage
import com.np.namasteyoga.ui.ayushMerchandise.detailScreen.fragments.ProductDetailViewPagerContentFragment

class ProductDetailScreenSlidePagerAdapter(fragment: FragmentActivity, private val list: ArrayList<ProductImage>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        return ProductDetailViewPagerContentFragment.newInstance(list[position])
    }

}