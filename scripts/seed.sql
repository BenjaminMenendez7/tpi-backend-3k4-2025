-- Limpieza opcional antes de insertar (solo para desarrollo)
TRUNCATE TABLE direccion, cliente, calle, barrio, ciudad, provincia, pais RESTART IDENTITY CASCADE;

-----------------------
-- Tabla pais
-----------------------
INSERT INTO pais (nombre)
VALUES
    ('Argentina'),
    ('Chile'),
    ('Uruguay'),
    ('Perú');

------------------------
-- Tabla provincia
-------------------------
INSERT INTO provincia (nombre, id_pais)
VALUES
    ('Buenos Aires',   (SELECT id FROM pais WHERE nombre = 'Argentina')),
    ('Córdoba',        (SELECT id FROM pais WHERE nombre = 'Argentina')),
    ('Santiago',       (SELECT id FROM pais WHERE nombre = 'Chile')),
    ('Montevideo',     (SELECT id FROM pais WHERE nombre = 'Uruguay')),
    ('Lima',           (SELECT id FROM pais WHERE nombre = 'Perú'));

-------------------------
-- Tabla ciudad
-------------------------
INSERT INTO ciudad (nombre, id_provincia)
VALUES
-- Provincia: Buenos Aires
('La Plata',        (SELECT id FROM provincia WHERE nombre = 'Buenos Aires')),
('Mar del Plata',   (SELECT id FROM provincia WHERE nombre = 'Buenos Aires')),
-- Provincia: Córdoba
('Córdoba Capital', (SELECT id FROM provincia WHERE nombre = 'Córdoba')),
('Villa Carlos Paz',(SELECT id FROM provincia WHERE nombre = 'Córdoba')),
-- Provincia: Santiago (Chile)
('Santiago Centro', (SELECT id FROM provincia WHERE nombre = 'Santiago')),
('Puente Alto',     (SELECT id FROM provincia WHERE nombre = 'Santiago')),
-- Provincia: Montevideo (Uruguay)
('Montevideo',      (SELECT id FROM provincia WHERE nombre = 'Montevideo')),
('Punta Carretas',  (SELECT id FROM provincia WHERE nombre = 'Montevideo')),
-- Provincia: Lima (Perú)
('Lima Centro',     (SELECT id FROM provincia WHERE nombre = 'Lima')),
('Miraflores',      (SELECT id FROM provincia WHERE nombre = 'Lima'));

-----------------------
-- Tabla barrio
------------------------
INSERT INTO barrio (nombre, id_ciudad)
VALUES
-- Ciudad: La Plata
('Centro',              (SELECT id FROM ciudad WHERE nombre = 'La Plata')),
('Tolosa',              (SELECT id FROM ciudad WHERE nombre = 'La Plata')),
-- Ciudad: Mar del Plata
('La Perla',            (SELECT id FROM ciudad WHERE nombre = 'Mar del Plata')),
('Constitución',        (SELECT id FROM ciudad WHERE nombre = 'Mar del Plata')),
-- Ciudad: Córdoba Capital
('Nueva Córdoba',       (SELECT id FROM ciudad WHERE nombre = 'Córdoba Capital')),
('Alta Córdoba',        (SELECT id FROM ciudad WHERE nombre = 'Córdoba Capital')),
-- Ciudad: Villa Carlos Paz
('Centro Viejo',        (SELECT id FROM ciudad WHERE nombre = 'Villa Carlos Paz')),
('Villa del Lago',      (SELECT id FROM ciudad WHERE nombre = 'Villa Carlos Paz')),
-- Ciudad: Santiago Centro
('Lastarria',           (SELECT id FROM ciudad WHERE nombre = 'Santiago Centro')),
('Bellavista',          (SELECT id FROM ciudad WHERE nombre = 'Santiago Centro')),
-- Ciudad: Puente Alto
('Bajos de Mena',       (SELECT id FROM ciudad WHERE nombre = 'Puente Alto')),
('Villa Primavera',     (SELECT id FROM ciudad WHERE nombre = 'Puente Alto')),
-- Ciudad: Montevideo
('Ciudad Vieja',        (SELECT id FROM ciudad WHERE nombre = 'Montevideo')),
('Pocitos',             (SELECT id FROM ciudad WHERE nombre = 'Montevideo')),
-- Ciudad: Punta Carretas
('Punta Carretas Norte',(SELECT id FROM ciudad WHERE nombre = 'Punta Carretas')),
('Buceo',               (SELECT id FROM ciudad WHERE nombre = 'Punta Carretas')),
-- Ciudad: Lima Centro
('Barrios Altos',       (SELECT id FROM ciudad WHERE nombre = 'Lima Centro')),
('Santa Beatriz',       (SELECT id FROM ciudad WHERE nombre = 'Lima Centro')),
-- Ciudad: Miraflores
('Kennedy',             (SELECT id FROM ciudad WHERE nombre = 'Miraflores')),
('Santa Cruz',          (SELECT id FROM ciudad WHERE nombre = 'Miraflores')),
-- Barrios de los depósitos
('Plataforma Logística Córdoba', (SELECT id FROM ciudad WHERE nombre = 'Córdoba Capital')),
('Centro de Carga Montevideo', (SELECT id FROM ciudad WHERE nombre = 'Montevideo')),
('Nodo Vial Andino', (SELECT id FROM ciudad WHERE nombre = 'Lima Centro'));


