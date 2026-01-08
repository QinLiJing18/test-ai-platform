
# 测试AI平台开发规范指南

为保证代码质量、可维护性、安全性与可扩展性，请在开发过程中严格遵循以下规范。

## 一、技术栈要求

- **主框架**：Spring Boot 2.7.18
- **语言版本**：Java 1.8
- **核心依赖**：
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa`
  - `lombok`
  - `mybatis-plus-boot-starter` (3.5.3)
  - `mysql-connector-j` (8.0.33)
  - `jjwt` (0.11.5)
  - `hutool-all` (5.8.22)

## 二、项目环境信息

- **操作系统版本**：Windows 11
- **工作区路径**：F:\ideaProject\test-ai-platform-fixed
- **使用框架**：JDK 1.8.0_321 Maven
- **当前时间**：2026-01-07 17:51:49
- **代码作者**：HUAWEI

## 三、项目目录结构

```
test-ai-platform-fixed
├── ai-agent-service
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── testai
│           │           └── ai
│           │               ├── controller
│           │               ├── engine
│           │               └── service
│           │                   └── impl
│           └── resources
├── auth-service
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── testai
│           │           └── auth
│           └── resources
├── common
│   └── src
│       └── main
│           └── java
│               └── com
│                   └── testai
│                       └── common
│                           ├── dto
│                           ├── entity
│                           └── utils
├── gateway-service
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── testai
│           │           └── gateway
│           └── resources
├── notification-service
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── testai
│           │           └── notification
│           └── resources
├── project-service
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── testai
│           │           └── project
│           └── resources
├── report-service
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── testai
│           │           └── report
│           └── resources
├── sql
├── system-service
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── testai
│           │           └── system
│           └── resources
├── testcase-service
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── testai
│           │           └── testcase
│           └── resources
├── testexecution-service
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── testai
│           │           └── testexecution
│           └── resources
└── user-service
    └── src
        └── main
            ├── java
            │   └── com
            │       └── testai
            │           └── user
            │               ├── common
            │               ├── controller
            │               ├── dto
            │               ├── entity
            │               ├── mapper
            │               ├── security
            │               ├── service
            │               │   └── impl
            │               └── utils
            └── resources
