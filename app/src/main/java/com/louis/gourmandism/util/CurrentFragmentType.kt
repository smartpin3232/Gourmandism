package com.louis.gourmandism.util

import android.provider.Settings.Global.getString
import com.louis.gourmandism.R

enum class CurrentFragmentType(val value: String) {
    HOME("HOME"),
    SEARCH("SEARCH"),
    EVENT("EVENT"),
    WISH("WISH"),
    PROFILE("PROFILE"),
    DETAIL("DETAIL"),
    FRIEND("FRIEND"),
    LOTTERY("LOTTERY"),
    WISH_DETAIL("WISH DETAIL"),
    COMMENT("COMMENT"),
}
