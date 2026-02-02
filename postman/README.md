# 📮 Colecciones Postman - Backend Quality API

Colecciones completas para probar las APIs de **Client** y **TypeDocument** con todos los métodos CRUD.

## 📦 Archivos Incluidos

- `TypeDocument-Collection.postman_collection.json` - CRUD de Tipos de Documento
- `Client-Collection.postman_collection.json` - CRUD de Clientes
- `Backend-Quality.postman_environment.json` - Variables de entorno

## 🚀 Importar en Postman

### 1. Importar las Colecciones

1. Abrir Postman
2. Click en **Import** (esquina superior izquierda)
3. Arrastrar los 3 archivos JSON o seleccionarlos
4. Click en **Import**

### 2. Configurar el Environment

1. En Postman, ir a **Environments** (icono de ojo)
2. Seleccionar **Backend Quality - Local**
3. Verificar que las variables estén configuradas:
   - `baseUrl`: `http://localhost:8080`
   - `xCorrelationId`: `550e8400-e29b-41d4-a716-446655440000` (UUID v4)
   - `xRequestId`: `6ba7b810-9dad-11d1-80b4-00c04fd430c8` (UUID v4)
   - `xTransactionId`: `7c9e6679-7425-40de-944b-e07fc1f90ae7` (UUID v4)

### 3. Activar el Environment

- En la esquina superior derecha, seleccionar **Backend Quality - Local** del dropdown

## 📋 Orden de Ejecución Recomendado

### Fase 1: Crear Tipos de Documento

Ejecutar **TypeDocument Collection** en este orden:

1. ✅ **Crear tipo de documento - DNI**
   - Crea DNI y guarda el ID en `{{dniTypeDocumentId}}`
   
2. ✅ **Crear tipo de documento - PASSPORT**
   - Crea Pasaporte y guarda el ID en `{{passportTypeDocumentId}}`
   
3. ✅ **Crear tipo de documento - RUC**
   - Crea RUC y guarda el ID en `{{rucTypeDocumentId}}`

4. ✅ **Listar todos los tipos de documento**
   - Verifica que se crearon correctamente

### Fase 2: Crear Clientes

Ahora ejecutar **Client Collection**:

5. ✅ **Crear cliente - Juan Pérez (DNI)**
   - Usa `{{dniTypeDocumentId}}` automáticamente
   
6. ✅ **Crear cliente - María López (PASSPORT)**
   - Usa `{{passportTypeDocumentId}}` automáticamente
   
7. ✅ **Crear cliente - Carlos Empresa (RUC)**
   - Usa `{{rucTypeDocumentId}}` automáticamente

8. ✅ **Listar todos los clientes**
   - Verifica los 3 clientes creados

## 🧪 Pruebas de Error Incluidas

### TypeDocument Collection

| Request | Error | Código | Descripción |
|---------|-------|--------|-------------|
| Error - Código duplicado | 409 | RBV-005 | Intenta crear DNI duplicado |
| Error - Validación campo | 400 | RBV-003 | Código muy corto (1 carácter) |
| Error - ID no existe | 404 | RNF-001 | Busca ID 99999 |

### Client Collection

| Request | Error | Código | Descripción |
|---------|-------|--------|-------------|
| Error - Email duplicado | 409 | RBV-005 | Intenta usar email existente |
| Error - Documento duplicado | 409 | RBV-005 | Intenta usar DNI existente |
| Error - TypeDocument no existe | 404 | RNF-001 | Usa idTypeDocument = 99999 |
| Error - Email inválido | 400 | RBV-001 | Email sin formato válido |
| Error - Cliente no existe | 404 | RNF-001 | Busca cliente ID 99999 |

## 🔍 Tests Automáticos

Cada request incluye tests que validan:

✅ **Status Codes** correctos (200, 201, 204, 400, 404, 409)  
✅ **Headers** requeridos (Location en POST)  
✅ **Estructura** de respuesta (campos obligatorios)  
✅ **Tipos de error** (type, subtype, typeCode)  
✅ **Guardado automático** de IDs en variables de entorno

## 📊 Ejecución en Collection Runner

### Ejecutar toda la colección automáticamente:

1. Click derecho en **TypeDocument API - Backend Quality**
2. Seleccionar **Run collection**
3. Verificar que el environment esté seleccionado
4. Click en **Run TypeDocument API**
5. Repetir para **Client API - Backend Quality**

