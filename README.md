# spring-boot-demo

Sprint boot blog system

## 🔧 Prerequisites

1. [Docker](https://www.docker.com)

2. Java >= 21

## 🚀 Getting Started

## 🌐 Endpoints(api/v1)

1. GET: posts

- parameters:

  - (optional) page: int, default: 1
  - (optional) limit: int, default: 15
  - (optional) name: string
  - (optional) tags: string[]

- response:

```json
{
  "data": [
    {
      "id": 1,
      "title": "title",
      "description": "description",
      "tags": ["tag1", "tag2"],
      "created_at": "2025-01-01T00:00:00Z",
      "updated_at": "2025-01-01T00:00:00Z"
    }
  ]
}
```

2. GET: posts/${id}

- response:

```json
{
  "data": [
    {
      "id": 1,
      "title": "title",
      "content": "html content",
      "created_at": "2025-01-01T00:00:00Z",
      "updated_at": "2025-01-01T00:00:00Z"
    }
  ]
}
```

3. GET: projects

- parameters:

  - (optional) page: int, default: 1
  - (optional) limit: int, default: 15
  - (optional) name: string
  - (optional) tags: string[]

- response:

```json
{
  "data": [
    {
      "id": 1,
      "title": "title",
      "description": "description",
      "tags": ["tag1", "tag2"],
      "created_at": "2025-01-01T00:00:00Z",
      "updated_at": "2025-01-01T00:00:00Z"
    }
  ]
}
```

4. POST: subscribe

- body:

  - email: string

5. GET: me

- response:

```json
{
  "data": {
    "id": 1,
    "name": "name",
    avatar: "url",
    "hero": "url",
    introduction: "introduction",
    "skills": ["skill1", "skill2"],
    "expericences": [
      {
        "title": "title",
        "description": "description"
        "company": "company",
        "started_at": "2025-01-01T00:00:00Z",
        "ended_at": "2025-01-01T00:00:00Z",
      }
    ],
    "educations": [
      {
        "title": "title",
        "description": "description"
        "school": "school",
        "started_at": "2025-01-01T00:00:00Z",
        "ended_at": "2025-01-01T00:00:00Z",
      }
    ],
    "created_at": "2025-01-01T00:00",
    "updated_at": "2025-01-01T00:00:00Z"
  }
}
```

## ✅ TODOs

- [x] Initialize spring boot project

- [x] Setup docker compose

  - postgresql

  - redis

- [x] Initialize project structure

- [x] Design api endpoints

- [ ] Design database table.

- [ ] Create migrations sql for database in docker.

## ⚖️ Licenses

This project is licensed under the [Apache-License](LICENSE).

```plaintext
Copyright 2024 arthur-mountain

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

The third-party licenses used in this project are listed in [THIRD-PARTY-LICENSE](THIRD-PARTY-LICENSE).

## 📚 Refs

[UI](https://www.figma.com/community/file/1235152009438565697)
