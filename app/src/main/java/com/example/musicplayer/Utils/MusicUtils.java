package com.example.musicplayer.Utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.widget.Toast;


public class MusicUtils {


    public static void playAssetSound(MediaPlayer mediaPlayer, Context context, String soundFileName) {
        try {
//            AssetFileDescriptor descriptor = context.getAssets().openFd(soundFileName);
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
    public static void playAudio(MediaPlayer mediaPlayer,String data) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(data);
            mediaPlayer.prepare();
            mediaPlayer.setVolume(1f, 1f);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
