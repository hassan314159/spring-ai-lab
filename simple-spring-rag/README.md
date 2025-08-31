# Simple Spring AI RAG with Ollama

This project is a Spring Boot REST controller that demonstrates a simple Retrieval-Augmented Generation (RAG) pipeline using Spring AI
, a local Ollama LLM runtime, and the Mistral model.

**Features**

- Load documents from a URL.
- Chunk documents into embeddings using TokenTextSplitter.
- Store and retrieve embeddings from a configured In-Memory VectorStore.
- Perform similarity search to retrieve relevant context for a user query.
- Call a local Ollama instance running Mistral for answering questions with context


## API Endpoints

- Load Endpoint: `POST /api/chat-rag/load?url=...`
- Ask Endpoint: `POST /api/chat-rag/ask?q=...`

###  Load a document into the VectorStore

```
POST /api/chat-rag/load?url={url}&onlyCode={false}
```

#### Parameters:
- url (string, required): The webpage URL to fetch and index.
- onlyCode (boolean, optional, default=false): If true, only <pre> and <code> blocks are extracted.

###  Load a document into the VectorStore

```
POST /api/chat-rag/ask?q={question}
```
#### Parameters:
- q (string, required): The user’s natural language query.


The included `docker-compose.yml` (at the project root) runs **Ollama** locally
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
You should see your models (e.g., mistral) listed.

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

```
curl -X POST "http://localhost:8080/api/chat-rag/load?url=https://en.wikipedia.org/wiki/Spring_Framework"
curl -X POST "http://localhost:8080/api/chat-rag/ask?q=What is Spring Framework?"
```


> ⚠️ **Note:** The first run will take longer because the **LLM models** must be loaded into memory.  
> Subsequent requests will be much faster once the model is already loaded.
