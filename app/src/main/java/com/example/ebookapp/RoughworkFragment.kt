package com.example.ebookapp
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.navdrawerscratch.R

class RoughworkFragment : Fragment() {

    private lateinit var customViewDrawing: CustomViewDrawing

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_roughwork, container, false)

        customViewDrawing = view.findViewById(R.id.cd)
        val cb = view.findViewById<Button>(R.id.clear)
        cb.setOnClickListener {
            customViewDrawing.clear()
        }

        return view
    }
}










