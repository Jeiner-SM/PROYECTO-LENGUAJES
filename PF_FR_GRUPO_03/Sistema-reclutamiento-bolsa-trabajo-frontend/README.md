# 🧑‍💼 TalentHub - Frontend Application

## 📖 Descripción

**TalentHub - Frontend Application** es el cliente web del sistema de reclutamiento y bolsa de trabajo, construido con **Angular**. Consume la API REST del back-end para permitir la gestión de ofertas laborales, postulaciones y perfiles de usuario, con una interfaz moderna basada en **PrimeNG** y **Tailwind CSS**.

Este repositorio contiene **exclusivamente la aplicación cliente**; la API que consume vive en un repositorio independiente.

---

## 🛠️ Tecnologías

- 🅰️ **Angular 20**
- 🎨 **PrimeNG 20.4** (tema *Aura*) — librería de componentes UI
- 🌬️ **Tailwind CSS 4**
- 🔁 **RxJS** para la programación reactiva
- 🗄️ **@ngrx/signals** para el manejo de estado reactivo
- 🪪 **jwt-decode** para la lectura del token en el cliente
- 🎯 **TypeScript 5.9**

---

## ⚙️ Instalación y Configuración

### 1. Requisitos previos

- **Node.js** (LTS recomendado) y **npm**.
- **Angular CLI** (opcional de forma global, ya incluido como dependencia del proyecto):

```bash
npm install -g @angular/cli
```

### 2. Clonar el repositorio e instalar dependencias

```bash
git clone <URL-del-repositorio>
cd Sistema-reclutamiento-bolsa-trabajo-frontend
npm install
```

### 3. Configurar la URL de la API

La URL del back-end se configura en los archivos de entorno de Angular, ubicados en `src/app/environment/`.

Para desarrollo, edita `environment.ts`:

```typescript
// src/app/environment/environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  apiBaseUrl: 'http://localhost:8080',
};
```

Para producción, edita `environment.prod.ts` con la URL real donde esté desplegado tu back-end:

```typescript
// src/app/environment/environment.prod.ts
export const environment = {
  production: true,
  apiUrl: 'https://<tu-dominio-backend>/api',
  apiBaseUrl: 'https://<tu-dominio-backend>',
};
```

> 💡 Asegúrate de que el back-end esté corriendo y accesible en la URL configurada antes de levantar el cliente, ya que la aplicación depende de esos endpoints para autenticación, ofertas, postulaciones, etc.

### 4. Levantar el proyecto en modo desarrollo

```bash
ng serve
```

O usando el script equivalente de `npm`:

```bash
npm start
```

Por defecto, la aplicación quedará disponible en:

```
http://localhost:4200
```

### 5. Compilar para producción (opcional)

```bash
ng build
```

Los artefactos compilados se generarán en la carpeta `dist/`, listos para ser desplegados en cualquier servidor de archivos estáticos.

---

## 🧪 Pruebas (opcional)

```bash
ng test
```
