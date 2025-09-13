CREATE OR REPLACE PROCEDURE add_album(
    artistName TEXT,
    albumName TEXT,
    songTitles JSONB
)
    LANGUAGE plpgsql
AS
$$
DECLARE
    val_artist_id INT;
    val_album_id  INT;
    i             INT := 0;
    num_items     INT;
    song_title    TEXT;
BEGIN
    -- count items in JSON array
    SELECT jsonb_array_length(songTitles) INTO num_items;

    -- check if artist exists
    SELECT artist_id
    INTO val_artist_id
    FROM music.artists
    WHERE artist_name = artistName;

    IF val_artist_id IS NULL THEN
        INSERT INTO music.artists (artist_name)
        VALUES (artistName)
        RETURNING artist_id INTO val_artist_id;
    END IF;

    -- check if album exists for that artist
    SELECT album_id
    INTO val_album_id
    FROM music.albums
    WHERE album_name = albumName
      AND artist_id = val_artist_id;

    IF val_album_id IS NULL THEN
        INSERT INTO music.albums (artist_id, album_name)
        VALUES (val_artist_id, albumName)
        RETURNING album_id INTO val_album_id;

        -- loop over songs JSON array
        FOR i IN 0 .. num_items - 1
            LOOP
                SELECT songTitles ->> i INTO song_title;

                INSERT INTO music.songs (album_id, track_number, song_title)
                VALUES (val_album_id, i + 1, song_title);
            END LOOP;
    END IF;
END;
$$;
