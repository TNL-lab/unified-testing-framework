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

---

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

### ** Phase 5 – API Context Implementation**

#### Implement

- `ApiResponseView`
- `RawJsonView` / `SnapshotView`
- `DefaultApiResponseView`
- `DefaultRawJsonView / DefaultSnapshotView`
- `ApiResponseAdapter`
- `ApiContext`
- `RestAssuredAdapter` / `OkHttpAdapter`
- `DefaultApiContext`
- `ApiContextBuilder`
- `ApiContextModule`

#### 6.1 API Flow

```
Response / HTTP Client / Library (RestAssured, OkHttp,etc)   		– Gửi request và trả về raw, tool-specific response
    ↓
Tool Adapter                                                 		– Biết tool, trích xuất status / headers / body
    - RestAssuredAdapter
    - OkHttpAdapter
    ↓
ApiResponseAdapter                                           		– Chuẩn hoá response, xoá phụ thuộc tool, bridge sang API-neutral layer
    ↓
ApiContext                                                   		– Contract/boundary, API duy nhất test được phép dùng
    ↓
DefaultApiContext                                            		– Runtime implementation, giữ response + adapter data
    ↓
ApiContextBuilder                                            		– Wiring & configuration, build đúng ApiContext trước khi test chạy
    ↓
ApiContextModule                                             		– Central wiring, register context & default views vào ContextRegistry / ContextViewFactory
    ↓
ApiResponseView / RawJsonView / SnapshotView                 		– Read-only, immutable, assert-friendly interface
    ↓
DefaultApiResponseView / DefaultRawJsonView / DefaultSnapshotView	– Implementation của interface, nhận context instance, expose dữ liệu API chuẩn
    ↓
Validator / Contract / Assertion                             		– Test layer, chỉ assert trên view, không phụ thuộc tool/context internals
```

### ** Phase 5 – API Context Core Implementation**

---

##### 6.1.1 API Contracts & View Layer

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

- `ApiResponseView`

  - Assertion-friendly interface
  - Immutable / read-only view cho API response(status code, body,etc)
  - Che giấu tool & adapter
  - Tách khỏi HTTP client
  - Không phụ thuộc DefaultApiContext hoặc DefaultView, chỉ định contract thuần (interface)

- `RawJsonView / SnapshotView`

  - Các view chuyên biệt, raw JSON access / snapshot
  - Extend từ ApiResponseView, vẫn immutable / read-only

- `DefaultApiResponseView`

  - Default read-only implementation of ApiResponseView
  - Provide immutable, assertion-friendly access to HTTP response data
  - Expose status code, body, and success flag
  - Serve as the default concrete view for DefaultApiContext

- `DefaultRawJsonView / DefaultSnapshotView	`

  - Mở rộng từ DefaultApiResponseView, default read-only implementation of RawJsonView / SnapshotView
  - Immutable / read-only / assertion-friendly / specialized methods

---

##### 6.1.2 API Tool-Specific Adapters

###### Files & Order

```
ApiResponseAdapter
    ↓
RestAssuredAdapter / OkHttpAdapter
    ↓
ApiContext
```

###### Responsibilities

- `ApiResponseAdapter`

  - Used to handle API responses
  - Tách biệt API context from HTTP client libraries
  - Chuẩn hóa API response (headers, status code, body, etc) từ tool-specific → ApiContext neutral
  - Không chứa test/assert logic
  - Bridge giữa tool-specific response (RestAssured, OkHttp, etc.) → DefaultApiContext

- `RestAssuredAdapter / OkHttpAdapter`

  - Tool-specific adapter
  - Platform-specific logic (RestAssured, OkHttp)
  - Converts tool-specific response into ApiResponseAdapter
  - Không chứa test/assert logic
  - Không expose RestAssuredAdapter / OkHttpAdapter ra ngoài
  - IOException handled at edge

> ✅ Đây là **điểm duy nhất** biết RestAssured hay OkHttp

##### 6.1.3 API Context Implementation

###### Files & Order

