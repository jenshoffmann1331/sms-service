# SMS Versand Service

Quarkus Kotlin Service für SMS-Versand über JSMPP mit RabbitMQ Consumer.

## Features

- RabbitMQ Consumer für Befehlsnachricht-Events
- JSMPP Integration mit Round-Robin Load Balancing
- Automatisches Session-Management mit Reconnect
- Message Age Validation (60 Sekunden)
- Requeue bei Versand-Fehler
- 3 SMSC Simulator Instanzen für Dev-Modus

## Starten

1. Docker Services starten:
```bash
docker compose up -d
```

2. Quarkus Dev Mode starten:
```bash
./gradlew quarkusDev
```

## Architektur

- **BefehlsnachrichtConsumer**: Konsumiert Messages aus RabbitMQ
- **SmsService**: Versendet SMS über JSMPP
- **SmppSessionManager**: Verwaltet SMPP Sessions mit Auto-Reconnect
- **SmscConfig**: Konfiguration für SMSC Instanzen

## Konfiguration

SMSC Instanzen werden in `application.properties` konfiguriert:
- Port 2775: smsc-simulator-1
- Port 2776: smsc-simulator-2
- Port 2777: smsc-simulator-3

## Tests

```bash
./gradlew test
```

## Message Flow

1. Message aus RabbitMQ Queue konsumieren
2. Alter der Message prüfen (max 60 Sekunden)
3. SMS über verfügbare SMPP Session senden (Round-Robin)
4. Bei Erfolg: ACK, bei Fehler: NACK (Requeue)
