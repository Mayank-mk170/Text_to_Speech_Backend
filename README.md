Text-to-Speech Backend

A Java Spring Boot REST API that receives text-to-speech requests, integrates with external TTS providers, validates requests, handles failures, and returns generated audio to the React frontend.

Responsibilities

Receive TTS requests

Validate text, language, voice, and audio format

Select/configure the TTS provider

Generate speech

Process provider responses

Return audio to the frontend

Provide health and voices endpoints

Handle provider/network/server errors

Keep provider API credentials on the backend

Tech Stack

Java

Spring Boot

Spring Web

Spring Boot Validation

Jackson

Maven

REST APIs

Architecture

React Frontend
|
| REST API
v
Controller Layer
|
v
Service Layer
|
v
TTS Integration
|
+--------------------+
|                    |
v                    v
Sarvam               Deepgram
|                    |
+---------+----------+
|
v
Audio Response
|
v
Spring Boot Backend
|
v
React Frontend

Project Structure

com.text_to_speech
├── config/
│   └── WebConfig.java
├── controller/
│   ├── HealthController.java
│   └── TtsController.java
├── dto/
│   ├── TtsRequest.java
│   └── TtsResponse.java
├── exception/
│   └── GlobalExceptionHandler.java
├── service/
│   └── TtsService.java
└── TextToSpeechApplication.java

API Endpoints

Health Check

GET /api/health

Example response:

Text-to-Speech Backend is running

Voices

GET /api/voices

Returns the available voice information exposed by the application.

Generate Speech

POST /api/tts
Content-Type: application/json

Example request:

{
"text": "Hello, welcome to my Text-to-Speech application.",
"language": "en-IN",
"voice": "female",
"format": "mp3"
}

Supported formats:

mp3
wav
ogg

The endpoint returns generated audio as binary data.

Validation

The backend validates:

Text is required

Text must contain at least 2 characters

Text maximum length is 500 characters

Language must be supported/configured

Voice must be provided and valid for the selected language/provider configuration

Audio format must be MP3, WAV, or OGG

Invalid requests are rejected with an appropriate HTTP status

TTS Provider Integration

External provider credentials remain on the backend. Provider selection and request construction are handled by the service layer.

The current configuration uses the following provider credential variables:

DEEPGRAM_API_KEY
SARVAM_API_KEY

Do not commit real API keys to GitHub.

Configuration

Example application.properties structure:

spring.application.name=text-to-speech
server.port=8080

deepgram.api.key=${DEEPGRAM_API_KEY}
deepgram.api.url=https://api.deepgram.com/v1/speak

sarvam.api.key=${SARVAM_API_KEY}
sarvam.api.url=https://api.sarvam.ai/text-to-speech

Set the required environment variables on the machine or deployment platform.

Local Setup

1. Clone

git clone https://github.com/Mayank-mk170/Text_to_Speech_Backend.git
cd Text_to_Speech_Backend

2. Configure environment variables

DEEPGRAM_API_KEY=your_deepgram_api_key
SARVAM_API_KEY=your_sarvam_api_key

Use your local environment/deployment secrets rather than committing these values.

3. Run with Maven

mvn spring-boot:run

Or with the Maven wrapper on Windows:

mvnw.cmd spring-boot:run

Backend URL:

http://localhost:8080

Error Handling

The backend provides centralized handling for:

Validation errors

Invalid request values

Unsupported format

TTS provider authentication errors

Provider rate limits

Provider availability errors

Network/connection failures

Unexpected server errors

Common status codes used by the application include:

200 OK
400 Bad Request
500 Internal Server Error
503 Service Unavailable

CORS

For local development, the backend allows the React development frontend:

http://localhost:5173

For production, update CORS to the deployed frontend origin.

Backend Flow

POST /api/tts
|
v
TtsController
|
v
TtsService
|
+----> Provider selection
|
+----> Provider request
|
v
TTS Provider API
|
v
Generated Audio
|
v
TtsService
|
v
TtsController
|
v
React Frontend

API Testing with Postman

Health:

GET http://localhost:8080/api/health

Voices:

GET http://localhost:8080/api/voices

TTS:

POST http://localhost:8080/api/tts

Body:

{
"text": "Hello world.",
"language": "en-IN",
"voice": "female",
"format": "mp3"
}

Security

Provider API keys are not exposed to the React frontend

Secrets are provided through environment variables

User input is validated on the backend

CORS is explicitly configured

Provider errors are handled centrally

Production deployments should use HTTPS

Testing Checklist

Health endpoint

Voices endpoint

Successful speech generation

Empty text

Text over 500 characters

Invalid language

Invalid voice

Unsupported format

Provider authentication failure

Provider unavailable

Network failure

CORS

Repository

https://github.com/Mayank-mk170/Text_to_Speech_Backend

Related Frontend

https://github.com/Mayank-mk170/Text_to_Speech_Frontend

License

Educational/project demonstration use.