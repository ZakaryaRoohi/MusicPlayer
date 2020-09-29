package com.example.musicplayer.controller.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.musicplayer.controller.activity.MainActivity;
import com.example.musicplayer.controller.activity.PlayerActivity;
import com.example.musicplayer.model.MusicFiles;

import java.util.List;

public class SongListFragment extends Fragment {
    public static final int PLAYER_ACTIVITY_REQUEST_CODE = 1;
    public static final int SPAN_COUNT = 3;
    public static final String TAG = "BBF";

    private SeekBar mSeekBar;
    private Runnable mRunnable;
    private Handler mHandler;
    private RecyclerView mRecyclerView;

    private MediaPlayer mMediaPlayer;
    private LinearLayout mLinearLayoutPlayer;
    private List<MusicFiles> mMusicFiles;
    private MusicFiles mCurrentMusicPlayed;
    private ImageView mBtnShuffle;
    private ImageView mBtnPrev;
    private ImageView mBtnPlayPause;
    private ImageView mBtnNext;
    private ImageView mBtnRepeat;

    public SongListFragment() {
        // Required empty public constructor
    }

    public static SongListFragment newInstance() {
        SongListFragment fragment = new SongListFragment();
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
        mMusicFiles = MainActivity.mMusicFiles;

        mMediaPlayer = new MediaPlayer();
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        mMediaPlayer.setAudioAttributes(audioAttributes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        Log.d(TAG, "onCreteView");
        findViews(view);
        setListeners();
        initViews();
        mLinearLayoutPlayer.setVisibility(View.INVISIBLE);
        SongListFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null) {
                    int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                    mSeekBar.setProgress((int) (((double) (mCurrentPosition) / mMediaPlayer.getDuration()) * 100));
                }
                mHandler.postDelayed(this, 1000);
            }
        });

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {


            @Override
            public void onPrepared(MediaPlayer mp) {
                SongListFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mMediaPlayer != null) {
                            int mCurrentPosition = mMediaPlayer.getCurrentPosition();

                            mSeekBar.setProgress((int) (((double) (mCurrentPosition) / mMediaPlayer.getDuration()) * 100));

                        }
                        mHandler.postDelayed(this, 1000);
                    }
                });
            }
        });
        return view;
    }


    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_beat_box);
        mLinearLayoutPlayer = view.findViewById(R.id.layout2);

        mBtnShuffle = view.findViewById(R.id.id_shuffle_float);
        mBtnPrev = view.findViewById(R.id.id_prev_float);
        mBtnPlayPause = view.findViewById(R.id.play_pause_float);
        mBtnNext = view.findViewById(R.id.id_next_float);
        mBtnRepeat = view.findViewById(R.id.id_repeat_float);
        mSeekBar = view.findViewById(R.id.seek_bar_float);

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

    }

    private void initUI() {

        MusicAdapter adapter = new MusicAdapter(mMusicFiles);
        mRecyclerView.setAdapter(adapter);
    }

    private class MusicHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTitle;
        private MusicFiles mMusic;
        private ImageView mImageViewImageMusic;

        public MusicHolder(@NonNull View itemView) {
            super(itemView);
            mImageViewImageMusic = itemView.findViewById(R.id.music_img);
            mTextViewTitle = itemView.findViewById(R.id.music_file_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
//                    int i = mMusic.getData().indexOf('-');
//                    Toast.makeText(getActivity(), "path: " + mMusic.getData().substring(0,i)+"/"+mMusic.getTitle()+".mp3", Toast.LENGTH_LONG).show();
//                    mMediaPlayer.stop();
//                    mMediaPlayer = new MediaPlayer();
//                   MusicUtils.playAudio(mMediaPlayer,mMusic.getData());
////                    mMediaPlayer.start();
//                    mButtonPlay.setText("pause");
//                    mCurrentMusicPlayed = mMusic;

                    Intent intent = PlayerActivity.newIntent(getActivity(), mMusicFiles.indexOf(mMusic));
                    startActivityForResult(intent, PLAYER_ACTIVITY_REQUEST_CODE);

                }
            });
        }
//

        public void bindMusic(MusicFiles music) {
            mMusic = music;
            mTextViewTitle.setText(mMusic.getTitle());

            byte[] art = getAlbumArt(mMusic.getData());
            if (art != null) {
                mImageViewImageMusic.setImageBitmap(BitmapFactory.decodeByteArray(art, 0, art.length));
            } else {
                mImageViewImageMusic.setImageResource(R.drawable.song_bytes_image);
            }

        }

        private byte[] getAlbumArt(String data) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(data);
            byte[] art = retriever.getEmbeddedPicture();
            retriever.release();
            return art;
        }
    }

    private class MusicAdapter extends RecyclerView.Adapter<MusicHolder> {

        private List<MusicFiles> mMusics;

        public MusicAdapter(List<MusicFiles> musics) {
            mMusics = musics;
        }

        @NonNull
        @Override
        public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.music_items, parent, false);

            return new MusicHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
            MusicFiles music = mMusics.get(position);
            holder.bindMusic(music);
        }

        @Override
        public int getItemCount() {
            return mMusics.size();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PLAYER_ACTIVITY_REQUEST_CODE){
            mLinearLayoutPlayer.setVisibility(View.VISIBLE);
        }
    }
}