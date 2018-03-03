package com.semaphores.techathlon.Models

import android.support.annotation.DrawableRes

data class Hunt(var name: String, var description: String, @DrawableRes var thumbnail: Int, var prize: String, var fee: Int, var claimed: Boolean)
