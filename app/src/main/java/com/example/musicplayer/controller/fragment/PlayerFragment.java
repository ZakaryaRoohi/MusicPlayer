package com.example.musicplayer.controller.fragment;

import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.Utils.MusicUtils;
import com.example.musicplayer.model.MusicFiles;
import com.example.musicplayer.repository.MusicRepository;
import com.example.musicplayer.repository.PlayerRepository;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends Fragment {

    public static final String EXTRA_POSITION = "com.example.musicplayer.controller.activity.extraPosition";
    public static final String EXTRA_MEDIA_PLAYER = "extraMediaPlayer";
    public static final String EXTRA_CURRENT_MUSIC_PLAYED = "extraCurrentMusicPlayed";
    public static final String BUNDLE_POSITION = "bundlePosition";
    public static final String BUNDLE_SAVE_INSTANCE_POSITION = "bundleSaveInstance";


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
    public MusicFiles mCurrentMusicPlayed;

    private int mPosition;
    private List<MusicFiles> mMusicFilesList;
    private SeekBar mSeekBar;
    private Handler mHandler;
    private static MediaPlayer mMediaPlayer;

    private PlayerFragment() {
        // Required empty public constructor
    }


    public static PlayerFragment newInstance(int position) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        if (savedInstanceState != null)
            mPosition = savedInstanceState.getInt(BUNDLE_SAVE_INSTANCE_POSITION);
        else
            mPosition = getArguments().getInt(BUNDLE_POSITION);

        mMusicFilesList = MusicRepository.getInstance(getActivity().getApplicationContext()).getAllAudio();

        if (mPosition >= 0)
            mMusic = mMusicFilesList.get(mPosition);
//        findViews();
        mMediaPlayer = PlayerRepository.getInstance().getMediaPlayer();
        mHandler = new Handler();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_SAVE_INSTANCE_POSITION, mPosition);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        findViews(view);

        setListeners();



//        mMediaPlayer.reset();
//        mMediaPlayer.stop();
//        MusicUtils.playAudio(mMediaPlayer, mMusic.getData());
        mBtnPlayPause.setImageResource(R.drawable.ic_baseline_pause_24);

        mCurrentMusicPlayed = mMusic;
        initViews(mMusic);


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null) {
                    int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                    mSeekBar.setProgress((int) (((double) (mCurrentPosition) / mMediaPlayer.getDuration()) * 100));
                }
                if (!mMediaPlayer.isPlaying())
                    mBtnPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);

                mHandler.postDelayed(this, 1000);
            }
        });


        return view;
    }


    private void initViews(MusicFiles music) {
        byte[] art = getAlbumArt(music.getData());
        if (art != null) {
            mSongCover.setImageBitmap(BitmapFactory.decodeByteArray(art, 0, art.length));
        } else {
            mSongCover.setImageResource(R.drawable.song_bytes_image);
        }
        mTextViewSongName.setText(music.getTitle());
        mTextViewSongArtist.setText(music.getArtist());


    }

    private byte[] getAlbumArt(String data) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(data);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    private void findViews(View view) {
        mBtnBack = view.findViewById(R.id.back_btn);
        mBtnMenu = view.findViewById(R.id.menu_btn);
        mSongCover = view.findViewById(R.id.song_cover);
        mTextViewSongName = view.findViewById(R.id.song_name);
        mTextViewSongArtist = view.findViewById(R.id.song_artist);
        mBtnShuffle = view.findViewById(R.id.id_shuffle);
        mBtnPrev = view.findViewById(R.id.id_prev);
        mBtnPlayPause = view.findViewById(R.id.play_pause);
        mBtnNext = view.findViewById(R.id.id_next);
        mBtnRepeat = view.findViewById(R.id.id_repeat);
        mSeekBar = view.findViewById(R.id.seek_bar);

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


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicFiles nextMusic;
                if (mCurrentMusicPlayed != null) {


                    int currentMusicIndex = mMusicFilesList.indexOf(mCurrentMusicPlayed);
                    if (currentMusicIndex == mMusicFilesList.size() - 1)
                        nextMusic = mMusicFilesList.get(0);
                    else
                        nextMusic = mMusicFilesList.get(currentMusicIndex + 1);

                    mMediaPlayer.stop();
                    MusicUtils.playAudio(mMediaPlayer, nextMusic.getData());
                    mCurrentMusicPlayed = nextMusic;
                    mBtnPlayPause.setImageResource(R.drawable.ic_baseline_pause_24);
                    initViews(nextMusic);
                }
            }
        });
        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicFiles prevMusic;
                if (mCurrentMusicPlayed != null) {


                    int currentMusicIndex = mMusicFilesList.indexOf(mCurrentMusicPlayed);
                    if (currentMusicIndex == 0)
                        prevMusic = mMusicFilesList.get(mMusicFilesList.size() - 1);
                    else
                        prevMusic = mMusicFilesList.get(currentMusicIndex - 1);

                    mMediaPlayer.stop();
                    MusicUtils.playAudio(mMediaPlayer, prevMusic.getData());
                    mCurrentMusicPlayed = prevMusic;
                    mBtnPlayPause.setImageResource(R.drawable.ic_baseline_pause_24);
                    initViews(prevMusic);
                }
            }
        });
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        mBtnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PlayerRepository.RepeatFlag) {
                    mBtnRepeat.setImageResource(R.drawable.ic_baseline_repeat_24_blue);
                    PlayerRepository.RepeatFlag = true;
                    mMediaPlayer.setLooping(true);
                } else {
                    mBtnRepeat.setImageResource(R.drawable.ic_baseline_repeat_24);
                    PlayerRepository.RepeatFlag = false;
                    mMediaPlayer.setLooping(false);

                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        PlayerRepository.setCurrentMusicPlayed(mCurrentMusicPlayed);

    }
}