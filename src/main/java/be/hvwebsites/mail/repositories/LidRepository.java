package be.hvwebsites.mail.repositories;

import be.hvwebsites.mail.domain.Lid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LidRepository extends JpaRepository<Lid, Long> {
}
