package com.shopwebsite.product;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/products") public class ProductController { private final ProductRepository repo; public ProductController(ProductRepository repo){this.repo=repo;}
 @GetMapping public List<Product> all(){return repo.findAll();} @GetMapping("/{id}") public Product one(@PathVariable String id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Product create(@RequestHeader("X-Role") String role,@Valid @RequestBody Product p){admin(role);return repo.save(p);} @PutMapping("/{id}") public Product update(@RequestHeader("X-Role") String role,@PathVariable String id,@Valid @RequestBody Product p){admin(role);p.setId(id);one(id);return repo.save(p);} @DeleteMapping("/{id}") public void delete(@RequestHeader("X-Role") String role,@PathVariable String id){admin(role);repo.delete(one(id));}
 private void admin(String role){if(!"ADMIN".equals(role))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Admin role required");}
}
