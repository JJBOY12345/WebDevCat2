package com.shopwebsite.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAddProductSuccessfully() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("Test Laptop", 50000, 10)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name", is("Test Laptop")))
                .andExpect(jsonPath("$.price", is(50000)))
                .andExpect(jsonPath("$.quantity", is(10)));
    }

    @Test
    void shouldRejectProductWithMissingOrInvalidData() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "price": 1000,
                                  "quantity": 5
                                }
                                """))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("Invalid Product", -1, 5)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRetrieveAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem("Laptop")));
    }

    @Test
    void shouldRetrieveProductByValidId() throws Exception {
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Laptop")));
    }

    @Test
    void shouldReturnNotFoundForInvalidProductId() throws Exception {
        mockMvc.perform(get("/api/products/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateExistingProductSuccessfully() throws Exception {
        long id = createProductAndGetId("Product To Update", 1000, 4);

        mockMvc.perform(put("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("Updated Product", 1250, 8)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.name", is("Updated Product")))
                .andExpect(jsonPath("$.price", is(1250)))
                .andExpect(jsonPath("$.quantity", is(8)));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentProduct() throws Exception {
        mockMvc.perform(put("/api/products/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("Missing Product", 1000, 2)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteProductSuccessfully() throws Exception {
        long id = createProductAndGetId("Product To Delete", 900, 3);

        mockMvc.perform(delete("/api/products/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotRetrieveDeletedProduct() throws Exception {
        long id = createProductAndGetId("Product To Verify Deletion", 700, 2);

        mockMvc.perform(delete("/api/products/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isNotFound());
    }

    private long createProductAndGetId(String name, int price, int quantity) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson(name, price, quantity)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        return response.get("id").asLong();
    }

    private String productJson(String name, int price, int quantity) {
        return """
                {
                  "name": "%s",
                  "price": %d,
                  "quantity": %d,
                  "description": "Test product",
                  "emoji": "🛍️"
                }
                """.formatted(name, price, quantity);
    }
}