------------------------
-- Tabla calle
------------------------
INSERT INTO calle (nombre, id_barrio)
VALUES
-- La Plata – Centro / Tolosa
('Calle 7',        (SELECT id FROM barrio WHERE nombre = 'Centro')),
('Calle 530',      (SELECT id FROM barrio WHERE nombre = 'Tolosa')),
-- Mar del Plata – La Perla / Constitución
('Boulevard Marítimo', (SELECT id FROM barrio WHERE nombre = 'La Perla')),
('Av. Constitución',   (SELECT id FROM barrio WHERE nombre = 'Constitución')),
-- Córdoba Capital – Nueva Córdoba / Alta Córdoba
('Av. Hipólito Yrigoyen', (SELECT id FROM barrio WHERE nombre = 'Nueva Córdoba')),
('Jerónimo Luis de Cabrera', (SELECT id FROM barrio WHERE nombre = 'Alta Córdoba')),
-- Carlos Paz – Centro Viejo / Villa del Lago
('Av. Libertad',       (SELECT id FROM barrio WHERE nombre = 'Centro Viejo')),
('Avenida Cárcano',    (SELECT id FROM barrio WHERE nombre = 'Villa del Lago')),
-- Santiago – Lastarria / Bellavista
('José Victorino Lastarria', (SELECT id FROM barrio WHERE nombre = 'Lastarria')),
('Pío Nono',                   (SELECT id FROM barrio WHERE nombre = 'Bellavista')),
-- Puente Alto – Bajos de Mena / Villa Primavera
('Av. Juanita',       (SELECT id FROM barrio WHERE nombre = 'Bajos de Mena')),
('Luis Matte Larraín',(SELECT id FROM barrio WHERE nombre = 'Villa Primavera')),
-- Montevideo – Ciudad Vieja / Pocitos
('Sarandí',           (SELECT id FROM barrio WHERE nombre = 'Ciudad Vieja')),
('Benito Blanco',     (SELECT id FROM barrio WHERE nombre = 'Pocitos')),
-- Punta Carretas – Punta Carretas Norte / Buceo
('21 de Septiembre',  (SELECT id FROM barrio WHERE nombre = 'Punta Carretas Norte')),
('Av. Rivera',        (SELECT id FROM barrio WHERE nombre = 'Buceo')),
-- Lima – Barrios Altos / Santa Beatriz
('Jirón Junín',       (SELECT id FROM barrio WHERE nombre = 'Barrios Altos')),
('Av. Arequipa',      (SELECT id FROM barrio WHERE nombre = 'Santa Beatriz')),
-- Miraflores – Kennedy / Santa Cruz
('Av. Larco',         (SELECT id FROM barrio WHERE nombre = 'Kennedy')),
('Calle Alcanfores',  (SELECT id FROM barrio WHERE nombre = 'Santa Cruz')),
-- Calles de los depósitos
('Corredor Logístico 1', (SELECT id FROM barrio WHERE nombre = 'Plataforma Logística Córdoba')),
('Andén Terrestre 4', (SELECT id FROM barrio WHERE nombre = 'Centro de Carga Montevideo')),
('Ruta Logística 9', (SELECT id FROM barrio WHERE nombre = 'Nodo Vial Andino'));


