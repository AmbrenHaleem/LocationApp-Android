package ambreen.example.project2_googlemaps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.net.CacheResponse

class DataViewModel : ViewModel(){
    val lat: MutableLiveData<Double> = MutableLiveData()
    val long: MutableLiveData<Double> = MutableLiveData()
    fun setData(latitude: Double, longitude: Double){
        lat.value = latitude
        long.value = longitude
    }
}