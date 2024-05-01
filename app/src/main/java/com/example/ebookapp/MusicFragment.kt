package com.example.ebookapp
import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybook.SongsAdapter
import com.example.navdrawerscratch.R

class MusicFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var songsAdapter: SongsAdapter
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var wakeLock: PowerManager.WakeLock
    private var currentSongIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        seekBar = view.findViewById(R.id.seekBar)
        nextButton = view.findViewById(R.id.nextButton)
        previousButton = view.findViewById(R.id.previousButton)


        val powerManager = requireContext().getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ON_AFTER_RELEASE,
            "MusicPlayer::WakeLock"
        )
        wakeLock.acquire()

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer().apply {
            setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            setOnCompletionListener {
                playNextSong()
            }
        }


        recyclerView.layoutManager = LinearLayoutManager(context)


        requestPermissions()


        nextButton.setOnClickListener { playNextSong() }
        previousButton.setOnClickListener { playPreviousSong() }

        return view
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            loadSongs()
        }
    }

    private fun loadSongs() {
        val songList = ArrayList<Song>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val cursor = requireContext().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )

        cursor?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val path = cursor.getString(pathColumn)
                val albumId = cursor.getLong(albumIdColumn)
                val albumArtUri = getAlbumArtUri(albumId)

                val song = Song(title, artist, path, albumArtUri)
                songList.add(song)
            }
        }

        songsAdapter = SongsAdapter(songList) { song ->
            // Handle song selection and playback
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            currentSongIndex = songList.indexOf(song)
            playSong(song)
        }
        recyclerView.adapter = songsAdapter
    }

    private fun playSong(song: Song) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(song.path)
            mediaPlayer.prepare()
            mediaPlayer.start()
            // Set isPlaying property to true for the selected song
            songsAdapter.updatePlayingSong(currentSongIndex)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playNextSong() {
        if (currentSongIndex != -1 && currentSongIndex < songsAdapter.itemCount - 1) {
            currentSongIndex++
            val nextSong = songsAdapter.getItem(currentSongIndex)
            playSong(nextSong)
        }
    }

    private fun playPreviousSong() {
        if (currentSongIndex != -1 && currentSongIndex > 0) {
            currentSongIndex--
            val previousSong = songsAdapter.getItem(currentSongIndex)
            playSong(previousSong)
        }
    }

    private fun getAlbumArtUri(albumId: Long): String {
        val albumArtUri = ContentUris.withAppendedId(
            Uri.parse("content://media/external/audio/albumart"),
            albumId
        )
        return albumArtUri.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Release MediaPlayer resources
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }
}


