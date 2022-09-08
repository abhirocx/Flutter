package com.np.namasteyoga.image

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.np.namasteyoga.utils.Util

import java.io.File
import java.io.IOException
import java.util.ArrayList

class ImageCompressTask : Runnable {

    private var mContext: Context? = null
    private var originalPaths = ArrayList<String>()
    private val mHandler = Handler(Looper.getMainLooper())
    private val result = ArrayList<File>()
    private var mIImageCompressTaskListener: IImageCompressTaskListener? = null


    constructor(context: Context, path: String, compressTaskListener: IImageCompressTaskListener) {

        originalPaths.add(path)
        mContext = context

        mIImageCompressTaskListener = compressTaskListener
    }

    constructor(context: Context, paths: ArrayList<String>, compressTaskListener: IImageCompressTaskListener) {
        originalPaths = paths
        mContext = context
        mIImageCompressTaskListener = compressTaskListener
    }

    override fun run() {

        try {

            //Loop through all the given paths and collect the compressed file from Util.getCompressed(Context, String)
            for (path in originalPaths) {
                val file = Util.getCompressed(mContext, path)
                //add it!
                result.add(file)
            }
            //use Handler to post the result back to the main Thread
            mHandler.post {
                if (mIImageCompressTaskListener != null)
                    mIImageCompressTaskListener!!.onComplete(result)
            }
        } catch (ex: IOException) {
            //There was an error, report the error back through the callback
            mHandler.post {
                if (mIImageCompressTaskListener != null)
                    mIImageCompressTaskListener!!.onError(ex)
            }
        }

    }
}