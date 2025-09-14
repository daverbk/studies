CREATE OR REPLACE FUNCTION calc_album_length(albumName text)
    RETURNS double precision
    LANGUAGE plpgsql
AS
$$
DECLARE
    length double precision := 0.0;
BEGIN
    SELECT COUNT(*) * 2.5
    INTO length
    FROM music.albumview
    WHERE album_name = albumName;

    RETURN length;
END;
$$;
