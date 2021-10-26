/*
 soundpool stuff (possibly working, kinda)

  //var soundToPlay: Int = 0
  val audioAttributes: AudioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(1)
        .setAudioAttributes(audioAttributes)
        .build()

        val assetManager = application.applicationContext.assets
        val currentNote = assetManager.openFd("notes/AE.ogg")
        soundToPlay = soundPool.load(currentNote, 0)

     // soundPool.play(soundToPlay, 1f, 1f, 0, 0, 0f)
 */