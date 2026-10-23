-- =============================================================================
-- TalentHub / Sistema de Reclutamiento — esquema PostgreSQL completo
-- =============================================================================
-- Uso recomendado: NO ejecutar este archivo si vas a arrancar el backend con
-- Flyway activo (spring.flyway.enabled=true). Al iniciar la app, Flyway aplica
-- automáticamente src/main/resources/db/migration/V1…V7 en orden.
--
-- Este script sirve como referencia o para cargar manualmente en el SQL Editor
-- de Supabase en una base VACÍA. Si lo ejecutas a mano, desactiva Flyway o
-- haz baseline de flyway_schema_history para evitar conflictos.
-- =============================================================================

-- V1__init.sql
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE permisos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE usuario_roles (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    CONSTRAINT fk_rol FOREIGN KEY (rol_id) REFERENCES roles (id),
    CONSTRAINT unique_usuario_rol UNIQUE (usuario_id, rol_id)
);

CREATE TABLE rol_permisos (
    id BIGSERIAL PRIMARY KEY,
    rol_id BIGINT NOT NULL,
    permiso_id BIGINT NOT NULL,
    CONSTRAINT fk_rol_perm FOREIGN KEY (rol_id) REFERENCES roles (id),
    CONSTRAINT fk_perm FOREIGN KEY (permiso_id) REFERENCES permisos (id),
    CONSTRAINT unique_rol_perm UNIQUE (rol_id, permiso_id)
);

CREATE TABLE empresas (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT UNIQUE,
    nombre_empresa VARCHAR(150),
    ruc VARCHAR(20),
    descripcion TEXT,
    direccion VARCHAR(200),
    pagina_web VARCHAR(150),
    razon_social VARCHAR(130),
    estado_validacion VARCHAR(30) DEFAULT 'PENDIENTE',
    CONSTRAINT fk_empresa_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);

CREATE TABLE postulantes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT UNIQUE,
    fecha_nacimiento DATE,
    genero VARCHAR(20),
    direccion VARCHAR(200),
    CONSTRAINT fk_postulante_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);

CREATE TABLE ofertas (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    titulo VARCHAR(150),
    descripcion TEXT,
    salario NUMERIC(10, 2),
    ubicacion VARCHAR(150),
    tipo_empleo VARCHAR(50),
    estado VARCHAR(30) DEFAULT 'PENDIENTE',
    fecha_publicacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_oferta_empresa FOREIGN KEY (empresa_id) REFERENCES empresas (id)
);

CREATE TABLE postulaciones (
    id BIGSERIAL PRIMARY KEY,
    postulante_id BIGINT NOT NULL,
    oferta_id BIGINT NOT NULL,
    fecha_postulacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(30) DEFAULT 'ENVIADO',
    CONSTRAINT fk_postulacion_postulante FOREIGN KEY (postulante_id) REFERENCES postulantes (id),
    CONSTRAINT fk_postulacion_oferta FOREIGN KEY (oferta_id) REFERENCES ofertas (id),
    CONSTRAINT unique_postulacion UNIQUE (postulante_id, oferta_id)
);

INSERT INTO roles (nombre) VALUES ('ADMIN'), ('EMPRESA'), ('POSTULANTE');

INSERT INTO permisos (nombre) VALUES
    ('OFERTA_CREATE'),
    ('OFERTA_VIEW'),
    ('OFERTA_UPDATE'),
    ('OFERTA_DELETE'),
    ('POSTULAR'),
    ('VALIDAR_OFERTA');

INSERT INTO rol_permisos (rol_id, permiso_id)
SELECT r.id, p.id FROM roles r, permisos p WHERE r.nombre = 'ADMIN';

INSERT INTO rol_permisos (rol_id, permiso_id)
SELECT r.id, p.id FROM roles r, permisos p
WHERE r.nombre = 'EMPRESA' AND p.nombre IN ('OFERTA_CREATE', 'OFERTA_VIEW', 'OFERTA_UPDATE');

INSERT INTO rol_permisos (rol_id, permiso_id)
SELECT r.id, p.id FROM roles r, permisos p
WHERE r.nombre = 'POSTULANTE' AND p.nombre IN ('OFERTA_VIEW', 'POSTULAR');

-- V2__agregar_cv_postulacion.sql
ALTER TABLE postulaciones ADD COLUMN cv_url VARCHAR(255);

