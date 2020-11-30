package com.louis.gourmandism.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.louis.gourmandism.NavigationDirections
import com.louis.gourmandism.data.BrowseRecently
import com.louis.gourmandism.data.Comment
import com.louis.gourmandism.data.User
import com.louis.gourmandism.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

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
//             id= "Emil",
//             shopId= "test002",
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
//

//        var user = User(
//            id = "003",
//            name = "Emil",
//            location = "",
//            currentPosition = "",
//            browseRecently = null,
//            friendList = mutableListOf("002")
//        )


//        db.collection("Comment")
//            .add(comment)
//            .addOnSuccessListener { documentReference ->
//                Log.d("Dialog", "Success")
//            }
//            .addOnFailureListener { e ->
//                Log.d("Dialog", "Error", e)
//            }


        db.collection("Comment").addSnapshotListener { value, e ->
            val commentList = mutableListOf<Comment>()
            value?.let {
                it.forEach { data ->
                    commentList.add(data.toObject(Comment::class.java))
                }
                commentList.sortByDescending { list->list.createdTime }
                adapter.submitList(commentList)
            }
        }

        viewModel.navigationStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.actionGlobalDetailFragment())
                viewModel.onNavigated()
            }
        })


        return binding.root
    }
}