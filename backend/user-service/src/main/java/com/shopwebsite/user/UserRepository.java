package com.shopwebsite.user;
import org.springframework.data.mongodb.repository.MongoRepository;import java.util.Optional;
public interface UserRepository extends MongoRepository<User,String>{Optional<User> findByEmailIgnoreCase(String email);}
