# Simple Spring AI Chat

A minimal Spring Boot app that talks to a **local Ollama** server using **Spring AI**.  
It exposes two endpoints:
- Text chat (LLM): `POST /ai/chat/ask?q=...`
- Vision describe (image + optional prompt): `POST /ai/vision/describe`

The included `docker-compose.yml` (at the project root) runs **Ollama** locally and prepares **two models**:
- **Text model** (e.g., `mistral`)
- **Vision model** (e.g., `llava`)

> The app expects Ollama on `http://localhost:11434` and serves HTTP on `http://localhost:8080`.


#SpringBoot #SpringAI #Ollama #Java #Docker #LLM #ComputerVision #AI #Mistral #LLaVA #RESTAPI

---

## Prerequisites
- Docker & Docker Compose
- JDK 21+
- Maven 

---

## 1) Start Ollama (dependencies)
From the project root where `docker-compose.yml` is located:
```bash
docker compose up -d
```

### ⏳ First Run (Ollama)

On the first run, Docker will pull the **Ollama** image and Ollama will download the **text** and **vision** models. This can take a while—wait for the downloads to finish before sending requests.

**Verify models are loaded**
- Open in browser: http://localhost:11434/api/tags
- Or via cURL:
```bash
  curl -s http://localhost:11434/api/tags | jq .
```
You should see your models (e.g., mistral, llava) listed.

## 2) Build the Java project


```bash
mvn clean package -DskipTests
```

## 3) Run the app

Run the packaged jar:
```bash
java -jar target/*.jar
```
or from your preferred IDE

The API will be available at http://localhost:8080.

## 4) How to use it (cURL)

Text chat
```
curl -s -X POST "http://localhost:8080/ai/chat/ask?q=Explain%20RAG%20briefly"
```
Vision describe (no prompt)

```
curl -s -X POST http://localhost:8080/ai/vision/describe \
-F "image=@/path/to/image.jpg" \
-F "prompt=Describe the scene."
```

> ⚠️ **Note:** The first run will take longer because the **LLM models** must be loaded into memory.  
> Subsequent requests will be much faster once the model is already loaded.
