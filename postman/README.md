# 📮 Colecciones Postman - Backend Quality API

Colecciones completas para probar las APIs del sistema bancario con todos los métodos CRUD.

## 📦 Archivos Incluidos

### Colecciones Base
- `TypeDocument-Collection.postman_collection.json` - CRUD de Tipos de Documento
- `Client-Collection.postman_collection.json` - CRUD de Clientes

### Colecciones del Sistema Bancario
- `TypeAccount-Collection.postman_collection.json` - CRUD de Tipos de Cuenta (Ahorros, Corriente)
- `Currency-Collection.postman_collection.json` - CRUD de Monedas (USD, PEN, EUR)
- `Account-Collection.postman_collection.json` - CRUD de Cuentas Bancarias (con número auto-generado)
- `AccountActivation-Collection.postman_collection.json` - Activación de Cuentas con validación de identidad

### Environment
- `Backend-Quality.postman_environment.json` - Variables de entorno

## 🚀 Importar en Postman

### 1. Importar las Colecciones

1. Abrir Postman
2. Click en **Import** (esquina superior izquierda)
3. Arrastrar los 7 archivos JSON o seleccionarlos
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

### Fase 2: Crear Tipos de Cuenta

Ejecutar **TypeAccount Collection**:

5. ✅ **Crear tipo de cuenta - Ahorros (SAV)**
   - Crea Savings y guarda el ID en `{{savingsTypeAccountId}}`
   
6. ✅ **Crear tipo de cuenta - Corriente (CHK)**
   - Crea Checking y guarda el ID en `{{checkingTypeAccountId}}`

### Fase 3: Crear Monedas

Ejecutar **Currency Collection**:

7. ✅ **Crear moneda - Dólares (USD)**
   - Crea USD y guarda el ID en `{{usdCurrencyId}}`
   
8. ✅ **Crear moneda - Soles (PEN)**
   - Crea PEN y guarda el ID en `{{penCurrencyId}}`
   
9. ✅ **Crear moneda - Euros (EUR)**
   - Crea EUR y guarda el ID en `{{eurCurrencyId}}`

### Fase 4: Crear Clientes

Ejecutar **Client Collection**:

10. ✅ **Crear cliente - Juan Pérez (DNI)**
    - Usa `{{dniTypeDocumentId}}` automáticamente
    - Guarda el número de documento en `{{documentNumber}}`
   
11. ✅ **Crear cliente - María López (PASSPORT)**
    - Usa `{{passportTypeDocumentId}}` automáticamente
   
12. ✅ **Crear cliente - Carlos Empresa (RUC)**
    - Usa `{{rucTypeDocumentId}}` automáticamente

### Fase 5: Crear Cuentas Bancarias

Ejecutar **Account Collection**:

13. ✅ **Crear cuenta - Ahorros USD**
    - El número de cuenta se genera automáticamente
    - Estado inicial: INACTIVE
    - Guarda ID en `{{savingsUsdAccountId}}` y número en `{{accountNumber}}`
   
14. ✅ **Crear cuenta - Corriente PEN**
    - Número auto-generado con formato: [Tipo][Moneda][Timestamp][Random][CheckDigit]
    - Ejemplo: `SAUSD17385212345678` (19 caracteres)

### Fase 6: Activar Cuentas

Ejecutar **AccountActivation Collection**:

15. ✅ **Activar cuenta - Exitoso**
    - Valida tipo de documento y número contra el cliente titular
    - Si coincide: `status` cambia a ACTIVE, `activationStatus` = SUCCESS
   
16. ✅ **Activar cuenta - Fallido**
    - Usa número de documento incorrecto (99999999)
    - Resultado: `activationStatus` = FAILED, `errorReason` con el motivo


## 🧪 Pruebas de Error Incluidas

### TypeDocument Collection

| Request | Error | Código | Descripción |
|---------|-------|--------|-------------|
| Error - Código duplicado | 409 | RBV-005 | Intenta crear DNI duplicado |
| Error - Validación campo | 400 | RBV-003 | Código muy corto (1 carácter) |
| Error - ID no existe | 404 | RNF-001 | Busca ID 99999 |

### TypeAccount Collection

| Request | Error | Código | Descripción |
|---------|-------|--------|-------------|
| Error - Código duplicado | 409 | RBV-005 | Intenta crear código duplicado |
| Error - ID no existe | 404 | RNF-001 | Busca ID inexistente |

### Currency Collection

| Request | Error | Código | Descripción |
|---------|-------|--------|-------------|
| Error - Código duplicado | 409 | RBV-005 | Intenta crear código duplicado |
| Error - ID no existe | 404 | RNF-001 | Busca ID inexistente |

### Account Collection

| Request | Error | Código | Descripción |
|---------|-------|--------|-------------|
| Error - Cliente no existe | 404 | RNF-001 | Usa idClient = 99999 |
| Error - TypeAccount no existe | 404 | RNF-001 | Usa idTypeAccount = 99999 |
| Error - Currency no existe | 404 | RNF-001 | Usa idCurrency = 99999 |

### AccountActivation Collection

| Request | Error | Código | Descripción |
|---------|-------|--------|-------------|
| Activación fallida | 201 | ACT-002 | Documento no coincide con titular |
| Error - Cuenta no existe | 404 | ACT-001 | Usa idAccount = 99999 |

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

