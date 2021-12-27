package com.projects.oliver_graham.earquizmvp.sounds

import android.app.Application
import androidx.compose.runtime.Immutable
import android.media.AudioAttributes
import android.media.SoundPool
import kotlinx.coroutines.delay

private const val MAX_STREAMS = 3
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

    suspend fun play(quizIndex: Int, pitches: List<Int>) {
        when (quizIndex) {
            0 -> playMelodicInterval(pitch1 = pitches[0], pitch2 = pitches[1])
            1 -> playHarmonicInterval(pitch1 = pitches[0], pitch2 = pitches[1])
            2 -> playChord(pitchList = pitches)
        }
    }

    private suspend fun playMelodicInterval(pitch1: Int, pitch2: Int) {
        playNote(pitch1)
        delay(timeMillis = 1200)
        playNote(pitch2)
        delay(timeMillis = 1500)
    }

    private suspend fun playHarmonicInterval(pitch1: Int, pitch2: Int) {
        playNote(pitch1)
        playNote(pitch2)
        delay(timeMillis = 1500)
    }

    private suspend fun playChord(pitchList: List<Int>) {
        pitchList.forEach { pitch ->
            playNote(pitch = pitch)
        }
        delay(timeMillis = 1500)
    }

    private fun playNote(pitch: Int) {
        soundPool.play(soundPoolMap.getValue(key = pitch), 1f, 1f, 0, 0, 1f)
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

    // load all sounds from .ogg files into SoundPool
    private fun initializeSounds() {
        for (i in START_PITCH..END_PITCH)
            soundPoolMap[i] = soundPool.load(application,
                application.resources.getIdentifier("pitch$i", "raw", application.packageName),
                0
            )

    }
}











