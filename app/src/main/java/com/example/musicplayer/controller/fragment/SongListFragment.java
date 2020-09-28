package com.example.musicplayer.controller.fragment;

import android.content.res.AssetFileDescriptor;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.Utils.MusicUtils;
import com.example.musicplayer.controller.activity.MainActivity;
import com.example.musicplayer.model.MusicFiles;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SongListFragment extends Fragment {

    public static final int SPAN_COUNT = 3;
    public static final String TAG = "BBF";
    private Button mButtonPlay;
    private ImageButton mImageButtonNext, mImageButtonPrevious, mImageButtonSeekBackward, mImageButtonSeekForward;
    private SeekBar mSeekBar;
    private Runnable mRunnable;
    private Handler mHandler;
    private RecyclerView mRecyclerView;

    private MediaPlayer mMediaPlayer;

    private List<MusicFiles> mMusicFiles;
    private MusicFiles mCurrentMusicPlayed;

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
        mSeekBar = view.findViewById(R.id.music_seek_bar);
        mImageButtonSeekForward = view.findViewById(R.id.button_seek_forward);
        mButtonPlay = view.findViewById(R.id.button_pause);
        mImageButtonSeekBackward = view.findViewById(R.id.button_seek_backward);
        mImageButtonNext = view.findViewById(R.id.image_button_next);
        mImageButtonPrevious = view.findViewById(R.id.image_button_previous);

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
        mButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mButtonPlay.setText("Play");
                } else {
                    mMediaPlayer.start();
                    mButtonPlay.setText("pause");
                }
            }
        });
        mImageButtonSeekForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() + 5000);
            }
        });
        mImageButtonSeekBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() - 5000);
            }
        });
        mImageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicFiles nextMusic;
                if (mCurrentMusicPlayed != null) {


                    int currentMusicIndex = mMusicFiles.indexOf(mCurrentMusicPlayed);
                    if (currentMusicIndex == mMusicFiles.size() - 1)
                        nextMusic = mMusicFiles.get(0);
                    else
                        nextMusic = mMusicFiles.get(currentMusicIndex + 1);

                    mMediaPlayer.stop();
                    mMediaPlayer = new MediaPlayer();
//                    MusicUtils.playAssetSound(mMediaPlayer, getActivity(), nextMusic.getPath());
                    mMediaPlayer.start();
                    mButtonPlay.setText("pause");
                }
            }
        });
        mImageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicFiles nextMusic;
                if (mCurrentMusicPlayed != null) {


                    int currentMusicIndex = mMusicFiles.indexOf(mCurrentMusicPlayed);
                    if (currentMusicIndex == 0)
                        nextMusic = mMusicFiles.get(mMusicFiles.size() - 1);
                    else
                        nextMusic = mMusicFiles.get(currentMusicIndex - 1);

                    mMediaPlayer.stop();
                    mMediaPlayer = new MediaPlayer();
//                    MusicUtils.playAssetSound(mMediaPlayer, getActivity(), nextMusic.getPath());
                    mMediaPlayer.start();
                    mButtonPlay.setText("pause");
                }
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
                    int i = mMusic.getData().indexOf('-');
                    Toast.makeText(getActivity(), "path: " + mMusic.getData().substring(0,i)+"/"+mMusic.getTitle()+".mp3", Toast.LENGTH_LONG).show();
                    mMediaPlayer.stop();
                    mMediaPlayer = new MediaPlayer();
                    MusicUtils.playAssetSound(mMediaPlayer, getActivity(),mMusic.getData().substring(0,i)+"/"+mMusic.getTitle()+".mp3");
                    mMediaPlayer.start();
                    mButtonPlay.setText("pause");
                    mCurrentMusicPlayed = mMusic;

                }
            });
        }
//

        public void bindMusic(MusicFiles music) {
            mMusic = music;
            mTextViewTitle.setText(mMusic.getTitle());

//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//            retriever.setDataSource(mMusic.getData());
//            byte[] art = retriever.getEmbeddedPicture();
            byte[] art = getAlbumArt(mMusic.getData());
            if (art != null) {
                mImageViewImageMusic.setImageBitmap(BitmapFactory.decodeByteArray(art, 0, art.length));
            } else {
                mImageViewImageMusic.setImageResource(R.drawable.song_bytes_image);
            }

        }
        private byte[] getAlbumArt(String data){
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
}