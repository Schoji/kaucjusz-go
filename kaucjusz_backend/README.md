# Kaucjusz Go – Backend

Serwerowe REST API aplikacji **Kaucjusz Go** napisane w **Javie 21** i **Spring Boot 4**, wykorzystujące **PostgreSQL z PostGIS**, **Flyway** oraz uwierzytelnianie **Firebase Auth**.

---

## Jak działa aplikacja (obecne moduły)

* **Użytkownicy i bezpieczeństwo:** Bezstanowa autoryzacja przez tokeny JWT z Firebase (`Authorization: Bearer <token>`). Podczas synchronizacji konta z bazą dla nowych użytkowników automatycznie tworzony jest wirtualny zwierzak (`Pet`).
* **Kaucjomaty i geolokalizacja:** Wyszukiwanie maszyn na bazie typu `geography(Point, 4326)` w PostGIS — odległości i promień liczone są w metrach, wyniki sortowane są od najbliższych maszyn, z możliwością filtrowania po frazie, statusie otwarcia (`ACTIVE`) oraz ocenie (`>= 4.5`).
* **Import danych:** Idempotentny import kaucjomatów z zewnętrznego źródła (`mapakaucji.pl`) z deduplikacją po unikalnym skrócie MD5 (`external_id`).
* **Oceny i recenzje:** Mechanizm *upsert* (jeden użytkownik może mieć maksymalnie jedną opinię dla danego kaucjomatu; ponowne wysłanie aktualizuje poprzednią ocenę/komentarz) z automatycznym przeliczaniem średniej ocen i licznika opinii maszyny.

---

## Wymagania i uruchomienie

### Wymagania
* **Docker & Docker Compose**
* **Java 21** (tylko przy uruchamianiu lokalnie poza Dockerem)
* Plik klucza serwisowego Firebase umieszczony w `src/main/resources/firebase-service-account.json` *(plik jest ignorowany przez Gita; ścieżkę można też nadpisać zmienną środowiskową `FIREBASE_CONFIG_PATH`)*.

### Uruchomienie

**1. Cały stos w Dockerze (API + PostgreSQL/PostGIS):**
```bash
docker compose up -d --build
```

**2. Hybrydowo (baza w Dockerze + backend z IDE / Mavena):**
```bash
docker compose up -d postgres-postgis
./mvnw spring-boot:run
```

* **API:** `http://localhost:8080`
* **Baza danych (z zewnątrz):** `localhost:5433` (migracje Flyway wykonują się automatycznie przy starcie).

---

## Autoryzacja i tryb deweloperski

Wszystkie endpointy biznesowe wymagają poprawnego tokena Firebase w nagłówku `Authorization: Bearer <token>`.

Do lokalnych testów (bez logowania w Firebase) można tymczasowo włączyć tryb deweloperski w `application.yml`:
```yaml
firebase:
  dev-mode: true # w produkcji i repozytorium pozostaw false
```
Przy `dev-mode: true` każdy token zaczynający się od `dev-` (np. `dev-user-1`) tworzy sesję testową bez odpytywania serwerów Google.

---

## Dokumentacja API (Swagger UI)

Pełna, zawsze aktualna specyfikacja requestów i odpowiedzi dostępna jest po uruchomieniu aplikacji pod adresem:
* **Swagger UI:** `http://localhost:8080/docs`
* **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

Aby testować zabezpieczone endpointy bezpośrednio w Swaggerze, kliknij **Authorize** i wklej sam token (bez prefiksu `Bearer `).

### Przegląd endpointów

| Metoda | Ścieżka | Opis | Auth |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/users/sync` | Rejestracja/synchronizacja użytkownika z Firebase (tworzy Peta dla nowych kont) | Tak |
| `GET` | `/api/vending-machines/search` | Wyszukiwanie maszyn wg `latitude`, `longitude`, opcjonalnego `radius` (m) i filtrów (`query`, `isOpen`, `isTopRated`) | Tak |
| `GET` | `/api/vending-machines/{id}` | Szczegóły kaucjomatu wraz ze stronicowaną listą opinii | Tak |
| `POST` | `/api/vending-machines/{id}/reviews` | Dodanie lub aktualizacja oceny (1–5) i komentarza dla maszyny | Tak |
| `POST` | `/api/vending-machines/import` | Ręczne uruchomienie importu listy kaucjomatów do bazy (Skrypt odpala się po włączeniu backendu) | Tak |