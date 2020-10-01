package com.example.musicplayer.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.musicplayer.model.MusicFiles;

import java.util.ArrayList;
import java.util.HashSet;

public class MusicRepository {

    private static MusicRepository sMusicRepository;
    public static ArrayList<MusicFiles> musicFilesList = new ArrayList<>();
    private static Context mContext;

    private static HashSet<String> mHashSetAlbums = new HashSet<>();
    private static ArrayList<String> mAlbums = new ArrayList<>();

    private static HashSet<String> mHashSetArtists = new HashSet<>();
    private static ArrayList<String> mArtists = new ArrayList<>();

    public static MusicRepository getInstance(Context context) {
        mContext = context;
        if (sMusicRepository == null)
            return new MusicRepository();
        return sMusicRepository;
    }

    private MusicRepository() {

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

                musicFilesList.add(new MusicFiles(data, title, album, artist, songId, albumId, artistId, path));
                mHashSetAlbums.add(album);
                mHashSetArtists.add(artist);
            }
        }
        assert cursor != null;
        cursor.close();

    }

    public ArrayList<MusicFiles> getAllAudio() {
        return musicFilesList;
    }

    public ArrayList<String> getAllAlbums() {
        mAlbums = new ArrayList<String>(mHashSetAlbums);
        return mAlbums;
    }

    public ArrayList<String> getAllArtists() {
        mArtists = new ArrayList<String>(mHashSetArtists);
        return mArtists;
    }

    public ArrayList<MusicFiles> getAllAlbumAudio(String albumName) {

        ArrayList<MusicFiles> musicFilesArrayList = new ArrayList<>();

        for (MusicFiles music : musicFilesList) {
            if (music.getAlbum().equals(albumName))
                musicFilesArrayList.add(music);

        }
        return musicFilesArrayList;

    }

    public ArrayList<MusicFiles> getAllArtistAudio(String artistName) {

        ArrayList<MusicFiles> musicFilesArrayList = new ArrayList<>();

        for (MusicFiles music : musicFilesList) {
            if (music.getArtist().equals(artistName))
                musicFilesArrayList.add(music);

        }
        return musicFilesArrayList;

    }


}
