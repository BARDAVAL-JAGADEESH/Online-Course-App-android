package com.example.ebookapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.navdrawerscratch.R


class CardFragment : Fragment() {

lateinit var editText: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_cardviewfragment, container, false)
        val card1 = view.findViewById<CardView>(R.id.cardView1)
        val card2 = view.findViewById<CardView>(R.id.cardView2)
        val card3 = view.findViewById<CardView>(R.id.cardView3)
        val card4 = view.findViewById<CardView>(R.id.cardView4)
        val card5 = view.findViewById<CardView>(R.id.cardView5)
        val card6 = view.findViewById<CardView>(R.id.cardView6)

        card1.setOnClickListener {
             editText= view.findViewById(R.id.textTitle1)
            openVideoFragment("video2.mp4") }
        card2.setOnClickListener {
            editText=view.findViewById(R.id.textTitle2)
            openVideoFragment("jenivideo.mp4") }
        card3.setOnClickListener {
            editText=view.findViewById(R.id.textTitle3)
            openVideoFragment(";video2.mp4") }
        card4.setOnClickListener {
            editText=view.findViewById(R.id.textTitle4)
            openVideoFragment("learnaddition.mp4") }
        card5.setOnClickListener {
            editText=view.findViewById(R.id.textTitle5)
            openVideoFragment("jenivideo.mp4") }
        card6.setOnClickListener {
            editText=view.findViewById(R.id.textTitle6)
            openVideoFragment("video1.mp4") }

        return view
    }

    private fun openVideoFragment(videoUr: String) {
        val input=editText.text.toString()
        val bundle=Bundle()
        bundle.putString("data",input)
        val videoFragment = VideoFragment()
        videoFragment.arguments=bundle
        val videoUrl = videoUr.removeSuffix(".mp4")
        val videoPath = "android.resource://${requireActivity().packageName}/raw/$videoUrl"

        videoFragment.ok(videoPath)
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, videoFragment)?.addToBackStack(null)?.commit()
    }


}

