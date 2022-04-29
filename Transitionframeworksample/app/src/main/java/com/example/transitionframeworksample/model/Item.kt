package com.example.transitionframeworksample.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
class Item(@DrawableRes val imageRes: Int, @StringRes val stringRes: Int) : Parcelable