package com.dicoding.picodiploma.fundamentalsubfinal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.dicoding.picodiploma.fundamentalsubfinal.BuildConfig
import com.dicoding.picodiploma.fundamentalsubfinal.model.FollowItems
import org.json.JSONArray

class FollowingViewModel : ViewModel() {
    private val listFollowing = MutableLiveData<ArrayList<FollowItems>>()

    val token = BuildConfig.GITHUB_TOKEN

    companion object {
        private val TAG = FollowingViewModel::class.java.simpleName
    }

    fun setListFollowing(username: String) {
        val listItems = ArrayList<FollowItems>()

        val url = "https://api.github.com/users/$username/following"
        AndroidNetworking.get(url)
            .setPriority(Priority.LOW)
            .addHeaders("Authorization", "token $token")
            .addHeaders("User-Agent", "request")
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {
                    val arrayResult = JSONArray(response.toString())
                    if (response != null) {
                        try {
                            for (i in 0 until response.length()) {
                                val objResult = arrayResult.getJSONObject(i)
                                val users = FollowItems(
                                    username = objResult.getString("login"),
                                    avatar = objResult.getString("avatar_url")
                                )
                                listItems.add(users)
                            }
                            listFollowing.postValue(listItems)

                        } catch (e: Exception) {
                            Log.d("Exception", e.message.toString())
                            e.printStackTrace()
                        }
                    }
                }

                override fun onError(anError: ANError) {
                    when (anError.errorCode) {
                        401 -> Log.d(TAG, "${anError.errorCode} : Bad Request")
                        403 -> Log.d(TAG, "${anError.errorCode} : Forbidden")
                        404 -> Log.d(TAG, "${anError.errorCode} : Not Found")
                        else -> Log.d(TAG, anError.errorCode.toString())
                    }
                }

            })
    }

    fun getListFollowing(): LiveData<ArrayList<FollowItems>> {
        return listFollowing
    }
}