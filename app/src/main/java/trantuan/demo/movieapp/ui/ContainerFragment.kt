package trantuan.demo.movieapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import trantuan.demo.movieapp.R
import trantuan.demo.movieapp.ViewPagerAdapter
import trantuan.demo.movieapp.databinding.FragmentContainerBinding

class ContainerFragment : Fragment() {
    private lateinit var binding: FragmentContainerBinding
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var onPageChangeListener: OnPageChangeListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_container, container, false)
        setupUI()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setOnPageChangeListener(context as OnPageChangeListener)
    }

    private fun setupUI() {
        adapter = ViewPagerAdapter(requireContext(), requireActivity().supportFragmentManager, 3)

        with(binding) {
            viewpager.adapter = adapter
            viewpager.offscreenPageLimit = 3
            viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    onPageChangeListener.onPageChange(position)
                }

                override fun onPageSelected(position: Int) {
                }

            })
        }
    }

    interface OnPageChangeListener{
        fun onPageChange(position: Int)
    }

    private fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener){
        this.onPageChangeListener = onPageChangeListener
    }
}