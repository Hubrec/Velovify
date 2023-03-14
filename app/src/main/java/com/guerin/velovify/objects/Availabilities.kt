package com.guerin.velovify.objects

data class Availabilities(
    val bikes: Int,
    val electricalBikes: Int,
    val electricalInternalBatteryBikes: Int,
    val electricalRemovableBatteryBikes: Int,
    val mechanicalBikes: Int,
    val stands: Int
): java.io.Serializable {}