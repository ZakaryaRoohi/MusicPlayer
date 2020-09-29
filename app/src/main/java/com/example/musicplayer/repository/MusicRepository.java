package com.example.musicplayer.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.musicplayer.model.MusicFiles;

import java.util.ArrayList;
import java.util.List;

public class MusicRepository {

    private static MusicRepository sMusicRepository;
    public static ArrayList<MusicFiles> musicFilesList= new ArrayList<>();
    private static Context mContext;
    public static MusicRepository getInstance(Context context){
        mContext = context;
        if (sMusicRepository==null)
            return new MusicRepository();
        return sMusicRepository;
    }
    private MusicRepository(){
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String songId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String artistId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

                musicFilesList.add(new MusicFiles(data, title, album, artist, songId, albumId, artistId,path));
            }
        }
        assert cursor != null;
        cursor.close();

    }
    public ArrayList<MusicFiles> getAllAudio(){
        return musicFilesList;
    }


}
