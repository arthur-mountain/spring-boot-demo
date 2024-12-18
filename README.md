# **Event Tracking System**

事件追蹤系統的目標是記錄、存儲、分析和可視化應用程序或用戶行為中的事件數據。

## 🔧 **Prerequisites**

1. [Docker](https://www.docker.com)

2. Java >= 21

## 🚀 **Getting Started**

### 系統架構

```
用戶事件 > API Gateway ---> Kafka ---> Processing Service ---> PostgreSQL (存儲) ---> Redis (緩存) ---> Grafana (可視化)
```

## 🧩 **核心功能模組**

### **1. 事件接收與記錄**

- 提供 RESTful API 接收事件，並將其推送至 Kafka。

- 支援多種事件類型（例如用戶行為、系統性能）。

#### API 範例

- **記錄事件**

  - URL: `POST /api/events`

  - Request Body:

    ```json
    {
      "event_type": "user_click",
      "user_id": "12345",
      "timestamp": "2024-01-01T12:00:00Z",
      "metadata": {
        "page": "/home",
        "button": "signup"
      }
    }
    ```

  - Response:

    ```json
    {
      "message": "Event received successfully"
    }
    ```

### **2. 實時數據處理**

- 使用 Kafka 消費事件數據，執行以下任務：

  - 數據清洗與過濾。

  - 聚合分析（例如統計事件類型的出現頻率）。

  - 異常檢測（例如某個 API 的錯誤率超過 5% 時觸發警報）。

### **3. 數據存儲與查詢**

- PostgreSQL 存儲事件數據，支援高效的數據查詢與分析。

- Redis 提供緩存支持，加速熱點數據查詢。

#### API 範例

- **查詢用戶事件**

  - URL: `GET /api/events/user/{user_id}

    - parameters:

      (optional) started_at: string, default: today

      (optional) ended_at: string, default: 30 days after started_at

  - Response:

    ```json
    [
      {
        "event_type": "user_click",
        "timestamp": "2024-01-01T12:00:00Z",
        "metadata": {
          "page": "/home",
          "button": "signup"
        }
      },
      {
        "event_type": "user_login",
        "timestamp": "2024-01-02T10:00:00Z",
        "metadata": {}
      }
    ]
    ```

### **4. 可視化與報告**

- 整合 Grafana，提供以下指標的圖形化展示：

  - 每小時的事件流量。

  - 熱門事件類型及分佈。

  - 異常事件提醒（如某類事件激增）。

## 🌐 **Endpoints(api/v1)**

### 1. 事件相關

- `POST /api/v1/events`：記錄事件。

- `GET /api/v1/events/user/{user_id}`：查詢用戶的事件數據。

- `GET /api/v1/events/stats`：獲取事件統計數據。

### 2. 系統健康檢查

- `GET /api/v1/health`：檢查系統服務狀態。

## 🗄 **Database Tables**

### **1. events**

存儲事件的基礎表。

| 字段名稱     | 類型   | 描述                        |
| ------------ | ------ | --------------------------- |
| `id`         | 主鍵   | 唯一標識符                  |
| `event_type` | 字符串 | 事件類型（如 `user_click`） |
| `user_id`    | 字符串 | 觸發事件的用戶 ID           |
| `timestamp`  | 時間戳 | 事件發生的時間              |
| `metadata`   | JSONB  | 可變的事件數據              |

### **2. aggregated_events**

存儲聚合後的事件統計數據。

| 字段名稱     | 類型   | 描述               |
| ------------ | ------ | ------------------ |
| `id`         | 主鍵   | 唯一標識符         |
| `event_type` | 字符串 | 事件類型           |
| `date`       | 日期   | 聚合的日期         |
| `count`      | 整數   | 該類事件的出現次數 |

## 🔑 **核心技術**

- **Redis**: 用於緩存熱點數據（如最新事件流）並提升查詢性能。

- **Kafka**: 處理事件流，確保高吞吐量與事件的解耦。

- **PostgreSQL**: 存儲結構化事件數據，支援靈活查詢。

- **Grafana**: 提供事件數據的可視化報表。

## 🛠 **擴展功能**

1. **異常檢測與提醒**：當某類事件激增時，通過 Kafka 消息觸發通知。

2. **全文檢索功能**：整合 Elasticsearch，支持基於事件數據的全文檢索。

3. **事件重播**：允許重播歷史事件流，用於數據恢復或錯誤調試。

4. **多租戶支持**：設計隔離的事件數據存儲與查詢，支援多應用場景。

## ✅ TODOs

- [x] Initialize spring boot project

- [x] Setup docker compose

  - postgresql

  - redis

- [x] Initialize project structure

- [x] Design api endpoints

- [x] Design database table.

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
