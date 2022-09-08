package com.np.namasteyoga.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Location
import android.media.ExifInterface
import android.util.TypedValue
import com.google.android.gms.maps.model.LatLng
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.datasource.pojo.City
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


@SuppressLint("ConstantLocale")
object Util {

    private val SDF = SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault())


    @Throws(IOException::class)
    fun getCompressed(context: Context?, path: String): File {

        if (context == null)
            throw NullPointerException("Context must not be null.")
        var cacheDir = context.externalCacheDir
        if (cacheDir == null)
            cacheDir = context.cacheDir

        val rootDir = cacheDir!!.absolutePath + "/ImageCompressor"
        val root = File(rootDir)

        if (!root.exists())
            root.mkdirs()

        val bitmap =
            decodeImageFromFiles(path, /* your desired width*/300, /*your desired height*/ 300)

        val exif = ExifInterface(path)
        val rotation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val rotationInDegrees = exifToDegrees(rotation)
        val matrix = Matrix()
        matrix.postRotate(rotationInDegrees.toFloat())


        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        //create placeholder for the compressed image file
        val compressed = File(root, SDF.format(Date()) + ".jpg" /*Your desired format*/)

        //convert the decoded bitmap to stream
        val byteArrayOutputStream = ByteArrayOutputStream()

        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)

        val fileOutputStream = FileOutputStream(compressed)
        fileOutputStream.write(byteArrayOutputStream.toByteArray())
        fileOutputStream.flush()

        fileOutputStream.close()
        return compressed
    }

    fun getFileToBitmap(context: Context?, bitmap: Bitmap):File{
        if (context == null)
            throw NullPointerException("Context must not be null.")
        var cacheDir = context.externalCacheDir
        if (cacheDir == null)
            cacheDir = context.cacheDir

        val rootDir = cacheDir!!.absolutePath + "/ImageCompressor"
        val root = File(rootDir)

        if (!root.exists())
            root.mkdirs()
        val compressed = File(root, SDF.format(Date()) + ".jpg" /*Your desired format*/)

        //convert the decoded bitmap to stream
        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)

        val fileOutputStream = FileOutputStream(compressed)
        fileOutputStream.write(byteArrayOutputStream.toByteArray())
        fileOutputStream.flush()

        fileOutputStream.close()
        return compressed
    }

    private fun exifToDegrees(exifOrientation: Int): Int {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }


    private fun decodeImageFromFiles(path: String, width: Int, height: Int): Bitmap {
        val scaleOptions = BitmapFactory.Options()
        scaleOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, scaleOptions)
        var scale = 1
        while (scaleOptions.outWidth / scale / 2 >= width && scaleOptions.outHeight / scale / 2 >= height) {
            scale *= 2
        }
        // decode with the sample size
        val outOptions = BitmapFactory.Options()
        outOptions.inSampleSize = scale
        return BitmapFactory.decodeFile(path, outOptions)
    }

//    fun loadMarkers(result: Result): MarkerOptions {
//        var lt = 0.0
//        var lg = 0.0
//        result.geometry?.location?.run {
//            lat?.run{
//                lt = this
//            }
//            lng?.run{
//                lg = this
//            }
//        }
//        return MarkerOptions()
//                .position(LatLng(lt, lg))
//                .title(result.name)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//                .snippet(result.id)
//    }
//
//
//    fun textCreateMarker(result: Result):MarkerOptions{
//        var lt = 0.0
//        var lg = 0.0
//        result.geometry?.location?.run {
//            lat?.run{
//                lt = this
//            }
//            lng?.run{
//                lg = this
//            }
//        }
//        return MarkerOptions()
//            .position(LatLng(lt, lg))
//            .title(result.name)
//            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//            .snippet(null)
//    }
//
//
//    @SuppressLint("SimpleDateFormat")
//    fun getDate(context: Context, date: Date):String{
//       return SimpleDateFormat(context.getString(R.string.date_format)).format(date)
//    }
//    @SuppressLint("SimpleDateFormat")
//    fun getOnlineDate(context: Context, date: Date):String{
//       return SimpleDateFormat(context.getString(R.string.online_date_format)).format(date)
//    }
//    @SuppressLint("SimpleDateFormat")
//    fun getReviewDate(context: Context, date: Date):String{
//        return SimpleDateFormat(context.getString(R.string.review_date_format)).format(date)
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    fun getLogDate(context: Context, date: Date):String{
//        return SimpleDateFormat(context.getString(R.string.log_date_format)).format(date)
//    }

    fun dipToPx(c: Context, dipValue: Float): Int {
        val metrics = c.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics).toInt()
    }


    var date:Calendar?=null

    fun calculateStepToDistance(steps: Int):String{
        val km = (steps.toFloat() / CommonInt.StepOneKM)
        return String.format("%.1f", km)
    }

    fun formatKM(steps: Float):String{
        return String.format("%.2f", steps)
    }
    fun calculateStepToCal(steps: Int):Int{
        return steps / CommonInt.StepOneCal

    }
    fun distance(start: LatLng, end: LatLng): Float {
        return try {
            val location1 = Location("locationA")
            location1.latitude = start.latitude
            location1.longitude = start.longitude
            val location2 = Location("locationB")
            location2.latitude = end.latitude
            location2.longitude = end.longitude
            val meter =location1.distanceTo(location2)
            meter/1000f
        } catch (e: Exception) {
            0f
        }
    }
     fun String.getDecryptData():String{
        return ConstUtility.decrypt(this)
    }

    var citySelected:City?=null

    fun getHeaderPHP(token: String): Map<String, String>? {
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token"
        return headers
    }



}