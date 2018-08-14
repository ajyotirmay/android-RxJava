package appjoe.wordpress.com.testdemo

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager, internal var mNoOfTabs: Int) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return Tab1()
            }
            1 -> {
                return Tab2()
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNoOfTabs
    }
}
