# ===== 阶段 1：编译 =====
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B
# ===== 阶段 2：运行 =====
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

#  ┌────────────────────────────────────────┬───────────────────────────┬──────────────────────────────────────────────────┐
#  │                   行                   │           干嘛             │                   真实项目要点                   │
#  ├────────────────────────────────────────┼───────────────────────────┼──────────────────────────────────────────────────┤
#  │ FROM maven:...AS builder               │ 起"编译房间"，自带           │ 多阶段构建核心——编译和运行分离                   │
#  │                                        │ JDK21+Maven               │                                                  │
#  ├────────────────────────────────────────┼───────────────────────────┼──────────────────────────────────────────────────┤
#  │ COPY pom.xml + RUN mvn                 │ 先只拷 pom，提前下载依赖     │ 缓存优化：改代码不改 pom                         │
#  │ dependency:go-offline                  │                           │ 时，依赖层命中缓存跳过，省 90% 时间              │
#  ├────────────────────────────────────────┼───────────────────────────┼──────────────────────────────────────────────────┤
#  │ COPY src + RUN mvn package             │ 拷源码，编译出 jar           │ -DskipTests 跳过测试（测试在 CI 里跑）           │
#  ├────────────────────────────────────────┼───────────────────────────┼──────────────────────────────────────────────────┤
#  │ FROM eclipse-temurin:21-jre-alpine     │ 起"运行房间"，只有 JRE       │ alpine 约 85MB，比完整 JDK 400MB 小很多          │
#  ├────────────────────────────────────────┼───────────────────────────┼──────────────────────────────────────────────────┤
#  │ COPY --from=builder                    │ 从编译房间拿 jar            │ 阶段 1 的 Maven/源码全部丢弃，镜像干净           │
#  ├────────────────────────────────────────┼───────────────────────────┼──────────────────────────────────────────────────┤
#  │ EXPOSE 8080                            │ 声明端口                    │ 只是文档，不自动映射                             │
#  ├────────────────────────────────────────┼───────────────────────────┼──────────────────────────────────────────────────┤
#  │ ENTRYPOINT ["java", "-jar", "app.jar"] │ 容器启动时跑的命令           │ = 终端里敲 java -jar app.jar                     │
#  └────────────────────────────────────────┴───────────────────────────┴──────────────────────────────────────────────────┘



#  Step 1/10 : FROM maven:3.9-eclipse-temurin-21 AS builder
#   → 下载 Maven 基础镜像（第一次慢，以后有缓存）
#
#  Step 2/10 : WORKDIR /app
#   → 在镜像里创建 /app 目录
#
#  Step 3/10 : COPY pom.xml .
#   → 把宿主机的 pom.xml 复制进镜像
#
#  Step 4/10 : RUN mvn dependency:go-offline -B
#   → 在镜像里执行 Maven 下载依赖（第一次很慢，5-10 分钟）
#
#  Step 5/10 : COPY src ./src
#   → 把源码复制进镜像
#
#  Step 6/10 : RUN mvn package -DskipTests -B
#   → 编译打包，得到 jar
#
#  Step 7/10 : FROM eclipse-temurin:21-jre-alpine
#   → 开启全新的一层，基于精简的 JRE 镜像
#
#  Step 8/10 : COPY --from=builder /app/target/*.jar app.jar
#   → 从前面的 builder 阶段拷 jar 过来
#
#  Step 9/10 : EXPOSE 8080
#   → 声明端口（只是记录，不产生实际操作）
#
#  Step 10/10 : ENTRYPOINT ["java", "-jar", "app.jar"]
#   → 记录启动命令
#
#   → 构建完成！输出镜像 ID


#Docker 去当前目录找 Dockerfile
#      ↓
#  找到了，开始逐行执行
#      ↓
#  第 1 行 FROM maven:...      → 去下载基础镜像
#  第 2 行 WORKDIR /app        → 在镜像里建目录
#  第 3 行 COPY pom.xml .      → 把你电脑的 pom.xml 拷进镜像
#  第 4 行 RUN mvn dependency  → 在镜像里下载 Maven 依赖
#  第 5 行 COPY src ./src      → 把你电脑的源码拷进镜像
#  第 6 行 RUN mvn package     → 在镜像里编译打包
#  第 7 行 FROM eclipse-...    → 开一个全新的精简镜像
#  第 8 行 COPY --from=builder → 从前面把 jar 搬过来
#  第 9 行 EXPOSE 8080         → 记录端口
#  第 10 行 ENTRYPOINT ...     → 记录启动命令
#      ↓
#  全部执行完 → 生成镜像，贴上标签 demo-backend-main:1.0
#      ↓
#  你用 docker images 就能看到它了