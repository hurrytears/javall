package com.apachee.mysql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete


public interface UserRepository extends CrudRepository<User, Integer> {

//    User findUserByEmail();

//    @Query("select u from user u where u.id = ?1 and u.password = ?2")
//    User getUser(String useId, String password);
//
//
//    @Query("select u from User u where u.username = :username or u.email = :email")
//    User getByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
//
//
//    @Query(value = "select * from tb_user u where u.email = ?1", nativeQuery = true)
//    User queryByEmail(String email);
//
//
//    @Query("select u from User u where u.username like %?1%")
//    Page<User> findByUsernameLike(String username, Pageable pageable);
//
//    @Query("select u from User u where u.username like %?1%")
//    List<User> findByUsernameAndSort(String username, Sort sort);
//
//    @Transactional
//    @Modifying
//    @Query("update User u set u.password = ?2 where u.username = ?1")
//    int updatePasswordByUsername(String username, String password);
//
//    @Transactional
//    @Modifying
//    @Query("delete from User where username = ?1")
//    void deleteByUsername(String username);




}
