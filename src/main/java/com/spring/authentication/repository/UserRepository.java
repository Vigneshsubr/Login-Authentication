package com.spring.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.spring.authentication.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    UserDetails findByLogin(String username);

}
