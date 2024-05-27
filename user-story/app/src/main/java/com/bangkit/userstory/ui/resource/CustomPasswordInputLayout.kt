package com.bangkit.userstory.ui.resource

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.bangkit.userstory.R
import com.google.android.material.textfield.TextInputLayout

class CustomPasswordInputLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputLayout(context, attrs), View.OnTouchListener {

    init {
        post {
            editText?.setOnTouchListener(this)

            editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    // Do nothing.
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.isNotEmpty()) {
                        if (s.length < 8) {
                            setError(context.getString(R.string.error_password_less_8))
                        } else {
                            setError(null)
                        }
                    } else {
                        setError(null)
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    // Do nothing.
                }
            })
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        editText?.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val editTextRight = editText?.right ?: 0
        }
        return false
    }
}
