package com.guerin.velovify.objects

data class StationsItem(
    val address: String,
    val banking: Boolean,
    val bonus: Boolean,
    val connected: Boolean,
    val contractName: String,
    val lastUpdate: String,
    val mainStands: MainStands,
    val name: String,
    val number: Int,
    val overflow: Boolean,
    val overflowStands: OverflowStands,
    val position: Position,
    val shape: Any,
    val status: String,
    val totalStands: TotalStands
)