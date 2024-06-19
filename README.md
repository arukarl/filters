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


## Example requests

### Authenticate for read-write or read-only access
`GET /api/v1/auth/write` or `GET /api/v1/auth/read`


JWT token
```
eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJNciBOb3JyaXMiLCJzdWIiOiJBc2tFbmQiLCJST0xFIjpbIlJFQUQiLCJXUklURSJdLCJleHAiOjE3MTg4MTI5MTd9.3qmL41bnte-G8xTIh2oRt0sh0LILAcrXj2DnzfCWZ7GZ8Sd44lidcRKTnS6XR7uJ
```
Header
```json
{
"alg": "HS384",
"typ": "JWT"
}
```
Payload
```json
{
  "iss": "Mr Norris",
  "sub": "AskEnd",
  "ROLE": [
    "READ",
    "WRITE"
  ],
  "exp": 1718812917
}
```
---
### Get available fields and criteria 
`GET /api/v1/classifications`

```json
{
  "criteria": [
    {
      "fieldName": "views",
      "fieldType": "NUMBER",
      "operators": [
        "GREATER_THAN",
        "GREATER_THAN_OR_EQUAL",
        "EQUAL",
        "LESS_THAN_OR_EQUAL",
        "LESS_THAN"
      ]
    },
    {
      "fieldName": "releaseDate",
      "fieldType": "DATE",
      "operators": [
        "AFTER",
        "NOT_BEFORE",
        "EQUAL",
        "NOT_AFTER",
        "BEFORE"
      ]
    },
    {
      "fieldName": "title",
      "fieldType": "STRING",
      "operators": [
        "BEGINS_WITH",
        "NOT_BEGINS_WITH",
        "CONTAINS",
        "NOT_CONTAINS",
        "ENDS_WITH",
        "NOT_ENDS_WITH"
      ]
    }
  ]
}
```

---

### Create filter 
`PATCH /api/v1/filter`

```json
{
  "criteria": [
    {
      "type": "STRING",
      "targetField": "title",
      "operator": "CONTAINS",
      "targetValue": "my"
    }
  ]
}
```

---

### Get filtered list of movies
`GET /api/v1/movie/filtered?filter=f47ac10b-58cc-4372-a567-0e02b2c3d479`

```json
[
  {
    "title": "The Matrix",
    "views": 20,
    "releaseDate": "1999-03-31"
  }
]
```
### Other endpoints @ [Swagger](http://localhost:8080/swagger-ui/index.html) or [docs](/api/docs/requests.example.http)