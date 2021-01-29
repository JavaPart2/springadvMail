package be.hvwebsites.mail.services;

import be.hvwebsites.mail.domain.Lid;

import java.util.Optional;

public interface LidService {
    void registreer(Lid lid);
    Optional<Lid> findById(long id);
}
