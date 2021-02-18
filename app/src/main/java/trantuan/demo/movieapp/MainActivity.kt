package trantuan.demo.movieapp

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.bottom_bar_item.*
import kotlinx.android.synthetic.main.fragment_container.*
import trantuan.demo.movieapp.databinding.ActivityMainBinding
import trantuan.demo.movieapp.ui.ContainerFragment

class MainActivity : AppCompatActivity(), ContainerFragment.OnPageChangeListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupUI()
    }

    private fun setupUI() {
        with(binding){
            format()
            tv_home.setTextColor(Color.BLUE)
            bottomBar.btnHome.setOnClickListener {
                viewpager.currentItem = 0
                format()
                tv_home.setTextColor(Color.BLUE)
            }

            bottomBar.btnUp.setOnClickListener {
                viewpager.currentItem = 1
                format()
                tv_up.setTextColor(Color.BLUE)
            }

            bottomBar.btnTop.setOnClickListener {
                viewpager.currentItem = 2
                format()
                tv_top.setTextColor(Color.BLUE)
            }
        }
        showFragment()
    }

    private fun showFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fr_container, ContainerFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun format(){
        tv_home.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.bottom))
        tv_up.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.bottom))
        tv_top.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.bottom))
    }

    override fun onPageChange(position: Int) {
        when (position) {
            0 -> {
                format()
                tv_home.setTextColor(Color.BLUE)
            }
            1 -> {
                format()
                tv_up.setTextColor(Color.BLUE)
            }

            2 -> {
                format()
                tv_top.setTextColor(Color.BLUE)
            }
        }
    }
}