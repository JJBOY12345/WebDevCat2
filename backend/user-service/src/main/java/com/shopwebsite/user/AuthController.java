package com.shopwebsite.user;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;import jakarta.validation.constraints.*;import org.springframework.http.*;import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;import org.springframework.web.bind.annotation.*;import java.util.*;import java.util.concurrent.ConcurrentHashMap;
@CrossOrigin(origins="http://localhost:5173") @RestController @RequestMapping("/api") public class AuthController {
 private final UserRepository repo; private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(); private final JwtService jwt;
 public AuthController(UserRepository repo,@org.springframework.beans.factory.annotation.Value("${jwt.secret}") String secret){this.repo=repo;this.jwt=new JwtService(secret);}
 @PostMapping("/auth/register") @ResponseStatus(HttpStatus.CREATED) public Map<String,Object> register(@Valid @RequestBody Credentials c){if(repo.findByEmailIgnoreCase(c.email()).isPresent())throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already registered");User u=repo.save(new User(c.name(),c.email().toLowerCase(),encoder.encode(c.password()),"CUSTOMER"));return session(u);}
 @PostMapping("/auth/login") public Map<String,Object> login(@Valid @RequestBody Credentials c){User u=repo.findByEmailIgnoreCase(c.email()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials"));if(!encoder.matches(c.password(),u.getPassword()))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials");return session(u);}
 @GetMapping("/users/{id}") public User user(@PathVariable String id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));}
 @GetMapping("/auth/me") public Map<String,Object> me(@RequestHeader("Authorization") String h){return publicUser(require(h));}
 private Map<String,Object> session(User u){return Map.of("token",jwt.issue(u.getId(),u.getEmail(),u.getRole()),"user",publicUser(u));}
 private User require(String h){try{return repo.findById(jwt.parse(h).getSubject()).orElseThrow();}catch(Exception e){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid or expired token");}}
 private Map<String,Object> publicUser(User u){return Map.of("id",u.getId(),"name",u.getName(),"email",u.getEmail(),"role",u.getRole());}
 public record Credentials(String name,@NotBlank @Email String email,@NotBlank @Size(min=6) String password){}
}
