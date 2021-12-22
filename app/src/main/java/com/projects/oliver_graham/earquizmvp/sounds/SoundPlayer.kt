package com.projects.oliver_graham.earquizmvp.sounds

import android.app.Application
import androidx.compose.runtime.Immutable
import android.media.AudioAttributes
import android.media.SoundPool

private const val MAX_STREAMS = 2
private const val START_PITCH = 40
private const val END_PITCH = 65

@Immutable
class SoundPlayer(
    private val application: Application
) {

    private lateinit var soundPool: SoundPool
    private var soundPoolMap: MutableMap<Int, Int>

    init {
        buildSoundPool()
        soundPoolMap = mutableMapOf()
        initializeSounds()
    }

    private var note1 = 0
    private var note2 = 0

    fun setCurrentSequence(note1: Int, note2: Int) {
        this.note1 = note1
        this.note2 = note2
    }

    fun playNote1() {
        playNote(note1)
    }
    fun playNote2() {
        playNote(note2)
    }

    private fun playNote(pitch: Int) {
        soundPool.play(
            soundPoolMap.getValue(key = pitch), 1f, 1f, 0, 0, 1f)
    }

    private fun buildSoundPool(maxStreams: Int = MAX_STREAMS) {
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(maxStreams)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    private fun initializeSounds() {
        for (i in START_PITCH..END_PITCH)
            soundPoolMap[i] = soundPool.load(application,
                application.resources.getIdentifier("pitch$i", "raw", application.packageName),
                0
            )

    }
}











