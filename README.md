# glace-api

API REST simple pour gérer une collection de glaces (desserts).

## Stack

- **Clojure** 1.12.2
- **Ring** + **Jetty** (serveur HTTP)
- **Reitit** (routage)
- **Ring-json** (sérialisation JSON)

## Architecture

L'API suit une architecture en 3 couches avec organisation par packages :

```
src/glace_api/
├── core.clj                           → HTTP (routes, middleware)
├── services/
│   └── glaces_service.clj            → Logique métier + validation
├── repositories/
│   ├── glaces_repository.clj         → Protocole (interface)
│   └── glaces_memory_repository.clj  → Implémentation mémoire
└── utils/
    └── validation.clj                → Utilitaires de validation
```

## Installation

```bash
git clone <repo>
cd glace-api
lein deps
```

## Lancement

```bash
lein run
```

Le serveur démarre sur `http://localhost:8090`

## API Endpoints

### GET /glaces
Retourne la liste de toutes les glaces.

```bash
curl http://localhost:8090/glaces
```

**Réponse 200:**
```json
{
  "success": [
    {"id": 1, "name": "fraise", "state": 1, "created_at": "2026-06-18T..."}
  ]
}
```

---

### GET /glaces/:id
Récupère une glace spécifique par id.

```bash
curl http://localhost:8090/glaces/1
```

**Réponse 200:**
```json
{
  "success": {"id": 1, "name": "fraise", "state": 1, "created_at": "2026-06-18T..."}
}
```

**Réponse 404:** (si id n'existe pas)
```json
{"error": "can't find element"}
```

---

### POST /glaces
Crée une nouvelle glace.

```bash
curl -X POST http://localhost:8090/glaces \
  -H "Content-Type: application/json" \
  -d '{"name": "fraise"}'
```

**Réponse 200:**
```json
{"success": "create : fraise"}
```

**Réponse 400:** (si name est vide ou nil)
```json
{"error": "missing a str field"}
```

---

### PUT /glaces
Incrémente l'état d'une glace.

États : 1=preparation, 2=freezing, 3=ready, 4=consumption

```bash
curl -X PUT http://localhost:8090/glaces \
  -H "Content-Type: application/json" \
  -d '{"id": 1}'
```

**Réponse 200:**
```json
{"success": "edited : 1"}
```

**Réponse 400:** (si state >= 4)
```json
{"error": "State out of bound"}
```

**Réponse 404:** (si id n'existe pas)
```json
{"error": "can't find element"}
```

---

### DELETE /glaces
Supprime une glace par id.

```bash
curl -X DELETE http://localhost:8090/glaces \
  -H "Content-Type: application/json" \
  -d '{"id": 1}'
```

**Réponse 200:**
```json
{"success": "supprimé : 1"}
```

**Réponse 404:** (si id n'existe pas)
```json
{"error": "can't find element"}
```

**Réponse 400:** (si id est nil)
```json
{"error": "missing an int field"}
```

## Tests

```bash
lein test
```

Tests séparés en deux niveaux :
- `service_test.clj` : logique métier et validation
- `core_test.clj` : endpoints HTTP et codes de réponse

## Stockage

Actuellement, les données sont stockées en mémoire (atoms). Pour ajouter une persistance :

1. Créer une nouvelle implémentation du protocole `GlacesRepository`
2. Implémenter les 4 méthodes : `create-glace`, `delete-glace`, `glace->exist`, `get-all`
3. Injecter l'implémentation dans `core.clj`

Exemple : `postgres_repository.clj` pour PostgreSQL.

## License

EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0
