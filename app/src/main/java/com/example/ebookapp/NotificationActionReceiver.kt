package com.example.ebookapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        when (intent?.action) {
            MusicService.ACTION_PLAY_PREVIOUS -> {

                val previousIntent = Intent(context, MusicService::class.java).apply {
                    action = MusicService.ACTION_PLAY_PREVIOUS
                }
                context?.startService(previousIntent)
            }
            MusicService.ACTION_TOGGLE_PLAYBACK -> {

                val toggleIntent = Intent(context, MusicService::class.java).apply {
                    action = MusicService.ACTION_TOGGLE_PLAYBACK
                }
                context?.startService(toggleIntent)
            }
            MusicService.ACTION_PLAY_NEXT -> {

                val nextIntent = Intent(context, MusicService::class.java).apply {
                    action = MusicService.ACTION_PLAY_NEXT
                }
                context?.startService(nextIntent)
            }
            else -> {
                // Do nothing
            }
        }
    }
}
