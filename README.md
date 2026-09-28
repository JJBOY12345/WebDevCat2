# ShopWebsite

A full-stack shopping platform with a Vue storefront and Spring Boot/MongoDB services organized into authentication, catalog, cart, and order service boundaries.

## Features

- Customer registration/login with `CUSTOMER` and `ADMIN` roles
- Product browsing and admin-only catalog mutation endpoints
- User-scoped cart, checkout, delivery address, and payment method
- Customer order history and admin order status workflow
- REST communication between services

## Run locally

```bash
cd backend/user-service
mvn spring-boot:run

# separate terminals
cd backend/product-service
mvn spring-boot:run

cd backend/cart-service
mvn spring-boot:run

cd backend/order-service
mvn spring-boot:run

# another terminal
cd frontend
npm install
npm run dev
```

Open http://localhost:5173. Demo users and passwords are listed in [USERS.md](USERS.md).

## Service layout

Each backend service is independently runnable from the same repository:

| Service | Port | Responsibility |
|---|---:|---|
| User service | 8080 | Registration, login, roles, user verification |
| Product service | 8081 | Product catalog and admin product management |
| Cart service | 8082 | User carts, quantities, totals |
| Order service | 8083 | Checkout, order history, status management |

## API outline

| Area | Endpoints |
|---|---|
| Auth | `POST /api/auth/register`, `POST /api/auth/login`, `GET /api/auth/me` |
| Catalog | `GET /api/products` on port 8081; supports `?q=term` case-insensitive partial search; admin writes require `X-Role: ADMIN` |
| Cart | `/api/carts/{userId}` on port 8082 |
| Checkout | `POST /api/orders` on port 8083 |
| Customer orders | `GET /api/orders/user/{userId}` on port 8083 |
| Admin orders | `GET /api/orders`, `PATCH /api/orders/{id}/status` on port 8083 |

MongoDB is used for persistence. For production, use hashed passwords, signed JWTs, and TLS.

Product stock is visible on product cards, checked when items are added to carts, checked again during checkout, and reduced when an order is placed. Restart the Product, Cart, and Order services after backend source changes.
