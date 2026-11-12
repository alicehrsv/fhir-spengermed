# Containerisierung & Orchestrierung einer Fullstack-Anwendung

## 1. Containerisierung & Orchestrierung

### 1.1 Worum geht’s bei Containerisierung?

Containerisierung bedeutet: Anwendungen laufen isoliert in standardisierten Laufzeitumgebungen (Containern), statt direkt am Host-System.

Dadurch wird das System:

- **portabel** (läuft überall gleich)
- **reproduzierbar** (gleiche Versionen und Dependencies)
- **einfach zu deployen** (ein kompletter Stack startet per Kommando)

---

### 1.2 Grundbegriffe: Image, Container, Dockerfile, Compose

**Docker Image**  
Ein Image ist der Bauplan bzw. ein Paket aus Anwendung, Laufzeitumgebung und allen benötigten Abhängigkeiten.  
Images sind **schreibgeschützt**.

**Container**  
Ein Container ist eine **laufende Instanz eines Images**. Er läuft isoliert vom restlichen System.

**Dockerfile**  
Ein Dockerfile ist eine Art **Rezept zum Bauen eines Images**. Es beschreibt Schritt für Schritt, wie das Image erstellt wird.

**Multi-Stage Build**  
Bei Multi-Stage Builds werden mehrere Build-Stufen verwendet:

- Stage 1: baut Artefakte (z. B. Angular Build oder Maven Package)
- Stage 2: enthält nur die Runtime

Vorteile:

- kleinere Images
- schnellerer Start
- bessere Sicherheit

**Docker Hub**  
Eine Registry zum **Speichern und Verteilen von Docker Images**.

**Docker Compose**  
Docker Compose beschreibt mehrere Services inklusive:

- Netzwerke
- Volumes
- Startreihenfolge

Damit kann ein kompletter **Stack mit einem einzigen Befehl gestartet werden**.

---

### 1.3 Architektur-Idee: Reverse Proxy als „Tor“

Ein **NGINX Reverse Proxy** fungiert als zentrale Außenstelle.

Der Browser spricht **nur den Proxy** an (Port 80 / 443).

Der Proxy verteilt Anfragen intern weiter:

- `/` → Frontend (NGINX statische Dateien)
- `/api/` → Backend (Spring Boot)
- Keycloak-Routen → Keycloak Service

Vorteile:

- nur **eine öffentliche Adresse**
- **HTTPS zentral verwaltet**
- interne Container müssen **nicht direkt ins Internet exponiert werden**

---

### 1.4 Frontend-Theorie: Angular als statische App + NGINX

Angular wird für Produktion gebaut:

```
npm run build
```

Danach werden nur noch **statische Dateien** ausgeliefert:

- HTML
- JavaScript
- CSS

#### Multi-Stage Build im Frontend-Dockerfile

**Stage 1 – Node**

- Dependencies installieren
- Angular Build erstellen

**Stage 2 – NGINX**

- liefert Inhalte aus:

```
/usr/share/nginx/html
```

#### Warum `patientUrl = "/api/patient"` statt `localhost`?

Im Container-Setup soll der Browser **nicht direkt auf localhost:8080 zugreifen**.

Stattdessen läuft alles über den **Reverse Proxy**.

Der Proxy routet:

```
/api/ → backend:8080
```

Ergebnis:

- gleiche URLs in **Dev und Produktion**
- keine Umgebungsabhängigkeit

#### NGINX im Frontend

Wichtige Konfigurationen:

- `try_files ... /index.html` für Angular Routing (Single Page App)
- `/api/` wird an `backend:8080` weitergeleitet

Für den Browser wirken **Frontend und API wie eine einzige Website**.

---

### 1.5 Reverse Proxy + HTTPS (Zertifikate)

HTTPS wird im **Reverse Proxy terminiert**.

- Port 80 → Redirect auf 443
- Port 443 → SSL aktiv + Proxy-Regeln

#### Selbstsigniertes Zertifikat

Für lokale Entwicklung ausreichend.

Das Zertifikat muss im Browser **als vertrauenswürdig importiert werden**.

#### Theorie dahinter

TLS/SSL benötigt:

- Zertifikat
- privaten Schlüssel

Der Proxy übernimmt:

- Verschlüsselung nach außen
- interne Kommunikation im Docker-Netz kann weiterhin **HTTP** sein

Das nennt man **Edge TLS**.

---

### 1.6 Backend-Theorie: Spring Boot als Resource Server (JWT)

Das Backend ist als **OAuth2 Resource Server** abgesichert.

Alle Requests auf:

```
/api/**
```

