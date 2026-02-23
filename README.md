# Spring AI Telecom Assistant

A multi-module project that demonstrates how to use **Spring AI** and **MCP (Model Context Protocol)** to build a simple AI assistant.
Throughout the conversations, the assistant might call the available MCP tools exposed by the two telecom-dedicated servers so that 
the context is enriched and the LLM produces more accurate answers.

## What It Demonstrates

- How to use **Spring AI** to build a chatbot interacting with an LLM (OpenAI)
- How to use **Spring AI MCP** to build independent specialized MCP servers that expose tools over HTTP
- How to use **Spring MCP Server Security** to secure the MCP client-server communication using API keys
- How to use **Spring Advisors** to address concerns like chat memory and token consumption monitoring

## Project Layout

- `telecom-assistant` — web UI + chat orchestration (Spring AI MCP *client* + OpenAI model)
- `invoice-mcp-server` — MCP server exposing invoice-related tools (Spring AI MCP *server*)
- `vendor-mcp-server` — MCP server exposing vendor-related tools (Spring AI MCP *server*)

## Repository Layout

- `telecom-assistant/` — main application (Spring MVC and Thymeleaf UI)
- `invoice-mcp-server/` — invoice MCP server (data is read from a PostgreSQL database)
- `vendor-mcp-server/` — vendor MCP server (data is kept in-memory)

## Prerequisites

- Java **25** (see `<java.version>` in the parent `pom.xml`)
- Maven (or use the included wrapper: `mvnw` / `mvnw.cmd`)
- PostgreSQL running locally (defaults assume `localhost:5432`)
- An OpenAI API key (the UI app uses `spring-ai-starter-model-openai`)

## Configuration

The apps are configured via `application.properties` plus environment variables.

### Environment variables

Set these before starting the apps:

- `OPEN_AI_J_API_KEY` — OpenAI API key used by `telecom-assistant`
- `POSTGRES_USER` — database user (used by `telecom-assistant` and `invoice-mcp-server`)
- `POSTGRES_PASSWORD` — database password (used by `telecom-assistant` and `invoice-mcp-server`)
- `API_KEY_ID` — API key id used by the MCP servers (and by the client when calling them)
- `API_KEY_SECRET` — API key secret used by the MCP servers (and by the client when calling them)

Notes:
- `telecom-assistant` configures the API Keys required by the two MCP servers via:
  - `mcp.server.api-key.parameters.invoice.id/secret`
  - `mcp.server.api-key.parameters.vendor.id/secret`
- `invoice-mcp-server` stores its API keys in the `ServerApiKeys` database table and implements a custom `ApiKeyEntityRepository`.
- `vendor-mcp-server` exposes `api.key.id` / `api.key.secret` properties mapped from the same env vars.

### Running Ports and Endpoints

- Telecom Assistant: `http://localhost:8080/telecom-assistant/`
- Invoice MCP server: `http://localhost:8081/mcp-invoice`
- Vendor MCP server: `http://localhost:8082/mcp-vendor`

These defaults come from the modules’ `application.properties`.

## Database

The projects use PostgreSQL.

`telecom-assistant` manages schema using Flyway migrations located at:

- `telecom-assistant/src/main/resources/db/migration/`

The default JDBC URL (from `telecom-assistant/src/main/resources/application.properties`) is:

- `jdbc:postgresql://localhost:5432/postgres?currentSchema=telecomassist`

A database schema named `telecomassist` is needed.

Flyway migrations create and seed:

- `Vendors`, `ServiceTypes`, `Invoices` tables (with sample data)
- `ServerApiKeys` table (API keys used by MCP server security)

## Build

From the repository root:

```powershell
./mvnw clean test
```

## Run (recommended order)

Run the MCP servers first, then the UI.

### 1) Start `invoice-mcp-server`

```powershell
./mvnw -pl invoice-mcp-server spring-boot:run
```

By default, it starts on port `8081` and exposes the MCP endpoint at `/mcp-invoice`.

### 2) Start `vendor-mcp-server`

```powershell
./mvnw -pl vendor-mcp-server spring-boot:run
```

By default, it starts on port `8082` and exposes the MCP endpoint at `/mcp-vendor`.

### 3) Start `telecom-assistant`

```powershell
./mvnw -pl telecom-assistant spring-boot:run
```

Open:

- `http://localhost:8080/telecom-assistant/`

## Usage

The UI is a simple chat screen (`telecom-assistant/src/main/resources/templates/chat.html`).

In this context, prompts as the following could be tried:

- “How many paid invoices are there?”
- “Provide me a few insights about the invoices containing 'vdf' in their number.”
- “What about the vendor?”
- “Summarize my paid invoices for Verizon.”

## Health / debugging

- The UI exposes Spring Boot Actuator endpoints (see `management.endpoints.web.exposure.include`):
  - `http://localhost:8080/telecom-assistant/actuator/health`
  - `http://localhost:8080/telecom-assistant/actuator/flyway`

## Troubleshooting

### 401/403 when the assistant calls MCP servers

Make sure:

- Both MCP servers are running on the configured ports.
- `API_KEY_ID` and `API_KEY_SECRET` are set (and match what the servers expect).
- The `telecom-assistant` properties point at the correct MCP URLs/endpoints.

### Database errors on startup

- Verify PostgreSQL is reachable at `localhost:5432`.
- Ensure `POSTGRES_USER` / `POSTGRES_PASSWORD` are set.
- Ensure the `telecomassist` schema exists (or adjust the JDBC URL).

### LLM errors

- Verify `OPEN_AI_J_API_KEY` is set.
- Check the configured model in `telecom-assistant/src/main/resources/application.properties`:
  - `spring.ai.openai.chat.options.model` (default is `gpt-5`)

