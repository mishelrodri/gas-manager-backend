# 🏢 Sistema de Gestión de Agencia de Gas

## 📋 Entidades del Sistema

### 👤 **Usuario**

- **Propósito**: Los 2 propietarios/administradores de la agencia
- **Campos**:
  - `id`, `username`, `password`, `nombre`, `apellido`, `email`
  - `rol` (PROPIETARIO o ADMINISTRADOR)
  - `activo`, `fechaCreacion`, `ultimoAcceso`
- **Relaciones**:
  - Puede realizar ventas
  - Puede hacer movimientos de tambos

### 👥 **Cliente**

- **Propósito**: Clientes de la agencia de gas
- **Campos**:
  - `id`, `nombre`, `apellido`, `dui`, `direccion`, `telefono`
  - `clienteLeal`, `añoNavidadActivo` (para clientes navideños)
  - `fechaRegistro`, `activo`
- **Relaciones**:
  - Puede tener muchas ventas

### 💰 **Venta**

- **Propósito**: Ventas realizadas por la agencia
- **Campos**:
  - `id`, `fecha`, `numeroReferencia`, `tipoTransaccion` (VENDIDO/COMPRADO)
  - `monto`, `cantidad` (tambos), `descripcion`
- **Relaciones**:
  - Pertenece a un cliente
  - Realizada por un usuario

### 🏪 **Tienda**

- **Propósito**: Tiendas pequeñas con convenio para préstamo de tambos
- **Campos**:
  - `id`, `nombre`, `numeroTambosPrestados`, `numeroTambosMaximo`
  - `direccion`, `telefono`, `contacto`
  - `fechaConvenio`, `activo`, `observaciones`
- **Relaciones**:
  - Tiene muchos movimientos de tambos

### 📦 **MovimientoTambo**

- **Propósito**: Control de préstamos y devoluciones de tambos a tiendas
- **Campos**:
  - `id`, `fecha`, `tipoMovimiento` (PRESTAMO/DEVOLUCION/AJUSTE)
  - `cantidad`, `observaciones`
- **Relaciones**:
  - Pertenece a una tienda
  - Realizado por un usuario

## 🔗 Relaciones Principales

```
Cliente (1) ←→ (N) Venta
Usuario (1) ←→ (N) Venta
Usuario (1) ←→ (N) MovimientoTambo
Tienda (1) ←→ (N) MovimientoTambo
```

## 📊 Casos de Uso Principales

1. **Gestión de Ventas**: Registro de tambos vendidos/comprados
2. **Control de Inventario**: Tambos prestados a tiendas pequeñas
3. **Clientes Leales**: Sistema navideño por años
4. **Usuarios Limitados**: Solo 2 propietarios máximo

## 🎄 Sistema Navideño Simplificado

- Campo `clienteLeal` en Cliente
- Campo `añoNavidadActivo` para marcar el año actual
- No requiere tabla separada para casos simples
