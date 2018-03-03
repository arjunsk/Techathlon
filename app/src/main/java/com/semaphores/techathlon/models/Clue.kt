package com.semaphores.gofind.Models

import android.support.annotation.DrawableRes

data class Clue(var isLocked: Boolean, var isImage: Boolean, var time: String, val description: String?, @DrawableRes val imageRes: Int?)
{
    // All clues are initially locked

    // Text clue
    constructor(time: String, description: String) : this(true, false, time, description, null)

    // Image clue
    constructor(time: String, imageRes: Int?) : this(true, true, time, null, imageRes)
}
