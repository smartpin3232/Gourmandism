package com.louis.gourmandism.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.data.*
import com.louis.gourmandism.databinding.FragmentHomeBinding
import com.louis.gourmandism.extension.getVmFactory

class HomeFragment : Fragment() {

//    private val viewModel: HomeViewModel by lazy {
//        ViewModelProvider(this).get(HomeViewModel::class.java)
//    }
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

        val db = FirebaseFirestore.getInstance()
//        var comment = Comment(
//             hostId= "Emil",
//             shopId= "test002",
//             commentId= ""
//             images= mutableListOf(
//                 "https://lh3.googleusercontent.com/proxy/RJWzrV422_40u68Eg-P6th1Fop2slX0SQ65FmBr69BilP7rSbUJRBQPoVVmIWlNn8yxm-qMd1wvYCqkHl28EovKGYhQugNgmhKfqAf2KdyZJmDJZzLNbsg0A"
//                 ,"https://top1cdn.top1health.com/cdn/am/21565/67151.jpg"
//             ),
//             title= "Emil 飲料店",
//             star= 3.5.toFloat(),
//             content= "一杯10元 ,加奶粉20!!!!",
//             like= mutableListOf(
//                 "Louis","Sylvie","Johnny"
//             )
//        )

//        val user = User(
//            id = "003",
//            name = "Emil",
//            location = "",
//            currentPosition = "",
//            browseRecently = mutableListOf<BrowseRecently>(
//                BrowseRecently(shopId = "test001",time = 0),
//                BrowseRecently(shopId = "test002",time = 0)
//            ),
//            friendList = mutableListOf("002")
//        )


//        var  shop = Shop(
//            id="test002",
//            name="Gary 麵包工坊",
//            location = Location(25.042083738250724, 121.56471261005666),
//            address= "110台北市信義區基隆路一段178號",
//            openTime=0,
//            closeTime=0,
//            phone= "0912345678",
//            image= mutableListOf("https://www.upmedia.mg/upload/article/20180828124352040787.JPG"
//                ,"data:image/jpeg;base64,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"),
//            star= 4.5.toFloat(),
//            type= mutableListOf("甲胖")
//        )

//        val event = Event(
//            id = "001",
//            host = User(
//                id = "002",
//                name = "Gary",
//                location = "",
//                currentPosition = "",
//                browseRecently = mutableListOf<BrowseRecently>(
//                    BrowseRecently(shopId = "test001",time = 0),
//                    BrowseRecently(shopId = "test002",time = 0)
//            ),
//            friendList = mutableListOf("001")
//        ),
//            title = "Gary",
//            content = "快來一起吃~~快來一起吃~~快來一起吃~~快來一起吃~~快來一起吃~~",
//            shopId = "test001",
//            status = 0,
//            createTime = 0,
//            member = mutableListOf("Louis","Sylvie")
//        )
//
//        db.collection("Shop")
//            .add(shop)
//            .addOnSuccessListener { documentReference ->
//                Log.d("Dialog", "Success")
//            }
//            .addOnFailureListener { e ->
//                Log.d("Dialog", "Error", e)
//            }


        viewModel.commentList.observe(viewLifecycleOwner, Observer {
            it?.let {list->
               adapter.submitList(list)
            }
        })

        viewModel.navigationStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalDetailFragment(it))
                viewModel.onNavigated()
            }
        })

        return binding.root
    }
}