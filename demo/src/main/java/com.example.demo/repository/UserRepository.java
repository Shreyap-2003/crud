package com.example.demo.repository;

import com.example.demo.domain.User;
import com.example.demo.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    List<User> findByFirstName(String firstName);

    List<User> findByUserType(UserType userType);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE " +
            "(:firstName IS NULL OR u.firstName = :firstName) AND " +
            "(:lastName IS NULL OR u.lastName = :lastName) AND " +
            "(:phoneNumber IS NULL OR u.phoneNumber = :phoneNumber)")
    List<User> searchUsers(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("phoneNumber") String phoneNumber
    );

    @Query(value = """

            SELECT
            u.id,
            u.first_name,
            u.last_name,
            (
                6371 * acos(
                    cos(radians(:latitude)) *
                    cos(radians(u.latitude)) *
                    cos(radians(u.longitude) - radians(:longitude)) +
                    sin(radians(:latitude)) *
                    sin(radians(u.latitude))
                )
            ) AS distanceInKms
        FROM user u
        WHERE u.user_type = 'PARTNER'
        HAVING distanceInKms <= :radius
        ORDER BY distanceInKms
        """, nativeQuery = true)
    List<Object[]> findNearbyPartners(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius
    );
    }


