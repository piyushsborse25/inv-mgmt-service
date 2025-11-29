# 🔎 Order Search API — Dynamic Filtering

This API allows searching Orders using **multiple dynamic filters**, supporting:

* Enum-based fields
* Various operators
* AND / OR chaining (`prevConj`)
* Pagination

---

## 📌 Endpoint

**POST**

```
/inv-mgmt-service/v1/orders/search
```

Base URL when running locally:

```
http://localhost:8090
```

---

## 📝 Request Structure

| Field              | Type         | Required | Description                                  |
| ------------------ | ------------ | :------: | -------------------------------------------- |
| filters            | List<Filter> |     ✔    | Search filter conditions                     |
| filters[].field    | Enum         |     ✔    | Search key (e.g. `ORDER_ID`, `PRODUCT_NAME`) |
| filters[].operator | Enum         |     ✔    | Condition operator                           |
| filters[].value    | Any          |     ✔    | Value to compare                             |
| filters[].prevConj | Enum         |     ✘    | `AND` / `OR` → default `AND`                 |
| page               | number       |     ✘    | Pagination offset (default `0`)              |
| size               | number       |     ✘    | Page size (default `10`)                     |

---

## 🎯 Example cURL Request

```bash
curl --location 'http://localhost:8090/inv-mgmt-service/v1/orders/search' \
--header 'Content-Type: application/json' \
--data '{
    "filters": [
        {
            "field": "ORDER_ID",
            "operator": "EQUALS",
            "value": 12,
            "prevConj": null
        },
        {
            "field": "PRODUCT_NAME",
            "operator": "CONTAINS",
            "value": "S",
            "prevConj": "OR"
        },
        {
            "field": "CUSTOMER_FULL_NAME",
            "operator": "CONTAINS",
            "value": "Y",
            "prevConj": "AND"
        }
    ],
    "page": 0,
    "size": 10
}'
```

---

## 🔍 Business Logic Interpretation

The above request will translate to:

```
(ORDER_ID = 12)
OR (PRODUCT_NAME LIKE '%S%')
AND (CUSTOMER_FULL_NAME LIKE '%Y%')
```

👉 Filters execute in sequence using `prevConj` value.

---

## 🧪 Response Example

```json
{
  "content": [
    {
      "id": 12,
      "customerName": "Yu min",
      "totalPrice": 52000,
      "status": "COMPLETED",
      "createdAt": "2025-10-26T14:23:00"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

---

## 🧩 Valid Filter Fields (Enum-Based)

Common examples:

| Field              | Meaning                      |
| ------------------ | ---------------------------- |
| ORDER_ID           | Order ID                     |
| PRODUCT_NAME       | Name of product inside order |
| CUSTOMER_FULL_NAME | Customer’s full name         |
| ORDER_TOTAL_PRICE  | Total order value            |
| CUSTOMER_EMAIL     | Customer email               |

Operators include:

```
EQUALS, NOT_EQUALS, CONTAINS,
GREATER_THAN, LESS_THAN, IN, BETWEEN,
STARTS_WITH, ENDS_WITH, IS_NULL, ...
```
