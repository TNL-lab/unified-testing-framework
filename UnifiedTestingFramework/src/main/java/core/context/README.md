# Core Context Architecture

## 1. Purpose

`core/context` là **trục kiến trúc trung tâm (execution backbone)** của framework.

Nó đảm bảo:

- Framework **không phụ thuộc tool** (RestAssured, Selenium, Appium, …)
- Có thể **thay đổi hoặc mở rộng platform** mà không phá vỡ test / validator
- API / Web / Mobile dùng **chung 1 execution model**

---

## 2. Design Principles

1. **Context ≠ Tool**
2. **Adapters isolate tools**
3. **Views are read-only**
4. **Strongly-typed keys**
5. **Centralized lifecycle & registry**

---

## 3. Final Structure

```
core/context
│
├── ContextException.java
├── ContextNamespace.java
├── ContextKey.java
├── ContextKeyFactory.java
├── ContextStore.java
├── TestContext.java
│
├── lifecycle
│   └── ContextBootstrap.java
│
├── registry
│   ├── ContextRegistry.java
│   └── ContextViewFactory.java
│
├── adapter
│   ├── ContextAdapter.java
│   └── ResponseAdapter.java
│
├── view
│   └── ContextView.java
│
├── api
│   ├── ApiContext.java
│   ├── ApiContextBuilder.java
│   ├── DefaultApiContext.java
│   │
│   ├── adapter
│   │   ├── ApiResponseAdapter.java
│   │   ├── RestAssuredAdapter.java
│   │   └── OkHttpAdapter.java
│   │
│   └── view
│       ├── ApiResponseView.java
│       ├── RawJsonView.java
│       ├── SnapshotView.java
│       └── impl
│       	  ├── DefaultApiResponseView.java
│       	  ├── DefaultRawJsonView.java
│	      	  └── DefaultSnapshotView.java
│
├── web
│   ├── WebContext.java
│   ├── WebContextBuilder.java
│   ├── DefaultWebContext.java
│   │
│   ├── adapter
│   │   ├── WebDriverAdapter.java
│   │   ├── SeleniumAdapter.java
│   │   └── PlaywrightAdapter.java
│   │
│   └── view
│       └── PageView.java
│
└── mobile
    ├── MobileContext.java
    ├── MobileContextBuilder.java
    ├── DefaultMobileContext.java
    │
    ├── adapter
    │   ├── MobileDriverAdapter.java
    │   └── AppiumAdapter.java
    │
    └── view
        └── ScreenView.java
```

---

## 4. Core Flow (High-Level)

```
ContextNamespace
    ↓
ContextKey / ContextKeyFactory
    ↓
ContextStore
    ↓
TestContext
    ↓
ContextBootstrap
    ↓
ContextRegistry
    ↓
ContextAdapter
    ↓
ContextViewFactory
    ↓
ContextView
    ↓
Validator / Contract / Assertion
```

---

## 5. Layer-by-Layer Execution Order

---

### ** Phase 1 – Core foundation (bắt buộc, không phụ thuộc tool)**

#### 5.0 Implement

- `ContextException` → Exception dùng chung cho toàn context layer
- `ContextNamespace`
- `ContextKey<T>`
- `ContextKeyFactory`

#### 5.1 Namespace & Key Layer (Foundation)

###### Files & Order

```
ContextNamespace
    ↓
ContextKey
    ↓
ContextKeyFactory
```

###### Responsibilities

- `ContextNamespace`

  - Định nghĩa **logical ownership** (ROOT, API, WEB, MOBILE,...)
  - Tránh key collision (api._, web._, mobile.\*)
  - **Quan trọng**
    - ❌ Không hard-code `"api"`, `"web"` ở bất kỳ file nào khác
    - Namespace là **SINGLE OF THE TRUTH**

- `ContextKey<T>`

  - Typed key (name + namespace + type), compile-time safety
  - Không còn `Map<String, Object>` bừa bãi
  - Compile-time hint cho IDE

