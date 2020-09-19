package com.example.musicplayer.Utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;


public class MusicUtils {


    public static void playAssetSound(MediaPlayer mediaPlayer, Context context, String soundFileName) {
        try {
            AssetFileDescriptor descriptor = context.getAssets().openFd(soundFileName);
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mediaPlayer.prepare();
            mediaPlayer.setVolume(1f, 1f);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
