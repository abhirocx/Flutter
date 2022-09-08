package com.np.namasteyoga.interfaces


interface ClickItem<T> {
    fun onClickPosition(position:Int, t:T?=null)
}