- `ContextKeyFactory`
  - Factory tạo ContextKey (root / api / web / byNamespace)
  - Centralized key creation
  - **Single source of truth** cho key naming
  - Không hard-code string ở nơi khác hay hard-code `"context"`
  - Không cần `*ContextKeys.java` lặt vặt
  - Nếu sau này thêm AI sau này **không sửa core**

👉 **Không có ContextStore nếu chưa có Key**

---

### ** Phase 1 – Core foundation (bắt buộc, không phụ thuộc tool)**

### ** Phase 2 – Runtime Context Storage**

#### Implement

- `ContextStore`
- `TestContext`

#### 5.2 Storage Layer

###### Files & Order

```
ContextKey
    ↓
ContextStore (internal)
```

###### Responsibilities

- `ContextStore`

  - Thread-safe storage (lưu trữ ContextKey → value (thread-safe, typed)) cho tất cả context data
  - Không chứa logic nghiệp vụ
  - Không expose map
  - Fail-fast nếu context thiếu
  - Vì sao ContextStore là final + package-private?
    - Không cho subclass (tránh phá invariant)
    - Chỉ TestContext được dùng
    - Không public API,WEB,MOBILE,... → dễ refactor

👉 Store **chỉ biết key & value**

---

#### 5.3 Execution Context Layer

###### Files & Order

```
ContextStore (internal)
    ↓
TestContext (public)
```

###### Responsibilities

- `TestContext`

  - Là facade duy nhất cho test / framework được dùng
  - Central execution object
  - Wrapper quanh ContextStore
  - Fail fast khi context thiếu
  - Mỗi test = 1 TestContext
  - Expose:
    - api()
    - web()
    - mobile()

👉 Test **không bao giờ** truy cập ContextStore trực tiếp

---

### ** Phase 2 – Runtime Context Storage**

### ** Phase 3 – Lifecycle + Registry**

#### Implement

- `ContextRegistry`
- `ContextViewFactory`
- `ContextBootstrap`

#### 5.4 Lifecycle Layer

##### Files & Order

```
TestContext
    ↓
ContextBootstrap
```

##### Responsibilities

- `ContextBootstrap`

  - Orchestrator
  - Init + register TestContext
  - Register adapters & views (implement phase 5)
  - Cleanup sau test

👉 Lifecycle tách biệt hoàn toàn khỏi test logic

---

#### 5.5 Registry Layer

##### Files & Order

```
ContextBootstrap
    ↓
ContextRegistry
    ↓
ContextViewFactory
```

##### Responsibilities

- `ContextRegistry`

  - Framework-level registry
  - Map `ContextClass → ContextNamespace`
  - Resolve `ContextKey<T>` **từ class**
  - Central wiring:
    - context
    - adapter
    - view

- `ContextViewFactory`

  - Map `ContextClass → ContextView`
  - Resolve `ContextView` **theo Context type**
  - Build correct View từ Context + Adapter
  - Không chứa tool-specific logic

---

### ** Phase 3 – Lifecycle + Registry**

### ** Phase 4 – Adapter & View Contracts (Platform-agnostic)**

#### Implement

- `ContextAdapter`
- `ResponseAdapter`
- `ContextView`

#### 5.6 Adapter Layer

##### Files & Order

```
Raw Tool Object
    ↓
ResponseAdapter
    ↓
ContextAdapter
```

##### Responsibilities

- `ContextAdapter`

  - Marker interface for all context adapters.
  - Bridge external tools to framework

- `ResponseAdapter`

  - Adapter chỉ expose data
  - Adapter interface for response-like objects
  - Mở rộng adapter và normalize cho **response-like objects**: status, headers, and body
  - Represents the adapter boundary between external tools (RestAssured, OkHttp, Selenium, etc.) and framework-level views.

👉 Adapter là **điểm duy nhất** biết tool

---

#### 5.7 View Layer (Read-only)

##### Files & Order

```
ContextAdapter
    ↓
ContextViewFactory
    ↓
ContextView
```

##### Responsibilities

- `ContextView`

  - Immutable / read-only views
  - Expose data in a test-friendly, assertable format
  - Chưa biết tool, platform, chỉ cung cấp contract.
  - View KHÔNG biết Adapter nào
  - Chỉ là abstraction cho test đọc dữ liệu

