package com.example.ebookapp

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import com.example.navdrawerscratch.R


class VideoFragment : Fragment() {
    private lateinit var videoView: VideoView
    private lateinit var videoUr: String
    private lateinit var textview: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_videofragment, container, false)
        videoView = view.findViewById<VideoView>(R.id.videoView)
        textview=view.findViewById(R.id.videotopic)
        return view;
    } fun ok(videoUrl: String){
        videoUr=videoUrl
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playVideo(videoUr)


    }
    fun playVideo(videoUrl: String) {


        val args=this.arguments
        val inputData=args?.get("data")
        textview.text=inputData.toString()


        videoView.setVideoURI(Uri.parse(videoUrl))


        val mediaController = MediaController(requireContext())


        mediaController.setAnchorView(videoView)


        videoView.setMediaController(mediaController)

        // Start playing video
        videoView.start()
    }



}