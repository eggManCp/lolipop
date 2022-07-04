package com.lolipop.reader.extensions

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T : ViewBinding> FragmentActivity.viewBinding(
    crossinline bind: (View) -> T = {
        T::class.java.getMethod("bind", View::class.java).invoke(null, it) as T
    },
): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) {
    val getContentView: FragmentActivity.() -> View = {
        checkNotNull(findViewById<ViewGroup>(android.R.id.content).getChildAt(0)) {
            "Call setContentView or Use Activity's secondary constructor passing layout res id."
        }
    }
    return@lazy bind(getContentView())
}

inline fun <reified T : ViewBinding> Fragment.viewBinding(
    crossinline bind: (View) -> T = {
        T::class.java.getMethod("bind", View::class.java).invoke(null, it) as T
    },
): ReadOnlyProperty<Fragment, T> = object : ReadOnlyProperty<Fragment, T> {
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        (requireView().getTag(property.name.hashCode()) as? T)?.let { return it }
        return bind(requireView()).also {
            requireView().setTag(property.name.hashCode(), it)
        }
    }
}