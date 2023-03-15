package com.guerin.velovify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guerin.velovify.objects.StationsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StationViewModel : ViewModel() {

    private var stationLiveData = MutableLiveData<List<StationsItem>>()

    fun getAllStations() {
        RetrofitInstance.api.getStations("72e8588f830327a17b61280992faa24f6887b8e4").enqueue(object : Callback<List<StationsItem>> {
            override fun onResponse(
                call: Call<List<StationsItem>>,
                response: Response<List<StationsItem>>
            ) {
                if (response.isSuccessful) {
                    stationLiveData.value = response.body()
                }
                else { return }
            }
            override fun onFailure(call: Call<List<StationsItem>>, t: Throwable) {
                Log.e("StationViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun searchStations(searchText: String) {
        if (stationLiveData.value?.isEmpty() == true) {
            getAllStations()
        } else {
            stationLiveData.value = stationLiveData.value?.filter { it.name.contains(searchText, true) }
        }
    }

    fun getFavoriteStations() {
        stationLiveData.value = stationLiveData.value?.filter { it.favorite }
    }

    fun observeStationsLiveData() = stationLiveData
}