- Platform-specific views:

  - ApiResponseView
  - PageView
  - ScreenView

👉 Validator **chỉ dùng View**

### ** Phase 4 – Adapter & View Contracts (Platform-agnostic)**

## 6. Platform-Specific Execution Flow

Tất cả platform đều tuân thủ **cùng một execution contract**:

```
Tool
  → Tool Adapter
    → Platform Adapter
      → Context
        → View
          → Validator
```

### ** Phase 5 – API Implementation**

#### Implement

- `ApiResponseAdapter`
- `DefaultApiResponseAdapter`
- `RestAssuredAdapter` / `OkHttpAdapter`
- `ApiContext`
- `DefaultApiContext`
- `ApiContextBuilder`
- `ApiResponseView`
- `RawJsonView` / `SnapshotView`
- `DefaultApiResponseView`
- `DefaultRawJsonView / DefaultSnapshotView`
- `ApiContextModule`

#### 6.1 API Flow

```
Raw HTTP Response / HTTP Client library (RestAssured, OkHttp,etc)   – Gửi request và trả về raw, tool-specific response
    ↓
Tool Adapter                                                 		    – Biết tool, trích xuất status / headers / body, converts raw response → ApiResponseAdapter
    - RestAssuredAdapter
    - OkHttpAdapter
    ↓
ApiResponseAdapter                                                  – API-neutral contract/interface, normalize response và bỏ phụ thuộc tool
    ↓
DefaultApiResponseAdapter                                           - Default concrete implementation, holds normalized data
    ↓
ApiContext                                                   		    – Boundary contract, API duy nhất test được phép dùng
    ↓
DefaultApiContext                                            		    – Runtime implementation, giữ ApiResponseAdapter
    ↓
ApiContextBuilder                                            		    – Wiring & configuration, build ApiContext hợp lệ trước khi test chạy
    ↓
ApiContextModule                                             		    – Central wiring, register context & views vào ContextRegistry + ContextViewFactory
    ↓
ApiResponseView / RawJsonView / SnapshotView                 		    – Read-only view contracts, assertion-friendly và immutable
    ↓
DefaultApiResponseView / DefaultRawJsonView / DefaultSnapshotView	  – Concrete views, nhận context và expose dữ liệu API đã chuẩn hoá
    ↓
Validator / Contract / Assertion                             		    – Test layer, chỉ assert trên view, không phụ thuộc tool/context internals
```

---

#### ** Phase 5 – API Core Implementation**

##### 6.1.1 API Response Adapters (Tool → API-neutral layer)

###### Files & Order

```
ApiResponseAdapter (interface / contract)
    ↓
DefaultApiResponseAdapter (implement)
    ↓
RestAssuredAdapter / OkHttpAdapter (tool-specific)
```

###### Responsibilities

- `ApiResponseAdapter`

  - Interface / contract, normalize API response
  - Convert tool-specific response (RestAssured, OkHttp, etc.) → API-neutral layer
  - Bridge from HTTP client → DefaultApiContext
  - No test/assert logic

- `DefaultApiResponseAdapter`

  - Concrete implementation of ApiResponseAdapter
  - Holds normalized response data (status, headers, body)
  - Immutable / read-only

- `RestAssuredAdapter / OkHttpAdapter`

  - Convert raw tool-specific response → DefaultApiResponseAdapter
  - Handle client-specific parsing
  - IOException handled here
  - Does not expose HTTP client outside

> ✅**Only this layer** knows about RestAssured / OkHttp

##### 6.1.2 API Context (Contract & Runtime Implementation)

###### Files & Order

```
ApiContext (interface / contract)
    ↓
DefaultApiContext  (implementation)
    ↓
ApiContextBuilder
```

###### Responsibilities

- `ApiContext`

  - API execution context contract
  - Exposes normalized API response via response(): ApiResponseAdapter
  - Decoupled from HTTP clients and storage
  - Immutable contract

