# Shopping Cart

Vue.js frontend with a minimal Spring Boot backend. The backend is a single service on port 8080 and uses in-memory storage, so MongoDB, an API gateway, and separate microservices are not required for local development.

## Requirements

- Java 17+
- Maven 3.8+
- Node.js 18+

## Run

Start the backend:

```bash
cd backend
mvn spring-boot:run
```

Start the frontend in another terminal:

```bash
cd frontend
npm install
npm run dev
```

Open http://localhost:5173. Backend data resets whenever the backend restarts.

## REST API

Products support the requested endpoints:

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/products` | Add a product |
| GET | `/api/products` | List products |
| GET | `/api/products/{id}` | Get one product |
| PUT | `/api/products/{id}` | Update a product |
| DELETE | `/api/products/{id}` | Delete a product |

The same product endpoints are also available without `/api` because the existing frontend calls `/products`.

The backend additionally provides the cart and order endpoints used by the frontend: `/cart/{cartId}`, `/cart/{cartId}/items`, and `/orders`.

Products include an `emoji` field used as their logo (for example `💻`, `⌨️`, or `🖥️`). If it is omitted, the backend uses `🛍️`. The backend starts with six sample products: Laptop, Keyboard, Mouse, Headphones, Monitor, and Webcam.
