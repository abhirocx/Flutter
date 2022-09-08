package com.np.namasteyoga.interfaces

interface OnClickItem<T> {
    fun onClick(position:Int, t:T?=null)
}