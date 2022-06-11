package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.TestUser;
import de.hohenheim.ticketcricket.model.repository.TestUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestUserService {

    @Autowired
    private TestUserRepository testUserRepository;

    public TestUser saveUser(TestUser user) {
        return testUserRepository.save(user);
    }

    public List<TestUser> findAllUsers() {
        return testUserRepository.findAll();
    }

    public TestUser getUser(int id) { return testUserRepository.getById(id);}
}
