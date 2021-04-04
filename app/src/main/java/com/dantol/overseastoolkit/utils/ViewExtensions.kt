package com.dantol.overseastoolkit.utils

import android.view.View
import androidx.annotation.IntDef


/**
 * An annotation to shadow View.VISIBILITY IntDef (which is hidden)
 *
 * Values: View.VISIBLE, View.GONE, View.INVISIBLE
 * */
@Retention(AnnotationRetention.SOURCE)
@IntDef(View.VISIBLE, View.GONE, View.INVISIBLE)
annotation class Visibility
