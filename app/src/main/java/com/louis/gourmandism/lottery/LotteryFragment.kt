package com.louis.gourmandism.lottery

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.louis.gourmandism.databinding.FragmentLotteryBinding
import com.louis.gourmandism.extension.getVmFactory
import com.louis.gourmandism.search.SearchViewModel
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

        binding.buttonLottery.setOnClickListener {

            viewModel.allShop.value?.let {
                object : CountDownTimer(3000, 100) {
                    override fun onFinish() {

                    }
                    override fun onTick(millisUntilFinished: Long) {
                        val count = Random().nextInt(it.size)
                        viewModel.lotteryShop.value = it[count]
                    }
                }.start()
            }

        }

        return binding.root
    }

}