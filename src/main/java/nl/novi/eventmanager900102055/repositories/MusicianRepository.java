package nl.novi.eventmanager900102055.repositories;

import nl.novi.eventmanager900102055.models.Musician;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicianRepository extends JpaRepository<Musician, Long> {

    Iterable<Musician> findByLastName(String lastName);
//    https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories
//  findAll geeft Iterable terug
//  findById(ID primaryKey) geeft een optional terug.
//public interface CrudRepository<T, ID> extends Repository<T, ID> {
//
//    <S extends T> S save(S entity);
//
//    Optional<T> findById(ID primaryKey);
//
//    Iterable<T> findAll();
//
//    long count();
//
//    void delete(T entity);
//
//    boolean existsById(ID primaryKey);
}