------------------------
-- Tabla cliente
-------------------------
INSERT INTO cliente (documento, nombre, apellido, email, telefono)
VALUES
    ('32.554.789',   'Ana',     'Pérez',        'ana.perez@example.com',      '+54 9 351 555-1111'),
    ('19.876.543-2', 'Luis',    'García',       'lgarcia@example.com',        '+56 9 9876-2222'),
    ('4.321.987-6',  'María',   'López',        'mlopez@example.com',         '+598 92 333-444'),
    ('74561234',     'Carlos',  'Fernández',    'cfernandez@example.com',     '+51 999 555 666'),
-- Empresa privada
    ('30-99999999-7', 'LogiTrans', 'S.A.', 'contacto@logitrans.com', '+54 11 4555-8899'),
-- Entidad gubernamental
    ('30-67891234-5', 'Ministerio', 'de Transporte', 'info@mintransporte.gov', '+54 11 4345-0000');


---------------------------
-- Direcciones de clientes
---------------------------
INSERT INTO direccion (
    nro_calle, id_calle, id_cliente, piso, dpto, observaciones, codigo_postal
)
VALUES
-- Ana Pérez (Argentina)
('123',
 (SELECT c.id FROM calle c WHERE c.nombre = 'Calle 7'),
 (SELECT cl.id FROM cliente cl WHERE cl.nombre = 'Ana' AND cl.apellido = 'Pérez'),
 '1', 'A', 'Departamento frente al parque', '1900'),
-- Luis García (Chile)
('456',
 (SELECT c.id FROM calle c WHERE c.nombre = 'José Victorino Lastarria'),
 (SELECT cl.id FROM cliente cl WHERE cl.nombre = 'Luis' AND cl.apellido = 'García'),
 NULL, NULL, 'Edificio histórico', '8320000'),
-- María López (Uruguay)
('789',
 (SELECT c.id FROM calle c WHERE c.nombre = 'Sarandí'),
 (SELECT cl.id FROM cliente cl WHERE cl.nombre = 'María' AND cl.apellido = 'López'),
 '3', 'B', NULL, '11000'),
-- Carlos Fernández (Perú)
('1025',
 (SELECT c.id FROM calle c WHERE c.nombre = 'Av. Arequipa'),
 (SELECT cl.id FROM cliente cl WHERE cl.nombre = 'Carlos' AND cl.apellido = 'Fernández'),
 NULL, NULL, 'Cerca del cruce principal', '15046'),
-- LogiTrans S.A. (Empresa privada)
('1500',
 (SELECT id FROM calle WHERE nombre = 'Av. Libertad'),
 (SELECT id FROM cliente WHERE nombre = 'LogiTrans'),
 NULL, NULL, 'Centro logístico regional', '5152'),
('2000',
 (SELECT id FROM calle WHERE nombre = 'Avenida Cárcano'),
 (SELECT id FROM cliente WHERE nombre = 'LogiTrans'),
 NULL, NULL, 'Depósito secundario', '5155'),
-- Ministerio de Transporte (Entidad estatal)
('300',
 (SELECT id FROM calle WHERE nombre = 'Av. Larco'),
 (SELECT id FROM cliente WHERE nombre = 'Ministerio' AND apellido = 'de Transporte'),
 NULL, NULL, 'Edificio gubernamental', '15074');

