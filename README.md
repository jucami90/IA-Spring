# IA-Spring

Proyecto de prueba para la implementación de Inteligencia Artificial en Spring Boot usando **Spring AI** con el proveedor **OpenAI**.

---

## 🚀 Tecnologías utilizadas

- Java 17+
- Spring Boot 3.x
- Spring AI (integración con OpenAI)
- WireMock (para tests de integración)
- Gradle

---

## ⚙️ Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/jucami90/IA-Spring.git
cd IA-Spring
```

### 2. Configurar la API Key de OpenAI

Este proyecto **no almacena** la API Key de OpenAI en el código fuente. En su lugar, se lee desde una variable de entorno del sistema operativo.

#### En macOS / Linux (zsh o bash)

Exportar la variable solo para la sesión actual:

```bash
export SPRING_AI_OPENAI_API_KEY="sk-tu_clave_aqui"
```

Para que sea **permanente** (se cargue siempre al abrir terminal):

```bash
echo 'export SPRING_AI_OPENAI_API_KEY="sk-tu_clave_aqui"' >> ~/.zshrc
source ~/.zshrc
```

#### En Windows (PowerShell)

```powershell
$env:SPRING_AI_OPENAI_API_KEY="sk-tu_clave_aqui"
```

Para hacerlo permanente en Windows:

```powershell
[System.Environment]::SetEnvironmentVariable("SPRING_AI_OPENAI_API_KEY","sk-tu_clave_aqui","User")
```

---

### 3. Cómo se usa la variable en la aplicación

En `src/main/resources/application.yaml`, la clave se referencia así:

```yaml
spring:
  application:
    name: ia-prompt
  ai:
    openai:
      api-key: ${SPRING_AI_OPENAI_API_KEY}
```

Spring Boot tomará automáticamente el valor de la variable de entorno al iniciar la aplicación.

---

## ▶️ Ejecutar la aplicación

```bash
./gradlew bootRun
```

---

## 🧪 Ejecutar los tests

Los tests de integración usan WireMock para simular las respuestas de OpenAI, por lo que **no requieren** una API Key real para correr.

```bash
./gradlew test
```

---

## 🔐 Seguridad

> ⚠️ **Nunca** agregues tu API Key directamente en `application.yaml` ni en ningún archivo que sea commiteado al repositorio.
> GitHub tiene protección automática contra secretos expuestos y bloqueará el push si detecta una clave real.

Si accidentalmente commiteaste una key:
1. **Revócala** inmediatamente en [platform.openai.com/api-keys](https://platform.openai.com/api-keys).
2. Genera una nueva clave.
3. Usa siempre variables de entorno.

---

## 📁 Estructura del proyecto

```
src/
├── main/
│   ├── java/com/test/ia_prompt/
│   │   ├── controller/       # Endpoints REST
│   │   ├── record/           # DTOs (Question, Answer)
│   │   └── service/          # Lógica de negocio con Spring AI
│   └── resources/
│       └── application.yaml  # Configuración (sin secretos)
└── test/
    ├── java/                  # Tests de integración con WireMock
    └── resources/
        └── test-openai-response.json  # Respuesta simulada de OpenAI
```
