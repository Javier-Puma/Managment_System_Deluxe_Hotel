-- =====================================================
-- DATOS INICIALES PARA EL SISTEMA HOTELERO
-- =====================================================

-- Insertar empleados
INSERT INTO empleados (nombre_completo, dni, telefono, turno_asignado) VALUES
    ('Admin Principal', '00000000', '999999999', 'MATUTINO');

-- Insertar usuario administrador (password: admin123)
INSERT INTO usuarios (username, password, email, rol, empleado_id) VALUES
    ('admin', '$2a$10$fQtlzoGpJz0CY40qW2UNF.MoWPDCfdl9Jai.ahD2htj0NJ57nTG0e', 'admin@hotel.com', 'ADMIN', 1);

-- Insertar habitaciones de prueba
INSERT INTO habitaciones (numero, tipo, descripcion, precio_por_noche, precio_por_horas) VALUES
                                                                                             ('204', 'PERSONAL', 'Habitación individual estándar', 80.00, 15.00),
                                                                                             ('205', 'PERSONAL', 'Habitación individual con vista', 90.00, 17.00),
                                                                                             ('305', 'MATRIMONIAL', 'Habitación matrimonial estándar', 120.00, 20.00),
                                                                                             ('301', 'MATRIMONIAL', 'Habitación matrimonial con balcón', 150.00, 25.00),
                                                                                             ('304', 'DOBLE', 'Habitación doble estándar', 100.00, 18.00),
                                                                                             ('308', 'TEMATICA', 'Suite temática romántica', 200.00, 35.00);

-- Insertar extras de prueba
INSERT INTO extras (nombre, descripcion, precio_unitario, stock_actual) VALUES
                                                                            ('Agua Mineral', 'Botella de agua 500ml', 2.50, 100),
                                                                            ('Gaseosa', 'Lata de gaseosa 355ml', 3.00, 80),
                                                                            ('Snacks', 'Paquete de papas fritas', 4.00, 60),
                                                                            ('Café', 'Taza de café americano', 5.00, 40);