package com.example.ebookapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.navdrawerscratch.R

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var notificationManager: NotificationManager

    private var currentSong: Song? = null

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


        mediaPlayer.setOnCompletionListener {

            stopSelf()
        }

        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_TOGGLE_PLAYBACK -> {

                togglePlayback()
            }
            ACTION_PLAY_PREVIOUS -> {

                playPreviousSong()
            }
            ACTION_PLAY_NEXT -> {

                playNextSong()
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun togglePlayback() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }

        currentSong?.let { updateNotification(it, mediaPlayer.isPlaying) }
    }

    private fun playPreviousSong() {

        currentSong?.let { updateNotification(it, mediaPlayer.isPlaying) }
    }

    private fun playNextSong() {

        currentSong?.let { updateNotification(it, mediaPlayer.isPlaying) }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Player",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateNotification(song: Song, isPlaying: Boolean) {

        val notificationLayout = RemoteViews(packageName, R.layout.custom_notification_layout)


        val bitmap = BitmapFactory.decodeFile(song.albumArtUri)
        notificationLayout.setImageViewBitmap(R.id.notification_album_art, bitmap)
        notificationLayout.setTextViewText(R.id.notification_title, song.title)
        notificationLayout.setTextViewText(R.id.notification_artist, song.artist)


        val playPauseIntent = PendingIntent.getService(
            this,
            REQUEST_CODE_PLAY_PAUSE,
            Intent(this, MusicService::class.java).apply { action = ACTION_TOGGLE_PLAYBACK },
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationLayout.setOnClickPendingIntent(R.id.notification_play_pause, playPauseIntent)

        val previousIntent = PendingIntent.getService(
            this,
            REQUEST_CODE_PLAY_PREVIOUS,
            Intent(this, MusicService::class.java).apply { action = ACTION_PLAY_PREVIOUS },
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationLayout.setOnClickPendingIntent(R.id.notification_previous, previousIntent)

        val nextIntent = PendingIntent.getService(
            this,
            REQUEST_CODE_PLAY_NEXT,
            Intent(this, MusicService::class.java).apply { action = ACTION_PLAY_NEXT },
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationLayout.setOnClickPendingIntent(R.id.notification_next, nextIntent)


        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .build()


        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        notificationManager.cancel(NOTIFICATION_ID)
    }

    companion object {
        const val ACTION_TOGGLE_PLAYBACK = "com.ex.musicplayer.TOGGLE_PLAYBACK"
        const val ACTION_PLAY_PREVIOUS = "com.ex.musicplayer.PLAY_PREVIOUS"
        const val ACTION_PLAY_NEXT = "com.ex.musicplayer.PLAY_NEXT"
        const val CHANNEL_ID = "MusicPlayerChannel"
        const val NOTIFICATION_ID = 1
        const val EXTRA_IS_PLAYING = "extra_is_playing"

        private const val REQUEST_CODE_PLAY_PAUSE = 1001
        private const val REQUEST_CODE_PLAY_PREVIOUS = 1002
        private const val REQUEST_CODE_PLAY_NEXT = 1003
    }
}
