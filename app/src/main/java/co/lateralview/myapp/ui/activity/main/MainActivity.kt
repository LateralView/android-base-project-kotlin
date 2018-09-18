package co.lateralview.myapp.ui.activity.main

import android.os.Bundle
import co.lateralview.myapp.R
import co.lateralview.myapp.application.MyAppApplication
import co.lateralview.myapp.ui.activity.base.BaseActivity
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View {

    @Inject lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependencies()
        presenter.init()
    }

    override fun initToolbar(backEnabled: Boolean, toolbarTitle: String) {
        initializeToolbar(backEnabled, toolbarTitle)
    }

    override fun getToolbarTitle(): String = getString(R.string.toolbar_title)

    override fun onToolbarBackPressed() {
        presenter.onToolbarBackPressed()
    }

    override fun finishScreen() {
        finish()
    }

    private fun injectDependencies() {
        (application as MyAppApplication).appComponent
            .plus(MainModule(this))
            .inject(this)
    }
}
