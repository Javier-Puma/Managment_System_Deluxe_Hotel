-- =====================================================
-- SISTEMA DE GESTIÓN HOTELERA - TABLAS PRINCIPALES
-- =====================================================

-- 1. TABLA DE EMPLEADOS
CREATE TABLE empleados (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nombre_completo VARCHAR(100) NOT NULL,
                           dni VARCHAR(20) UNIQUE NOT NULL,
                           telefono VARCHAR(20),
                           turno_asignado VARCHAR(20) NOT NULL,
                           fecha_ingreso TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           activo BOOLEAN DEFAULT TRUE
);

-- 2. TABLA DE USUARIOS (Autenticación)
CREATE TABLE usuarios (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          username VARCHAR(50) UNIQUE NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          email VARCHAR(100) NOT NULL,
                          rol VARCHAR(30) NOT NULL,
                          empleado_id BIGINT UNIQUE,
                          activo BOOLEAN DEFAULT TRUE,
                          FOREIGN KEY (empleado_id) REFERENCES empleados(id) ON DELETE SET NULL
);

-- 3. TABLA DE CLIENTES
CREATE TABLE clientes (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          dni VARCHAR(20) UNIQUE NOT NULL,
                          nombre_completo VARCHAR(100) NOT NULL,
                          telefono VARCHAR(20),
                          email VARCHAR(100),
                          es_problematico BOOLEAN DEFAULT FALSE,
                          motivo_problema TEXT,
                          fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. TABLA DE HABITACIONES
CREATE TABLE habitaciones (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              numero VARCHAR(10) UNIQUE NOT NULL,
                              tipo VARCHAR(20) NOT NULL,
                              estado VARCHAR(20) NOT NULL DEFAULT 'DISPONIBLE',
                              descripcion TEXT,
                              precio_por_noche DECIMAL(10,2) NOT NULL,
                              precio_por_horas DECIMAL(10,2),
                              caracteristicas TEXT,
                              fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. TABLA DE TURNOS
CREATE TABLE turnos (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        fecha_hora_inicio TIMESTAMP NOT NULL,
                        fecha_hora_fin TIMESTAMP,
                        tipo_turno VARCHAR(20) NOT NULL,
                        empleado_id BIGINT NOT NULL,
                        estado VARCHAR(20) DEFAULT 'ABIERTO',
                        FOREIGN KEY (empleado_id) REFERENCES empleados(id),
                        CONSTRAINT chk_estado_turno CHECK (estado IN ('ABIERTO', 'CERRADO'))
);

-- 6. TABLA DE ESTADÍAS (Check-in/out)
CREATE TABLE estadias (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          fecha_hora_inicio TIMESTAMP NOT NULL,
                          fecha_hora_fin TIMESTAMP,
                          modo_uso VARCHAR(10) NOT NULL,
                          tarifa_aplicada DECIMAL(10,2) NOT NULL,
                          total DECIMAL(10,2),
                          cliente_id BIGINT NOT NULL,
                          habitacion_id BIGINT NOT NULL,
                          turno_id BIGINT NOT NULL,
                          estado VARCHAR(20) DEFAULT 'ACTIVA',
                          FOREIGN KEY (cliente_id) REFERENCES clientes(id),
                          FOREIGN KEY (habitacion_id) REFERENCES habitaciones(id),
                          FOREIGN KEY (turno_id) REFERENCES turnos(id),
                          CONSTRAINT chk_estado_estadia CHECK (estado IN ('ACTIVA', 'FINALIZADA', 'CANCELADA'))
);

-- 7. TABLA DE EXTRAS (Catálogo)
CREATE TABLE extras (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(100) UNIQUE NOT NULL,
                        descripcion TEXT,
                        precio_unitario DECIMAL(10,2) NOT NULL,
                        stock_actual INTEGER NOT NULL DEFAULT 0,
                        stock_minimo INTEGER DEFAULT 5,
                        activo BOOLEAN DEFAULT TRUE
);

-- 8. TABLA DE VENTAS
CREATE TABLE ventas (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        fecha_hora TIMESTAMP NOT NULL,
                        total DECIMAL(10,2) NOT NULL,
                        cliente_id BIGINT NOT NULL,
                        estadia_id BIGINT,
                        turno_id BIGINT NOT NULL,
                        empleado_id BIGINT NOT NULL,
                        FOREIGN KEY (cliente_id) REFERENCES clientes(id),
                        FOREIGN KEY (estadia_id) REFERENCES estadias(id),
                        FOREIGN KEY (turno_id) REFERENCES turnos(id),
                        FOREIGN KEY (empleado_id) REFERENCES empleados(id)
);

-- 9. TABLA DE DETALLE DE VENTAS
CREATE TABLE detalle_ventas (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                venta_id BIGINT NOT NULL,
                                extra_id BIGINT NOT NULL,
                                cantidad INTEGER NOT NULL,
                                precio_unitario DECIMAL(10,2) NOT NULL,
                                subtotal DECIMAL(10,2) NOT NULL,
                                FOREIGN KEY (venta_id) REFERENCES ventas(id),
                                FOREIGN KEY (extra_id) REFERENCES extras(id)
);

-- 10. TABLA DE GASTOS
CREATE TABLE gastos (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        fecha_hora TIMESTAMP NOT NULL,
                        concepto VARCHAR(255) NOT NULL,
                        monto DECIMAL(10,2) NOT NULL,
                        tipo VARCHAR(20) NOT NULL,
                        turno_id BIGINT NOT NULL,
                        empleado_id BIGINT NOT NULL,
                        FOREIGN KEY (turno_id) REFERENCES turnos(id),
                        FOREIGN KEY (empleado_id) REFERENCES empleados(id),
                        CONSTRAINT chk_tipo_gasto CHECK (tipo IN ('INGRESO', 'GASTO'))
);

-- Índices para mejorar el rendimiento
CREATE INDEX idx_estadias_cliente_id ON estadias(cliente_id);
CREATE INDEX idx_estadias_habitacion_id ON estadias(habitacion_id);
CREATE INDEX idx_estadias_turno_id ON estadias(turno_id);
CREATE INDEX idx_ventas_fecha_hora ON ventas(fecha_hora);
CREATE INDEX idx_gastos_fecha_hora ON gastos(fecha_hora);
CREATE INDEX idx_turnos_fecha_inicio ON turnos(fecha_hora_inicio);