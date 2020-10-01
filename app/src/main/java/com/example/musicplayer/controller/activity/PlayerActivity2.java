package com.example.musicplayer.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.musicplayer.R;
import com.example.musicplayer.controller.fragment.PlayerFragment;

public class PlayerActivity2 extends AppCompatActivity {
    public static final String EXTRA_POSITION = "com.example.musicplayer.controller.activity.extraPosition";
    private int mPosition;

    public static Intent newIntent(Context context, int position) {
        Intent playerIntent = new Intent(context, PlayerActivity2.class);
        playerIntent.putExtra(EXTRA_POSITION, position);
        return playerIntent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player2);

        Intent intent = getIntent();
        mPosition = intent.getIntExtra(EXTRA_POSITION, -1);
        FragmentManager fragmentManager= getSupportFragmentManager();

        Fragment playerFragment = PlayerFragment.newInstance(mPosition);
        fragmentManager
                .beginTransaction()
                .replace(R.id.container_player,playerFragment)
                .commit();

    }
}