```

## 四、服务端口配置

| 服务名称 | 端口号 | 服务名称 | 端口号 |
|---------|--------|---------|--------|
| gateway-service | 8080 | testcase-service | 8083 |
| user-service | 8081 | testexecution-service | 8084 |
| project-service | 8082 | ai-agent-service | 8085 |
| report-service | 8086 | system-service | 8087 |
| notification-service | 8088 | auth-service | 8089 |

## 五、分层架构规范

| 层级        | 职责说明                         | 开发约束与注意事项                                               |
|-------------|----------------------------------|----------------------------------------------------------------|
| **Controller** | 处理 HTTP 请求与响应，定义 API 接口 | 不得直接访问数据库，必须通过 Service 层调用                  |
| **Service**    | 实现业务逻辑、事务管理与数据校验   | 必须通过 Repository 层访问数据库；返回 DTO 而非 Entity（除非必要） |
| **Repository** | 数据库访问与持久化操作             | 继承 `JpaRepository`；使用 `@EntityGraph` 避免 N+1 查询问题     |
| **Entity**     | 映射数据库表结构                   | 不得直接返回给前端（需转换为 DTO）；包名统一为 `entity`         |

### 接口与实现分离

- 所有接口实现类需放在接口所在包下的 `impl` 子包中。

## 六、安全与性能规范

### 输入校验

- 使用 `@Valid` 与 JSR-303 校验注解（如 `@NotBlank`, `@Size` 等）
  - 注意：Spring Boot 2.x 中校验注解位于 `jakarta.validation.constraints.*`

- 禁止手动拼接 SQL 字符串，防止 SQL 注入攻击。

### 事务管理

- `@Transactional` 注解仅用于 **Service 层**方法。
- 避免在循环中频繁提交事务，影响性能。

### JWT安全规范

- 使用 JJWT (0.11.5) 处理 JWT 令牌
- 密钥配置在 `application.yml` 的 `jwt.secret` 属性中
- 令牌过期时间配置在 `jwt.expiration` 属性中（毫秒）

## 七、代码风格规范

### 命名规范

| 类型       | 命名方式             | 示例                  |
|------------|----------------------|-----------------------|
| 类名       | UpperCamelCase       | `UserServiceImpl`     |
| 方法/变量  | lowerCamelCase       | `saveUser()`          |
| 常量       | UPPER_SNAKE_CASE     | `MAX_LOGIN_ATTEMPTS`  |

### 注释规范

- 所有类、方法、字段需添加 **Javadoc** 注释。

### 类型命名规范（阿里巴巴风格）

| 后缀 | 用途说明                     | 示例         |
|------|------------------------------|--------------|
| DTO  | 数据传输对象                 | `UserDTO`    |
| DO   | 数据库实体对象               | `UserDO`     |
| BO   | 业务逻辑封装对象             | `UserBO`     |
| VO   | 视图展示对象                 | `UserVO`     |
| Query| 查询参数封装对象             | `UserQuery`  |

### 实体类简化工具

- 使用 Lombok 注解替代手动编写 getter/setter/构造方法：
  - `@Data`
  - `@NoArgsConstructor`
  - `@AllArgsConstructor`

## 八、扩展性与日志规范

### 接口优先原则

- 所有业务逻辑通过接口定义（如 `UserService`），具体实现放在 `impl` 包中（如 `UserServiceImpl`）。

### 日志记录

- 使用 `@Slf4j` 注解代替 `System.out.println`

## 九、数据库配置规范

### MySQL配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test_ai_user?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: qlj11280108
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### Redis配置

```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

## 十、编码原则总结

| 原则       | 说明                                       |
|------------|--------------------------------------------|
| **SOLID**  | 高内聚、低耦合，增强可维护性与可扩展性     |
| **DRY**    | 避免重复代码，提高复用性                   |
| **KISS**   | 保持代码简洁易懂                           |
| **YAGNI**  | 不实现当前不需要的功能                     |
| **OWASP**  | 防范常见安全漏洞，如 SQL 注入、XSS 等      |

## 十一、常用工具依赖

### Hutool工具包

- 版本：5.8.22
- 用途：解决StrUtil报错，提供丰富的工具方法

### Spring Security加密模块

- 版本：5.7.8
- 用途：解决BCryptPasswordEncoder报错，提供密码加密功能

### JWT处理

- 版本：0.11.5
- 包含：
  - jjwt-api：JWT标准接口
  - jjwt-impl：JWT实现逻辑（运行时）
  - jjwt-jackson：JSON处理（运行时）

## 十二、服务间通信规范

### 公共模块（common）

- 所有服务都依赖 `common` 模块
- 公共DTO、实体和工具类放在 `com.testai.common` 包下
- 公共配置放在 `common` 模块的 `resources` 目录下

### 服务发现

- 网关服务（gateway-service）启用服务发现：
```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
```

## 十三、MyBatis-Plus配置

```yaml
mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
```

- 使用MyBatis-Plus 3.5.3版本
- Mapper文件统一放在 `mapper` 目录下
- 支持多模块的Mapper文件扫描

## 十四、开发注意事项

1. **MySQL 8.0配置**：必须添加 `serverTimezone=Asia/Shanghai` 和 `useSSL=false` 参数
2. **JJWT依赖**：分为api、impl、jackson三个部分，impl和jackson为运行时依赖
3. **模块化开发**：每个服务都是独立的Spring Boot应用，通过网关统一访问
4. **端口分配**：各服务使用8080-8089端口，避免冲突
5. **中文注释**：所有注释使用中文，提高代码可读性
