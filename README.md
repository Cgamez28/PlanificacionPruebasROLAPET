# 📋 Plan de Pruebas - ROLA PET App

## 1. Objetivo
Establecer la arquitectura, configuración y lineamientos para las pruebas automatizadas del proyecto, asegurando la calidad del código desde las primeras fases del desarrollo.

## 2. Alcance
- Configuración base de **JUnit 5**, **Mockito**, **AssertJ**, **H2**.
- Definición de estructura de carpetas y convenciones.
- Estrategia para pruebas **unitarias**, **de integración** y **de sistema** (futuras).
- Documentación y ejecución local y en CI/CD.

## 3. Tipos de pruebas
| Tipo | Objetivo | Herramientas | Responsable |
|------|-----------|---------------|--------------|
| Unitarias | Validar la lógica de clases individuales | JUnit5, Mockito, AssertJ | Desarrolladores |
| Integración | Validar interacción entre módulos y BD | JUnit5, H2, Testcontainers | QA + Backend |
| Sistema (futuro) | Validar flujo end-to-end | Selenium/Playwright | QA |

## 4. Estructura
src/test/java/com/rolapet/...
├─ catalog/ (unit tests)
└─ dao/ (integration tests)


## 5. Estrategia de ejecución
- Local: `mvn test`
- Integración: `mvn verify`
- CI/CD: GitHub Actions (workflow `ci-tests.yml`)

## 6. Cobertura
- Mínimo 60 % en módulos core.
- Incremento gradual por sprint.

## 7. Buenas prácticas
- Nombres descriptivos en tests (`should<Action>When<Condition>`).
- AAA (Arrange–Act–Assert).
- Tests deterministas, sin dependencias externas no simuladas.
- Uso de mocks/stubs para aislamiento.

## 8. Herramientas adicionales
- Jacoco (futura integración para reportes de cobertura).
- Testcontainers (para pruebas con bases reales en CI).

## 9. Riesgos
- Baja cobertura inicial.
- Falta de integración continua.
- Dependencias externas no mockeadas.

## 10. Entregables
1. Proyecto configurado con dependencias de test.
2. Estructura `src/test/java` definida.
3. Plan de pruebas documentado.
4. Ejemplo de prueba unitaria e integración.
5. Workflow CI de pruebas.