-- V3__nuevos_permisos.sql
INSERT INTO permisos (nombre) VALUES
    ('EMPRESA_VIEW'), ('EMPRESA_UPDATE'), ('VALIDAR_EMPRESA'),
    ('POSTULANTE_VIEW'), ('POSTULANTE_UPDATE'),
    ('POSTULACION_VIEW'), ('POSTULACION_REJECT'), ('POSTULACION_ACCEPT'),
    ('CV_UPLOAD'), ('CV_VIEW'), ('CV_UPDATE'), ('CV_DELETE'), ('CV_DOWNLOAD'),
    ('USUARIO_VIEW'), ('USUARIO_CREATE'), ('USUARIO_UPDATE'), ('USUARIO_DELETE'),
    ('ROL_VIEW'), ('ROL_ASSIGN'),
    ('EMPRESA_VIEW_ALL'), ('POSTULANTE_VIEW_ALL'), ('OFERTA_VIEW_ALL'), ('POSTULACION_VIEW_ALL');

INSERT INTO rol_permisos (rol_id, permiso_id)
SELECT r.id, p.id FROM roles r, permisos p
WHERE r.nombre = 'ADMIN'
  AND NOT EXISTS (
    SELECT 1 FROM rol_permisos rp WHERE rp.rol_id = r.id AND rp.permiso_id = p.id
  );

INSERT INTO rol_permisos (rol_id, permiso_id)
SELECT r.id, p.id FROM roles r
JOIN permisos p ON p.nombre IN (
    'OFERTA_CREATE', 'OFERTA_VIEW', 'OFERTA_UPDATE', 'OFERTA_DELETE',
    'EMPRESA_VIEW', 'EMPRESA_UPDATE',
    'POSTULACION_VIEW', 'POSTULACION_ACCEPT', 'POSTULACION_REJECT',
    'POSTULANTE_VIEW', 'CV_VIEW', 'CV_DOWNLOAD'
)
WHERE r.nombre = 'EMPRESA'
  AND NOT EXISTS (
    SELECT 1 FROM rol_permisos rp WHERE rp.rol_id = r.id AND rp.permiso_id = p.id
  );

INSERT INTO rol_permisos (rol_id, permiso_id)
SELECT r.id, p.id FROM roles r
JOIN permisos p ON p.nombre IN (
    'POSTULANTE_VIEW', 'POSTULANTE_UPDATE',
    'CV_UPLOAD', 'CV_VIEW', 'CV_UPDATE', 'CV_DELETE'
)
WHERE r.nombre = 'POSTULANTE';

-- V4__agregar_modalidad_oferta.sql
ALTER TABLE ofertas ADD COLUMN modalidad VARCHAR(20) NOT NULL DEFAULT 'PRESENCIAL';

-- V5__mensajes_contacto.sql
CREATE TABLE mensajes_contacto (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    asunto VARCHAR(200) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- V6__indices_performance.sql
CREATE INDEX IF NOT EXISTS idx_ofertas_estado ON ofertas (estado);
CREATE INDEX IF NOT EXISTS idx_ofertas_empresa_id ON ofertas (empresa_id);
CREATE INDEX IF NOT EXISTS idx_empresas_estado_validacion ON empresas (estado_validacion);
CREATE INDEX IF NOT EXISTS idx_empresas_ruc ON empresas (ruc);
CREATE INDEX IF NOT EXISTS idx_usuarios_activo ON usuarios (activo);
CREATE INDEX IF NOT EXISTS idx_postulaciones_oferta_id ON postulaciones (oferta_id);

-- V7__tablas_faltantes.sql
CREATE TABLE curriculum_vitae (
    id BIGSERIAL PRIMARY KEY,
    titulo_profesional VARCHAR(255) NOT NULL,
    descripcion TEXT,
    experiencia TEXT,
    habilidades TEXT,
    educacion TEXT,
    telefono VARCHAR(255),
    linkedin VARCHAR(255),
    usuario_id BIGINT,
    CONSTRAINT fk_cv_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);
CREATE INDEX IF NOT EXISTS idx_curriculum_vitae_usuario_id ON curriculum_vitae (usuario_id);

CREATE TABLE refresh_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    revoked BOOLEAN DEFAULT FALSE,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_refresh_token_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);
CREATE INDEX IF NOT EXISTS idx_refresh_token_usuario_id ON refresh_token (usuario_id);

CREATE TABLE entrevistas (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP,
    modalidad VARCHAR(255),
    enlace VARCHAR(255),
    observacion VARCHAR(255),
    postulacion_id BIGINT,
    CONSTRAINT fk_entrevista_postulacion FOREIGN KEY (postulacion_id) REFERENCES postulaciones (id)
);
CREATE INDEX IF NOT EXISTS idx_entrevistas_postulacion_id ON entrevistas (postulacion_id);
