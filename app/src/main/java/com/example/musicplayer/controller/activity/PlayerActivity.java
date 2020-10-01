package com.example.musicplayer.controller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.musicplayer.R;
import com.example.musicplayer.controller.fragment.PlayerFragment;
import com.example.musicplayer.controller.fragment.SongListFragment;

public class PlayerActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "com.example.musicplayer.controller.activity.extraPosition";
    public static final String BUNDLE_POSITION = "bundlePosition";
    public static final String EXTRA_ALBUM_NAME = "extraAlbumName";
    public static final String EXTRA_ARTIST_NAME = "extraArtistName";
    private int mPosition;
    private String mAlbumName;
    private String mArtistName;
    PlayerFragment mPlayerFragment;
    SongListFragment mSongListFragment;
    public static Intent newIntent(Context context, int position) {
        Intent playerIntent = new Intent(context, PlayerActivity.class);
        playerIntent.putExtra(EXTRA_POSITION, position);
        return playerIntent;
    }

    public static Intent newIntentForAlbum(Context context, String album) {
        Intent playerIntent = new Intent(context, PlayerActivity.class);
        playerIntent.putExtra(EXTRA_ALBUM_NAME, album);
        return playerIntent;
    }
    public static Intent newIntentForArtist(Context context, String artist) {
        Intent playerIntent = new Intent(context, PlayerActivity.class);
        playerIntent.putExtra(EXTRA_ARTIST_NAME, artist);
        return playerIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
//        if (savedInstanceState != null)
//            mPosition = savedInstanceState.getInt(BUNDLE_POSITION);
//        else {
//            Intent intent = getIntent();
//            mPosition = intent.getIntExtra(EXTRA_POSITION, -1);
//        }
        Intent intent = getIntent();
        mPosition = intent.getIntExtra(EXTRA_POSITION, -1);
        mAlbumName = intent.getStringExtra(EXTRA_ALBUM_NAME);
        mArtistName = intent.getStringExtra(EXTRA_ARTIST_NAME);
        if (mPosition != -1){
            mPlayerFragment = PlayerFragment.newInstance(mPosition);
        FragmentManager fragmentManager = getSupportFragmentManager();

//        Fragment playerFragment = PlayerFragment.newInstance(mPosition);
        fragmentManager
                .beginTransaction()
                .replace(R.id.container_player, mPlayerFragment)
                .commit();
        }
        else if(mAlbumName!=null){
            mSongListFragment = SongListFragment.newInstanceForAlbum(mAlbumName);
            FragmentManager fragmentManager = getSupportFragmentManager();

//        Fragment playerFragment = PlayerFragment.newInstance(mPosition);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container_player, mSongListFragment)
                    .commit();
        }
        else if(mArtistName!=null){
            mSongListFragment = SongListFragment.newInstanceForArtist(mArtistName);
            FragmentManager fragmentManager = getSupportFragmentManager();

//        Fragment playerFragment = PlayerFragment.newInstance(mPosition);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container_player, mSongListFragment)
                    .commit();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_POSITION, mPosition);
    }
}