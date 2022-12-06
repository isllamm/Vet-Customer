package com.tawajood.vet.ui.main.dialogs
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.tawajood.vet.databinding.DialogResultBinding
import com.tawajood.vet.ui.main.MainActivity
import com.tawajood.vet.utils.Constants


import kotlin.properties.Delegates

class ResultDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(msg: String, img: Int): ResultDialogFragment {
            val args = Bundle()
            args.putString(Constants.MSG, msg)
            args.putInt(Constants.IMAGE, img)
            val f = ResultDialogFragment()
            f.arguments = args
            return f
        }
    }


    private lateinit var binding: DialogResultBinding
    private lateinit var msg: String
    private lateinit var parent: MainActivity
    private var img by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogResultBinding.inflate(inflater)
        msg = requireArguments().getString(Constants.MSG, "")
        img = requireArguments().getInt(Constants.IMAGE)
        parent = requireActivity() as MainActivity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        onClick()
    }

    private fun setupUI() {
        val width = (resources.displayMetrics.widthPixels * 0.80).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window?.setLayout(width, height)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.msgTv.text = msg
        Glide.with(requireContext())
            .load(img)
            .into(binding.statusImg)
    }

    private fun onClick() {
        binding.okTv.setOnClickListener {
            parent.navController.popBackStack()
            dismiss()
        }
    }
}