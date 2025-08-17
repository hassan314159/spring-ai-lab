# simple-summarizer

Simple module demonstrating a direct call to [Hugging Face](https://huggingface.co/learn/cookbook/en/enterprise_hub_serverless_inference_api)
(no Spring AI yet). Future modules will migrate this approach to **Spring AI** abstractions.

### Environment Variables

This module requires a Hugging Face API key to be provided via environment variable:

- `HF_API_KEY` — your Hugging Face API token

Example (Linux/macOS):

```bash
export HF_API_KEY=hf_your_api_token_here
```
Or in Windows PowerShell:

```bash
$env:HF_API_KEY="hf_your_api_token_here"
```

The key is injected into the app through application.properties:
```
huggingface.apiKey=${HF_API_KEY:}
```

## Build & Run

```bash
# From repo root
mvn -pl simple-summarizer -am clean package

# Run
mvn -pl simple-summarizer -am package && java -jar target/simple-summarizer-1.0.0.jar
```

## REST Endpoints

## Example Usage
### Request

Send a stream of texts (newline-delimited JSON, application/x-ndjson) to the summarizer endpoint:
```bash
curl --location 'http://localhost:8080/api/summarize' \
--header 'Content-Type: application/x-ndjson' \
--data '
"<<< first input text here >>>"
"<<< second input text here >>>"
"<<< more texts if needed >>>"
'
```

- Endpoint: POST /api/summarize
- Consumes: application/x-ndjson
- Produces: application/x-ndjson (each line = one summary)

### Response

Example output:
```bash
"<<< summary of first input >>>"
"<<< summary of second input >>>"
```
## Notes

- This module calls Hugging Face APIs directly.
- When upgrading to Spring AI, extract the API call into a `@Service` and inject a Spring AI client configured in `application.yml`.
