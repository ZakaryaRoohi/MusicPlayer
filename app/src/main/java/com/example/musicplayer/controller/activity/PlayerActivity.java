package com.example.musicplayer.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.Utils.MusicUtils;
import com.example.musicplayer.controller.fragment.SongListFragment;
import com.example.musicplayer.model.MusicFiles;

import java.util.List;

public class PlayerActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "com.example.musicplayer.controller.activity.extraPosition";


    private ImageView mBtnBack;
    private ImageView mBtnMenu;
    private ImageView mSongCover;
    private TextView mTextViewSongName;
    private TextView mTextViewSongArtist;
    private ImageView mBtnShuffle;
    private ImageView mBtnPrev;
    private ImageView mBtnPlayPause;
    private ImageView mBtnNext;
    private ImageView mBtnRepeat;

    private MusicFiles mMusic;
    private MusicFiles mCurrentMusicPlayed;

    private int mPosition;
    private List<MusicFiles> mMusicFilesList;
    private SeekBar mSeekBar;
    private Handler mHandler;
    private static MediaPlayer mMediaPlayer;

    public static Intent newIntent(Context context, int position) {
        Intent playerIntent = new Intent(context, PlayerActivity.class);
        playerIntent.putExtra(EXTRA_POSITION, position);
        return playerIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        mPosition = intent.getIntExtra(EXTRA_POSITION, -1);
        mMusicFilesList = MainActivity.getAllAudio(this);
        if (mPosition >= 0)
            mMusic = mMusicFilesList.get(mPosition);
        findViews();
        setListeners();


        mHandler = new Handler();
        mMusicFilesList = MainActivity.mMusicFiles;

            mMediaPlayer = new MediaPlayer();
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        mMediaPlayer.setAudioAttributes(audioAttributes);

        mMediaPlayer.reset();
        mMediaPlayer.stop();
//        mMediaPlayer = new MediaPlayer();
        MusicUtils.playAudio(mMediaPlayer,mMusic.getData());
        mBtnPlayPause.setImageResource(R.drawable.ic_baseline_pause_24);
        mCurrentMusicPlayed = mMusic;
initViews();


       runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null) {
                    int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                    mSeekBar.setProgress((int) (((double) (mCurrentPosition) / mMediaPlayer.getDuration()) * 100));
                }
                mHandler.postDelayed(this, 1000);
            }
        });

//        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mMediaPlayer != null) {
//                            int mCurrentPosition = mMediaPlayer.getCurrentPosition();
//
//                            mSeekBar.setProgress((int) (((double) (mCurrentPosition) / mMediaPlayer.getDuration()) * 100));
//
//                        }
//                        mHandler.postDelayed(this, 1000);
//                    }
//                });
//            }
//        });
    }


private void initViews(){
    byte[] art = getAlbumArt(mMusic.getData());
    if (art != null) {
        mSongCover.setImageBitmap(BitmapFactory.decodeByteArray(art, 0, art.length));
    } else {
        mSongCover.setImageResource(R.drawable.song_bytes_image);
    }
    mTextViewSongName.setText(mMusic.getTitle());
    mTextViewSongArtist.setText(mMusic.getArtist());

}
    private byte[] getAlbumArt(String data){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(data);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    private void findViews() {
        mBtnBack = findViewById(R.id.back_btn);
        mBtnMenu = findViewById(R.id.menu_btn);
        mSongCover = findViewById(R.id.song_cover);
        mTextViewSongName = findViewById(R.id.song_name);
        mTextViewSongArtist = findViewById(R.id.song_artist);
        mBtnShuffle = findViewById(R.id.id_shuffle);
        mBtnPrev = findViewById(R.id.id_prev);
        mBtnPlayPause = findViewById(R.id.play_pause);
        mBtnNext = findViewById(R.id.id_next);
        mBtnRepeat = findViewById(R.id.id_repeat);
        mSeekBar = findViewById(R.id.seek_bar);

    }

    private void setListeners() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int currentPosition;

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                this.currentPosition = i;
                if (b) {
//                    mMediaPlayer.seekTo((i * mMediaPlayer.getDuration()) / 100);
//                    (int) ((double) (mMediaPlayer.getDuration() * i) / 100);
                }
                Log.d("position", i + "");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("position2", "5655");
//                seekBar.setProgress(mMediaPlayer.getCurrentPosition());
//                mMediaPlayer.seekTo( (int) ((double) (mMediaPlayer.getDuration() * currentPosition) / 100));
                mMediaPlayer.seekTo((int) ((double) (mMediaPlayer.getDuration() * currentPosition) / 100));
            }
        });
        mBtnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mBtnPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                } else {
                    mMediaPlayer.start();
                    mBtnPlayPause.setImageResource(R.drawable.ic_baseline_pause_24);

                }
            }
        });
    }
}