erfordern Authentifizierung.

Das Backend überprüft **JWT Tokens**.

#### Wichtige Konfiguration

**server.address**

```
server.address=0.0.0.0
```

Dadurch ist der Service im **Container-Netz erreichbar**.

**issuer-uri**

zeigt auf die Realm-URL:

```
https://localhost/realms/...
```

**jwk-set-uri**

zeigt auf Keycloak intern:

```
http://keycloak:8080/.../certs
```

Hier holt Spring die **Public Keys**, um JWT-Signaturen zu prüfen.

---

#### Rollen und Authorities

Keycloak liefert Rollen im JWT:

```
realm_access.roles
```

Ein Converter wandelt sie in Spring Authorities um:

- `ROLE_ADMIN`
- `ROLE_USER`

Diese werden später verwendet für:

- `hasRole(...)`
- `@PreAuthorize(...)`

---

#### CORS / CSRF

**CORS**

erlaubt Browser-Requests von bestimmten Origins (z. B. localhost).

**CSRF**

wird deaktiviert, da es sich um eine **REST API mit Token/JWT** handelt.

---

#### Multi-Stage Build im Backend

**Stage 1**

- Maven
- JDK
- baut das JAR

**Stage 2**

- JRE ohne Build-Tools
- führt das JAR aus

Vorteile:

- kleinere Images
- höhere Sicherheit
- schnellerer Start

---

### 1.7 Datenbank-Theorie: MySQL als Service + Persistenz

MySQL wird als fertiges Image verwendet.

Wichtige Konzepte:

**Volumes**

```
db_data
```

Sorgen für **Persistenz** der Daten nach Container-Neustarts.

**Init-Skripte**

SQL-Dateien in:

```
/docker-entrypoint-initdb.d/
```

werden beim **ersten Start automatisch ausgeführt**.

In diesem Setup werden Datenbanken für:

- Anwendung
- Keycloak

angelegt.

---

### 1.8 Keycloak-Theorie: Identity Provider + Realm Import

Keycloak übernimmt:

- Login (OpenID Connect)
- Token-Erstellung (JWT)
- Benutzerverwaltung
- Rollen
- Clients

#### Realm-Import (JSON)

Statt manueller Konfiguration wird ein definierter Zustand importiert.

Beispiel:

Realm: `spengermed`

Clients:

- `frontend` (public client mit PKCE)
- `backend` (confidential client)

Rollen:

- `user`
- `admin`

Test-User inklusive Passwort sind ebenfalls definiert.

Vorteil:

→ **reproduzierbare IAM-Konfiguration**

---

### 1.9 Docker Compose: Orchestrierung, Netzwerke, Healthchecks

Docker Compose beschreibt die gesamte Orchestrierung.

Services:

- proxy
- frontend
- backend
- db
- keycloak

Alle Services befinden sich im selben Netzwerk:

```
appnet
```

Services können sich über DNS-Namen erreichen:

- `backend`
- `db`
- `keycloak`

#### depends_on + healthcheck

Stellt sicher, dass z. B. das Backend **erst startet, wenn DB und Keycloak bereit sind**.

---

### Ports vs Expose

**ports**

macht einen Service öffentlich:

```
443:443
```

**expose**

macht einen Service nur **im Docker-Netz sichtbar**:

```
8080
```

In diesem Setup ist **nur der Proxy öffentlich erreichbar**.

---

# Terminologien & Ontologien (Medizin)

## 2. Bedeutung von Terminologien

Begriffe sind die Grundlage jeder Zusammenarbeit.

Ohne gemeinsame Sprache kann Information nicht zuverlässig übertragen werden.

In jeder Domäne existieren Fachbegriffe.

Beispiele:

- Informatik → Variable, Klasse, Objekt
- Medizin → Diagnosen, Anatomie

Ein Begriff soll bei verschiedenen Personen **dieselbe Vorstellung auslösen**.

---

### Warum ist das in der Medizin besonders wichtig?

Missverständnisse können echte Schäden verursachen.

Beispiel:

- „linkes Bein“
- Perspektive Patient vs. Arzt

Daher existieren standardisierte medizinische Lage- und Richtungsbezeichnungen.

---

### Komplexität medizinischer Begriffe

Es existieren **hunderttausende Begriffe**.

Selbst nach Ausbildung kennt man nur einen Teil.

Deshalb sind wichtig:

- Nachschlagewerke
- elektronische Hilfen
- automatische Vorschläge

---

## 2.2 Definition: Terminologie

Terminologien sind **standardisierte Fachwortschätze**.

Eigenschaften:

