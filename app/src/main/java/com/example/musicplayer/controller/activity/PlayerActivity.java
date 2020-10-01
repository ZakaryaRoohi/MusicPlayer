package com.example.musicplayer.controller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.musicplayer.R;
import com.example.musicplayer.controller.fragment.PlayerFragment;

public class PlayerActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "com.example.musicplayer.controller.activity.extraPosition";
    public static final String BUNDLE_POSITION = "bundlePosition";
    private int mPosition;
PlayerFragment mPlayerFragment;

    public static Intent newIntent(Context context, int position) {
        Intent playerIntent = new Intent(context, PlayerActivity.class);
        playerIntent.putExtra(EXTRA_POSITION, position);
        return playerIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        if (savedInstanceState != null)
            mPosition = savedInstanceState.getInt(BUNDLE_POSITION);
        else {
            Intent intent = getIntent();
            mPosition = intent.getIntExtra(EXTRA_POSITION, -1);
        }
        mPlayerFragment = PlayerFragment.newInstance(mPosition);

        FragmentManager fragmentManager = getSupportFragmentManager();

//        Fragment playerFragment = PlayerFragment.newInstance(mPosition);
        fragmentManager
                .beginTransaction()
                .replace(R.id.container_player, mPlayerFragment)
                .commit();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_POSITION, mPosition);
    }
}