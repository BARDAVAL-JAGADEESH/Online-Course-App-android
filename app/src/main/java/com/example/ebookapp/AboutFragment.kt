package com.example.ebookapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.navdrawerscratch.R

class AboutFragment : Fragment() {

    private lateinit var youtubeId: ImageView
    private lateinit var emailId: TextView
    private lateinit var linkedInUrl: TextView
    private lateinit var instaId: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_about, container, false)

        youtubeId = view.findViewById(R.id.youtube_id)
        emailId = view.findViewById(R.id.email_id)

        instaId = view.findViewById(R.id.insta_id)
        linkedInUrl = view.findViewById(R.id.lindin_url)
        youtubeId.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCzLGchsyCR0p8YDrsWIC-uw"))
            startActivity(intent)
        }

        emailId.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:jagadeeshbardaval78@gmail.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, "FROM BACK YOUR Book")
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }

        linkedInUrl.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/bardaval-jagadeesh/"))
            startActivity(intent)
        }

        instaId.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/jagadeesh_0078?igsh=MWh2enhlODRta2lqbQ=="))
            startActivity(intent)
        }

        return view
    }
}
