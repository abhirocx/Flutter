package com.np.namasteyoga.ui.eventModule.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.R
import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.comman.with
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.Event
import com.np.namasteyoga.datasource.pojo.GetEventRequest
import com.np.namasteyoga.interfaces.OnClickItem
import com.np.namasteyoga.interfaces.PaginationListener
import com.np.namasteyoga.repository.EventRepository
import com.np.namasteyoga.ui.eventModule.adapter.EventListAdapter
import com.np.namasteyoga.ui.eventModule.viewmodels.PastEventListViewModel
import com.np.namasteyoga.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_past_event.*
import kotlinx.android.synthetic.main.fragment_past_event.recyclerView
import org.jetbrains.anko.support.v4.toast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class PastEventFragment : Fragment(), OnClickItem<Event>, PaginationListener {
    protected val compositeDisposable by lazy { CompositeDisposable() }
    val model: PastEventListViewModel by viewModel(PastEventListViewModel::class)
    private val eventRepository:EventRepository by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_past_event, container, false)
    }

    private val list = ArrayList<Event>()
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var myllNoEvents: View
    private lateinit var progress: ProgressBar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val lat = LatLng(
            Util.citySelected?.lat?.toDouble() ?: 0.0,
            Util.citySelected?.lng?.toDouble() ?: 0.0
        )
        myllNoEvents = llNoEvents
        myRecyclerView = recyclerView
        progress = progress_circular
        myRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
//            adapter = EventListAdapter(
//                list,
//                this@PastEventFragment,
//                this@PastEventFragment,
//                lat
//            )
        }

//        model.response.observe(requireParentFragment(), Observer{
//            isShowDialog(false)
//            if (it == null) {
//                toast(R.string.something_went_wrong)
//                return@observe
//            }
//            try {
//                if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
//                    if (it.data?.isNotEmpty() == true) {
//                        it.data.run {
//                            list.addAll(this)
//                        }
//                    }
//                    recyclerView.adapter?.notifyDataSetChanged()
//                    if (list.isNotEmpty())
//                        llNoEvents.hide()
//                    else
//                        llNoEvents.show()
//
//                }
//            } catch (e: Exception) {
//                toast(R.string.something_went_wrong)
//                if (C.DEBUG)
//                    e.printStackTrace()
//            }
//        })


        city = Util.citySelected
        getUserList()
    }

    private fun isShowDialog(b: Boolean=true) {
        if (b) {
            progress.hide()
        } else {
            progress.show()
        }
    }

    private var city: City? = null
    private fun getUserList() {
        if (city == null) {
            toast(R.string.something_went_wrong)
            return
        }
        if (NetworkUtil.isInternetAvailable(requireContext())) {
            isShowDialog(true)
            city?.run {
//                model.getListWithPagination(this)
                getListWithPagination(this)
            }
        } else
            toast(R.string.no_internet_connection)
    }

    companion object {
        const val TAG = "PastEventFragment"
    }

    override fun onClick(position: Int, t: Event?) {
        t?.run {

        }
    }

    fun getListWithPagination(_city: City){

        model._page.value = model.page.value?.plus(1)

            val userListRequest = GetEventRequest().apply {
                page = model.page.value?: CommonInt.One
                city = _city.city
                state = _city.state_name
                country = _city.country_name
                eventType = CommonString.PAST
            }
          val dis=  eventRepository.getEventList(userListRequest,model.token.value?: CommonString.Empty)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    isShowDialog(false)
                    if (it == null) {
                     //   toast(R.string.something_went_wrong)
                        return@subscribe
                    }
                    try {
                        if ((it.status ?: C.NP_STATUS_FAIL) == C.NP_STATUS_SUCCESS) {
                            if (it.data?.isNotEmpty() == true) {
                                it.data.run {
                                    list.addAll(this)
                                }
                            }
                            myRecyclerView.adapter?.notifyDataSetChanged()
                            if (list.isNotEmpty())
                                myllNoEvents.hide()
                            else
                                myllNoEvents.show()

                        }
                    } catch (e: Exception) {
                        toast(R.string.something_went_wrong)
                        if (C.DEBUG)
                            e.printStackTrace()
                    }
                },{
                    it?.message?.run(Logger::Error)
                    isShowDialog(false)
                    if (list.isNotEmpty())
                        myllNoEvents.hide()
                    else
                        myllNoEvents.show()
                })

          compositeDisposable.add(dis)
    }

    override fun page() {
        if (model.response.value?.currentPage == model.response.value?.lastPage)
            return
        getUserList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}