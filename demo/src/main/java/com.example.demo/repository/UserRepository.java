package com.example.demo.repository;

import com.example.demo.domain.User;
import com.example.demo.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    List<User> findByFirstName(String firstName);

    List<User> findByUserType(UserType userType);

    @Query("SELECT u FROM User u WHERE " +
            "(:firstName IS NULL OR u.firstName = :firstName) AND " +
            "(:lastName IS NULL OR u.lastName = :lastName) AND " +
            "(:phoneNumber IS NULL OR u.phoneNumber = :phoneNumber)")
    List<User> searchUsers(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("phoneNumber") String phoneNumber
    );
}