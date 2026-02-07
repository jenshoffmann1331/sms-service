# Befehlsnachricht Service

Quarkus Kotlin Backend mit PostgreSQL und RabbitMQ.

## Features

- POST /befehlsnachricht Endpoint
- JPA Entity Persistierung in PostgreSQL
- Event Publishing über RabbitMQ nach erfolgreichem Speichern
- Docker Compose für lokale Entwicklung

## Starten

1. Docker Services starten:
```bash
docker compose up -d
```

2. Quarkus Dev Mode starten:
```bash
./gradlew quarkusDev
```

## API

### POST /befehlsnachricht
Erstellt eine neue Befehlsnachricht, speichert sie in PostgreSQL und publiziert ein Event in RabbitMQ.

**Request:**
```json
{
  "inhalt": "Beispiel Nachricht"
}
```

**Response (201):**
```json
{
  "id": 1,
  "inhalt": "Beispiel Nachricht",
  "erstelltAm": "2026-02-07T10:30:00"
}
```

**Beispiel:**
```bash
curl -X POST http://localhost:8080/befehlsnachricht \
  -H "Content-Type: application/json" \
  -d '{"inhalt":"Test Nachricht"}'
```

## Tests

```bash
./gradlew test
```

## Datenbank prüfen

```bash
docker exec befehlsnachricht-postgres psql -U postgres -d befehlsnachricht \
  -c "SELECT * FROM befehlsnachricht;"
```

## RabbitMQ

- Management UI: http://localhost:15672 (guest/guest)
- Exchange: befehlsnachricht.events (topic, durable)

**Messages prüfen:**
```bash
curl -u guest:guest http://localhost:15672/api/exchanges/%2F/befehlsnachricht.events
```
