package com.socket9.thetsl.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.socket9.thetsl.R
import com.socket9.thetsl.activities.NewsEventActivity
import com.socket9.thetsl.adapter.EventAdapter
import com.socket9.thetsl.managers.HttpManager
import com.socket9.thetsl.models.Model
import kotlinx.android.synthetic.main.fragment_event.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.indeterminateProgressDialog
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import rx.Subscription

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */
class NewsEventFragment : Fragment(), AnkoLogger, EventAdapter.EventInteractionListener {

    /** Variable zone **/
    private var getListNewsEventSubscription: Subscription? = null
    private var eventNewsAdapter: EventAdapter? = null
    private var dialogEventProgress: ProgressDialog? = null
    private var dialogNewsProgress: ProgressDialog? = null
    var isNews: Boolean = true


    /** Static method zone **/
    companion object {
        val MODE_IS_NEWS = "IS_NEWS_MODE"

        fun newInstance(isModeNews: Boolean): NewsEventFragment {
            var bundle: Bundle = Bundle()
            bundle.putBoolean(MODE_IS_NEWS, isModeNews)
            val newsEventFragment: NewsEventFragment = NewsEventFragment()
            newsEventFragment.arguments = bundle
            return newsEventFragment
        }

    }

    /** Activity method zone  **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            /* if newly created */
            isNews = arguments.getBoolean(MODE_IS_NEWS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_event, container, false)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()

    }

    override fun onPause() {
        super.onPause()
        dialogEventProgress?.dismiss()
        dialogNewsProgress?.dismiss()
        getListNewsEventSubscription?.unsubscribe()
    }

    /** Method zone **/

    private fun initInstance() {
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        eventNewsAdapter = EventAdapter(mutableListOf())
        recyclerView.adapter = eventNewsAdapter
        eventNewsAdapter?.setListener(this)
        getListNewsEvent()
    }

    private fun getListNewsEvent() {
        when (isNews) {
            true -> {
                dialogNewsProgress = indeterminateProgressDialog(R.string.dialog_progress_news_list_content, R.string.dialog_progress_title)
                dialogNewsProgress?.setCancelable(false)
                dialogNewsProgress?.show()
                getListNewsEventSubscription = HttpManager.getListNews()
                        .subscribe ({
                            dialogNewsProgress?.dismiss()
                            eventNewsAdapter?.setList(it.data)
                        }, { error ->
                            dialogNewsProgress?.dismiss()
                            error.printStackTrace()
                            toast(getString(R.string.toast_internet_connection_problem))
                        })
            }
            false -> {
                dialogEventProgress= indeterminateProgressDialog(R.string.dialog_progress_event_list_content, R.string.dialog_progress_title)
                dialogEventProgress?.setCancelable(false)
                dialogEventProgress?.show()
                getListNewsEventSubscription = HttpManager.getListEvent()
                        .subscribe ({
                            dialogEventProgress?.dismiss()
                            eventNewsAdapter?.setList(it.data)
                        }, { error ->
                            dialogEventProgress?.dismiss()
                            error.printStackTrace()
                            toast(getString(R.string.toast_internet_connection_problem))
                        })
            }
        }

    }

    /** Listener zone **/
    override fun onClickedEvent(index: Int, model: Model.NewsEventEntity) {
        startActivity<NewsEventActivity>("id" to model.id, "isNews" to isNews)
    }
}