----------------------------
-- Direcciones de depósitos
-----------------------------

INSERT INTO direccion (
    nro_calle, id_calle, id_cliente, piso, dpto, observaciones, codigo_postal
)
VALUES
    (
        '100',
        (SELECT id FROM calle WHERE nombre = 'Corredor Logístico 1'),
        NULL,
        NULL,
        NULL,
        'Deposito Central Terrestre',
        '5020'
    ),
    (
        '700',
        (SELECT id FROM calle WHERE nombre = 'Andén Terrestre 4'),
        NULL,
        NULL,
        NULL,
        'Deposito Regional',
        '11050'
    ),
    (
        '300',
        (SELECT id FROM calle WHERE nombre = 'Ruta Logística 9'),
        NULL,
        NULL,
        NULL,
        'Deposito Nodo Andino',
        '15100'
    );

---------------------------
-- Tabla estado_contenedor
---------------------------
INSERT INTO estado_contenedor (nombre)
VALUES
    ('disponible'),
    ('fuera de servicio'),
    ('en viaje'),
    ('reservado'),
    ('entregado');

--------------------
-- Tabla contenedor
--------------------
INSERT INTO contenedor (id_estado_contenedor, peso, volumen, id_cliente)
VALUES
    ((SELECT id FROM estado_contenedor WHERE nombre = 'disponible'),        2300.00, 33.00, 1),
    ((SELECT id FROM estado_contenedor WHERE nombre = 'reservado'),         2400.00, 33.00, 2),
    ((SELECT id FROM estado_contenedor WHERE nombre = 'en viaje'),          2500.00, 67.00, 3),
    ((SELECT id FROM estado_contenedor WHERE nombre = 'fuera de servicio'), 2200.00, 33.00, 4),
    ((SELECT id FROM estado_contenedor WHERE nombre = 'entregado'),         2600.00, 67.00, 5),
    ((SELECT id FROM estado_contenedor WHERE nombre = 'disponible'),        2700.00, 40.00, 6);

--------------------------
-- Tabla estado_solicitud
--------------------------
INSERT INTO estado_solicitud (nombre)
VALUES
    ('BORRADOR'),
    ('PROGAMADA'),
    ('EN_TRANSITO'),
    ('ENTREGADA');


-----------------------
-- Tabla transportista
------------------------
INSERT INTO transportista (documento, nombre, apellido, email, telefono)
VALUES
    ('27.456.789',     'Jorge',    'Sánchez',     'jsanchez@transporte.com',      '+54 9 351 445-1122'),
    ('18.345.678-2',   'Ricardo',  'Muñoz',       'rmunoz@cargoexpress.cl',       '+56 9 7654-8899'),
    ('5.678.912-3',    'Silvana',  'Pereira',     'spereira@logistica.com.uy',    '+598 92 555-777'),
    ('72654321',       'Miguel',   'Quispe',      'mquispe@andescargo.pe',        '+51 987 654 321');

----------------------
-- Tabla tipo_camion
----------------------
INSERT INTO tipo_camion (nombre)
VALUES
    ('Mediano'),
    ('Pesado'),
    ('Carreton'),
    ('Semi Remolque');

-----------------
-- Tabla camion
-----------------
INSERT INTO camion (
    id_transportista, id_tipo_camion, patente, capacidad_peso, capacidad_volumen,
    disponible, costo_base_km, consumo_combustible_promedio
)
VALUES
-- Camión pesado – Argentina (Jorge Sánchez)
(
    (SELECT id FROM transportista WHERE documento = '27.456.789'),
    2,
    'AA123BB',
    28000.00,
    60.00,
    TRUE,
    150.00,
    32.5
),
-- Semirremolque – Chile (Ricardo Muñoz)
(
    (SELECT id FROM transportista WHERE documento = '18.345.678-2'),
    4,
    'CL-FR45-22',
    30000.00,
    70.00,
    TRUE,
    180.00,
    29.0
),
-- Carretón – Uruguay (Silvana Pereira)
(
    (SELECT id FROM transportista WHERE documento = '5.678.912-3'),
    3,
    'UY-AB1234',
    35000.00,
    85.00,
    TRUE,
    220.00,
    34.8
),
-- Pesado – Perú (Miguel Quispe)
(
    (SELECT id FROM transportista WHERE documento = '72654321'),
    2,
    'PE-9876',
    26000.00,
    58.00,
    TRUE,
    140.00,
    31.0
);

