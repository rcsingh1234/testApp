package com.test.card.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.test.card.MainActivity
import com.test.card.MainActivity.Companion.db
import com.test.card.MainActivity.Companion.loader
import com.test.card.db.Entity
import com.test.card.model.PersonsList
import com.test.card.retrofit.ApiRequest
import com.test.card.retrofit.RetrofitModule
import com.test.card.model.Result
import com.test.card.utils.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class PersonViewModel(private val context: Context) : ViewModel() {

    val TAG = PersonViewModel::class.java.simpleName
    var personsMutableLiveData: MutableLiveData<ArrayList<Result>> = MutableLiveData()
    val personLiveDataList: LiveData<ArrayList<Result>> = personsMutableLiveData
    var apiRequest: ApiRequest

    init {
        val retrofitModule = RetrofitModule
        apiRequest = retrofitModule.createApiRequest(retrofitModule.getRetrofit())
        getPersonsList()
    }

    fun getPersonsList() {
        val listOfData = db.dao().entityList

        if (listOfData.size > 1) {
            getDataForDB(listOfData)
            return
        }
        if(Util.isInternetConnected(context)){
            getDataFromWebServices()
        }else{
            val activity: MainActivity = context as MainActivity
            Util.showNoNetworkDialog(activity)
        }

    }

    private fun getDataFromWebServices(){
        loader.showLoading()
        apiRequest.getPersonsList(10)
            .enqueue(object : Callback<PersonsList> {
                override fun onResponse(
                    call: Call<PersonsList>,
                    response: Response<PersonsList>
                ) {
                    Log.d(TAG, "Response : " + response.body())
                    if (response.body() != null) {
                        personsMutableLiveData!!.value = response.body()!!.results
                        insertDataIntoDB(personsMutableLiveData!!.value!!)
                        loader.dismissLoading()
                    }
                }

                override fun onFailure(call: Call<PersonsList>, t: Throwable) {
                    Log.d(TAG, "Error Msg : " + t)
                    loader.dismissLoading()
                }
            })
    }

    fun insertDataIntoDB(resultList: ArrayList<Result>) {
        for (result in resultList) {
            val id = result.email
            val gson = Gson()
            val jsonObject = gson.toJson(result)
            val entity = Entity(id, jsonObject)
            db.dao().insert(entity)
        }
    }

    private fun getDataForDB(listOfData: List<Entity>) {
        val list: ArrayList<Result> = ArrayList()
        for (entity in listOfData) {
            val gson = Gson()
            val result = gson.fromJson(entity.result, Result::class.java)
            list.add(result)
        }
        personsMutableLiveData!!.value = list

    }
}

