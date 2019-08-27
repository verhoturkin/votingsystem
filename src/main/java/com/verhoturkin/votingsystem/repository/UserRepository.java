package com.verhoturkin.votingsystem.repository;

import com.verhoturkin.votingsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByOrderByNameAscEmailAsc();

    Optional<User> findByEmail(String email);
}

