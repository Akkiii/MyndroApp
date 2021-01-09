package com.myndrotest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myndrotest.adapter.UserListAdapter
import com.myndrotest.common.isInternetAvailable
import com.myndrotest.listener.ServiceResponseListener
import com.myndrotest.model.UserModel
import com.myndrotest.network.AppResponse
import com.myndrotest.network.WebServiceResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call

class MainActivity : AppCompatActivity() {
    private var response: Call<AppResponse>? = null

    private var userList = ArrayList<UserModel>()
    private var userAdapter: UserListAdapter? = null
    private var hasMoreData = false
    private var isLoading = false
    private val limit = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureView()
    }

    private fun configureView() {
        val layoutManager = LinearLayoutManager(baseContext, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        userAdapter = UserListAdapter(baseContext, userList)
        recyclerView.adapter = userAdapter

        getUserList(0, true)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + pastVisibleItems >= totalItemCount && !isLoading && hasMoreData) {
                    isLoading = true
                    showProgressFooter()
                    getUserList(userList.size, false)
                } else {
                    hideProgressFooter()
                }
            }
        })
    }

    private fun showProgressFooter() {
        userAdapter?.showProgressView()
    }

    private fun hideProgressFooter() {
        userAdapter?.hideProgressBar()
    }

    private fun getUserList(offset: Int, isProgress: Boolean) {
        if (!isInternetAvailable()) {
            progressBar.visibility = View.GONE
            textViewEmptyMessage.visibility = View.VISIBLE
            textViewEmptyMessage.text = getString(R.string.check_connection)
            return
        }

        if (isProgress)
            progressBar.visibility = View.VISIBLE
        response = WebServiceResponse.getInstance().getUserList(offset, limit, object : ServiceResponseListener {
            override fun serviceResponse(resStatus: Int?, objectRes: Any?, errorMessage: String?) {
                progressBar.visibility = View.GONE
                hideProgressFooter()
                if (objectRes != null) {
                    val res = objectRes as AppResponse
                    if (res.status) {
                        hasMoreData = res.data.has_more
                        userList.addAll(res.data.users)
                        userAdapter?.notifyDataSetChanged()
                        isLoading = false
                    } else {
                        textViewEmptyMessage.visibility = View.VISIBLE
                        textViewEmptyMessage.text = errorMessage
                    }
                } else {
                    textViewEmptyMessage.visibility = View.VISIBLE
                    textViewEmptyMessage.text = errorMessage
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (response != null)
            response?.cancel()
    }
}