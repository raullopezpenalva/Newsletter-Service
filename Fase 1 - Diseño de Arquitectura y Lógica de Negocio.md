## 🧠 Visión General

Este microservicio se encargará de:
1. Gestionar suscripciones por doble opt-in.
2. Gestionar bajas seguras (unsubscribe).
3. Proveer end points de administración.
4. Generar dinámicamente los enlaces personalizados (confirmación y baja).
5. Exponer un endpoint para que el sistema de envío de newsletter pueda obtener el enlace unsubscribe correcto por suscriptor.

## 🧬 Entidades principales

### ✅ Subscriber

Representa a una persona suscrita (o en proceso) a la newsletter.

| Campo         | Tipo      | Descrición                                                        |
| ------------- | --------- | ----------------------------------------------------------------- |
| `id`          | UUID      | Identificador  único                                               	|
| `email`       | String    | Email del subscriptor                                            	 |
| `status`      | Enum      | PENDING, ACTIVE, UNSUBCRIBED                                      		|
| `createdAt`   | Timestamp | Fecha de suscripción                                              |
| `verifiedAt`  | Timestamp | Fecha de verificación                                             |
| `usercreated` | Boolean   | True: viene de crear una cuenta -> no envía email de confirmación |

### ✅ VerificatinoToken

Token temporal que se genera cuando un usuario se suscribe.

| Campo          | Tipo      | Descripción               |
| -------------- | --------- | ------------------------- |
| `id`           | UUID      | ID del token              |
| `token`        | String    | Token seguro aleatorio    |
| `subscribedId` | UUID      | FK de Subscriber          |
| `type`         | Enum      | CONFIRMATION, UNSUBSCRIBE |
| `expireAt`     | Timestamp | Fecha de expiración       |
| `used`         | Boolean   | Si el token ya fue usado  |

## 🔁 Casos de Uso y Flujos

### 📥 1. Alta de suscriptor (/subscribe)

**Flujo**:
1. El usuario envía su email al endpoint.
2. Se validad el formato del email
3. Se comprueba si ya está suscrito y activado (else si está suscrito y activado enviar respuesta “Ya estás suscrito”; else si está susbcrito y no activado, generar nuevo token de activación, anular anterior token y volver a enviar email de confirmación).
4. Se comprueba si viene de usuario creado o no: 
		If el email viene de usuario creado (boolean usercreated: true) -> Se crea un Subscriber con status = ACTIVE (ya que hace la confirmación de email al crear el usuario por otro microservicio). (SE TERMINA EL FLUJO)
		Else el email viene sin usuario asociado (boolean usercreated:false) -> Se crea un Subcriber con status = PENDING.
			Se genera un VerificationTOken tipo CONFIRMARTION (Validez: 24h).
				Se envía un email con un link de confirmación (SE TERMINA FLUJO)
				Ejemplo``
				```https://newsletter.miweb.com/verify?token=XYZ```

### 📬 2. Confirmación de email (/verify?token=XYZ)

**Flujo**:
1. Se recibe el token por URL.
2. Se busca el token en base de datos:
	- ¿Existe?
	- ¿Es de tipo CONFIRMATION?
	- ¿Está caducado?
	- ¿Está usado?
3. Si todo es correcto:
	- Se marca used = true
	- Se actualiza Subscriber.status = ACTIVE y se guarda verfiedAt.

### ❌ 3. Baja de suscripción (/unsubscribe?token=XYZ)

**Flujo**:
1. Cada vez que se envía una newsletter, se **genera un token exlusivo de tipo UNSUBSCRIBE** para ese email, con validez de 7 días (por ejemplo).
2. Ese token se inserta en el email:
````text
https://newsletter.miweb.com/unsubscribe?token=XYZ
````
3. Cuando el usuario hace clic:
	- Se comprueba el token:
		- ¿Existe?
		- ¿Es de tipo Unsubscribe?
		- ¿Está caducado?
		- ¿Está usado?
	- Se responde con un fronend donde sale el botón unsubscribe y encuesta de razones.
	- Se espera confirmación con otro end point POST con cuerpo { token: mismo que link unsubscribe, reason: “encuesta” }
	- Se comprueba toke en body con token unsubscribe de este mismo link.
	- Se marca Subscriber.status = UNSUBSCRIBED.
	- Se marca el token como used= true.

✅ Cada email tiene su propio token de baja (seguridad + trazabilidad).
No se requiere autentificación adicional.

### 🔁 4. Generación de enlace de baja (/admin/unsubscribe-link?email=…)

Este endpoint lo usará el **servicio que envía newsletters** para generar dinámicamente el token de baja antes de cada envío.

**Flujo**:
1. Se recibe el email del destinatario.
2. Se busca el Subscriber.
3. Si está ACTIVE, se genera un VerificationToken tipo UNSUBSCRIBE.
4. Se devuelve la URL de baja personalizada.

✅Este endpoint estará protegido por **API Key o JWT**.


## 🛡️Seguridad

| Capa               | Protección                                                      |
| ------------------ | --------------------------------------------------------------- |
| Endpoints públicos | Validación con token temporal, sin necesidad de autentificación |
| Endpoints admin    | Protegidos con API Key o JWT + roles                            |
| Rate Limiting      | Implementación por IP por endpoint (en futuras fases)           |
| Validación         | Validación fuerte de inputs (@Email, regex, etc.)               |

## 🌐 Endpoints RESTful

| Método | Ruta                           | Descripción                                                                                                             | Seguridad   |
| ------ | ------------------------------ | ----------------------------------------------------------------------------------------------------------------------- | ----------- |
| POST   | /subscribe                     | Alta de suscriptor                                                                                                      | Pública     |
| GET    | /verify?token=…                | Verifica email y activa suscripción                                                                                     | Pública     |
| GET    | /unsubscribe?token=…           | Responde con fronend para doble confirmación + encuesta                                                                 | Pública     |
| POST   | /unsubscribe/confirm?          | Cambia subscriber a UNSUBSCRIBER si token en body es el mismo que el token del link de unsubscribe y guarda la encuenta | Pública     |
| GET    | /admin/subscribers             | Listado de suscriptoresq                                                                                                | Admin (JWT) |
| GET    | /admin/stats                   | Méticas de suscriptores                                                                                                 | Admin (JWT) |
| GET    | /admin/unsubscribe-link?emai=… | Generar link personalizado de baja                                                                                      | Interno/API |

## 🏗️ Estructura de paquetes
````text
com.raulcode.newsletter
├── controller
├── service
├── model
├── dto
├── repository
├── config
├── exception
└── util
````

## 🔒 Seguridad por token

- Todos los tokens (confirmación y baja) son:
	- **UUID aleatorios**
	- **Con fecha de expiración configurable**
	- **De un solo uso (se marcan como used = true)**
- El POST /unsubscribe/confirm exige el mismo token que estaba embebido en el link -> proteges el flujo frente a ataques CSRF indirectos (requiere conocimiento del token exacto).
