package com.guerin.velovify.objects

import com.guerin.velovify.objects.Availabilities

data class TotalStands(
    val availabilities: Availabilities,
    val capacity: Int
): java.io.Serializable {}