package com.projects.oliver_graham.earquizmvp.sounds

import android.app.Application
import androidx.compose.runtime.Immutable
import android.media.AudioAttributes
import android.media.SoundPool
import com.projects.oliver_graham.earquizmvp.data.quiz.Quiz
import kotlinx.coroutines.delay

private const val MAX_STREAMS = 3       // change to play more notes simultaneously
private const val START_PITCH = 40      // c3
private const val END_PITCH = 64        // c5

@Immutable
class SoundPlayer(
    private val application: Application
) {

    private lateinit var soundPool: SoundPool
    private val soundPoolMap: MutableMap<Int, Int> = mutableMapOf()
    private val pitches: MutableList<Int> = mutableListOf()

    init {
        buildSoundPool()
        initializeSounds()
    }

    fun addPitches(pitchList: List<Int>) { pitchList.forEach { pitch -> pitches.add(pitch) }  }
    fun emptyPitchList() { pitches.clear() }

    suspend fun play(
        quiz: Quiz,
        randomInterval: Boolean,
    ) {
        when (quiz) {
            Quiz.EasyChordQuiz -> TODO()
            Quiz.FakeQuiz -> TODO()
            Quiz.HarmonicIntervalQuiz -> playHarmonicInterval()
            Quiz.MediumChordQuiz -> TODO()
            Quiz.MelodicIntervalQuiz -> playMelodicInterval()
            Quiz.RandomIntervalsQuiz -> {
                if (randomInterval) playHarmonicInterval() else playMelodicInterval()
            }
            Quiz.RandomMixQuiz -> TODO()
        }
    }

    private suspend fun playMelodicInterval() {
        playNote(pitches[0])
        delay(timeMillis = 1200)
        playNote(pitches[1])
        delay(timeMillis = 1500)
    }

    private suspend fun playHarmonicInterval() {
        playNote(pitches[0])
        playNote(pitches[1])
        delay(timeMillis = 1500)
    }

    // TODO: can likely use all play() functions as this single function
    private suspend fun playChord() {
        pitches.forEach { pitch ->
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
        for (i in START_PITCH..END_PITCH)               // inclusive range
            soundPoolMap[i] = soundPool.load(application,
                application.resources.getIdentifier("pitch$i", "raw", application.packageName),
                0
            )

    }
}











