<?xml version="1.0"?>
<recipe>

  <merge from="root/AndroidManifest.xml.ftl"
         to="${escapeXmlAttribute(manifestOut)}/AndroidManifest.xml" />

  <instantiate from="root/res/layout/activity_layout.xml"
               to="${escapeXmlAttribute(resOut)}/layout/${activityLayout}.xml" />

  <instantiate from="root/src/app_package/Contract.kt.ftl"
               to="${escapeXmlAttribute(srcOut)}/${className}Contract.kt" />
  <instantiate from="root/src/app_package/Activity.kt.ftl"
               to="${escapeXmlAttribute(srcOut)}/${className}Activity.kt" />
  <instantiate from="root/src/app_package/Presenter.kt.ftl"
               to="${escapeXmlAttribute(srcOut)}/${className}Presenter.kt" />
  <instantiate from="root/src/app_package/ActivityModule.kt.ftl"
               to="${escapeXmlAttribute(srcOut)}/${className}Module.kt" />

  <instantiate from="root/test/app_package/Test.kt.ftl"
               to="${escapeXmlAttribute(unitTestOut)}/${className}PresenterTest.kt" />

  <open file="${srcOut}/${className}Presenter.java"/>
  <open file="${srcOut}/${className}Contract.java"/>
  <open file="${srcOut}/${className}Activity.java"/>
  <open file="${srcOut}/${className}Module.java"/>

</recipe>
