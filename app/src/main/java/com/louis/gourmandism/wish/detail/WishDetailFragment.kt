package com.louis.gourmandism.wish.detail


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.FragmentWishDetailBinding
import com.louis.gourmandism.extension.getVmFactory

class WishDetailFragment : Fragment() {

    private val viewModel by viewModels<WishDetailViewModel> {
        getVmFactory(
            WishDetailFragmentArgs.fromBundle(
                requireArguments()
            ).favorite
        )
    }

    lateinit var binding : FragmentWishDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val favorite = WishDetailFragmentArgs.fromBundle(requireArguments()).favorite
        viewModel.checkListStatus(favorite)

        val adapter = WishDetailAdapter(viewModel)
        binding.recyclerViewFavorite.adapter = adapter

        initStatus(favorite.type)

        binding.textAddAttention.setOnClickListener {
            changeAttentionStatus()
        }

        binding.textRemove.setOnClickListener {
            showDialog()
        }

        binding.switchStatus.setOnCheckedChangeListener { buttonView, isChecked ->
            changeShareStatus(isChecked, buttonView)
        }

        viewModel.navigateInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalDetailFragment(it.id))
                viewModel.onNavigationDone()
            }
        })

        viewModel.favoriteInfo.observe(viewLifecycleOwner, Observer {

            viewModel.getUser(it.userId)
            viewModel.shop.value?.let {shop->
                adapter.submitList(viewModel.getNewShop(it.shops!!))
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.shop.observe(viewLifecycleOwner, Observer {

            viewModel.favoriteInfo.value?.shops?.let {
                if(viewModel.getNewShop(it).size > 0){
                    adapter.submitList(viewModel.getNewShop(it))
                    adapter.notifyDataSetChanged()
                }
            }
        })

        return binding.root
    }

    private fun changeShareStatus(
        isChecked: Boolean,
        buttonView: CompoundButton
    ) {
        if (isChecked) {
            viewModel.setShare(1)
            buttonView.text = getString(R.string.Public)
        } else {
            viewModel.setShare(0)
            buttonView.text = getString(R.string.Private)
        }
    }

    private fun changeAttentionStatus() {
        val status =
            if (binding.textAddAttention.text.toString() == getString(R.string.follow_collection)) {
                binding.textAddAttention.text = getString(R.string.follow_cancel)
                true
            } else {
                binding.textAddAttention.text = getString(R.string.follow_collection)
                false
            }
        viewModel.setAttention(status)
    }

    private fun showDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_check)
        val yesBtn = dialog.findViewById(R.id.text_yes) as TextView
        val noBtn = dialog.findViewById(R.id.text_no) as TextView
        yesBtn.setOnClickListener {
            viewModel.removeList()
            dialog.dismiss()
            findNavController().navigate(NavigationDirections.actionGlobalWishFragment())
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun initStatus(type: Int) {
        if(type == 0){
            binding.switchStatus.isChecked = false
            binding.switchStatus.text = getString(R.string.Private)
        }else{
            binding.switchStatus.isChecked  = true
            binding.switchStatus.text = getString(R.string.Public)
        }
    }

}