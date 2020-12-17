package com.louis.gourmandism.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.louis.gourmandism.R
import com.louis.gourmandism.data.Shop
import com.louis.gourmandism.databinding.FragmentDetailBinding
import com.louis.gourmandism.extension.getVmFactory
import java.util.*


class DetailFragment : Fragment() {

    private val viewModel by viewModels<DetailViewModel> { getVmFactory() }
    private lateinit var bottomBehavior : BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val id = DetailFragmentArgs.fromBundle(requireArguments()).shopId

        bottomBehavior = BottomSheetBehavior.from(binding.bottomDialog.bottomSheetLayout)
        bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        id?.let {
            viewModel.setBrowserHistory(it)
            viewModel.getShop(it,1)
            viewModel.getComment(it)
        }

        paddingPicture(binding.textStarAverage, R.drawable.tasty_select,0)
        paddingPicture(binding.textAddComment, R.drawable.comment,2)

        val adapter = DetailAdapter(viewModel)
        binding.recyclerViewDetailComment.adapter = adapter

        binding.bottomDialog.editTime.setOnClickListener {
            val builder = SingleDateAndTimePickerDialog.Builder(context)
                .bottomSheet()
                .curved()
                .backgroundColor(resources.getColor(R.color.mainStyleColor))
                .mainColor(Color.WHITE)
                .titleTextColor(Color.WHITE)
                .displayListener {}
                .title("選擇時間")
                .listener { date ->
                    binding.bottomDialog.editTime.text = date.toString()
                    viewModel.date.value = date.time
                }

                .build()
            builder.display()
        }

        binding.bottomDialog.buttonGo.setOnClickListener{
            val newEventContent = binding.bottomDialog.editContent.text.toString()
            val newEventMemberLimit = binding.bottomDialog.editNumber.text.toString()
            val createTime = Calendar.getInstance().timeInMillis
            viewModel.newEvent(newEventContent, newEventMemberLimit, createTime)
        }

        viewModel.commentList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.leaveDetail.observe(viewLifecycleOwner, Observer {

            it?.let {
                if (it) findNavController().navigateUp()
            }
        })

        viewModel.newEventStatus.observe(viewLifecycleOwner, Observer {

            slideUpDownBottomSheet()
        })

        binding.textAddComment.setOnClickListener {

            findNavController().navigate(DetailFragmentDirections.actionGlobalAdd2commentDialog(
                viewModel.shopInfo.value!!
            ))
        }

        binding.textAddEvent.setOnClickListener {

            slideUpDownBottomSheet()
        }

        binding.textAddWishList.setOnClickListener {

            id?.let {
                findNavController().navigate(DetailFragmentDirections.actionGlobalAdd2wishFragment(it))
            }
        }

        return binding.root
    }

    private fun slideUpDownBottomSheet() {
        if (bottomBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN;
        }
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