# ✅ STRUCTURE CHUẨN CỦA `core/context

core/context
│
├── ContextException.java
├── ContextKey.java
├── ContextKeyFactory.java
├── ContextNamespace.java
├── ContextStore.java
├── TestContext.java
│
├── view
│ ├── ContextView.java
│ └── ResponseView.java
│
├── adapter
│ ├── ContextAdapter.java
│ └── ResponseAdapter.java
│
├── registry
│ ├── ContextRegistry.java
│ └── ContextViewFactory.java
│
├── lifecycle
│ └── ContextBootstrap.java
│
├── api
│ ├── ApiContext.java
│ ├── DefaultApiContext.java
│ └── ApiContextBuilder.java
│
├── web
│ ├── WebContext.java
│ ├── DefaultWebContext.java
│ └── WebContextBuilder.java
│
└── mobile
├── MobileContext.java
├── DefaultMobileContext.java
└── MobileContextBuilder.java

````

👉 **KHÔNG có ValidationContext trong core/context nữa**

---

# 🧠 GIẢI THÍCH THEO TẦNG (RẤT QUAN TRỌNG)

## 1️⃣ CORE PRIMITIVES (xương sống)

```text
ContextKey
ContextKeyFactory
ContextNamespace
ContextStore
ContextException
````

### Vai trò

- **100% platform-agnostic**
- Không biết API / Web / Mobile
- Không biết validation
- Không biết tool

👉 Đây là **infrastructure layer**, không bao giờ import ngược lên trên.

---

## 2️⃣ TestContext (ROOT AGGREGATOR)

```text
TestContext
```

### Vai trò

- Wrap `ContextStore`
- Expose typed getters:

  - `api()`
  - `web()`
  - `mobile()`

- Là entry point cho test & lifecycle

👉 **TestContext không chứa logic**
👉 Chỉ là **context orchestrator**

---

## 3️⃣ VIEW LAYER (READ-ONLY PROJECTION)

```text
view/
 ├── ContextView
 └── ResponseView
```

### Vai trò

- Interface cho validator / assertion
- Không phụ thuộc tool
- Không mutate state

👉 **View ≠ Adapter**
👉 View chỉ đọc từ context

---

## 4️⃣ ADAPTER LAYER (TOOL → CONTEXT)

```text
adapter/
 ├── ContextAdapter
 └── ResponseAdapter
```

### Vai trò

- Bridge giữa tool (RestAssured, Selenium, Appium…) và Context
- Không expose cho validator
- Không trả View trực tiếp

👉 Adapter **ghi vào context**
👉 View **đọc từ context**

---

## 5️⃣ REGISTRY (TRÁNH HARD-CODE)

```text
registry/
 ├── ContextRegistry
 └── ContextViewFactory
```

### Vai trò

- Centralized registration:

  - Context type
  - Adapter
  - View factory

- Không sinh string
- Không biết tool cụ thể

👉 **ContextRegistry = single source of truth**

---

## 6️⃣ LIFECYCLE (BOOTSTRAP / CLEANUP)

```text
lifecycle/
 └── ContextBootstrap
```

### Vai trò

- Init context per test
- Attach adapter
- Clear store after test
- Enable / disable theo platform

👉 Đây là nơi gắn với BaseTest / JUnit / TestNG

---

## 7️⃣ PLATFORM CONTEXTS (STATE ONLY)

### API

```text
api/
 ├── ApiContext
 ├── DefaultApiContext
 └── ApiContextBuilder
```

### WEB

```text
web/
 ├── WebContext
 ├── DefaultWebContext
 └── WebContextBuilder
```

### MOBILE

```text
mobile/
 ├── MobileContext
 ├── DefaultMobileContext
 └── MobileContextBuilder
```

### Vai trò

- Chỉ giữ state
- Không biết adapter
- Không biết view
- Không validate

👉 Context = **state holder thuần**

---

# ❌ NHỮNG THỨ ĐÃ BỊ LOẠI / DI CHUYỂN

| Thành phần                            | Trạng thái                    |
| ------------------------------------- | ----------------------------- |
| `ValidationContext`                   | ❌ **Loại khỏi core/context** |
| `ValidationContextKeys`               | ❌ Không tồn tại ở đây        |
| Hard-code `"context"`                 | ❌ Không cho phép             |
| Tool-specific logic trong ViewFactory | ❌                            |

---

# 🎯 KẾT LUẬN (CHỐT)

> 👉 **Structure trên là “điểm neo”**
