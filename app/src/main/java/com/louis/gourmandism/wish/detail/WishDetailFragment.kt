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
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.FragmentWishDetailBinding
import com.louis.gourmandism.extension.getVmFactory
import it.beppi.tristatetogglebutton_library.TriStateToggleButton


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

        binding.textAddAttention.setOnClickListener {
            val status = if(binding.textAddAttention.text.toString() == getString(R.string.follow_collection)){
                binding.textAddAttention.text = getString(R.string.follow_cancel)
                true
            } else{
                binding.textAddAttention.text = getString(R.string.follow_collection)
                false
            }
            viewModel.setAttention(status)
        }

        binding.toggle.setOnToggleChanged { toggleStatus, booleanToggleStatus, toggleIntValue ->
            when(toggleStatus){
                TriStateToggleButton.ToggleStatus.on->{
                    binding.textStatus.text = getString(R.string.Public)
                    viewModel.setShare(1)
                }
                TriStateToggleButton.ToggleStatus.off->{
                    binding.textStatus.text = getString(R.string.Private)
                    viewModel.setShare(0)
                }
                else -> null
            }
        }

        binding.textRemove.setOnClickListener {
            showDialog()
        }

        viewModel.navigateInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalDetailFragment(it.id))
                viewModel.onNavigationDone()
            }
        })

        return binding.root
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
            binding.toggle.toggleStatus = TriStateToggleButton.ToggleStatus.off
            binding.textStatus.text = getString(R.string.Private)
        }else{
            binding.toggle.toggleStatus = TriStateToggleButton.ToggleStatus.on
            binding.textStatus.text = getString(R.string.Public)
        }
    }

}