- `DefaultApiContext`

  - Concrete implementation
  - Holds DefaultApiResponseAdapter
  - Expose API data for tests
  - No validation logic
  - Runtime state holder only

- `ApiContextBuilder`

  - Builds DefaultApiContext in a controlled way
  - Sets ResponseAdapter (DefaultApiResponseAdapter)
  - Can execute HTTP requests via tool adapter before building
  - Ensures context is fully initialized

> Context = **state holder**, not a service

##### 6.1.3 API Views (Contracts & Implementations)

###### Files & Order

```
ApiResponseView
    ↓
RawJsonView / SnapshotView
    ↓
DefaultApiResponseView
    ↓
DefaultRawJsonView / DefaultSnapshotView
    ↓
ApiResponseAdapter
```

###### Responsibilities

- `ApiResponseView` (interface / contract)

  - Read-only, assertion-friendly interface for API response
  - Expose status code, body, success flag
  - Che giấu tool & adapter
  - Decoupled from HTTP client & implementation
  - Immutable / view-only

- `RawJsonView / SnapshotView`

  - Specialized read-only views
  - Extend from ApiResponseView
  - Expose raw JSON / snapshot data

- `DefaultApiResponseView` (implementation)

  - Default concrete read-only view for DefaultApiContext
  - Immutable, assertion-friendly
  - Expose normalized API response

- `DefaultRawJsonView / DefaultSnapshotView	`

  - Extend DefaultApiResponseView
  - Specialized, immutable views for RawJson / Snapshot data

#### ** Phase 5 – API Core Implementation**

---

#### ** Phase 5 – API Wiring & Bootstrap Implementation**

##### 6.1.4 API Wiring Module (Bootstrap & Registration)

###### Files & Order

```
ContextStore (internal)
    ↓
TestContext (public)
    ↓
ApiContextModule
    ↓
ContextRegistry
    ↓
ContextBootstrap
    ↓
ContextViewFactory
```

###### Responsibilities

- `ContextRegistry`

  - Store mapping: API Context type → ContextNamespace.API
  - Registered via ApiContextModule

- `ContextViewFactory`

  - Store mapping: API Context → default view factory / specialized view resolver
  - Used to create assertion-friendly views

- `ApiContextModule`

  - Central bootstrap, register all API contexts & views
  - Register ApiContext in ContextRegistry
  - Register views in ContextViewFactory:
    - Register ApiContext → DefaultApiResponseView
    - Register ApiContext + RawJsonView → DefaultRawJsonView
    - Register ApiContext + SnapshotView → DefaultSnapshotView
  - Wire everything into ContextRegistry & ContextViewFactory
  - Maintain centralized wiring for easy maintenance

- `TestContext`

  - Container for runtime context
  - Access contexts by type + namespace
  - Test layer retrieves context here

- `ContextStore`

  - Enforce integrity of context storage
  - Backing store for TestContext

#### ** Phase 5 – API Wiring & Bootstrap Implementation**

---

##### 6.1.5 API Full Test Runtime Flow

```
[Framework Bootstrap]

(0) BOOTSTRAP / REGISTRATION PHASE
↓
ApiContextModule                                                          – register context & views
    ├─ ApiContext → DefaultApiResponseView
    ├─ ApiContext + RawJsonView → DefaultRawJsonView
    └─ ApiContext + SnapshotView → DefaultSnapshotView
↓
ContextRegistry                                                           – store Context type → Namespace
↓
ContextViewFactory                                                        – store Context → View factory / resolver
↓
(1) REQUEST & RESPONSE NORMALIZATION
[Test Case]
↓
Raw HTTP Response (RestAssured / OkHttp)                                  – execute request, return raw response
↓
Tool Adapter                                                              – RestAssuredAdapter / OkHttpAdapter
↓
ApiResponseAdapter (interface)                                            – normalize response
↓
DefaultApiResponseAdapter                                                 – concrete implementation, holds normalized data
↓
(2) CONTEXT CREATION & LIFECYCLE MANAGEMENT
↓
ApiContextBuilder                                                         – build fully initialized DefaultApiContext
↓
ApiContext                                                                – interface, boundary contract for test layer
↓
DefaultApiContext                                                         – runtime implementation, holds DefaultApiResponseAdapter
↓
ContextNamespace.API                                                      – identifier for API context
↓
ContextKey / ContextKeyFactory                                            – generate unique keys
↓
ContextStore                                                              – store context by scope (test / suite / thread)
↓
TestContext                                                               – runtime container, test retrieves context
↓
(3) VIEW RESOLUTION & ASSERTION
↓
ContextViewFactory                                                        – resolve view by Context type + View contract
↓
ApiResponseView / RawJsonView / SnapshotView                              – read-only contracts
↓
DefaultApiResponseView / DefaultRawJsonView / DefaultSnapshotView         – concrete views exposing API data
↓
Validator / Assertion                                                     – test layer asserts only via views, context/tool agnostic

```

