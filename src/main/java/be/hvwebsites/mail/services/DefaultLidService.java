package be.hvwebsites.mail.services;

import be.hvwebsites.mail.domain.Lid;
import be.hvwebsites.mail.mailing.LidMailing;
import be.hvwebsites.mail.repositories.LidRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class DefaultLidService implements LidService{
    private final LidRepository repository;
    private final LidMailing mailing;

    public DefaultLidService(LidRepository repository, LidMailing mailing) {
        this.repository = repository;
        this.mailing = mailing;
    }

    @Override
    public void registreer(Lid lid) {
        repository.save(lid);
        mailing.stuurMailNaRegistratie(lid);

    }

    @Override
    public Optional<Lid> findById(long id) {
        return repository.findById(id);
    }
}
