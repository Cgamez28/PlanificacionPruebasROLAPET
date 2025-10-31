# 🧩 Explicación Detallada de las Dependencias y Pruebas de Integración

Este documento describe las **dependencias de testing** configuradas en el archivo `pom.xml`, así como la **estructura y propósito** de la prueba de integración `CatalogRepositoryIT`.

---

## ⚙️ 4. Dependencias en `pom.xml`

A continuación, se detalla la justificación de cada dependencia incluida en el entorno de pruebas propuesto.

### 🧪 Dependencias de Testing

| Dependencia                                                                      | Propósito                                                                                                                                                       | Justificación                                                                                                                                                           |
| -------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **JUnit 5**<br>`org.junit.jupiter:junit-jupiter`                                 | Framework principal para pruebas en Java moderno.<br>Incluye anotaciones (`@Test`, `@BeforeEach`, `@AfterEach`) y aserciones básicas.                           | Motor de ejecución para pruebas unitarias e integraciones.<br>Ejecuta todas las pruebas anotadas con `@Test`.                                                           |
| **Mockito**<br>`org.mockito:mockito-core`<br>`org.mockito:mockito-junit-jupiter` | Creación de *mocks* de dependencias.<br>Integración con JUnit 5 mediante `@Mock`, `MockitoAnnotations.openMocks(this)` y `@ExtendWith(MockitoExtension.class)`. | Aísla la clase bajo prueba (`CatalogService`) de sus dependencias reales.<br>Permite simular comportamientos y verificar interacciones sin acceder a recursos externos. |
| **AssertJ**<br>`org.assertj:assertj-core`                                        | API fluida y legible para aserciones (`assertThat(x).hasSize(2).contains(...)`).                                                                                | Mejora la legibilidad de las pruebas y genera mensajes de error descriptivos.                                                                                           |
| **H2 Database**<br>`com.h2database:h2`                                           | Base de datos SQL embebida *in-memory*, ideal para pruebas de integración ligeras.                                                                              | Permite ejecutar pruebas SQL sin servidores externos.<br>Rápida, determinista y fácilmente inicializable con scripts.                                                   |
| **Testcontainers (opcional)**<br>`org.testcontainers:postgresql`                 | Permite instanciar contenedores Docker (p. ej. Postgres) durante las pruebas.                                                                                   | Proporciona un entorno realista con el mismo dialecto SQL que producción.<br>Recomendado en CI si Docker está disponible.                                               |

---

### 🔧 Plugins Maven (Build)

| Plugin                    | Fase                         | Función                                                                                                                            |
| ------------------------- | ---------------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| **maven-surefire-plugin** | `test`                       | Ejecuta pruebas unitarias (`*Test.java`). Es el plugin que inicia el *runner* de JUnit.                                            |
| **maven-failsafe-plugin** | `integration-test`, `verify` | Diseñado para pruebas de integración (`*IT.java`). Separa las unitarias de las integraciones para controlar el orden de ejecución. |

---

## 🧠 5. Prueba de Integración `CatalogRepositoryIT`

Esta prueba valida la correcta integración entre la implementación JDBC del repositorio (`JdbcCatalogRepository`) y la base de datos.

---

### 🎯 Objetivo

Verificar que `JdbcCatalogRepository`:

* Se conecta correctamente a la base de datos.
* Lee filas y mapea columnas a objetos `Item`.
* Devuelve los resultados esperados mediante consultas SQL reales.

---

### 🔄 Flujo de Ejecución

#### **1️⃣ Inicialización del DataSource H2**

Se crea un `JdbcDataSource` apuntando a:

```properties
jdbc:h2:mem:rolapet;DB_CLOSE_DELAY=-1
```

* **Memoria:** Base de datos en memoria cuyo ciclo de vida depende de la JVM.
* **DB_CLOSE_DELAY=-1:** Evita que la base de datos se cierre al liberar la última conexión.

---

#### **2️⃣ Creación de Esquema y Carga de Datos (Seed)**

Se ejecutan los siguientes scripts SQL:

```sql
CREATE TABLE items (id VARCHAR PRIMARY KEY, name VARCHAR, price INT);
INSERT INTO items VALUES ('1','Casco',10000);
```

> 💡 En entornos avanzados, se recomienda ubicar los scripts en
> `src/test/resources/test-data.sql` para ejecución automática.

---

#### **3️⃣ Construcción del Repositorio JDBC**

```java
JdbcCatalogRepository repo = new JdbcCatalogRepository(ds);
```

El repositorio utiliza el `DataSource` proporcionado para ejecutar consultas.

---

#### **4️⃣ Ejecución del Método a Probar**

```java
List<Item> items = repo.findAll();
```

La consulta ejecutada es:

```sql
SELECT id, name, price FROM items;
```

Devuelve una lista de objetos `Item` mapeados desde las filas.

---

#### **5️⃣ Validaciones (Aserciones)**

Se utiliza **AssertJ**:

```java
assertThat(items).hasSize(1); // Debe devolver exactamente una fila.
assertThat(items.get(0).getName()).isEqualTo("Casco"); // Valida el mapeo de la columna 'name'.
```

---

#### **6️⃣ Teardown (Limpieza)**

```java
@AfterEach
void tearDown() {
    conn.close();
}
```

Libera recursos y garantiza aislamiento entre ejecuciones, gracias al uso de una base H2 en memoria.

---

### 🧾 Valor de la Prueba

| Aspecto              | Descripción                                                                                 |
| -------------------- | ------------------------------------------------------------------------------------------- |
| **Integración Real** | Valida SQL, mapeo y lógica de `JdbcCatalogRepository` en conjunto.                          |
| **Determinismo**     | La prueba crea y puebla su propio esquema en `@BeforeEach`, evitando dependencias externas. |
| **Rapidez**          | H2 ejecuta en milisegundos, ideal para CI/CD.                                               |

---

## ⚖️ Consideraciones y Variantes

### 🔹 H2 vs Testcontainers

| Criterio             | H2                              | Testcontainers                  |
| -------------------- | ------------------------------- | ------------------------------- |
| **Velocidad**        | 🚀 Muy rápida (en memoria)      | 🐢 Más lenta (usa Docker)       |
| **Realismo SQL**     | Parcial (dialecto simplificado) | Total (usa Postgres/MySQL real) |
| **Recomendado para** | Desarrollo local                | CI/CD con Docker habilitado     |

---

### 🔹 Separación Unitarias vs Integración

| Tipo            | Sufijo       | Dependencias        | Plugin Maven            |
| --------------- | ------------ | ------------------- | ----------------------- |
| **Unitarias**   | `*Test.java` | Mockito, AssertJ    | `maven-surefire-plugin` |
| **Integración** | `*IT.java`   | H2 / Testcontainers | `maven-failsafe-plugin` |

---

### 🔹 Ejecución en CI/CD

* ✅ **H2:** Simplemente ejecutar

  ```bash
  mvn verify
  ```
* 🐳 **Testcontainers:** Requiere entorno CI con soporte Docker
  (por ejemplo: `runs-on: ubuntu-latest` en GitHub Actions).

---

📘 **Conclusión:**
Este entorno de pruebas ofrece un balance entre **rapidez**, **aislamiento** y **realismo**, utilizando **H2** para desarrollo ágil y **Testcontainers** para validaciones más fieles al entorno de producción.
