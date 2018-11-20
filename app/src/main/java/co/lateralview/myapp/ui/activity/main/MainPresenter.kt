package co.lateralview.myapp.ui.activity.main

class MainPresenter(
    private val view: MainContract.View
) : MainContract.Presenter {

    override fun init() {
        view.initToolbar(true, view.getToolbarTitle())
    }

    override fun onToolbarBackPressed() {
        view.finishScreen()
    }
}
