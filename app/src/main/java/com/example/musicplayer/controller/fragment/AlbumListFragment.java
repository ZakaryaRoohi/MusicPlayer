package com.example.musicplayer.controller.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.Utils.MusicUtils;
import com.example.musicplayer.controller.activity.PlayerActivity;
import com.example.musicplayer.model.MusicFiles;
import com.example.musicplayer.repository.MusicRepository;
import com.example.musicplayer.repository.PlayerRepository;

import java.util.List;

public class AlbumListFragment extends Fragment {
    public static final int PLAYER_ACTIVITY_REQUEST_CODE = 1;
    public static final int SPAN_COUNT = 3;
    public static final String TAG = "BBF";

    private SeekBar mSeekBar;
    private Runnable mRunnable;
    private Handler mHandler;
    private RecyclerView mRecyclerView;

    private MediaPlayer mMediaPlayer;
    private LinearLayout mLinearLayoutPlayer;
    private List<MusicFiles> mMusicFilesList;
    private List<String> mAlbumsList;

    private MusicFiles mCurrentMusicPlayed;
    private ImageView mBtnShuffle;
    private ImageView mBtnPrev;
    private ImageView mBtnPlayPause;
    private ImageView mBtnNext;
    private ImageView mBtnRepeat;
    private TextView mTextViewSongName;

    public AlbumListFragment() {
        // Required empty public constructor
    }

    public static AlbumListFragment newInstance() {
        AlbumListFragment fragment = new AlbumListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCrete");

        setRetainInstance(true);
        mHandler = new Handler();
        mMusicFilesList = MusicRepository.getInstance(getActivity().getApplicationContext()).getAllAudio();
        mAlbumsList = MusicRepository.getInstance(getActivity().getApplicationContext()).getAllAlbums();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album_list, container, false);

        findViews(view);
        setListeners();
        initViews();
        mLinearLayoutPlayer.setVisibility(View.INVISIBLE);
        AlbumListFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null) {
                    int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                    mSeekBar.setProgress((int) (((double) (mCurrentPosition) / mMediaPlayer.getDuration()) * 100));
                }

                mHandler.postDelayed(this, 1000);
            }
        });

        return view;
    }


    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_albums);
        mLinearLayoutPlayer = view.findViewById(R.id.layout2);

        mBtnShuffle = view.findViewById(R.id.id_shuffle_float);
        mBtnPrev = view.findViewById(R.id.id_prev_float);
        mBtnPlayPause = view.findViewById(R.id.play_pause_float);
        mBtnNext = view.findViewById(R.id.id_next_float);
        mBtnRepeat = view.findViewById(R.id.id_repeat_float);
        mSeekBar = view.findViewById(R.id.seek_bar_float);
        mTextViewSongName = view.findViewById(R.id.song_name_float);

    }

    private void initViews() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initUI();
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
                    initFloatViews(nextMusic);

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
                    initFloatViews(prevMusic);

                }
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

    private void initUI() {

        AlbumAdapter adapter = new AlbumAdapter(mAlbumsList);
        mRecyclerView.setAdapter(adapter);
    }

    private class AlbumHolder extends RecyclerView.ViewHolder {

        SongListFragment mSongListFragment;
        private TextView mTextViewAlbumName;
        private String mAlbum;
        private ImageView mImageViewAlbumImage;

        public AlbumHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewAlbumName = itemView.findViewById(R.id.album_name);
            mImageViewAlbumImage = itemView.findViewById(R.id.album_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {

                    Intent intent = PlayerActivity.newIntentForAlbum(getActivity(),mAlbum);
                    startActivityForResult(intent, PLAYER_ACTIVITY_REQUEST_CODE);


                }
            });
        }
//

        public void bindMusic(String album) {
            mAlbum = album;
            mTextViewAlbumName.setText(album);

        }

    }

    private class AlbumAdapter extends RecyclerView.Adapter<AlbumHolder> {

        private List<String> mAlbums;

        public AlbumAdapter(List<String> albums) {
            mAlbums = albums;
        }

        @NonNull
        @Override
        public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.album_items, parent, false);

            return new AlbumHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
            String album = mAlbums.get(position);
            holder.bindMusic(album);
        }

        @Override
        public int getItemCount() {
            return mAlbums.size();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PLAYER_ACTIVITY_REQUEST_CODE) {
//            mMediaPlayer = PlayerRepository.getInstance().getMediaPlayer();
//            if (mMediaPlayer.isPlaying()) {
//                mLinearLayoutPlayer.setVisibility(View.VISIBLE);
//                if (mMediaPlayer.isPlaying())
//                    mBtnPlayPause.setImageResource(R.drawable.ic_baseline_pause_24);
//                else
//                    mBtnPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
//                mCurrentMusicPlayed = PlayerRepository.getCurrentMusicPlayed();
//                initFloatViews(mCurrentMusicPlayed);
//            }
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMediaPlayer = PlayerRepository.getInstance().getMediaPlayer();
        if (mMediaPlayer.isPlaying()) {
            mLinearLayoutPlayer.setVisibility(View.VISIBLE);
            if (mMediaPlayer.isPlaying())
                mBtnPlayPause.setImageResource(R.drawable.ic_baseline_pause_24);
            else
                mBtnPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            mCurrentMusicPlayed = PlayerRepository.getCurrentMusicPlayed();
            initFloatViews(mCurrentMusicPlayed);
        }
    }


    private void initFloatViews(MusicFiles music) {

        mTextViewSongName.setText(music.getTitle());
        if (!PlayerRepository.RepeatFlag) {
            mBtnRepeat.setImageResource(R.drawable.ic_baseline_repeat_24_blue);
        } else {
            mBtnRepeat.setImageResource(R.drawable.ic_baseline_repeat_24);
        }

    }
}