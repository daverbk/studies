package org.studies.task4;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.studies.lesson5.music.Album;
import org.studies.lesson5.music.Artist;
import org.studies.lesson5.music.Song;

public class SongQuery {

    public static void main(String[] args) {
        try (
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("music");
            EntityManager em = emf.createEntityManager()
        ) {

            var transaction = em.getTransaction();
            transaction.begin();

            var artists = getArtistsJPQL(em, "%Storm%");
//            artists.forEach(System.out::println);

            var cbArtists = getArtistsBuilder(em, "%Storm%");
            cbArtists.forEach(System.out::println);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Artist> getArtistsJPQL(EntityManager em, String matchedSongName) {
        String jpql = """
             SELECT ar
             FROM Artist ar
             JOIN albums al
             JOIN songs s
             WHERE s.songTitle LIKE ?1
            """;

        var query = em.createQuery(jpql, Artist.class);
        query.setParameter(1, matchedSongName);
        return query.getResultList();
    }

    private static List<Tuple> getArtistsBuilder(EntityManager em, String matchedArtistValue) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = builder.createQuery(Tuple.class);

        Root<Artist> root = criteriaQuery.from(Artist.class);
        Join<Artist, Album> albumJoin = root.join("albums");
        Join<Album, Song> songJoin = albumJoin.join("songs");

        criteriaQuery
            .multiselect(root, albumJoin, songJoin)
            .where(builder.like(songJoin.get("songTitle"), matchedArtistValue))
            .orderBy(builder.asc(root.get("artistName")));

        return em.createQuery(criteriaQuery).getResultList();
    }
}
