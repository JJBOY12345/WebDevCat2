# ShopWebsite

A full-stack shopping platform with a Vue storefront and Spring Boot/MongoDB services organized into authentication, catalog, cart, and order service boundaries.

## Features

- Customer registration/login with `CUSTOMER` and `ADMIN` roles
- Product browsing and admin-only catalog mutation endpoints
- User-scoped cart, checkout, delivery address, and payment method
- Customer order history and admin order status workflow
- Case-insensitive partial product search
- Product images or emoji logos
- Admin product editor for name, price, description, image, emoji, and stock
- Admin order management with customer name and email displayed
- Signed JWT authentication with `CUSTOMER` and `ADMIN` roles

## Run locally

MongoDB must be running locally on port `27017`. This project does not require Docker.

The four backend services share one local JWT signing secret. It is kept outside GitHub in `backend/application-local.properties`. That file is already configured locally and ignored by Git. For a new clone, copy `backend/application-local.properties.example` to `backend/application-local.properties` and replace the placeholder with the same random secret in that file.

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

After JWT changes, restart all four backend services and log in again so the browser receives a new token.

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
| Catalog | `GET /api/products` on port 8081; supports `?q=term` case-insensitive partial search; admin writes require a valid admin JWT |
| Cart | `/api/carts/{userId}` on port 8082 |
| Checkout | `POST /api/orders` on port 8083 |
| Customer orders | `GET /api/orders/user/{userId}` on port 8083 |
| Admin orders | `GET /api/orders`, `PATCH /api/orders/{id}/status` on port 8083 |

MongoDB is used for persistence. Passwords are stored with BCrypt, and authenticated API requests use signed bearer JWTs. The shared secret approach is intentionally simple for this college project; production deployment should use a dedicated authorization server, stronger secret management, HTTPS, and refresh-token handling.

Product stock is visible on product cards, checked when items are added to carts, checked again during checkout, and reduced when an order is placed. Restart the Product, Cart, and Order services after backend source changes.
