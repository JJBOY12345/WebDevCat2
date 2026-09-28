# ShopWebsite

A full-stack shopping platform with a Vue storefront and Spring Boot API organized into authentication, catalog, cart, and order service boundaries. The default local profile uses in-memory storage for easy development.

## Features

- Customer registration/login with `CUSTOMER` and `ADMIN` roles
- Product browsing and admin-only catalog mutation endpoints
- User-scoped cart, checkout, delivery address, and payment method
- Customer order history and admin order status workflow
- Health endpoint and Docker Compose services

## Run locally

```bash
cd backend
mvn spring-boot:run

# another terminal
cd frontend
npm install
npm run dev
```

Open http://localhost:5173. Demo accounts: `demo@shop.local` / `demo123`, `admin@shop.local` / `admin123`.

## Run with Docker

```bash
docker compose up --build
```

## API outline

| Area | Endpoints |
|---|---|
| Auth | `POST /api/auth/register`, `POST /api/auth/login`, `GET /api/auth/me` |
| Catalog | `GET /api/products`; admin writes under `/api/products/admin` |
| Cart | `/cart/1` and `/cart/1/items` with a bearer token |
| Checkout | `POST /orders/checkout` |
| Customer orders | `GET /orders/mine` |
| Admin orders | `GET /orders/manage`, `PATCH /orders/{id}/status` |

Storage is currently in-memory for the coursework-sized app. For production, hash passwords, move sessions to Redis or signed JWTs, persist catalog/orders in a database, and place services behind an API gateway/TLS.
