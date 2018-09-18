package co.lateralview.myapp.ui.activity.main

interface MainContract {
    interface View {
        fun initToolbar(backEnabled: Boolean, toolbarTitle: String)
        fun getToolbarTitle(): String
        fun finishScreen()
    }

    interface Presenter {
        fun init()
        fun onToolbarBackPressed()
    }
}
