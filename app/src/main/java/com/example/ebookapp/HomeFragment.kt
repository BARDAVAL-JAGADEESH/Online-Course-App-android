package com.example.ebookapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.example.navdrawerscratch.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_homefragment, container, false)
        val imageList = ArrayList<SlideModel>()


        imageList.add(SlideModel(R.drawable.onlinecourse))
        imageList.add(SlideModel(R.drawable.courseonlinepic))
        imageList.add(SlideModel(R.drawable.onlinecourse))

        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)

        val card1 = view.findViewById<CardView>(R.id.cardView1)
        val card2 = view.findViewById<CardView>(R.id.cardView2)
        val card3 = view.findViewById<CardView>(R.id.cardView3)
        val card4 = view.findViewById<CardView>(R.id.cardView4)

        card1.setOnClickListener {
            openVideoFragment(CardFragment()) }
        card2.setOnClickListener { openVideoFragment(CardFragment()) }
        card3.setOnClickListener { openVideoFragment(CardFragment()) }
        card4.setOnClickListener { openVideoFragment(CardFragment()) }


        return view
    }

    private fun openVideoFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container,fragment)?.addToBackStack(null)?.commit()

    }


}
