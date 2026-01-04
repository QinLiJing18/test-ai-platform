# Test AI Platform

基于Spring Cloud微服务架构的智能测试管理平台

## 项目结构

```
test-ai-platform/
├── pom.xml                    # Maven父项目配置
├── README.md                  # 项目说明
├── docker-compose.yml        # Docker编排文件
├── common/                   # 公共模块
├── gateway-service/          # API网关 (8080)
├── ai-agent-service/         # AI智能体 (8085)
├── user-service/             # 用户服务 (8081)
├── project-service/          # 项目服务 (8082)
├── testcase-service/         # 用例服务 (8083)
├── testexecution-service/    # 执行服务 (8084)
├── report-service/          # 报告服务 (8086)
├── system-service/           # 系统服务 (8087)
├── notification-service/     # 通知服务 (8088)
├── auth-service/             # 认证服务 (8089)
└── sql/                      # 数据库脚本
```

## 快速开始

### 使用Docker Compose启动基础设施

```bash
docker-compose up -d
```

### 构建项目

```bash
mvn clean install
```

### 启动各个服务

```bash
# 启动网关
cd gateway-service && mvn spring-boot:run

# 启动AI智能体服务
cd ai-agent-service && mvn spring-boot:run

# 启动其他服务...
```

## 技术栈

- Spring Boot 2.7.18
- Spring Cloud 2021.0.8
- MyBatis-Plus 3.5.3.1
- MySQL 8.0
- Redis 7
- JWT
