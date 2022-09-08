package com.np.namasteyoga.utils

import android.text.InputFilter
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.np.namasteyoga.R
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}


fun View.manageEditText(type: Int, length: Int, string: String) {
    find<EditText>(R.id.editText).run {
        inputType = type
        filters = arrayOf(InputFilter.LengthFilter(length))
        hint = string
    }
}
fun View.manageAccountEditText(type: Int, length: Int, string: String,@DrawableRes res: Int?=null) {
    find<TextView>(R.id.hint).run {
        text = string
        if (res!=null)
        setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
    }
    find<EditText>(R.id.editText).run {
        inputType = type
        filters = arrayOf(InputFilter.LengthFilter(length))
        hint = string
    }
}

fun View.setColoredButtonText(txt: String) {
    find<TextView>(R.id.buttonColorLayoutTxt).text = txt
}

fun View.getMyEditText(): EditText = find(R.id.editText)
fun View.getMyImageView(): ImageView = find(R.id.supportImg)

fun View.setImage(@DrawableRes id: Int) = find<ImageView>(R.id.supportImg).setImageResource(id)

fun View.updatePasswordVisibility() {
    val editText: EditText = find(R.id.editText)
    val supportImg: ImageView = find(R.id.supportImg)
    if (editText.transformationMethod is PasswordTransformationMethod) {
        editText.transformationMethod = null
        supportImg.setImageResource(R.drawable.ic_eye_hide)
    } else {
        editText.transformationMethod = PasswordTransformationMethod()
        supportImg.setImageResource(R.drawable.ic_eye)
    }
    editText.setSelection(editText.length())
}

fun EditText.setReadOnly(value: Boolean, inputType: Int = InputType.TYPE_NULL) {
    isFocusable = !value
    isFocusableInTouchMode = !value
    this.inputType = inputType
}

fun View.setData(msg: String, @DrawableRes id: Int) {
    find<TextView>(R.id.textView).run {
        text = msg
        setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
    }
}
fun View.setMyAccountData(hintString: String,msg: String, @DrawableRes id: Int=0) {
    find<TextView>(R.id.hint).run {
        text = hintString
        setCompoundDrawablesWithIntrinsicBounds(0, 0, id, 0)
    }
    find<TextView>(R.id.text).text = msg
}
fun View.setMyAccountDataEdit(hintString: String,msg: String, @DrawableRes id: Int=0) {
    find<TextView>(R.id.hint).run {
        text = hintString
        setCompoundDrawablesWithIntrinsicBounds(0, 0, id, 0)
    }
    find<TextView>(R.id.editText).text = msg
}
fun View.setMyAccountTextColor(@ColorRes id: Int) {
    find<TextView>(R.id.editText).textColor = id
}