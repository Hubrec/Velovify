package com.guerin.velovify.objects

import com.guerin.velovify.objects.Availabilities

data class OverflowStands(
    val availabilities: Availabilities,
    val capacity: Int
)