--------------------
-- Tabla solicitud
--------------------
INSERT INTO solicitud (
    id_contenedor,
    id_cliente,
    id_estado_solicitud,
    id_camion,
    fecha_creacion,
    costo_estimado,
    tiempo_estimado,
    costo_final,
    tiempo_real
)
VALUES
    (
        1,
        (SELECT id FROM cliente WHERE documento = '32.554.789'),
        (SELECT id FROM estado_solicitud WHERE nombre = 'borrador'),
        NULL,
        DATE '2025-01-10',
        NULL, NULL, NULL, NULL
    ),
    (
        2,
        (SELECT id FROM cliente WHERE documento = '19.876.543-2'),
        (SELECT id FROM estado_solicitud WHERE nombre = 'borrador'),
        NULL,
        DATE '2025-01-11',
        NULL, NULL, NULL, NULL
    ),
    (
        3,
        (SELECT id FROM cliente WHERE documento = '4.321.987-6'),
        (SELECT id FROM estado_solicitud WHERE nombre = 'programada'),
        NULL,
        DATE '2025-01-12',
        NULL, NULL, NULL, NULL
    ),
    (
        4,
        (SELECT id FROM cliente WHERE documento = '74561234'),
        (SELECT id FROM estado_solicitud WHERE nombre = 'programada'),
        NULL,
        DATE '2025-01-13',
        NULL, NULL, NULL, NULL
    ),
    (
        5,
        (SELECT id FROM cliente WHERE documento = '30-99999999-7'),
        (SELECT id FROM estado_solicitud WHERE nombre = 'en transito'),
        NULL,
        DATE '2025-01-14',
        NULL, NULL, NULL, NULL
    ),
    (
        6,
        (SELECT id FROM cliente WHERE documento = '30-67891234-5'),
        (SELECT id FROM estado_solicitud WHERE nombre = 'entregada'),
        NULL,
        DATE '2025-01-15',
        NULL, NULL, NULL, NULL
    );

----------------------------------
-- Tabla solicitud_estado_historial
-----------------------------------

INSERT INTO solicitud_estado_historial (id_solicitud, id_estado_solicitud, fecha_registro)
SELECT s.id, s.id_estado_solicitud, NOW()
FROM solicitud s;

--------------------
-- Tabla ruta
--------------------
INSERT INTO ruta (
    cantidad_tramos,
    cantidad_depositos,
    distancia_total_km,
    tiempo_total_minutos,
    costo_total,
    seleccionada,
    id_solicitud
)
VALUES
    (3, 2, 120.5, 180, NULL, FALSE, 1),
    (2, 1, 75.0, 120, NULL, TRUE, 2),
    (4, 3, 180.7, 260, NULL, FALSE, 3),
    (1, 1, 40.2, 60,  NULL, TRUE, 4),
    (5, 4, 230.9, 320, NULL, FALSE, 5),
    (3, 2, 150.3, 220, NULL, TRUE, 6);

-----------------
-- Tabla tarifa
-----------------
INSERT INTO tarifa (id_tipo_camion, rango_peso, rango_volumen, costo_km, costo_combustible, costo_estadia_deposito)
VALUES
-- MEDIANO (id = 1)
(1, '0-8000',      '0-15',   3200.00,  0.40,  7000.00),
(1, '0-8000',      '15-35',  3500.00,  0.45,  7300.00),
(1, '8000-15000',  '0-15',   3900.00,  0.55,  7800.00),
(1, '8000-15000',  '15-35',  4200.00,  0.60,  8200.00),

