package co.lateralview.myapp.infrastructure.networking.gson

import kotlin.annotation.Retention
import kotlin.annotation.Target

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Exclude
