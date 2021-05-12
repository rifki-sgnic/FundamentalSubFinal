package com.dicoding.picodiploma.fundamentalsubfinal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.dicoding.picodiploma.fundamentalsubfinal.BuildConfig
import com.dicoding.picodiploma.fundamentalsubfinal.model.UserItems
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel : ViewModel() {
    private val listUser = MutableLiveData<ArrayList<UserItems>>()
    private val searchUser = MutableLiveData<ArrayList<UserItems>>()
    private val detailUser = MutableLiveData<UserItems>()

    private val token = BuildConfig.GITHUB_TOKEN

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    fun setListUser() {
        val listItems = ArrayList<UserItems>()
        val url = "https://api.github.com/users"
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
                                val users = UserItems(
                                    username = objResult.getString("login"),
                                    id = objResult.getInt("id"),
                                    avatar = objResult.getString("avatar_url")
                                )
                                listItems.add(users)
                            }
                            listUser.postValue(listItems)
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

    fun getListUser(): LiveData<ArrayList<UserItems>> {
        return listUser
    }

    fun setSearchUser(username: String) {
        val listItems = ArrayList<UserItems>()
        val url = "https://api.github.com/search/users?q=${username}"
        AndroidNetworking.get(url)
            .setPriority(Priority.LOW)
            .addPathParameter("username", username)
            .addHeaders("Authorization", "token $token")
            .addHeaders("User-Agent", "request")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val result = response.toString()
                    if (response != null) {
                        try {
                            val responseObject = JSONObject(result)
                            val items = responseObject.getJSONArray("items")

                            for (i in 0 until items.length()) {
                                val objResult = items.getJSONObject(i)
                                val user = UserItems(
                                    username = objResult.getString("login"),
                                    id = objResult.getInt("id"),
                                    avatar = objResult.getString("avatar_url")
                                )
                                listItems.add(user)
                            }
                            listUser.postValue(listItems)
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

    fun getSearchUser(): LiveData<ArrayList<UserItems>> {
        return searchUser
    }

    fun setDetailUser(username: String) {
        val url = "https://api.github.com/users/${username}"
        AndroidNetworking.get(url)
            .setPriority(Priority.LOW)
            .addPathParameter("username", username)
            .addHeaders("Authorization", "token $token")
            .addHeaders("User-Agent", "request")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val result = response.toString()
                    if (response != null) {
                        try {
                            val responseObject = JSONObject(result)
                            val user = UserItems(
                                id = responseObject.getInt("id"),
                                username = responseObject.getString("login"),
                                avatar = responseObject.getString("avatar_url"),
                                name = responseObject.getString("name"),
                                location = responseObject.getString("location"),
                                company = responseObject.getString("company"),
                                followers = responseObject.getInt("followers"),
                                following = responseObject.getInt("following"),
                                repository = responseObject.getInt("public_repos")
                            )
                            detailUser.postValue(user)
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

    fun getDetailUser(): LiveData<UserItems> {
        return detailUser
    }
}