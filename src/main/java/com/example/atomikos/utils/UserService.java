package com.example.atomikos.utils;

import com.example.atomikos.hibinaction.HAUser;
import com.example.atomikos.hibinaction.HAUserRepository;
import com.example.atomikos.hibtest.HTUser;
import com.example.atomikos.hibtest.HTUserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {
    private final HTUserRepository htUserRepo;
    private final HAUserRepository haUserRepo;

    public UserService(final @Qualifier("htUserRepository") HTUserRepository htUserRepo,
                       final @Qualifier("haUserRepository") HAUserRepository haUserRepo)
    {
        this.htUserRepo = htUserRepo;
        this.haUserRepo = haUserRepo;
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class, transactionManager = "jtaTransactionManager")
    public void createUser(final UserInput input)
    {
        HAUser haUser = new HAUser(null, input.getName(), input.getAge());
        HTUser htUser = new HTUser(null, input.getName(), input.getAge());

        haUser = haUserRepo.save(haUser);
        System.out.println(haUser);
        htUserRepo.save(htUser);
        throw new RuntimeException("test");
    }
}
