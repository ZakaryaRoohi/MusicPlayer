package com.example.musicplayer.repository;

import android.media.AudioAttributes;
import android.media.MediaPlayer;

import java.util.ArrayList;

public class PlayerRepository {

    private static PlayerRepository sPlayerRepository;
    private static MediaPlayer mMediaPlayer;
    public static PlayerRepository getInstance() {
        if (sPlayerRepository == null)
            sPlayerRepository = new PlayerRepository();
        return sPlayerRepository;
    }

    private PlayerRepository() {

        mMediaPlayer = new MediaPlayer();
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        mMediaPlayer.setAudioAttributes(audioAttributes);

        }
    public MediaPlayer getMediaPlayer(){

        return mMediaPlayer;

    }
}
