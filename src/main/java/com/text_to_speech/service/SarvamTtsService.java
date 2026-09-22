package com.text_to_speech.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class SarvamTtsService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${sarvam.api.key}")
    private String sarvamApiKey;

    @Value("${sarvam.api.url}")
    private String sarvamApiUrl;

    public SarvamTtsService() {
        this.restClient = RestClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }

    public byte[] synthesizeSpeech(
            String text,
            String language,
            String voice,
            String format
    ) {

        // ==========================================
        // VALIDATION
        // ==========================================

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "Text is required for speech synthesis."
            );
        }

        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException(
                    "Language is required for speech synthesis."
            );
        }

        if (voice == null || voice.isBlank()) {
            throw new IllegalArgumentException(
                    "Voice is required for speech synthesis."
            );
        }

        if (format == null || format.isBlank()) {
            throw new IllegalArgumentException(
                    "Audio format is required."
            );
        }

        // ==========================================
        // REQUEST BODY
        // ==========================================

        Map<String, Object> requestBody = new HashMap<>();

        requestBody.put(
                "text",
                text
        );

        requestBody.put(
                "language_code",
                language
        );

        requestBody.put(
                "model",
                "bulbul:v3"
        );

        requestBody.put(
                "speaker",
                voice
        );

        requestBody.put(
                "output_audio_codec",
                getSarvamCodec(format)
        );

        // ==========================================
        // CALL SARVAM
        // ==========================================

        String response = restClient.post()
                .uri(sarvamApiUrl)
                .header(
                        "api-subscription-key",
                        sarvamApiKey
                )
                .contentType(
                        MediaType.APPLICATION_JSON
                )
                .accept(
                        MediaType.APPLICATION_JSON
                )
                .body(requestBody)
                .retrieve()
                .body(String.class);

        // ==========================================
        // PROCESS SARVAM JSON RESPONSE
        // ==========================================

        try {

            if (response == null || response.isBlank()) {
                throw new IllegalStateException(
                        "Sarvam returned an empty response."
                );
            }

            JsonNode root =
                    objectMapper.readTree(response);

            JsonNode audios =
                    root.get("audios");

            if (audios == null
                    || !audios.isArray()
                    || audios.isEmpty()) {

                throw new IllegalStateException(
                        "Sarvam response does not contain audio."
                );
            }

            JsonNode firstAudio =
                    audios.get(0);

            if (firstAudio == null
                    || firstAudio.isNull()
                    || firstAudio.asText().isBlank()) {

                throw new IllegalStateException(
                        "Sarvam returned empty audio data."
                );
            }

            String base64Audio =
                    firstAudio.asText();

            // ==========================================
            // BASE64 → AUDIO BYTES
            // ==========================================

            byte[] audio =
                    Base64.getDecoder()
                            .decode(base64Audio);

            if (audio.length == 0) {

                throw new IllegalStateException(
                        "Decoded Sarvam audio is empty."
                );
            }

            return audio;

        } catch (IllegalArgumentException e) {

            throw new IllegalStateException(
                    "Sarvam returned invalid Base64 audio.",
                    e
            );

        } catch (IllegalStateException e) {

            throw e;

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to process Sarvam response.",
                    e
            );
        }
    }

    // ==========================================
    // SARVAM CODEC
    // ==========================================

    private String getSarvamCodec(String format) {

        return switch (
                format.trim().toLowerCase()
                ) {

            case "mp3" -> "mp3";

            case "wav" -> "wav";

            case "ogg" -> "opus";

            default -> throw new IllegalArgumentException(
                    "Supported audio formats are MP3, WAV and OGG."
            );
        };
    }
}