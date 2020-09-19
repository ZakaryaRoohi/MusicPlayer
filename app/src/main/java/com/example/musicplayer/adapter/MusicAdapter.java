package com.example.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.model.MusicFiles;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder> {

    private Context mContext;
    private ArrayList<MusicFiles> mMusicFiles;

    public MusicAdapter(Context context, ArrayList<MusicFiles> musicFiles) {
        this.mContext = context;
        this.mMusicFiles = musicFiles;
    }


    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items, parent, false);

        return new MusicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
        holder.mTextViewMusicFileName.setText(mMusicFiles.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MusicHolder extends RecyclerView.ViewHolder {
        private ImageView mImageViewMusicImage;
        private TextView mTextViewMusicFileName;

        private MusicFiles mMusicFile;

        public MusicHolder(@NonNull View itemView) {
            super(itemView);
            mImageViewMusicImage=itemView.findViewById(R.id.music_img);
            mTextViewMusicFileName =itemView.findViewById(R.id.music_file_name);
        }
    }
}
