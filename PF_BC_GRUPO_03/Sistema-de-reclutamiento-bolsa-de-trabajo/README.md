# 🧑‍💼 TalentHub - Backend API

## 📖 Descripción

**TalentHub - Backend API** es el servicio REST que da soporte al sistema de reclutamiento y bolsa de trabajo. Expone los endpoints necesarios para la gestión de usuarios, empresas, ofertas laborales y postulaciones, con autenticación y autorización basadas en **JWT**.

Este repositorio contiene **exclusivamente la API**; el cliente web que la consume vive en un repositorio independiente.

---

## 🛠️ Tecnologías

- ⚙️ **Java 21**
- 🌱 **Spring Boot 3.3.2**
  - Spring Web (API REST)
  - Spring Data JPA / Hibernate
  - Spring Security
- 🔐 **JWT** (`jjwt` 0.12.6) para autenticación y autorización stateless
- 🐘 **PostgreSQL** como motor de base de datos
- 🧬 **Flyway** para el versionado y migración del esquema de base de datos
- 🗺️ **MapStruct 1.5.5** para el mapeo entre entidades y DTOs
- 🧩 **Lombok** para la reducción de código repetitivo (getters, setters, builders)
- 📦 **Maven** como gestor de dependencias y build

---

## ⚙️ Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone <URL-del-repositorio>
cd Sistema-de-reclutamiento-bolsa-de-trabajo
```

### 2. Requisitos previos

- **JDK 21** instalado y configurado (`JAVA_HOME`).
- Una instancia de **PostgreSQL** disponible en local (o accesible remotamente).

### 3. Configurar la base de datos local

Crea una base de datos vacía en tu instancia de PostgreSQL, por ejemplo:

```sql
CREATE DATABASE talenthub;
```

Luego, configura las credenciales de conexión en `src/main/resources/application.properties`:

```properties
spring.application.name=SistemaDeReclutamiento

spring.datasource.url=jdbc:postgresql://localhost:5432/talenthub
spring.datasource.username=<tu_usuario>
spring.datasource.password=<tu_contraseña>
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

jwt.secret=<una_clave_secreta_larga_y_aleatoria>
jwt.expiration=900000
```

> 💡 **Buena práctica:** evita subir credenciales reales a tu repositorio. Se recomienda externalizarlas como variables de entorno (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET`), ya que Spring Boot las reconoce automáticamente y sobrescriben lo definido en `application.properties`.

No necesitas crear tablas manualmente: al iniciar la aplicación, **Flyway** ejecuta automáticamente todas las migraciones (`db/migration`, V1–V7) y construye el esquema completo.

#### Base de datos en Supabase (nuevo proyecto)

1. Crea un proyecto en [Supabase](https://supabase.com) y guarda la **Database password**.
2. En **Project Settings → Database → Connection string**, copia la URI JDBC (modo **Session pooler** o **Direct**).
3. Configura la conexión (variables de entorno recomendadas):

```powershell
# Windows PowerShell — ejemplo
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://TU_HOST:5432/postgres?sslmode=require"
$env:SPRING_DATASOURCE_USERNAME="postgres.TU_PROJECT_REF"
$env:SPRING_DATASOURCE_PASSWORD="TU_PASSWORD"
```

4. Arranca la API; Flyway creará tablas, roles, permisos e índices en la base vacía `postgres`.
5. En el **primer arranque** se crea el usuario administrador: `AdminWork@gmail.com` / `123456` (cámbialo en producción).

Referencia del esquema completo (solo consulta o carga manual): `scripts/supabase_schema_completo.sql`.  
Plantilla de configuración: `src/main/resources/application.properties.example`.

### 4. Compilar y ejecutar

Con el Maven Wrapper incluido en el proyecto (no requiere tener Maven instalado globalmente):

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

O bien, si tienes Maven instalado globalmente:

```bash
mvn spring-boot:run
```

Por defecto, la API quedará disponible en:

```
http://localhost:8080/api
```

### 5. Ejecutar las pruebas (opcional)

```bash
mvn test
```

---

## 🔐 Seguridad

Todas las peticiones (salvo los endpoints públicos de registro, login y consulta de ofertas/empresas) requieren un token **JWT** válido enviado en el header:

```
Authorization: Bearer <token>
```

La autorización de cada endpoint se valida por **rol** y **permiso específico** mediante Spring Security.
