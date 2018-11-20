package co.lateralview.myapp.ui.util.ui

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

object UIUtils {
    fun getColor(context: Context, @ColorRes colorRes: Int): Int = ContextCompat.getColor(context, colorRes)

    fun getDrawable(context: Context, @DrawableRes drawableRes: Int): Drawable =
        ContextCompat.getDrawable(context, drawableRes)!!

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }
}