### Ejecutar todas las colecciones automáticamente en este orden:

1. **TypeDocument API** (crear tipos de documento primero)
   - Click derecho → Run collection → Run
   
2. **TypeAccount API** (crear tipos de cuenta)
   - Click derecho → Run collection → Run
   
3. **Currency API** (crear monedas)
   - Click derecho → Run collection → Run
   
4. **Client API** (crear clientes)
   - Click derecho → Run collection → Run
   
5. **Account API** (crear cuentas bancarias)
   - Click derecho → Run collection → Run
   
6. **AccountActivation API** (activar cuentas)
   - Click derecho → Run collection → Run

**Resultado esperado:**
- TypeDocument: 10/10 tests passed ✅
- TypeAccount: 6/6 tests passed ✅
- Currency: 7/7 tests passed ✅
- Client: 12/12 tests passed ✅
- Account: 7/7 tests passed ✅
- AccountActivation: 7/7 tests passed ✅

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

// TypeAccount (generados automáticamente)
{{typeAccountId}}          // Primer tipo encontrado en GET all
{{savingsTypeAccountId}}   // ID de Cuenta de Ahorros (SAV)
{{checkingTypeAccountId}}  // ID de Cuenta Corriente (CHK)

// Currency (generados automáticamente)
{{currencyId}}             // Primera moneda encontrada en GET all
{{usdCurrencyId}}          // ID de Dólar USD
{{penCurrencyId}}          // ID de Sol PEN
{{eurCurrencyId}}          // ID de Euro EUR

// Client (generados automáticamente)
{{clientId}}               // Primer cliente encontrado en GET all
{{juanClientId}}           // ID de Juan Pérez
{{mariaClientId}}          // ID de María López
{{carlosClientId}}         // ID de Carlos Empresa
{{documentNumber}}         // Número de documento del cliente activo

// Account (generados automáticamente)
{{accountId}}              // Primera cuenta encontrada en GET all
{{savingsUsdAccountId}}    // ID de Cuenta Ahorros USD
{{checkingPenAccountId}}   // ID de Cuenta Corriente PEN
{{accountNumber}}          // Número de cuenta auto-generado (19 chars)

// AccountActivation (generados automáticamente)
{{accountActivationId}}    // Primera activación encontrada en GET all
{{successActivationId}}    // ID de activación exitosa
{{failedActivationId}}     // ID de activación fallida
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

### TypeAccount - POST/PUT

```json
{
  "code": "SAV",
  "description": "Cuenta de Ahorros",
  "active": true
}
```

### Currency - POST/PUT

```json
{
  "code": "USD",
  "name": "Dólar Americano",
  "symbol": "$",
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

### Account - POST/PUT

```json
{
  "idClient": 1,
  "idTypeAccount": 1,
  "idCurrency": 1,
  "balance": 5000.00
}
```
**NOTA:** El campo `accountNumber` se genera automáticamente y NO debe incluirse en el request.

### AccountActivation - POST (Activar Cuenta)

```json
{
  "accountNumber": "SAUSD17385212345678",
  "idTypeDocument": 1,
  "documentNumber": "12345678"
}
```
**NOTA:** 
- Usa `accountNumber` en lugar de `idAccount` por seguridad (no expone IDs internos)
- Valida que el tipo de documento y número coincidan con el cliente titular de la cuenta
- Si coincide: `activationStatus` = SUCCESS y la cuenta cambia a ACTIVE
- Si no coincide: `activationStatus` = FAILED con `errorReason` descriptivo

## 🎯 Validaciones Implementadas

### TypeDocument
- ✅ `code`: 2-20 caracteres, único
- ✅ `description`: 3-100 caracteres
- ✅ `active`: Boolean requerido
- ✅ `validationPattern`, `minLength`, `maxLength`: Opcionales

### TypeAccount
- ✅ `code`: 2-10 caracteres, único
- ✅ `description`: 3-100 caracteres
- ✅ `active`: Boolean requerido

### Currency
- ✅ `code`: 3 caracteres exactos (ISO 4217), único
- ✅ `name`: 3-50 caracteres
- ✅ `symbol`: 1-5 caracteres
- ✅ `active`: Boolean requerido

### Account
- ✅ `accountNumber`: Generado automáticamente (19 caracteres con Luhn check digit)
- ✅ `idClient`: Requerido, debe existir
- ✅ `idTypeAccount`: Requerido, debe existir
- ✅ `idCurrency`: Requerido, debe existir
- ✅ `balance`: BigDecimal, opcional (default: 0.00)
- ✅ `status`: Asignado automáticamente (INACTIVE)
- ✅ `createdDate`: Asignado automáticamente

### AccountActivation
- ✅ `idAccount`: Requerido, debe existir
- ✅ `idTypeDocument`: Requerido, debe existir
- ✅ `documentNumber`: Requerido, debe coincidir con el titular
- ✅ `activationStatus`: Asignado automáticamente (SUCCESS/FAILED)
- ✅ `errorReason`: Generado automáticamente si falla
- ✅ `attemptDate`: Asignado automáticamente

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

**Versión:** 2.0.0  
**Fecha:** Febrero 2, 2026  
**API:** Backend Quality - Sistema Bancario Completo  
**Entidades:** TypeDocument, Client, TypeAccount, Currency, Account, AccountActivation
