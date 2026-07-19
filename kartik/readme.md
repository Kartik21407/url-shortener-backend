# 🚀 URL Shortener Backend

A robust, secure, and scalable backend service built with Spring Boot for shortening long URLs. It includes JWT-based authentication, rate limiting, and real-time click analytics.

## 🛠 Tech Stack

- **Framework**: Spring Boot 4.1.0
- **Language**: Java 17
- **Database**: PostgreSQL (Production-ready)
- **Security**: Spring Security + JWT
- **Build Tool**: Maven
- **Containerization**: Docker
- **Deployment**: Render

## 📋 Features

- **Secure Auth**: JWT-based login/signup for user privacy.
- **Shortening Engine**: Generates unique, compact aliases for long URLs.
- **Analytics**: Tracks click counts for every shortened link.
- **Rate Limiting**: Protects the API from excessive requests.

## ⚙️ Local Setup

1. **Clone the repo**: `git clone https://github.com/Kartik21407/url-shortener-backend.git`
2. **Configure DB**: Update `application.properties` with your PostgreSQL credentials.
3. **Run**: `./mvnw spring-boot:run`
4. **Access**: The API runs on `http://localhost:8080/api`.

## 🐳 Docker Deployment

To build and run the image locally:

```bash
docker build -t url-shortener-backend .
docker run -p 8080:8080 url-shortener-backend
```