-- PESADO (id = 2)
(2, '8000-15000',  '15-35',  4800.00,  0.70,  9500.00),
(2, '15000-30000', '15-35',  5500.00,  0.85, 11000.00),
(2, '15000-30000', '35-70',  6100.00,  1.00, 13000.00),

-- CARRETON (id = 3) - cargas especiales
(3, '8000-15000',  '15-35',  6200.00,  0.95, 13500.00),
(3, '15000-30000', '35-70',  7800.00,  1.20, 16500.00),

-- SEMI REMOLQUE (id = 4)
(4, '0-8000',      '15-35',  4000.00,  0.50,  8800.00),
(4, '8000-15000',  '15-35',  4600.00,  0.60,  9500.00),
(4, '15000-30000', '35-70',  5300.00,  0.75, 11500.00);

--------------------
-- Tabla tipo_tramo
---------------------
INSERT INTO tipo_tramo (nombre)
VALUES
    ('origen-depósito'),
    ('depósito-depósito'),
    ('depósito-destino'),
    ('origen-destino');

----------------------
-- Tabla estado_tramo
----------------------
INSERT INTO estado_tramo (nombre)
VALUES
    ('estimado'),
    ('asignado'),
    ('iniciado'),
    ('finalizado');

------------------------
-- Tabla tramo
------------------------
INSERT INTO tramo (
    id_ruta,
    id_camion,
    id_tipo_tramo,
    id_estado_tramo,
    origen,
    destino,
    fecha_hora_inicio,
    fecha_hora_fin,
    costo_aproximado,
    costo_real
)
VALUES
    (1, 1, 1, (SELECT id FROM estado_tramo WHERE nombre = 'estimado'), 1, 2, '2025-01-16 08:00:00', '2025-01-16 12:00:00', 120000, NULL),
    (1, 2, 2, (SELECT id FROM estado_tramo WHERE nombre = 'estimado'), 2, 3, '2025-01-16 13:00:00', '2025-01-16 17:00:00', 130000, NULL),
    (2, 3, 3, (SELECT id FROM estado_tramo WHERE nombre = 'estimado'), 3, 4, '2025-01-17 08:00:00', '2025-01-17 12:00:00', 140000, NULL),
    (2, 1, 4, (SELECT id FROM estado_tramo WHERE nombre = 'estimado'), 4, 5, '2025-01-17 13:00:00', '2025-01-17 17:00:00', 150000, NULL),
    (3, 3, 2, (SELECT id FROM estado_tramo WHERE nombre = 'estimado'), 5, 6, '2025-01-18 08:00:00', '2025-01-18 12:00:00', 170000, NULL),
    (3, 1, 3, (SELECT id FROM estado_tramo WHERE nombre = 'estimado'), 6, 7, '2025-01-18 13:00:00', '2025-01-18 17:00:00', 175000, NULL),
    (4, 2, 1, (SELECT id FROM estado_tramo WHERE nombre = 'estimado'), 7, 1, '2025-01-19 08:00:00', '2025-01-19 12:00:00', 190000, NULL),
    (4, 1, 2, (SELECT id FROM estado_tramo WHERE nombre = 'estimado'), 1, 2, '2025-01-19 13:00:00', '2025-01-19 17:00:00', 200000, NULL);


--------------------
-- Tabla depósito
---------------------
INSERT INTO deposito (id_direccion, nombre, costo_estadia_diario)
VALUES
    (
        (SELECT id FROM direccion WHERE observaciones = 'Deposito Central Terrestre'),
        'Deposito Central Terrestre',
        9500.00
    ),
    (
        (SELECT id FROM direccion WHERE observaciones = 'Deposito Regional'),
        'Deposito Regional',
        8500.00
    ),
    (
        (SELECT id FROM direccion WHERE observaciones = 'Deposito Nodo Andino'),
        'Deposito Nodo Andino',
        7800.00
    );




















































































































































































































































































