**Resultado esperado:**
- TypeDocument: 10/10 tests passed ✅
- Client: 12/12 tests passed ✅

## 🔄 Variables de Entorno Autogeneradas

Las colecciones guardan automáticamente los IDs creados:

```javascript
// Headers (con valores UUID v4 predefinidos)
{{xCorrelationId}}         // 550e8400-e29b-41d4-a716-446655440000
{{xRequestId}}             // 6ba7b810-9dad-11d1-80b4-00c04fd430c8
{{xTransactionId}}         // 7c9e6679-7425-40de-944b-e07fc1f90ae7

// TypeDocument (generados automáticamente)
{{typeDocumentId}}         // Primer tipo encontrado en GET all
{{dniTypeDocumentId}}      // ID del DNI creado
{{passportTypeDocumentId}} // ID del Pasaporte creado
{{rucTypeDocumentId}}      // ID del RUC creado

// Client (generados automáticamente)
{{clientId}}               // Primer cliente encontrado en GET all
{{juanClientId}}           // ID de Juan Pérez
{{mariaClientId}}          // ID de María López
{{carlosClientId}}         // ID de Carlos Empresa
```

## 🛠️ Headers Requeridos

Todos los requests incluyen estos headers obligatorios según el proyecto:

```
x-correlation-id: {{xCorrelationId}}   (UUID v4 - 36 caracteres)
x-request-id: {{xRequestId}}           (UUID v4 - 36 caracteres)
x-transaction-id: {{xTransactionId}}   (UUID v4 - 36 caracteres)
Content-Type: application/json         (solo POST/PUT)
```

**⚠️ IMPORTANTE:** Estos headers son validados por el `HeaderValidationFilter` del proyecto. Todos deben ser UUIDs v4 válidos con exactamente 36 caracteres.

## 📝 Ejemplos de Body

### TypeDocument - POST/PUT

```json
{
  "code": "DNI",
  "description": "Documento Nacional de Identidad",
  "validationPattern": "^[0-9]{8}$",
  "minLength": 8,
  "maxLength": 8,
  "active": true
}
```

### Client - POST/PUT

```json
{
  "firstName": "Juan",
  "lastName": "Pérez García",
  "idTypeDocument": 1,
  "documentNumber": "12345678",
  "email": "juan.perez@example.com",
  "phone": "+51987654321"
}
```

## 🎯 Validaciones Implementadas

### TypeDocument
- ✅ `code`: 2-20 caracteres, único
- ✅ `description`: 3-100 caracteres
- ✅ `active`: Boolean requerido
- ✅ `validationPattern`, `minLength`, `maxLength`: Opcionales

### Client
- ✅ `firstName`: 2-50 caracteres
- ✅ `lastName`: 2-50 caracteres
- ✅ `email`: Formato válido, único, máx 100 caracteres
- ✅ `documentNumber`: 3-20 caracteres, único
- ✅ `phone`: Formato válido 7-20 caracteres (regex)
- ✅ `idTypeDocument`: Debe existir en TypeDocument

## 🌐 Swagger UI

Alternativamente, puedes probar la API en Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

## ⚙️ Cambiar Entorno

Para usar otro servidor (Dev, QA, Prod):

1. Duplicar el environment **Backend Quality - Local**
2. Renombrar (ej: "Backend Quality - Dev")
3. Cambiar `baseUrl` a la URL del servidor
4. Seleccionar el nuevo environment
✅ Verifica que el servidor esté corriendo en `http://localhost:8080`
2. ✅ Verifica que los headers sean UUIDs v4 válidos con exactamente 36 caracteres
3. ✅ Los headers `x-correlation-id`, `x-request-id` y `x-transaction-id` ya vienen con valores válidos en el environment
4. ✅ Ejecuta primero TypeDocument Collection antes que Client Collection
5. ✅ Revisa la consola de Postman para ver los errores detallados
6. ✅ Si obtienes error 400 de headers, verifica que estés usando el environment "Backend Quality - Local"

1. Verifica que el servidor esté corriendo en `http://localhost:8080`
2. Verifica que los headers tengan la longitud correcta (36 caracteres)
3. Ejecuta primero TypeDocument Collection antes que Client Collection
4. Revisa la consola de Postman para ver los errores detallados

---

**Versión:** 1.0.0  
**Fecha:** Febrero 2, 2026  
**API:** Backend Quality - Client & TypeDocument
