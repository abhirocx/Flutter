package com.np.namasteyoga.ui.eventModule.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.pojo.Event
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.modules.NetworkModule
import com.np.namasteyoga.modules.SharedPreference
import com.np.namasteyoga.utils.*
import com.np.namasteyoga.utils.UIUtils.getDistanceFormat
import com.np.namasteyoga.utils.UIUtils.navigateLocation
import com.np.namasteyoga.utils.UIUtils.shareData
import com.np.namasteyoga.utils.Util.getDecryptData
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.my_event_past_details_layout.*
import kotlinx.android.synthetic.main.past_event_details_layout.etAddress
import kotlinx.android.synthetic.main.past_event_details_layout.eventDescription
import kotlinx.android.synthetic.main.past_event_details_layout.lessDetails
import kotlinx.android.synthetic.main.past_event_details_layout.moreDetails
import kotlinx.android.synthetic.main.past_event_details_layout.navigation
import kotlinx.android.synthetic.main.past_event_details_layout.share
import kotlinx.android.synthetic.main.past_event_details_layout.tvCalender
import kotlinx.android.synthetic.main.past_event_details_layout.tvDiscription
import kotlinx.android.synthetic.main.past_event_details_layout.tvDistance
import kotlinx.android.synthetic.main.past_event_details_layout.tvEventName
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import java.io.File
import java.util.HashMap
import java.util.regex.Matcher
import java.util.regex.Pattern


