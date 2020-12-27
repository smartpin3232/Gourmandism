package com.louis.gourmandism.event

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.louis.gourmandism.R
import com.louis.gourmandism.databinding.DialogJoinBinding


class EventJoinDialog : DialogFragment() {

    var message: String? = null
    private val messageType by lazy {
        EventJoinDialogArgs.fromBundle(requireArguments()).MessageType
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.custom_dialog)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogJoinBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.dialog = this
        init()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({ this.dismiss() }, 2000)
    }

    private fun init() {
        when (messageType) {
            MessageType.JOIN -> {
                message = "加入成功"
            }
            MessageType.LEAVE -> {
                message = "退出成功"
            } else -> {

            }
        }
    }

    enum class MessageType(val value: Message) {
        JOIN(Message()),
        LEAVE(Message())
    }

    interface IMessage {
        var message: String
    }

    class Message : IMessage {
        private var _message = ""
        override var message: String
            get() = _message
            set(value) { _message = value }
    }
}