- Begriffe besitzen klare Definitionen
- Codes sind eindeutig
- Maschinen können Codes verarbeiten

Das ermöglicht **Interoperabilität**.

---

## 2.3 Ontologie

Eine Ontologie ist ein **Netzwerk von Informationen mit logischen Beziehungen**.

Funktionen:

- Strukturierung von Wissen
- Datenaustausch
- Zusammenführung von Wissensbeständen
- Suche in Wissensdaten
- Ableitung neuer Informationen

---

### Unterschied Terminologie vs Ontologie

**Terminologie**

- Sammlung von Begriffen
- Wörterbuch oder Code-System

**Ontologie**

- Wissensmodell
- Begriffe + Beziehungen + Logik

Merksatz:

**Terminologie = Vokabular**  
**Ontologie = Struktur des Wissens**

---

## 2.4 Wichtige medizinische Terminologien

### SNOMED CT

Systematized Nomenclature of Medicine – Clinical Terms

Eigenschaften:

- sehr umfangreich
- strukturiertes klinisches Vokabular
- maschinenlesbar

Einsatz:

- elektronische Gesundheitsakten
- klinische Dokumentation

Österreich ist seit **2018 Mitglied**.

---

### ICD-10 / ICD-11

International Classification of Diseases.

Entwickelt von der **WHO**.

ICD-10:

- veröffentlicht 1994

ICD-11:

- seit 2022 Nachfolger

Umfang:

ca. **85.000 Einträge**

Zweck:

- Statistik
- Klassifikation
- Abrechnung

---

### ICPC-2 / ICPC-3

International Classification of Primary Care.

Einsatzgebiet:

- Allgemeinmedizin
- Primärversorgung

---

## 2.5 Verbindung zu FHIR: CodeSystem & ValueSet

FHIR verwendet zwei wichtige Konzepte.

### CodeSystem

Definiert **woher ein Code stammt**.

Beispiele:

- SNOMED CT
- ICD
- LOINC

---

### ValueSet

Definiert **welche Codes erlaubt sind**.

Ein ValueSet ist eine **Auswahl von Codes** aus einem oder mehreren CodeSystems.

---

### Warum ist das wichtig?

Interoperabilität bedeutet:

- gleiche Codes
- gleiche Bedeutung
- gleiche Version
- gleiche Darstellung

---

# Interoperabilität im Gesundheitswesen

## 1. Definition

Interoperabilität bedeutet:

IT-Systeme können

- miteinander kommunizieren
- Daten austauschen
- Daten korrekt interpretieren

Beispiele:

- Krankenhaussoftware
- Arztpraxis-Systeme
- Laborsysteme
- Apotheken
- elektronische Patientenakten

---

## Beispiel

Ein Krankenhaus sendet Laborwerte digital an eine Hausarztpraxis.

Das Praxissystem kann die Daten:

- lesen
- speichern
- anzeigen

ohne manuelle Eingabe.

---

## 2. Warum ist Interoperabilität wichtig?

### 1. Bessere Patientenversorgung

- vollständige Krankengeschichte verfügbar
- weniger Fehlentscheidungen

### 2. Weniger Fehler

- keine manuelle Dateneingabe
- weniger Übertragungsfehler

### 3. Schnellere Behandlung

- Daten sofort verfügbar

### 4. Effizientere Abläufe

- weniger Papier
- weniger doppelte Untersuchungen

### 5. Kostenersparnis

- redundante Tests werden vermieden

---

## 3. Herausforderungen der Digitalisierung

### Fehlende Standardisierung

Viele Systeme nutzen unterschiedliche Formate.

Beispiele:

- verschiedene Krankheitscodes
- unterschiedliche Befundformate

---

### Datenschutz und Sicherheit

Gesundheitsdaten sind besonders sensibel.

Wichtige Themen:

- Schutz vor Hackerangriffen
- Datenschutzgesetze
- Zugriffskontrollen

---

### Legacy-Systeme

Viele Krankenhäuser nutzen alte IT-Systeme, die schwer integrierbar sind.

---

### Kosten

Digitalisierung erfordert:

- Software
- Infrastruktur
- Schulungen

---

### Akzeptanz

Neue Systeme können:

- kompliziert
- zeitaufwendig

sein und daher Widerstand erzeugen.

---

# Vorteile standardisierter Datenformate

### 1. Kontinuierliche Behandlung

Patientendaten sind überall verfügbar.

### 2. Weniger doppelte Untersuchungen

Bereits vorhandene Untersuchungen müssen nicht wiederholt werden.

### 3. Schnellere Diagnosen

Ärzte erhalten sofort Zugriff auf wichtige Informationen.

