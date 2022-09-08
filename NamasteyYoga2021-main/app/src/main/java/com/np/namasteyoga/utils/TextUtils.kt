package com.np.namasteyoga.utils

import android.widget.EditText
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString

object TextUtils {

    fun errorEmptyText(text: EditText, fieldName: String) {
        text.requestFocus()
        text.error = text.context.getString(R.string.please_provide, fieldName)

    }

    fun errorValidText(text: EditText, fieldName: String) {
        text.requestFocus()
        text.error = text.context.getString(R.string.valid_msg, fieldName)
    }

    fun passwordValidationMsg(text: EditText) {
        text.requestFocus()
        text.error = text.context.getString(R.string.pwd_validation_msg)
    }
    fun capacityValidationMsg(text: EditText) {
        text.requestFocus()
        text.error = text.context.getString(R.string.sitting_capacity_validation_msg)
    }
    fun lengthValidation(text: EditText,fieldName: String) {
        text.requestFocus()
        text.error = text.context.getString(R.string.length_validation,fieldName)
    }


    fun errorNotMatchText(text: EditText, fieldName: String, fieldName2: String) {
        text.requestFocus()
        text.error = text.context.getString(R.string.not_match, fieldName, fieldName2)
    }


    fun firstLetterUpper(str: String): String {

        if (str.startsWith(CommonString.COMMA)) {
            return CommonString.Empty
        }
        return str.substring(CommonInt.Zero, CommonInt.One).toUpperCase() + str.substring(CommonInt.One).toLowerCase()


    }

}