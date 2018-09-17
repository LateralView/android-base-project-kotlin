package co.lateralview.myapp.ui.activity.base

import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import co.lateralview.myapp.R

abstract class BaseActivity : AppCompatActivity() {

    private var localBackEnabled: Boolean = false

    protected fun initializeToolbar(backEnabled: Boolean, title: String?) {
        localBackEnabled = backEnabled
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val customTitle = findViewById<TextView>(R.id.toolbar_title)

        if (toolbar != null) {
            setSupportActionBar(toolbar)

            if (title != null && !title.isEmpty()) {
                if (customTitle != null) {
                    customTitle.text = title
                    supportActionBar?.title = ""
                } else {
                    supportActionBar?.title = title
                }
            } else {
                supportActionBar?.title = ""
                if (customTitle != null) {
                    customTitle.text = ""
                }
            }

            supportActionBar?.setDisplayHomeAsUpEnabled(localBackEnabled)
            supportActionBar?.setHomeButtonEnabled(localBackEnabled)
            if (localBackEnabled) {
                toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!localBackEnabled) {
            return false
        }
        when (item.itemId) {
            android.R.id.home -> onToolbarBackPressed()
        }
        return true
    }

    protected open fun onToolbarBackPressed() {
        // childs can override this method to handle the click event on the back arrow of the toolbar
    }
}
