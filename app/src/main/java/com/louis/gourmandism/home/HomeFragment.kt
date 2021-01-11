package com.louis.gourmandism.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.databinding.FragmentHomeBinding
import com.louis.gourmandism.extension.getVmFactory

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = HomeAdapter(viewModel)
        binding.recyclerViewHome.adapter = adapter

        binding.layoutSwipeRefreshHome.setOnRefreshListener {
            adapter.notifyDataSetChanged()
            binding.layoutSwipeRefreshHome.isRefreshing = false
        }

        viewModel.commentList.observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                adapter.submitList(list)
            }
        })

        viewModel.navigateDetailInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalDetailFragment(it))
                viewModel.onNavigationDone()
            }
        })

        viewModel.navigateCommentInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalCommentFragment(it))
                viewModel.onNavigationDone()
            }
        })

        viewModel.navigateProfileInfo.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().navigate(HomeFragmentDirections.actionGlobalProfileFragment(it))
                viewModel.onNavigationDone()
            }
        })

        return binding.root
    }
}
