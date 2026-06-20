# Changelog
Tous les changements notables du projet sont documentés ici.

## [0.1.0] - 2026-06-20

### Added
- API REST pour gérer une collection de glaces
- Endpoints : GET /glaces, GET /glaces/:id, POST /glaces, PUT /glaces, DELETE /glaces
- Architecture en 3 couches (HTTP, service, repository)
- Protocole `GlacesRepository` pour abstraction des données
- Implémentation en mémoire (`GlaceMemoryRepository`) avec atoms
- Validation centralisée des entrées (champs obligatoires)
- Gestion centralisée des erreurs HTTP (400, 404, 500)
- Tests unitaires (service et HTTP) avec fixtures
- Documentation API complète (README, doc/intro)
- Réponses standardisées avec wrapper `{:success ...}`

### Features
- Création de glaces avec validation du nom
- Suppression de glaces par id avec vérification d'existence
- Listing de toutes les glaces
- Récupération d'une glace par id
- Mise à jour d'état avec validation (états 1-4)
- Codes HTTP appropriés (200, 400, 404, 500)
- Organization par packages (services/, repositories/, utils/)

### Fixed
- Accès cohérent aux champs du defrecord via `this`
- Correction des namespaces dans les imports
- Standardisation des noms de fonctions du protocole

## [Unreleased]

### Planned
- Persistance PostgreSQL
- Authentification / Autorisation
- Pagination pour GET /glaces
- Logging structuré
- Swagger/OpenAPI documentation
- Docker support
- CI/CD (GitHub Actions)
- Result/Either pattern pour gestion d'erreurs
