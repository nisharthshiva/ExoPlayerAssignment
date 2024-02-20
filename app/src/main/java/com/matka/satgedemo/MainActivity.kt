package com.matka.satgedemo

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.matka.satgedemo.databinding.ActivityMainBinding


class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    private var isFullScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong("playbackPosition")
            playWhenReady = savedInstanceState.getBoolean("playWhenReady", true)
        }
        preparePlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("playbackPosition", playbackPosition)
        outState.putBoolean("playWhenReady", playWhenReady)
        outState.putBoolean("isFullScreen", isFullScreen)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        playbackPosition = savedInstanceState.getLong("playbackPosition")
        playWhenReady = savedInstanceState.getBoolean("playWhenReady", true)
    }

    private fun setView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.fullscreen.setOnClickListener {
            requestedOrientation = if (isFullScreen){
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }else{
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            toggleFullScreen()
        }
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        if (requestedOrientation==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            toggleFullScreen()
        }
    }

    private fun toggleFullScreen() {
        if (isFullScreen) {
            // Exit fullscreen
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(window, binding.root).show(WindowInsetsCompat.Type.systemBars())
            val layoutParams = binding.root.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            binding.root.layoutParams = layoutParams
        } else {
            // Enter fullscreen
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, binding.root).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.hide(WindowInsetsCompat.Type.navigationBars())
                controller.hide(WindowInsetsCompat.Type.statusBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            val layoutParams = binding.root.layoutParams
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            binding.root.layoutParams = layoutParams
        }
        isFullScreen = !isFullScreen
    }

    private fun preparePlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer?.playWhenReady = true
        binding.playerView.player = exoPlayer
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaItem = MediaItem.fromUri(URL)
        val mediaSource =
            HlsMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)
        exoPlayer?.apply {
            setMediaSource(mediaSource)
            seekTo(playbackPosition)
            playWhenReady = playWhenReady
            prepare()
        }
    }

    private fun releasePlayer() {
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            playWhenReady = player.playWhenReady
            player.release()
            exoPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    companion object {
        private const val URL = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"
    }

    override fun onBackPressed() {
        if (isFullScreen) {
            // If in fullscreen mode, toggle fullscreen to exit
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            toggleFullScreen()
        } else {
            // If not in fullscreen mode, proceed with default back button behavior
            super.onBackPressed()
        }
    }
}