### 4. Mehr Patientensicherheit

Allergien oder Vorerkrankungen sind sofort sichtbar.

### 5. Patienten können ihre Daten selbst verwalten

z. B. über elektronische Patientenakten.

---

# FHIR-Kommunikation vs Papier

### Papierbasierte Kommunikation

Probleme:

- langsam
- fehleranfällig
- schwer zu archivieren
- manuelle Dateneingabe

---

### FHIR-basierte Kommunikation

Vorteile:

- schneller Datenaustausch
- strukturierte Daten
- automatische Verarbeitung
- Integration in verschiedene Systeme

---

### Beispiel

Papier:

Arzt schreibt Befund → Fax → Praxis tippt Daten ab.

FHIR:

System sendet strukturierte Daten → Zielsystem übernimmt sie automatisch.

---

# Rolle von WHO und FHIR

### WHO (World Health Organization)

Die WHO entwickelt internationale Klassifikationen.

Beispiele:

- ICD – Krankheiten
- ICF – Funktionsfähigkeit

Ziel:

globale Standardisierung medizinischer Daten.

---

### FHIR

FHIR wurde von **HL7** entwickelt.

Ziele:

- standardisierte Kommunikation
- moderne Webtechnologien
- einfache Integration

FHIR nutzt:

- JSON
- XML
- REST APIs

Dadurch können Systeme wie **Webservices miteinander kommunizieren**.

---

# Weitere wichtige Terminologien

### LOINC

Codes für:

- Laborwerte
- medizinische Untersuchungen

---

### ATC-Klassifikation

Klassifikation von Medikamenten nach Wirkstoffen.

Einsatz:

- Medikamentendokumentation
- Arzneimittelüberwachung

# Kubernetes (K8s) – Die wichtigsten Basics

## Was ist Kubernetes?

**Kubernetes (K8s)** ist eine Plattform zur **Orchestrierung von Containern**.  
Sie wird verwendet, um Container-Anwendungen automatisch zu **deployen, skalieren und verwalten**.

Container werden häufig mit Tools wie **Docker** erstellt.

Kurz gesagt:  
Kubernetes sorgt dafür, dass deine Container-Anwendungen stabil laufen.

---

# Grundidee von Kubernetes

Wenn du viele Container auf vielen Servern betreibst, übernimmt Kubernetes automatisch:

- Starten von Anwendungen
- Neustarten abgestürzter Container
- Skalierung bei hoher Last
- Load Balancing
- Deployment neuer Versionen

**Kurz:** Automatisches Management von Container-Anwendungen.

---

# Wichtige Kubernetes Komponenten

## Cluster

Ein **Cluster** ist eine Gruppe von Maschinen (Nodes), auf denen Kubernetes läuft.

Er besteht aus:

- **Control Plane** → steuert den Cluster
- **Worker Nodes** → führen Container aus

---

## Node

Ein **Node** ist ein Server oder eine VM im Cluster.

Typische Komponenten:

- **kubelet** – Agent der mit Kubernetes kommuniziert
- **container runtime** – z.B. Docker oder containerd
- **kube-proxy** – Netzwerk-Komponente

---

## Pod

Ein **Pod** ist die **kleinste deploybare Einheit in Kubernetes**.

Ein Pod enthält:

- einen oder mehrere Container
- gemeinsames Netzwerk
- gemeinsame Storage-Volumes

Beispiel:
Pod
├─ Container: Backend
└─ Container: Logging

---

## Deployment

Ein **Deployment** verwaltet Pods.

Funktionen:

- startet mehrere Pods
- ersetzt abgestürzte Pods automatisch
- ermöglicht **Rolling Updates**

Beispiel:
3 Pods mit einer Web-App

Wenn ein Pod abstürzt → Kubernetes erstellt automatisch einen neuen.

---

## Service

Pods bekommen ständig neue IP-Adressen.  
Ein **Service** gibt ihnen eine **stabile Netzwerkadresse**.

Typen:

- **ClusterIP** → nur intern im Cluster erreichbar
- **NodePort** → Zugriff von außen über Node-Port
- **LoadBalancer** → externer Cloud Load Balancer

---

## ConfigMap und Secret

Konfiguration wird vom Code getrennt.

**ConfigMap**
- normale Konfiguration
- Environment Variablen
- Config-Dateien

**Secret**
- Passwörter
- Tokens
- API Keys

---

## Ingress

**Ingress** ermöglicht HTTP-Routing von außen in den Cluster.

Beispiel:
example.com → web-service
api.example.com → api-service

---

# Typischer Kubernetes Workflow

