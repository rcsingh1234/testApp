package com.test.card

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.test.card.adapter.PersonsListAdapter
import com.test.card.db.AppDatabase
import com.test.card.listener.LoadingListener
import com.test.card.viewmodel.PersonViewModel
import com.test.card.model.Result
import java.util.*

class MainActivity : AppCompatActivity(), LoadingListener {

    private val TAG = MainActivity::class.java.simpleName
    private var dialog: Dialog? = null
    private lateinit var personViewModel: PersonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loader = this;
        initDB()
        personViewModel = PersonViewModel(this)
        setContentView(R.layout.activity_main)
        personViewModel.personLiveDataList.observe(this,
            Observer { dataList ->
                if (dataList != null) {
                    Log.d(TAG, "List : " + dataList.toString())
                    setData(dataList)
                }
            })
    }

    private fun initDB() {
        db = Room
            .databaseBuilder(this, AppDatabase::class.java, "Result")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }


    private fun setData(dataList: ArrayList<Result>) {
        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        val adapter = PersonsListAdapter(dataList)
        recyclerView.adapter = adapter

    }

    companion object {
        lateinit var loader: LoadingListener
        lateinit var db: AppDatabase
    }

    override fun showLoading() {
        runOnUiThread { showLoader(this) }
    }

    override fun dismissLoading() {
        runOnUiThread { hideLoader() }
    }

    fun hideLoader() {
        Log.d(TAG, "Hide Loading !!!!")
        try {
            if (dialog!! != null) {
                if (dialog!!.isShowing) dialog!!.dismiss()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Exception $e")
        }
    }

    fun showLoader(context: Context) {
        Log.d(TAG, "Show Loading !!!!")
        try {
            dialog = getProgressDialog(context)
            dialog!!.show()
        } catch (e: Exception) {
            Log.d(TAG, "Exception $e")
        }
    }

    fun getProgressDialog(context: Context): Dialog {
        if (dialog == null) {
            dialog = Dialog(context)
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(false)
        val factory = LayoutInflater.from(context)
        val customPopupView: View = factory.inflate(R.layout.loading_dialog, null)
        dialog!!.setContentView(customPopupView)
        return dialog!!
    }
}