package trantuan.demo.movieapp

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import trantuan.demo.movieapp.ui.home.HomeFragment
import trantuan.demo.movieapp.ui.top.TopFragment
import trantuan.demo.movieapp.ui.upcoming.UpcomingFragment

@Suppress("DEPRECATION")
class ViewPagerAdapter(private val myContext : Context, fm: FragmentManager, private var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                HomeFragment()
            }
            1 -> {
                UpcomingFragment()
            }
            2 -> {
                TopFragment()
            }
            else -> Fragment()
        }
    }
    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}