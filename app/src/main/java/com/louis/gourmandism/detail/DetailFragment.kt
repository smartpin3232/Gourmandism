package com.louis.gourmandism.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.R
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.OpenTime
import com.louis.gourmandism.databinding.FragmentDetailBinding
import com.louis.gourmandism.extension.getVmFactory
import java.util.*
import kotlin.time.ExperimentalTime


class DetailFragment : Fragment() {

    private val viewModel by viewModels<DetailViewModel> { getVmFactory() }
    private lateinit var bottomBehavior : BottomSheetBehavior<ConstraintLayout>
    private lateinit var binding : FragmentDetailBinding

    @ExperimentalTime
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val shopId = DetailFragmentArgs.fromBundle(requireArguments()).shopId

        bottomBehavior = BottomSheetBehavior.from(binding.bottomDialog.bottomSheetLayout)
        bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        shopId?.let {
            viewModel.setBrowserHistory(it)
            viewModel.getShop(it,1)
            viewModel.getComment(it)
        }

        paddingPicture(binding.textStarAverage, R.drawable.tasty_select,0)
        paddingPicture(binding.textAddComment, R.drawable.comment,2)

        val adapter = DetailAdapter(viewModel)
        binding.recyclerViewDetailComment.adapter = adapter

        val timeAdapter = DetailTimeAdapter(viewModel)
        binding.recyclerOpenTime.adapter = timeAdapter

        val businessTime = mutableListOf<OpenTime>(
            OpenTime(
                day = "0",
                startTime = "09:00",
                endTime = "22:30"
            ),
            OpenTime(
                day = "1",
                startTime = "09:00",
                endTime = "16:00"
            ),OpenTime(
                day = "2",
                startTime = "09:00",
                endTime = "22:30"
            ),OpenTime(
                day = "3",
                startTime = "09:00",
                endTime = "22:00"
            ),OpenTime(
                day = "4",
                startTime = "09:00",
                endTime = "22:30"
            ),OpenTime(
                day = "5",
                startTime = "09:00",
                endTime = "22:00"
            ),OpenTime(
                day = "6",
                startTime = "09:00",
                endTime = "22:30"
            )
        )
        timeAdapter.submitList(businessTime)

        binding.recyclerOpenTime.tag = false
        binding.textBusinessTimeContent.setOnClickListener {
            binding.recyclerOpenTime.visibility = when(binding.recyclerOpenTime.tag){
                true -> {
                    binding.recyclerOpenTime.tag = false
                    View.GONE
                }
                else -> {
                    binding.recyclerOpenTime.tag = true
                    View.VISIBLE
                }
            }
        }


        binding.bottomDialog.editTime.setOnClickListener {
            showDateAndTimePicker()
        }

        binding.bottomDialog.buttonGo.setOnClickListener{
            val newEventContent = binding.bottomDialog.editContent.text.toString()
            val newEventMemberLimit = binding.bottomDialog.editNumber.text.toString()
            val createTime = Calendar.getInstance().timeInMillis

            checkInputStatus(newEventMemberLimit, newEventContent, createTime)
        }

        viewModel.shopInfo.observe(viewLifecycleOwner, Observer {
            val whichDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            val openingHours = viewModel.getTodayBusinessTime(businessTime, (whichDay-1).toString())
            businessStatusInit(openingHours)
        })

        viewModel.commentList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                binding.textStarAverage.text = getAverageRating(it)
            }
        })

        viewModel.leaveDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) findNavController().navigateUp()
            }
        })

        viewModel.newEventStatus.observe(viewLifecycleOwner, Observer {
            slideUpDownBottomSheet()
            findNavController().navigate(NavigationDirections.actionGlobalEventFragment())
        })

        viewModel.toProfileStatus.observe(viewLifecycleOwner, Observer {
            it?.let{
                findNavController().navigate(DetailFragmentDirections.actionGlobalProfileFragment(it))
            }
        })

        binding.textAddComment.setOnClickListener {
            viewModel.shopInfo.value?.let {
                findNavController().navigate(DetailFragmentDirections.actionGlobalAdd2commentDialog(it))
            }
        }

        binding.textAddEvent.setOnClickListener {
            slideUpDownBottomSheet()
        }

        binding.textAddWishList.setOnClickListener {
            shopId?.let {
                findNavController().navigate(DetailFragmentDirections.actionGlobalAdd2wishFragment(it))
            }
        }

        return binding.root
    }

    private fun checkInputStatus(
        newEventMemberLimit: String,
        newEventContent: String,
        createTime: Long
    ) {
        if (viewModel.date.value == null) {
            Toast.makeText(requireContext(), "請輸入開始日期 !", Toast.LENGTH_SHORT).show()
        } else if (newEventMemberLimit == "") {
            Toast.makeText(requireContext(), "請輸入人數限制 !", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.newEvent(newEventContent, newEventMemberLimit, createTime)
        }
    }

    private fun showDateAndTimePicker() {
        val builder = SingleDateAndTimePickerDialog.Builder(context)
            .bottomSheet()
            .curved()
            .backgroundColor(resources.getColor(R.color.gray_646464))
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

    @SuppressLint("SetTextI18n")
    private fun businessStatusInit(businessTime: OpenTime) {

        binding.textBusinessTimeContent.text =
            businessTime.startTime + getString(R.string.hyphen) + businessTime.endTime

        val businessStatus =
            viewModel.checkBusinessStatus(businessTime, viewModel.getCurrentHourAndMinute())

        if (businessStatus) {
            binding.textStatus.apply {
                text = getString(R.string.open)
                setTextColor(context.getColor(R.color.black_3f3a3a))
                setBackgroundResource(R.drawable.round_layout)
            }
        } else {
            binding.textStatus.apply {
                text = getString(R.string.close)
                setTextColor(context.getColor(R.color.white))
                setBackgroundResource(R.drawable.round_layout_3)
            }
        }
    }

    private fun getAverageRating(
        it: List<Comment>
    ): String {
        var starTotal = 0.0
        it.forEach { comment ->
            starTotal += comment.star
        }
        return String.format("%.1f", (starTotal / it.size))
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