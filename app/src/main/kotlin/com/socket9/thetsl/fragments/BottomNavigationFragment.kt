package com.socket9.thetsl.fragments

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.roughike.bottombar.BottomBar
import com.roughike.bottombar.OnMenuTabClickListener
import com.socket9.thetsl.R
import com.socket9.thetsl.activities.MainActivity
import com.socket9.thetsl.extensions.replaceFragment
import com.socket9.thetsl.extensions.toast
import com.socket9.thetsl.utils.DialogUtil
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find

/**
 * Created by Euro (ripzery@gmail.com) on 3/10/16 AD.
 */
class BottomNavigationFragment : Fragment(), AnkoLogger {

    /** Variable zone **/
    private var bottomBarIndex: Int = 1
    private var bottomBar: BottomBar? = null
    private var emergencyFragment: EmergencyFragment? = null
    private var serviceFragment: ServiceFragment? = null
    private var carTrackingFragment: CarTrackingFragment? = null
    private var onChangedTabListener: OnChangeTabListener? = null
    private var isLaunchedByGcm: Boolean = false

    /** Static method zone **/
    companion object {
        val ARG_1 = "ARG_1"
        val ARG_2 = "ARG_2"
        val EMERGENCY = 0
        val SERVICE_TRACKING = 1
        val CAR_TRACKING = 2

        fun newInstance(param1: Int, onChangeTabListener: OnChangeTabListener, launchedByGcm: Boolean): BottomNavigationFragment {
            var bundle: Bundle = Bundle()
            bundle.putInt(ARG_1, param1)
            bundle.putBoolean(ARG_2, launchedByGcm)
            val bottomNavigationFragment: BottomNavigationFragment = BottomNavigationFragment()
            bottomNavigationFragment.arguments = bundle
            bottomNavigationFragment.onChangedTabListener = onChangeTabListener
            return bottomNavigationFragment
        }

    }

    /** Activity method zone  **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            /* if newly created */
            bottomBarIndex = arguments.getInt(ARG_1)
            isLaunchedByGcm = arguments.getBoolean(ARG_2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_bottom_navigation, container, false)
        bottomBar = BottomBar.attach(rootView, null)
        return bottomBar
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance(view, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
    }

    /** Method zone **/

    private fun initInstance(view: View?, savedInstanceState: Bundle?) {
        initFragment()
        initListener()
        bottomBar?.selectTabAtPosition(bottomBarIndex, true)
    }

    private fun initFragment() {
        carTrackingFragment = CarTrackingFragment.newInstance("CarTrackingFragment")
        emergencyFragment = EmergencyFragment.newInstance("EmergencyFragment")
        serviceFragment = ServiceFragment.newInstance("ServiceFragment")

    }

    private fun initListener() {
        bottomBar?.setItemsFromMenu(R.menu.menu_bottom_bar, object : OnMenuTabClickListener {
            override fun onMenuTabSelected(menuItemId: Int) {
                when (menuItemId) {
                    R.id.menu_bottom_emergency -> {
                        toast("Bottom Emergency")
                        replaceFragment(R.id.fragmentContainer, emergencyFragment!!)
                        onChangedTabListener?.onChangedTab(MainActivity.FRAGMENT_DISPLAY_EMERGENCY)
                    }
                    R.id.menu_bottom_service_tracking -> {
                        replaceFragment(R.id.fragmentContainer, serviceFragment!!)
                        onChangedTabListener?.onChangedTab(MainActivity.FRAGMENT_DISPLAY_SERVICE)
                    }
                    R.id.menu_bottom_car_tracking -> {
                        replaceFragment(R.id.fragmentContainer, carTrackingFragment!!)
                        onChangedTabListener?.onChangedTab(MainActivity.FRAGMENT_DISPLAY_CAR_TRACKING)
                    }
                }
            }

            override fun onMenuTabReSelected(menuItemId: Int) {

            }
        })


    }

    fun setTab(index: Int, isLaunchedByGcm: Boolean) {
        try {
            if (isLaunchedByGcm) {
                when (index) {
                    0 -> {
                        Handler().postDelayed({
                            showEmergencyConfirmationDialog()
                        }, 1000)
                    }
                    1 -> {
                        Handler().postDelayed({
                            showServiceConfirmationDialog()
                        }, 1000)
                    }
                }
            }
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showEmergencyConfirmationDialog() {
        DialogUtil.getEmergencyConfirmationDialog(context).show()
    }

    private fun showServiceConfirmationDialog() {
        val dialog = DialogUtil.getServiceBookingConfirmationDialog(context)
        val view = dialog.customView!!

        with(view) {
            val tvType = find<TextView>(R.id.tvType)
            val tvDate = find<TextView>(R.id.tvDate)
            val tvTime = find<TextView>(R.id.tvTime)
            val tvBranch = find<TextView>(R.id.tvBranch)
        }

        dialog.show()
    }

    interface OnChangeTabListener {
        fun onChangedTab(index: Int)
    }
}