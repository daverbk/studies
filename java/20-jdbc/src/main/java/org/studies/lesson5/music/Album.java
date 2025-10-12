package org.studies.lesson5.music;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "albums", schema = "music")
public class Album implements Comparable<Album> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private int albumId;

    @Column(name = "album_name")
    private String albumName;

    @OneToMany
    @JoinColumn(name="album_id")
    private List<Song> songs = new ArrayList<>();

    public Album() {
    }

    public Album(String albumName) {
        this.albumName = albumName;
    }

    public Album(int albumId, String albumName) {
        this.albumId = albumId;
        this.albumName = albumName;
    }

    public Album(int albumId, String albumName, List<Song> songs) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.songs = songs;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    @Override
    public String toString() {

        songs.sort(Comparator.comparing(Song::getTrackNumber));
        StringBuilder sb = new StringBuilder();
        for (Song song : songs) {
            sb.append("\n\t").append(song);
        }
        sb.append("\n");
        return "Album{" +
            "albumId=" + albumId +
            ", albumName='" + albumName + '\'' +
            ", songs=" + sb +
            '}';
    }

    @Override
    public int compareTo(Album o) {
        return this.albumName.compareTo(o.getAlbumName());
    }
}
