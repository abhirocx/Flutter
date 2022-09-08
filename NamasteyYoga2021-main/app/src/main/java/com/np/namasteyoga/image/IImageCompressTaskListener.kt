package com.np.namasteyoga.image

import java.io.File

interface IImageCompressTaskListener {
    fun onComplete(compressed: List<File>)
    fun onError(error: Throwable)
}