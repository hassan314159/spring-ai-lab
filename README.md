# spring-ai-lab

Monorepo (multi-module) Spring project to demonstrate **Spring AI** across several modules.

> **Status (2025-08-17)**: Initial commit includes one simple module that calls Hugging Face directly (without Spring AI). Next modules will add Spring AI examples.

## Project Structure

- **Parent (this folder)** — Maven aggregator/build configuration
- **simple-summarizer** — Demo using direct Hugging Face API (no Spring AI yet)

## Build

```bash
# From the repository root
mvn -v
mvn clean install
```

- Java: 24
- Spring Boot parent: ⟂ (not using spring-boot-starter-parent at root)
- Spring Boot version: 3.5.4
- Group: `dev.springai` — Artifact: `spring-ai-lab` — Version: `1.0.0`

## Modules

- `simple-summarizer` — artifactId: `simple-summarizer`

## Notes

- Child modules can depend on shared BOMs or starter dependencies from the parent.
- When you add Spring AI to future modules, prefer the official Spring AI starters and configure providers via `application.yml`.
- Keep modules small and focused: one provider/feature per module for clarity.
