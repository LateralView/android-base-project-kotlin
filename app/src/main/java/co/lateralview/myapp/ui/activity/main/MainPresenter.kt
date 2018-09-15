package co.lateralview.myapp.ui.activity.main

import javax.inject.Inject

class MainPresenter @Inject constructor(private val view: MainContract.View) : MainContract.Presenter {

    override fun init() {
        view.initToolbar(true, view.getToolbarTitle())
    }

    override fun onToolbarBackPressed() {
        view.finishScreen()
    }
}