1. Container Image erstellen (z.B. Docker)
2. Image in eine Registry pushen
3. Deployment YAML schreiben
4. Deployment starten

Beispiel:
kubectl apply -f deployment.yaml

`kubectl` ist das CLI Tool zum Steuern eines Kubernetes Clusters.

---

# Beispiel Deployment YAML

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: web-app

spec:
  replicas: 3

  selector:
    matchLabels:
      app: web

  template:
    metadata:
      labels:
        app: web

    spec:
      containers:
      - name: web
        image: nginx
        ports:
        - containerPort: 80
```

Vorteile von Kubernetes

automatische Skalierung

Self-Healing (Container werden neu gestartet)

Rolling Updates

Load Balancing

funktioniert in Cloud und On-Premise

Wichtigste Begriffe
Begriff	Bedeutung
Cluster	Gruppe von Nodes
Node	Server im Cluster
Pod	kleinste Einheit mit Containern
Deployment	verwaltet Pods
Service	Netzwerkzugriff
Ingress	HTTP Routing
kubectl	CLI Tool
Merksatz

Kubernetes organisiert Container so, dass Anwendungen automatisch laufen, skalieren und sich selbst reparieren.

1️⃣ Warum ist Interoperabilität im Gesundheitswesen wichtig?

Interoperabilität bedeutet, dass unterschiedliche IT-Systeme (z. B. Krankenhaussoftware, Arztpraxisprogramme, Laborsysteme) miteinander kommunizieren und Daten austauschen können.

🔹 Warum ist das wichtig?

📁 Vermeidung von Doppeluntersuchungen

⏱️ Schnellere Behandlungen

🚑 Bessere Notfallversorgung

❌ Weniger Fehler durch Medienbrüche

👩‍⚕️ Bessere Zusammenarbeit zwischen Einrichtungen

Beispiel: Ein Krankenhaus kann sofort auf Laborwerte oder Röntgenbilder zugreifen, die bei einer anderen Einrichtung erstellt wurden.

2️⃣ Herausforderungen bei der Digitalisierung von Gesundheitsdaten

Die Digitalisierung bringt viele Vorteile – aber auch Probleme:

⚠️ Technische Herausforderungen

Unterschiedliche Softwaresysteme

Fehlende einheitliche Standards

Alte IT-Systeme (Legacy-Systeme)

🔐 Datenschutz & Sicherheit

Schutz sensibler Patientendaten

Cyberangriffe

Einhaltung von Datenschutzgesetzen (z. B. DSGVO)

💰 Organisatorische & finanzielle Probleme

Hohe Kosten

Schulungsbedarf für Personal

Widerstand gegen Veränderungen

3️⃣ Vorteile für Patient:innen durch standardisierte Formate

Standardisierte Datenformate (z. B. FHIR) sorgen dafür, dass Informationen überall gleich strukturiert sind.

✅ Vorteile:

Schnellere Diagnosen

Weniger Behandlungsfehler

Transparenz für Patient:innen

Einfacher Zugriff auf eigene Gesundheitsdaten

Bessere Kontinuität der Behandlung

4️⃣ FHIR-Kommunikation im Vergleich zu Papier

Health Level Seven International (HL7) hat den Standard
Fast Healthcare Interoperability Resources (FHIR) entwickelt.

📄 Papier:

Langsam

Fehleranfällig

Schwer lesbar

Kein automatischer Datenaustausch

💻 FHIR:

Elektronischer Datenaustausch in Echtzeit

Einheitliche Struktur

Maschinenlesbar (API-basiert)

Integration in Apps & E-Health-Systeme

👉 FHIR funktioniert ähnlich wie moderne Web-Schnittstellen (APIs).

5️⃣ Rolle von HA, WHO und FHIR bei Standards
🏥 HA (Health Authority / Gesundheitsbehörden)

Setzen nationale Richtlinien um

Entscheiden, welche Standards verpflichtend sind

Überwachen Datenschutz & Qualität

🌍 World Health Organization (WHO)

Gibt internationale Empfehlungen

Fördert globale Gesundheitsstandards

Unterstützt Länder bei Digitalisierungsstrategien

💻 FHIR / HL7

Entwickeln technische Standards

Ermöglichen internationale Kompatibilität

Sorgen für strukturierte, interoperable Daten

📌 Zusammenfassung in einem Satz:

Interoperabilität ist entscheidend für eine sichere, effiziente und moderne Gesundheitsversorgung, wobei Standards wie FHIR – unterstützt von Organisationen wie WHO und nationalen Behörden – den digitalen Datenaustausch ermöglichen und Patient:innen direkt davon profitieren.