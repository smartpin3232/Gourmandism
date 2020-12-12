package com.louis.gourmandism.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.louis.gourmandism.R
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.databinding.FragmentDetailBinding
import com.louis.gourmandism.extension.getVmFactory


class DetailFragment : Fragment() {

    private val viewModel by viewModels<DetailViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val id = DetailFragmentArgs.fromBundle(requireArguments()).shopId

        id?.let {
            viewModel.getShop(it,1)
            viewModel.getComment(it)
        }

        paddingPicture(binding.textStarAverage, R.drawable.tasty_select,0)
        paddingPicture(binding.textAddComment, R.drawable.comment,2)

        val adapter = DetailAdapter(viewModel)
        binding.recyclerViewDetailComment.adapter = adapter

        viewModel.commentList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.leaveDetail.observe(viewLifecycleOwner, Observer {

            it?.let {
                if (it) findNavController().navigateUp()
            }
        })

        binding.textAddComment.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionGlobalAdd2commentDialog(
                viewModel.shopInfo.value!!
            ))
        }

        binding.textAddEvent.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionGlobalNewEventFragment(
                viewModel.shopInfo.value!!
            ))
        }

        binding.textAddWishList.setOnClickListener {
            id?.let {
                findNavController().navigate(DetailFragmentDirections.actionGlobalAdd2wishFragment(it))
            }
        }


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        toggleFullScreen()
    }

    override fun onStop() {
        super.onStop()
        toggleFullScreen()
    }

    private fun toggleFullScreen() {
        val window = requireActivity().window
        if (window.decorView.systemUiVisibility == View.SYSTEM_UI_FLAG_VISIBLE) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    private fun paddingPicture(tv: TextView, pic: Int, direction: Int) {
        val drawable1 = resources.getDrawable(pic)
        drawable1.setBounds(0, 0, 50, 50)
        when(direction){
            0->tv.setCompoundDrawables(drawable1, null, null, null)
            1->tv.setCompoundDrawables(null, drawable1, null, null)
            2->tv.setCompoundDrawables(null, null, drawable1, null)
            else->tv.setCompoundDrawables(null, null, null, drawable1)
        }

    }

}