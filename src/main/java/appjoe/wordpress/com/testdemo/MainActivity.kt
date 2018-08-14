package appjoe.wordpress.com.testdemo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity(), Tab1.OnFragmentInteractionListener, Tab2.OnFragmentInteractionListener {

    fun writePrefs() {
        val prefs = this.getSharedPreferences("com.wordpress.appjoe.prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("status", 0)
        editor.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tabLayout = findViewById<View>(R.id.tabLayout) as TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Part 1"))
        tabLayout.addTab(tabLayout.newTab().setText("Part 2"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        /*
         * Creating Tab view using ViewPager
         */
        val viewPager = findViewById<View>(R.id.pager) as ViewPager
        val pagerAdapter = PagerAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = pagerAdapter
        viewPager.setOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        // End of code for Tab layout
    }

    override fun onBackPressed() {
        finish()
        System.exit(0)
    }

    fun logout(v: View) {
        writePrefs()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onFragmentInteraction(uri: Uri) {}
}


