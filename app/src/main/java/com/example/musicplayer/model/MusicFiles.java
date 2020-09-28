package com.example.musicplayer.model;

public class MusicFiles {
//    private String  path;
//////    private String  title;
//////    private String  artist;
//////    private String  album;
//////    private String  duration;
//////
//////    public MusicFiles(String path, String title, String artist, String album, String duration) {
//////        this.path = path;
//////        this.title = title;
//////        this.artist = artist;
//////        this.album = album;
//////        this.duration = duration;
//////    }
//////
//////    public String getPath() {
//////        return path;
//////    }
//////
//////    public void setPath(String path) {
//////        this.path = path;
//////    }
//////
//////    public String getTitle() {
//////        return title;
//////    }
//////
//////    public void setTitle(String title) {
//////        this.title = title;
//////    }
//////
//////    public String getArtist() {
//////        return artist;
//////    }
//////
//////    public void setArtist(String artist) {
//////        this.artist = artist;
//////    }
//////
//////    public String getAlbum() {
//////        return album;
//////    }
//////
//////    public void setAlbum(String album) {
//////        this.album = album;
//////    }
//////
//////    public String getDuration() {
//////        return duration;
//////    }
//////
//////    public void setDuration(String duration) {
//////        this.duration = duration;
//////    }

    private String title;
    private String album;
    private String artist;
    private String data;
    private String songId;
    private String albumId;
    private String artistId;
    private String path;

    public MusicFiles(String data, String title, String album, String artist, String songId, String albumId, String artistId,String path) {
        this.data = data;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.songId = songId;
        this.albumId = albumId;
        this.artistId = artistId;
        this.path=path;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getData() {
        return data;
    }

    public String getSongId() {
        return songId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getPath() {
        return path;
    }
}