### ** Phase 5 – API Implementation**

---

### 6.2 Web Platform Flow

```

Browser Automation Tool (Selenium / Playwright)
↓
Tool Adapter - SeleniumAdapter - PlaywrightAdapter
↓
WebDriverAdapter
↓
DefaultWebContext
↓
PageView
↓
UI Validator / Assertion / Diff

```

#### Giải thích từng bước

1. **Browser Tool**

   - Selenium / Playwright trả về:

     - DOM
     - Page state
     - Screenshot
     - Network info (nếu có)

2. **Tool Adapter**

   - Chuẩn hóa dữ liệu tool
   - Không expose WebDriver / Playwright API ra ngoài

3. **WebDriverAdapter**

   - Chuyển dữ liệu browser → Web-neutral model
   - Là adapter “cuối” trước Context

4. **DefaultWebContext**

   - Lưu state của web execution
   - Không chứa logic assertion

5. **PageView**

   - Read-only view:

     - page.title()
     - page.dom()
     - page.screenshot()

6. **Validator**

   - Chỉ làm việc với `PageView`
   - Không biết Selenium hay Playwright

### 6.3 Mobile Platform Flow

```

Mobile Automation Tool (Appium)
↓
Tool Adapter - AppiumAdapter
↓
MobileDriverAdapter
↓
DefaultMobileContext
↓
ScreenView
↓
Mobile Validator / Assertion

```

#### Giải thích từng bước

1. **Mobile Tool**

   - Appium trả về:

     - UI tree
     - Screen source
     - Screenshot
     - Device state

2. **AppiumAdapter**

   - Tách framework khỏi Appium API
   - Normalize mobile raw data

3. **MobileDriverAdapter**

   - Chuyển raw mobile data → neutral representation

4. **DefaultMobileContext**

   - Đại diện cho **1 screen execution**
   - Không phụ thuộc device / OS

5. **ScreenView**

   - Read-only abstraction:

     - screen.elements()
     - screen.texts()
     - screen.snapshot()

6. **Validator**

   - Assert UI behavior
   - Không phụ thuộc Android / iOS / Appium

---

## 7. Platform Parity Summary

| Layer            | API                  | Web                   | Mobile               |
| ---------------- | -------------------- | --------------------- | -------------------- |
| Tool             | RestAssured / OkHttp | Selenium / Playwright | Appium               |
| Tool Adapter     | RestAssuredAdapter   | SeleniumAdapter       | AppiumAdapter        |
| Platform Adapter | ApiResponseAdapter   | WebDriverAdapter      | MobileDriverAdapter  |
| Context          | DefaultApiContext    | DefaultWebContext     | DefaultMobileContext |
| View             | ApiResponseView      | PageView              | ScreenView           |
| Validator        | API Validator        | UI Validator          | Mobile Validator     |

## 8. Strict Rules (Must-Follow)

- ❌ Không dùng raw tool trong validator
- ❌ Không hard-code key string
- ❌ Không logic trong ContextStore
- ❌ Không Adapter ngoài adapter layer

## 9. Why This Matters

- ✅ Validator **100% reusable**
- ✅ Không bị lock tool
- ✅ Dễ thêm:

  - Cypress
  - Espresso
  - WebDriver BiDi

- ✅ Kiến trúc **predictable & auditable**

```

```