```
ApiContext
    ↓
DefaultApiContext
    ↓
ApiContextBuilder
```

###### Responsibilities

- `ApiContext`

  - Interface / Contract to API context (no implementation logic)
  - Represents an API execution context in a test
  - Exposes API-related data in a normalized way
  - Does NOT know any HTTP client (RestAssured / OkHttp)
  - Does NOT know lifecycle or storage

- `DefaultApiContext`

  - Implement lưu response từ adapter
  - Expose API context cho test
  - Giữ raw data (nếu cần debug)
  - Không chứa logic validate
  - Binds ResponseAdapter to ApiContext
  - Execute HTTP call nằm trong Builder / Adapter, DefaultApiContext chỉ hold response và expose data.

> Context = **state holder**, không phải service

- `ApiContextBuilder`

  - Pattern builder
  - Builder builds DefaultApiContext + sets adapter, but does not execute assertions
  - Construct ApiContext in a controlled way (DefaultApiContext instance) linh hoạt (chọn adapter, view)
  - Create adapter from tools
  - Validate required components
  - Execute HTTP requests
  - Access ContextRegistry or lifecycle

---

### ** Phase 5 – API Context Core Implementation**

### ** Phase 5 – API Context Wiring Implementation**

##### 6.1.4 API Wiring Implement

###### Files & Order

```
ContextStore (internal)
    ↓
TestContext (public)
    ↓
ContextBootstrap
    ↓
ApiContextModule
    ↓
ContextRegistry
    ↓
    ↓ ContextAdapter
    ↓
ContextViewFactory
```

###### Responsibilities

- `ContextRegistry`

  - API context types should register via ApiContextModule

- `ContextViewFactory`

  - Register context type -> view factory function (IMPORTANT FIX)

- `ApiContextModule`

  - Wiring API contexts and views into the core framework (Registry + ViewFactory)
  - Register ApiContext into ContextRegistry
  - register ApiResponseView into ContextViewFactory
  - Central place to wire all API components
  - Easy to maintain

- `TestContext`

  - Extend TestContext with overloaded access methods and fast-fail validation

- `ContextStore`

  - Enforce integrity protection without semantic constraints

---

### ** Phase 5 – API Context Wiring Implementation**

##### 6.1.5 Full API Test Runtime Flow

```
[Test Case]
│
│ (1) REQUEST & RESPONSE NORMALIZATION
│
↓
Response / HTTP Client / Library (RestAssured, OkHttp, etc)     – send HTTP request, get raw, tool-specific response
↓
Tool Adapter                                                    – extract status / headers / body, normalize tool-specific response
    - RestAssuredAdapter
    - OkHttpAdapter
↓
ApiResponseAdapter                                               – normalize response, remove tool dependency, bridge to API-neutral layer
│
│ (2) CONTEXT CREATION & LIFECYCLE MANAGEMENT
│
↓
ApiContextBuilder                                                – assemble ApiContext (choose adapter, set response)
↓
DefaultApiContext                                                – concrete runtime implementation, hold response and adapter state
↓
ApiContext (contract)                                            – interface, exposes normalized API data, no tool knowledge
↓
ContextNamespace                                                 – categorize context type (API / Web / Mobile)
↓
ContextKey / ContextKeyFactory                                   – manage unique keys for context storage
↓
ContextStore                                                     – hold runtime context data
↓
TestContext                                                      – lifecycle-aware container for current test
│
│ (3) VIEW RESOLUTION & ASSERTION
│
↓
ApiContextModule (register contexts & views)                      – setup wiring, optional to show
↓
ContextRegistry                                                   – resolve context types
↓
ContextViewFactory                                                – create views from context (default + specialized)
↓
ApiResponseView / RawJsonView / SnapshotView                      – read-only, immutable, assertion-friendly interface (contract)
↓
DefaultApiResponseView / DefaultRawJsonView / DefaultSnapshotView – default concrete implementations for each view
↓
Validator / Contract / Assertion                                  – test layer, asserts only on views, context/tool agnostic
```

### ** Phase 5 – API Context Implementation**

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
