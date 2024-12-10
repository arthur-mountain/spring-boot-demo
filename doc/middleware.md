### **1. 使用場景分析**

| 特性                   | **Filter**                        | **Interceptor**                | **AOP + Annotation**       |
| ---------------------- | --------------------------------- | ------------------------------ | -------------------------- |
| **層級**               | 最底層（Servlet 層）              | Spring MVC 層                  | 方法層（業務邏輯更細粒度） |
| **適用範圍**           | 適用於所有請求，包括靜態資源      | 適用於 Spring MVC 的控制器     | 適用於特定方法             |
| **與 Spring 集成程度** | 與 Spring 集成較弱                | 與 Spring MVC 完美整合         | 與業務邏輯緊密結合         |
| **性能**               | 性能高，適合高並發需求            | 性能較高，但需 Spring MVC 支持 | 性能取決於 AOP 實現        |
| **靈活性**             | 需手動配置路徑或 URL 篩選         | 支持路徑模式，靈活性高         | 使用注解實現，粒度更細     |
| **擴展性**             | 適合簡單的驗證邏輯，如 Token 驗證 | 適合更高層次的業務邏輯驗證     | 適合特定方法或高複雜度邏輯 |

---

### **推薦使用場景**

#### **1. 使用 Filter（首選於底層驗證需求）**

**適用場景**：

- 您的身份驗證邏輯是低層級的，比如：

  - 驗證 API Token（如 JWT）。

  - 驗證 OAuth2 授權。

- 您需要攔截靜態資源或非 MVC 的請求（如 `/actuator`、`/public` 等）。

**推薦理由**：

- Filter 的執行在 **所有請求（包括靜態資源）** 的最早階段，適合處理全局性驗證。

- 性能高效，適合高並發場景。

**範例**：
JWT 驗證：

```java
public class JwtAuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);
        if (!validateToken(token)) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response); // 繼續處理請求
    }

    private boolean validateToken(String token) {
        return "valid-token".equals(token); // 模擬驗證邏輯
    }
}
```

---

#### **2. 使用 Interceptor（首選於 MVC 驗證需求）**

**適用場景**：

- 您的應用是典型的 Spring MVC 應用。

- 您只需要對特定的 API 請求進行身份驗證。

- 您希望驗證邏輯與 MVC 路徑配置緊密結合。

**推薦理由**：

- 支持 Spring MVC 的路徑匹配模式，靈活設置需要驗證的 URL。

- 更適合處理業務層的身份驗證需求，例如基於角色的訪問控制。

**範例**：
攔截 `/api/protected/**` 路徑：

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new AuthInterceptor())
            .addPathPatterns("/api/protected/**")
            .excludePathPatterns("/api/public/**");
}
```

---

#### **3. 使用 AOP + Annotation（首選於細粒度方法驗證需求）**

**適用場景**：

- 您希望按需為特定方法添加身份驗證邏輯（而不是整個控制器）。

- 您的身份驗證邏輯與業務邏輯緊密相關。

- 您需要高靈活性，比如使用注解標記需要驗證的方法。

**推薦理由**：

- 注解方式更具可讀性，適合大型應用中具有特定安全需求的方法。

- 更適合處理跨切面需求，比如基於角色的訪問控制。

**範例**：
在特定方法上應用驗證：

```java
@RequireAuth
@GetMapping("/api/protected/data")
public String getProtectedData() {
    return "Protected data";
}
```

AOP 切面驗證：

```java
@Before("@annotation(RequireAuth)")
public void checkAuth() {
    String token = getTokenFromRequest();
    if (!isValidToken(token)) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
}
```

---

### **結論與建議**

1. **Filter** 是底層驗證的最佳選擇，適合處理 **JWT Token** 或其他全局性的身份驗證需求。

2. **Interceptor** 更適合典型的 Spring MVC 應用，專注於對 API 路徑的身份驗證。

3. **AOP + Annotation** 是細粒度方法驗證的最佳選擇，適合具有複雜業務需求的場景。

#### **選擇建議**

- **簡單的驗證需求（如 JWT）**：推薦 **Filter**。

- **基於路徑的驗證（如角色驗證）**：推薦 **Interceptor**。

- **方法級別的驗證（如注解標記方法驗證）**：推薦 **AOP + Annotation**。

這三者可以根據需求進行靈活組合，滿足不同層次的身份驗證需求！
