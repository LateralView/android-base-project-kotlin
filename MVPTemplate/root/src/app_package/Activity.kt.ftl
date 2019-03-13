package ${packageName}

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlinx.android.synthetic.main.${activityLayout}.*
<#if applicationPackage??>
import ${applicationPackage}.R
</#if>

class ${className}Activity : DaggerAppCompatActivity(), ${className}Contract.View {

    companion object {
        fun newInstance(context: Context): Intent = Intent(context, ${className}Activity::class.java)
    }

    @Inject lateinit var presenter: ${className}Contract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.${activityLayout})
    }
}
