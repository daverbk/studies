package org.studies.lesson5.music;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "songs", schema = "music")
public class Song {
    @Id
    @Column(name = "song_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int songId;

    @Column(name = "track_number")
    private int trackNumber;

    @Column(name = "song_title")
    private String songTitle;

    public Song() {
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    @Override
    public String toString() {

        return "Song{" +
            "songId=" + songId +
            ", trackNumber=" + trackNumber +
            ", songTitle='" + songTitle + '\'' +
            '}';
    }
}
