package com.louis.gourmandism.lottery

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.FragmentLotteryBinding
import com.louis.gourmandism.extension.getVmFactory
import java.util.*

class LotteryFragment : Fragment() {

    private val viewModel by viewModels<LotteryViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLotteryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val foodType = resources.getStringArray(R.array.food_type)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.food_type,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.spinnerFoodType.adapter = adapter

        binding.spinnerFoodType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.filterShop(foodType[position])
                }

            }

        binding.buttonLottery.setOnClickListener {
            if (!viewModel.filterShopList.value.isNullOrEmpty()) {
                viewModel.filterShopList.value?.let {
                    object : CountDownTimer(3000, 100) {
                        override fun onFinish() {

                        }

                        override fun onTick(millisUntilFinished: Long) {
                            val count = Random().nextInt(it.size)
                            viewModel.lotteryShop.value = it[count]
                        }
                    }.start()
                }
            } else {
                Toast.makeText(context,"沒有相關餐廳資訊",Toast.LENGTH_LONG).show()
            }
        }

        viewModel.allShop.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                viewModel.filterShop("全部")
            }
        })

        return binding.root
    }

}