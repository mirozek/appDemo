package Michal.appdemo.admin;

import Michal.appdemo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository("adminRepository")
public interface AdminRepository extends JpaRepository<User, Integer> {

    User findUserById(int id);

    @Modifying
    @Query("UPDATE User u SET u.active = :intActive WHERE u.id= :id")
    void updateActivationUser(@Param("intActive") int active, @Param("id") int id);

    @Modifying
    @Query(value = "UPDATE user_role r SET r.role_id = :roleId WHERE r.user_id= :id", nativeQuery = true)
    void updateRoleUser(@Param("roleId") int nrRoli, @Param("id") int id);
}