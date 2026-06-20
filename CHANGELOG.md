# Changelog
Tous les changements notables du projet sont documentés ici.

## [0.1.0] - 2026-06-18

### Added
- API REST pour gérer une collection de glaces
- Endpoints : GET /glaces, POST /glaces, DELETE /glaces
- Architecture en 3 couches (HTTP, service, repository)
- Protocole `GlacesRepository` pour abstraction des données
- Implémentation en mémoire (`GlaceMemoryRepository`)
- Validation des entrées (champs obligatoires)
- Gestion centralisée des erreurs HTTP (400, 404, 500)
- Tests unitaires (service et HTTP)
- Documentation API complète

### Features
- Création de glaces avec validation du nom
- Suppression de glaces par id avec vérification d'existence
- Listing de toutes les glaces
- Réponses JSON standardisées
- Codes HTTP appropriés (200, 201, 400, 404, 500)

## [Unreleased]

### Planned
- Persistance PostgreSQL
- Authentification / Autorisation
- Pagination pour GET /glaces
- Mise à jour partielle (PATCH /glaces/:id)
- Rate limiting
- Logging structuré
- Swagger/OpenAPI documentation
- Docker support
