package com.shopwebsite.product;

import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(ProductRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(product("Laptop", "50000", 10, "Powerful laptop", "\uD83D\uDCBB"));
                repository.save(product("Keyboard", "1500", 20, "Comfortable keyboard", "\u2328\uFE0F"));
                repository.save(product("Mouse", "800", 30, "Wireless mouse", "\uD83D\uDDB1\uFE0F"));
                repository.save(product("Headphones", "2500", 15, "Clear sound", "\uD83C\uDFA7"));
                repository.save(product("Monitor", "12000", 8, "Full HD monitor", "\uD83D\uDDA5\uFE0F"));
            }
        };
    }

    private Product product(String name, String price, int stock, String description, String emoji) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(new BigDecimal(price));
        product.setStock(stock);
        product.setDescription(description);
        product.setEmoji(emoji);
        return product;
    }
}
