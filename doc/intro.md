# Documentation glace-api

## Vue d'ensemble

`glace-api` est une API REST pour gérer une collection de glaces. Le projet est construit autour d'une architecture propre avec séparation nette des responsabilités.

## Architecture

### Diagramme de flux

```
HTTP Request
    ↓
core.clj (Ring handlers)
    ↓
Middleware (JSON, Error handling)
    ↓
services/glaces_service.clj (Validation + Logique métier)
    ↓
repositories/glaces_repository.clj (Protocole)
    ↓
repositories/glaces_memory_repository.clj (Implémentation)
    ↓
HTTP Response
```

### Couches

#### 1. **core.clj** — HTTP Layer
- Définit les routes (`/glaces`)
- Gère le middleware (parsing JSON, gestion d'erreurs)
- Injecte le repository dans les handlers

Responsabilités :
- Recevoir/envoyer HTTP
- Traduire les exceptions en codes HTTP
- Passer les requêtes au service

#### 2. **services/glaces_service.clj** — Business Logic Layer
- Valide les entrées (via `utils/validation`)
- Orchestre les appels au repository
- Lève les exceptions métier

Responsabilités :
- Valider que le nom n'est pas vide
- Vérifier qu'une glace existe avant suppression
- Générer les messages de réponse

#### 3. **repositories/glaces_repository.clj** — Repository Protocol
Définit l'interface que toute implémentation doit respecter :

```clojure
(defprotocol GlacesRepository
  (create-glace [this name])
  (delete-glace [this id])
  (glace->exist [this id])
  (get-all [this]))
```

#### 4. **repositories/glaces_memory_repository.clj** — Data Layer
Implémentation en mémoire avec atoms :
- `glaces` : vecteur contenant les entités
- `next-id` : compteur pour les IDs

Permet de tester rapidement sans base de données.

#### 5. **utils/validation.clj** — Shared Utilities
Fonctions de validation réutilisables :
- `validate-field-str` : vérifie qu'un string n'est pas vide
- `validate-field-int` : vérifie qu'un int n'est pas nil

## Gestion des erreurs

Les exceptions métier sont levées avec `ex-info` et classées par type :

```clojure
:type :validation   → 400 Bad Request
:type :not-found    → 404 Not Found
:type :*            → 500 Internal Server Error
```

Le middleware `handle-error-middleware` traduit ces exceptions en réponses HTTP propres.

## Extensibilité

### Ajouter une nouvelle implémentation de repository

1. Créer `postgres_repository.clj`
2. Implémenter le protocole `GlacesRepository`
3. Dans `core.clj`, remplacer :
   ```clojure
   (def repository (mem-repo/make-memory-repository))
   ```
   par :
   ```clojure
   (def repository (pg-repo/make-postgres-repository config))
   ```

Le reste du code reste identique.

### Ajouter une nouvelle validation

1. Ajouter la fonction dans `validation.clj`
2. L'utiliser dans `glaces_service.clj`

### Ajouter une nouvelle route

1. Ajouter l'endpoint dans le `ring/router` de `core.clj`
2. Implémenter la logique dans `glaces_service.clj`
3. Couvrir avec des tests dans `core_test.clj`

## Tests

### Service tests (`service_test.clj`)
- Testent la logique métier isolée du HTTP
- Utilisent un fake repository en mémoire
- Vérifient les validations et les erreurs

### HTTP tests (`core_test.clj`)
- Testent les endpoints et codes HTTP
- Utilisent `ring-mock` pour simuler les requêtes
- Vérifient que les erreurs sont bien traduites en status HTTP

## Décisions de Design

### Pourquoi 3 couches ?

1. **HTTP** ne doit pas connaître la logique métier — seule interface vers le client
2. **Service** ne doit pas connaître HTTP — encapsule la logique métier
3. **Repository** ne doit pas connaître les détails métier — abstrait juste l'accès aux données

Cette séparation permet :
- De tester chaque couche indépendamment
- De swapper une implémentation (PostgreSQL pour Memory)
- De réutiliser la logique métier dans d'autres contextes (CLI, batch, etc)

### Pourquoi les exceptions plutôt qu'un Result type ?

Les exceptions avec types métier (`:validation`, `:not-found`) sont simples et suffisantes ici.

Un Result pattern (`{:ok value}` ou `{:error detail}`) serait utile si :
- Plusieurs contextes d'erreur nécessitent des messages différents
- La validation doit être décorrélée de la levée d'exception
- Le projet devient très grand

Pour l'instant, exceptions = bon compromis simplicité/clarté.

### Standardisation des réponses

Toutes les réponses métier retournent `{:success data}` pour clarifier :
- Ce n'est pas une erreur
- La data est cohérente

Les erreurs lèvent des exceptions, pas des réponses normales.

---

## Améliorations futures

- [ ] Persistance PostgreSQL
- [ ] Authentification JWT
- [ ] Logging structuré
- [ ] Pagination
- [ ] Swagger/OpenAPI
- [ ] Docker
- [ ] CI/CD (GitHub Actions)
- [ ] Result/Either pattern pour plus de flexibilité
