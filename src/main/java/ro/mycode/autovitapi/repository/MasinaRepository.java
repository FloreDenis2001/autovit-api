package ro.mycode.autovitapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.mycode.autovitapi.model.Masina;


@Repository
public interface MasinaRepository extends JpaRepository<Masina,Long> {
}