class MyEventPastDialogFragment : BottomSheetDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_event_past_details_layout, container, false)
    }

    private val list = ArrayList<Image>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.clear()
        listFile.clear()
        progressDialog.setView(R.layout.progress)
        progressDialog.setCancelable(false)
        dialogg = progressDialog.create()

        youtubeURL.manageEditText(
            InputType.TYPE_TEXT_VARIATION_URI,
            CommonInt.AddressLength,
            getString(R.string.youtube_video_url)
        )
        youtubeURL.getMyEditText().setText(CommonString.Empty)

        lessDetails.paintFlags = lessDetails.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        moreDetails.paintFlags = moreDetails.paintFlags or Paint.UNDERLINE_TEXT_FLAG


        sView.isHorizontalScrollBarEnabled = false

    }

    private val keys = arrayOf("image_one", "image_two", "image_three", "image_four")

    private fun createMultipartBody(filePath: String, param: String): MultipartBody.Part {
        val file = File(filePath)
        val requestBody: RequestBody = createRequestBody(file)
        return MultipartBody.Part.createFormData(param, file.name, requestBody)
    }

    private fun createMultipartBody(file: File, param: String): MultipartBody.Part {
        val requestBody: RequestBody = createRequestBody(file)
        return MultipartBody.Part.createFormData(param, file.name, requestBody)
    }

    private fun createRequestBody(file: File): RequestBody {
        return file.asRequestBody(getString(R.string.image_type).toMediaTypeOrNull())
        //  return RequestBody.create(MediaType.parse(getString(R.string.image_type)), file)

    }

    private fun createRequestBodyViaString(string: String): RequestBody {
        return string.toRequestBody(getString(R.string.text_type).toMediaTypeOrNull())
        //return RequestBody.create(MediaType.parse(getString(R.string.text_type)), string)
    }


    private fun uploadMedia(file: File?, param: String): MultipartBody.Part? {
        return if (file == null) {
            return null
        } else
            try {
                createMultipartBody(file, param)
            } catch (e: java.lang.Exception) {
                null
            }
    }

    private fun getYoutubeIdFromUrl(url: String): String {
        var s = CommonString.Empty
        return try {
            val sFirstSplit = url.split(C.YOU_TUBE_SPIT)
            if (sFirstSplit.size > CommonInt.One) {
                val temp = sFirstSplit[CommonInt.One].split(CommonString.AND)
                s = temp[CommonInt.Zero]
            }
            s
        } catch (e: Exception) {
            s
        }


    }

    private fun extractVideoId(ytUrl: String): String? {
        var videoId: String? = null
        val regex =
            "^((?:https?:)?//)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be|youtube-nocookie.com))(/(?:[\\w\\-]+\\?v=|feature=|watch\\?|e/|embed/|v/)?)([\\w\\-]+)(\\S+)?\$"
        val pattern: Pattern = Pattern.compile(
            regex,
            Pattern.CASE_INSENSITIVE
        )

        val matcher: Matcher = pattern.matcher(ytUrl)
        if (matcher.matches()) {
            videoId = matcher.group(5)
        }

        return videoId
    }

    @SuppressLint("SetTextI18n")
    fun setData(event: Event?) {
        event?.let { item ->
            tvEventName.text = item.eventName ?: CommonString.NA
            val number3digits = if (item.nearest == CommonInt.One) {
                tvDistance.show()
                item.nearest_distance?.getDistanceFormat()
            } else {
                tvDistance.hide()
                CommonString.Empty
            }
            tvDistance.text = "${
                getString(
                    R.string.s_km_away,
                    number3digits.toString()
                )
            } ${item.cityName ?: CommonString.Empty}"

            val sDate = if (item.startTime.isNullOrEmpty()) {
                CommonString.NA
            } else {
                val date =
                    DateUtils.getDateToString(item.startTime ?: CommonString.Empty)?.time ?: 0
                DateUtils.getDataString(
                    getString(R.string.hh_mm_a_dd_mmm_yyyy),
                    date
                )
            }

            val eDate = if (item.endTime.isNullOrEmpty()) {
                CommonString.NA
            } else {
                val date = DateUtils.getDateToString(item.endTime ?: CommonString.Empty)?.time ?: 0
                DateUtils.getDataString(
                    getString(R.string.hh_mm_a_dd_mmm_yyyy),
                    date
                )
            }
            tvCalender.text = "$sDate ${getString(R.string.to)} $eDate"
            etAddress.text = item.address ?: CommonString.NA
            tvDiscription.text = item.short_description ?: CommonString.NA
            loadImages()

            lessDetails.setOnClickListener {
                moreDetails.show()
                eventDescription.hide()
            }
            moreDetails.setOnClickListener {
                moreDetails.hide()
                eventDescription.show()
            }

            navigation.onClick {
                navigation.context.navigateLocation(item.lat.toString() + CommonString.COMMA + item.lng.toString())
            }


            share.onClick {
                val data = share.context.getString(
                    R.string.share_event_data,
                    item.eventName,
                    item.contactNo?.getDecryptData() ?: CommonString.NA,
                    item.email?.getDecryptData() ?: CommonString.NA,
                    item.address
                )
                share.context.shareData(data)
            }
            tvSubmit.setOnClickListener {
//                if (listFile.isEmpty()) {
//                    toast(R.string.please_select_at_least_one_photo)
//                    return@setOnClickListener
//                }
                val url = youtubeURL.getMyEditText().text.toString()
                val videoId = if (url.isNotEmpty()) {
                    val videoIdTemp = getYoutubeIdFromUrl(url)
//                    val videoIdTemp = extractVideoId(url)
                    if (videoIdTemp.isEmpty()) {
                        TextUtils.errorValidText(
                            youtubeURL.getMyEditText(),
                            getString(R.string.youtube_video_url)
                        )
                        return@setOnClickListener
                    }
                    videoIdTemp
                } else {
                    CommonString.Empty
                }
                Logger.Debug(videoId)
//                uploadData(item.id?.toString() ?: CommonString.Empty, videoId)

            }

        }
    }

    private fun loadImages() {
        images.removeAllViews()
        val inflater = LayoutInflater.from(context)
        if (list.size < CommonInt.Four) {
            val layout =
                inflater.inflate(R.layout.past_my_event_images_item_layout, null, false) as View
            layout.find<ImageView>(R.id.close).hide()
            layout.setOnClickListener {
                imagePicker()
            }
            images.addView(layout)
        }
        for (i in 0 until list.size) {
            val layout =
                inflater.inflate(R.layout.past_my_event_images_item_layout, null, false) as View
            val image = layout.find<ImageView>(R.id.image)
            val close = layout.find<ImageView>(R.id.close)
            close.tag = list[i]
            val size = getImageSize(list[i])
            Logger.Debug(size.toString())
            close.setOnClickListener {
                list.remove(it.tag as Image)
                loadImages()
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                image.load(list[i].uri) {
                    crossfade(true)
                    placeholder(R.drawable.ic_place_holder)
                    transformations(RoundedCornersTransformation(18f))
                }

            } else {
                image.load(list[i].uri) {
                    crossfade(true)
                    placeholder(R.drawable.ic_place_holder)
                    transformations(RoundedCornersTransformation(18f))
                }


            }
            images.addView(layout)
        }
    }

    private val listFile = ArrayList<File>()
    private fun compressImage() {
        listFile.clear()
        list.forEach { image ->
            image.path.let { imageFile ->
                lifecycleScope.launch {
                    val compressedImage = Compressor.compress(requireContext(), File(imageFile))
                    Logger.Debug((compressedImage.length() / CommonInt.KB).toString())
                    listFile.add(compressedImage)
                }
            }
        }
    }


    private fun getImageSize(uri: Image): Long {
        val file = File(uri.path)
        return (file.length() / CommonInt.KB)// returns size in bytes
    }

    private fun imagePicker() {
        ImagePicker.with(this)
            .setFolderMode(true)
            .setFolderTitle(getString(R.string.album))
            .setRootDirectoryName(Config.ROOT_DIR_DCIM)
            .setDirectoryName(getString(R.string.image_picker))
            .setMultipleMode(true)
            .setShowNumberIndicator(true)
            .setMaxSize(4)
            .setLimitMessage(getString(R.string.you_can_select_up_to_images))
            .setSelectedImages(list)
            .setRequestCode(100)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 100)) {
            val images = ImagePicker.getImages(data)
            list.clear()
            list.addAll(images)
            loadImages()
            compressImage()
        }
        super.onActivityResult(requestCode, resultCode, data)

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                mBehavior = (this@MyEventPastDialogFragment.dialog as BottomSheetDialog).behavior
                mBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }
    }

    private var mBehavior: BottomSheetBehavior<FrameLayout>? = null

    override fun onResume() {
        super.onResume()
        mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private val apiServiceWithHeader: RESTApi by inject(named(NetworkModule.RETROFIT_API))
    private val sharedPreference: SharedPreferences by inject(named(SharedPreference.DataPrefs))
    private fun uploadData(eventId: String, videoId: String?) {
        val hashMap = HashMap<String, String>()
        hashMap["events_id"] = eventId
        hashMap["videoId"] = eventId
        hashMap[keys[CommonInt.Zero]] = CommonString.Empty
        hashMap[keys[CommonInt.One]] = CommonString.Empty
        hashMap[keys[CommonInt.Two]] = CommonString.Empty
        hashMap[keys[CommonInt.Three]] = CommonString.Empty
        val json = Gson().toJson(hashMap)
        val header = Constants.getMD5EncryptedStringWithToken(
            json,
            SharedPreferencesUtils.getUserDetails(sharedPreference)?.token ?: CommonString.Empty
        )
//        val headers = HashMap<String, String>()
//        headers[C.TOKEN_KEY] = "${C.TOKEN_VALUE} ${SharedPreferencesUtils.getUserDetails(sharedPreference)?.token ?: CommonString.Empty}"
        if (NetworkUtil.isInternetAvailable(requireContext())) {
            isShowDialog(true)
            val file1 = if (listFile.size > CommonInt.Zero) {
                createMultipartBody(listFile[CommonInt.Zero], keys[CommonInt.Zero])
            } else {
                null
            }
            val file2 = if (listFile.size > CommonInt.One) {
                createMultipartBody(listFile[CommonInt.One], keys[CommonInt.One])
            } else {
                null
            }
            val file3 = if (listFile.size > CommonInt.Two) {
                createMultipartBody(listFile[CommonInt.Two], keys[CommonInt.Two])
            } else {
                null
            }
            val file4 = if (listFile.size > CommonInt.Three) {
                createMultipartBody(listFile[CommonInt.Three], keys[CommonInt.Three])
            } else {
                null
            }

            val apiDisposal = apiServiceWithHeader.uploadMedia(
                createRequestBodyViaString(eventId),
                createRequestBodyViaString(videoId ?: CommonString.Empty),
                file1,
                file2,
                file3,
                file4,
                header
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isShowDialog(false)
                    if (it == null) {
                        toast(R.string.something_went_wrong)
                        return@subscribe
                    }

                    try {
                        val status = (it.status ?: C.NP_STATUS_FAIL)
                        if (status == C.NP_INVALID_TOKEN || status == C.NP_TOKEN_EXPIRED || status == C.NP_TOKEN_NOT_FOUND) {
                            SharedPreferencesUtils.setUserDetails(sharedPreference, null)
                            (this@MyEventPastDialogFragment).dismiss()
                            return@subscribe
                        }
                        if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                            toast(it.message ?: getString(R.string.something_went_wrong))
                            (this@MyEventPastDialogFragment).dismiss()
                        } else {
                            toast(it.message ?: getString(R.string.something_went_wrong))
                        }
                    } catch (e: Exception) {
                        toast(R.string.something_went_wrong)
                        if (C.DEBUG)
                            e.printStackTrace()
                    }
                }, {
                    isShowDialog(false)
                    Logger.Debug(it.message ?: getString(R.string.something_went_wrong))
                    toast(R.string.something_went_wrong)
                })
            compositeDisposable.add(apiDisposal)
        } else {
            toast(R.string.no_internet_connection)
        }
    }

    private var dialogg: Dialog? = null
    private val progressDialog by lazy { AlertDialog.Builder(requireContext()) }
    private val compositeDisposable by lazy { CompositeDisposable() }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        dialogg?.dismiss()
    }


    private fun isShowDialog(isShow: Boolean) {
        if (isShow) {
            dialogg?.show()
        } else
            dialogg?.dismiss()
    }


}