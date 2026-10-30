# ===== 第一阶段：构建（用带 Maven 的镜像，把源码编译成 jar）=====
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

# 把整个项目拷进镜像（target/、.git/ 会被 .dockerignore 排除掉）
COPY . .
# 编译打包，-DskipTests 跳过测试（测试要连真 MySQL，容器构建时先不跑）
RUN mvn clean package -DskipTests -B -s settings.xml

# ===== 第二阶段：运行（只留一个轻量 JRE，最终镜像小很多）=====
FROM eclipse-temurin:17-jre
WORKDIR /app

# 从上一个阶段把打好的 jar 拷过来
COPY --from=build /app/target/*.jar app.jar

# 暴露 8081 端口（和 application.properties 里 server.port 一致）
EXPOSE 8081

# 容器启动时执行
ENTRYPOINT ["java", "-jar", "app.jar"]
