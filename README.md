# Filters API

## Requirements

- Java 21
- Postgres
- Redis

## Quickstart

`docker compose up`

Access [Swagger](http://localhost:8080/swagger-ui/index.html)

## Design considerations

- Main Entity is a **Filter**
- Filter has a list of **Criterions**
- **Criterion** is an abstract class that must be extended to create criterion for concrete type
- All **Criterion** implementations should be collectable as a list at runtime
- Criteria list should be mapped to **Classifications** that can be later used by Frondend to show correct options for each field
- Endpoints (except for authentication) are secured by a JWT Bearer token

To make it easier to test, I've created a **Movie** entity that can be used to test the filters.

- All movies can be fetched from database; using redis as cache
- Filtered movies list can be queried by a filter uuid; using redis as cache
- Change in filter results in a new filter; filtered list of movies with old filter is evicted from cache