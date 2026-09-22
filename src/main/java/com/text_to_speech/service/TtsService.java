////
////
////package com.text_to_speech.service;
////
////import com.fasterxml.jackson.databind.JsonNode;
////import com.fasterxml.jackson.databind.ObjectMapper;
////import com.text_to_speech.dto.TtsRequest;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.http.HttpHeaders;
////import org.springframework.http.MediaType;
////import org.springframework.stereotype.Service;
////import org.springframework.web.client.RestClient;
////
////import java.util.Base64;
////import java.util.HashMap;
////import java.util.Map;
////
////@Service
////public class TtsService {
////
////    private final RestClient restClient;
////    private final ObjectMapper objectMapper;
////
////    // =========================================================
////    // DEEPGRAM CONFIGURATION
////    // =========================================================
////
////    @Value("${deepgram.api.key}")
////    private String deepgramApiKey;
////
////    @Value("${deepgram.api.url}")
////    private String deepgramApiUrl;
////
////    // =========================================================
////    // SARVAM CONFIGURATION
////    // =========================================================
////
////    @Value("${sarvam.api.key}")
////    private String sarvamApiKey;
////
////    @Value("${sarvam.api.url}")
////    private String sarvamApiUrl;
////
////    // =========================================================
////    // CONSTRUCTOR
////    // =========================================================
////
////    public TtsService() {
////        this.restClient = RestClient.builder().build();
////        this.objectMapper = new ObjectMapper();
////    }
////
////    // =========================================================
////    // MAIN TTS METHOD
////    // =========================================================
////
////    public byte[] generateSpeech(TtsRequest request) {
////
////        validateFormat(request.getFormat());
////
////        String language = request.getLanguage();
////
////        /*
////         * Sarvam:
////         * English  -> en-IN
////         * Hindi    -> hi-IN
////         * Gujarati -> gu-IN
////         * Marathi  -> mr-IN
////         *
////         * Deepgram:
////         * Spanish -> es
////         * French  -> fr
////         * German  -> de
////         */
////
////        if (isSarvamLanguage(language)) {
////            return generateWithSarvam(request);
////        }
////
////        return generateWithDeepgram(request);
////    }
////
////    // =========================================================
////    // SARVAM TEXT TO SPEECH
////    // =========================================================
////
////    private byte[] generateWithSarvam(TtsRequest request) {
////
////        Map<String, Object> requestBody = new HashMap<>();
////
////        requestBody.put("text", request.getText());
////        requestBody.put("language_code", request.getLanguage());
////        requestBody.put("model", "bulbul:v3");
////        requestBody.put("speaker", request.getVoice());
////
////        requestBody.put(
////                "output_audio_codec",
////                getSarvamCodec(request.getFormat())
////        );
////
////        String response = restClient.post()
////                .uri(sarvamApiUrl)
////                .header(
////                        "api-subscription-key",
////                        sarvamApiKey
////                )
////                .contentType(MediaType.APPLICATION_JSON)
////                .accept(MediaType.APPLICATION_JSON)
////                .body(requestBody)
////                .retrieve()
////                .body(String.class);
////
////        try {
////
////            JsonNode root =
////                    objectMapper.readTree(response);
////
////            JsonNode audios =
////                    root.get("audios");
////
////            if (audios == null
////                    || !audios.isArray()
////                    || audios.isEmpty()) {
////
////                throw new IllegalStateException(
////                        "Sarvam returned no audio."
////                );
////            }
////
////            String base64Audio =
////                    audios.get(0).asText();
////
////            if (base64Audio == null
////                    || base64Audio.isBlank()) {
////
////                throw new IllegalStateException(
////                        "Sarvam returned empty audio."
////                );
////            }
////
////            return Base64.getDecoder()
////                    .decode(base64Audio);
////
////        } catch (Exception e) {
////
////            throw new IllegalStateException(
////                    "Failed to process Sarvam response.",
////                    e
////            );
////        }
////    }
////
////    // =========================================================
////    // DEEPGRAM TEXT TO SPEECH
////    // =========================================================
////
////    private byte[] generateWithDeepgram(TtsRequest request) {
////
////        String format =
////                request.getFormat().toLowerCase();
////
////        String url;
////
////        String acceptType;
////
////        switch (format) {
////
////            case "wav":
////
////                url = deepgramApiUrl
////                        + "?model=" + request.getVoice()
////                        + "&encoding=linear16"
////                        + "&container=wav";
////
////                acceptType = "audio/wav";
////
////                break;
////
////            case "ogg":
////
////                url = deepgramApiUrl
////                        + "?model=" + request.getVoice()
////                        + "&encoding=opus";
////
////                /*
////                 * Deepgram returns Opus here.
////                 * The response is handled as binary audio.
////                 */
////                acceptType = "audio/ogg";
////
////                break;
////
////            case "mp3":
////
////                url = deepgramApiUrl
////                        + "?model=" + request.getVoice()
////                        + "&encoding=mp3";
////
////                acceptType = "audio/mpeg";
////
////                break;
////
////            default:
////
////                throw new IllegalArgumentException(
////                        "Unsupported audio format: "
////                                + request.getFormat()
////                );
////        }
////
////        byte[] audio = restClient.post()
////                .uri(url)
////                .header(
////                        HttpHeaders.AUTHORIZATION,
////                        "Token " + deepgramApiKey
////                )
////                .contentType(MediaType.APPLICATION_JSON)
////                .accept(
////                        MediaType.parseMediaType(
////                                acceptType
////                        )
////                )
////                .body("""
////                        {
////                          "text": %s
////                        }
////                        """.formatted(
////                        toJsonString(
////                                request.getText()
////                        )
////                ))
////                .retrieve()
////                .body(byte[].class);
////
////        if (audio == null
////                || audio.length == 0) {
////
////            throw new IllegalStateException(
////                    "Deepgram returned empty audio."
////            );
////        }
////
////        return audio;
////    }
////
////    // =========================================================
////    // SARVAM AUDIO FORMAT
////    // =========================================================
////
////    private String getSarvamCodec(String format) {
////
////        return switch (
////                format.toLowerCase()
////                ) {
////
////            case "mp3" -> "mp3";
////
////            case "wav" -> "wav";
////
////            case "ogg" -> "opus";
////
////            default -> throw new IllegalArgumentException(
////                    "Unsupported audio format: "
////                            + format
////            );
////        };
////    }
////
////    // =========================================================
////    // FORMAT VALIDATION
////    // =========================================================
////
////    private void validateFormat(String format) {
////
////        if (format == null
////                || format.isBlank()) {
////
////            throw new IllegalArgumentException(
////                    "Audio format is required."
////            );
////        }
////
////        String normalized =
////                format.toLowerCase();
////
////        if (!normalized.equals("mp3")
////                && !normalized.equals("wav")
////                && !normalized.equals("ogg")) {
////
////            throw new IllegalArgumentException(
////                    "Supported audio formats are MP3, WAV and OGG."
////            );
////        }
////    }
////
////    // =========================================================
////    // SARVAM LANGUAGE CHECK
////    // =========================================================
////
////    private boolean isSarvamLanguage(
////            String language
////    ) {
////
////        return "en-IN".equals(language)
////                || "hi-IN".equals(language)
////                || "gu-IN".equals(language)
////                || "mr-IN".equals(language);
////    }
////
////    // =========================================================
////    // JSON STRING ESCAPING
////    // =========================================================
////
////    private String toJsonString(String value) {
////
////        if (value == null) {
////            return "\"\"";
////        }
////
////        return "\""
////                + value
////                .replace("\\", "\\\\")
////                .replace("\"", "\\\"")
////                .replace("\n", "\\n")
////                .replace("\r", "\\r")
////                .replace("\t", "\\t")
////                + "\"";
////    }
////}
//
//
//package com.text_to_speech.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.text_to_speech.dto.TtsRequest;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestClient;
//
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class TtsService {
//
//    private final RestClient restClient;
//    private final ObjectMapper objectMapper;
//    private final VoiceValidationService voiceValidationService;
//
//    @Value("${deepgram.api.key}")
//    private String deepgramApiKey;
//
//    @Value("${deepgram.api.url}")
//    private String deepgramApiUrl;
//
//    @Value("${sarvam.api.key}")
//    private String sarvamApiKey;
//
//    @Value("${sarvam.api.url}")
//    private String sarvamApiUrl;
//
//    public TtsService(
//            VoiceValidationService voiceValidationService
//    ) {
//        this.restClient = RestClient.builder().build();
//        this.objectMapper = new ObjectMapper();
//        this.voiceValidationService = voiceValidationService;
//    }
//
//    public byte[] generateSpeech(TtsRequest request) {
//
//        // 1. Validate language + voice combination
//        voiceValidationService.validateLanguageAndVoice(
//                request.getLanguage(),
//                request.getVoice()
//        );
//
//        // 2. Validate audio format
//        validateFormat(request.getFormat());
//
//        // 3. Send request to correct provider
//        String language = request.getLanguage();
//
//        if (isSarvamLanguage(language)) {
//            return generateWithSarvam(request);
//        }
//
//        return generateWithDeepgram(request);
//    }
//
//    // ============================================================
//    // SARVAM
//    // ============================================================
//
//    private byte[] generateWithSarvam(TtsRequest request) {
//
//        Map<String, Object> requestBody = new HashMap<>();
//
//        requestBody.put(
//                "text",
//                request.getText()
//        );
//
//        requestBody.put(
//                "language_code",
//                request.getLanguage()
//        );
//
//        requestBody.put(
//                "model",
//                "bulbul:v3"
//        );
//
//        requestBody.put(
//                "speaker",
//                request.getVoice()
//        );
//
//        requestBody.put(
//                "output_audio_codec",
//                getSarvamCodec(request.getFormat())
//        );
//
//        String response = restClient.post()
//                .uri(sarvamApiUrl)
//                .header(
//                        "api-subscription-key",
//                        sarvamApiKey
//                )
//                .contentType(
//                        MediaType.APPLICATION_JSON
//                )
//                .accept(
//                        MediaType.APPLICATION_JSON
//                )
//                .body(requestBody)
//                .retrieve()
//                .body(String.class);
//
//        try {
//
//            JsonNode root =
//                    objectMapper.readTree(response);
//
//            JsonNode audios =
//                    root.get("audios");
//
//            if (audios == null
//                    || !audios.isArray()
//                    || audios.isEmpty()) {
//
//                throw new IllegalStateException(
//                        "Sarvam returned no audio."
//                );
//            }
//
//            String base64Audio =
//                    audios.get(0).asText();
//
//            if (base64Audio == null
//                    || base64Audio.isBlank()) {
//
//                throw new IllegalStateException(
//                        "Sarvam returned empty audio."
//                );
//            }
//
//            return Base64.getDecoder()
//                    .decode(base64Audio);
//
//        } catch (Exception e) {
//
//            throw new IllegalStateException(
//                    "Failed to process Sarvam response.",
//                    e
//            );
//        }
//    }
//
//    // ============================================================
//    // DEEPGRAM
//    // ============================================================
//
//    private byte[] generateWithDeepgram(
//            TtsRequest request
//    ) {
//
//        String format =
//                request.getFormat().toLowerCase();
//
//        String url;
//        String acceptType;
//
//        switch (format) {
//
//            case "wav":
//
//                url =
//                        deepgramApiUrl
//                                + "?model="
//                                + request.getVoice()
//                                + "&encoding=linear16"
//                                + "&container=wav";
//
//                acceptType = "audio/wav";
//
//                break;
//
//            case "ogg":
//
//                url =
//                        deepgramApiUrl
//                                + "?model="
//                                + request.getVoice()
//                                + "&encoding=opus";
//
//                acceptType = "audio/ogg";
//
//                break;
//
//            case "mp3":
//
//                url =
//                        deepgramApiUrl
//                                + "?model="
//                                + request.getVoice()
//                                + "&encoding=mp3";
//
//                acceptType = "audio/mpeg";
//
//                break;
//
//            default:
//
//                throw new IllegalArgumentException(
//                        "Unsupported audio format: "
//                                + request.getFormat()
//                );
//        }
//
//        byte[] audio = restClient.post()
//                .uri(url)
//                .header(
//                        HttpHeaders.AUTHORIZATION,
//                        "Token " + deepgramApiKey
//                )
//                .contentType(
//                        MediaType.APPLICATION_JSON
//                )
//                .accept(
//                        MediaType.parseMediaType(
//                                acceptType
//                        )
//                )
//                .body(
//                        """
//                        {
//                          "text": %s
//                        }
//                        """.formatted(
//                                toJsonString(
//                                        request.getText()
//                                )
//                        )
//                )
//                .retrieve()
//                .body(byte[].class);
//
//        if (audio == null
//                || audio.length == 0) {
//
//            throw new IllegalStateException(
//                    "Deepgram returned empty audio."
//            );
//        }
//
//        return audio;
//    }
//
//    // ============================================================
//    // FORMAT
//    // ============================================================
//
//    private String getSarvamCodec(
//            String format
//    ) {
//
//        return switch (
//                format.toLowerCase()
//                ) {
//
//            case "mp3" -> "mp3";
//
//            case "wav" -> "wav";
//
//            case "ogg" -> "opus";
//
//            default -> throw new IllegalArgumentException(
//                    "Supported audio formats are MP3, WAV and OGG."
//            );
//        };
//    }
//
//    private void validateFormat(
//            String format
//    ) {
//
//        if (format == null
//                || format.isBlank()) {
//
//            throw new IllegalArgumentException(
//                    "Audio format is required."
//            );
//        }
//
//        String normalized =
//                format.toLowerCase();
//
//        if (!normalized.equals("mp3")
//                && !normalized.equals("wav")
//                && !normalized.equals("ogg")) {
//
//            throw new IllegalArgumentException(
//                    "Supported audio formats are MP3, WAV and OGG."
//            );
//        }
//    }
//
//    // ============================================================
//    // PROVIDER ROUTING
//    // ============================================================
//
//    private boolean isSarvamLanguage(
//            String language
//    ) {
//
//        return "en-IN".equals(language)
//                || "hi-IN".equals(language)
//                || "gu-IN".equals(language)
//                || "mr-IN".equals(language);
//    }
//
//    // ============================================================
//    // JSON ESCAPING
//    // ============================================================
//
//    private String toJsonString(
//            String value
//    ) {
//
//        if (value == null) {
//            return "\"\"";
//        }
//
//        return "\""
//                + value
//                .replace("\\", "\\\\")
//                .replace("\"", "\\\"")
//                .replace("\n", "\\n")
//                .replace("\r", "\\r")
//                .replace("\t", "\\t")
//                + "\"";
//    }
//}
package com.text_to_speech.service;

import com.text_to_speech.dto.TtsRequest;
import org.springframework.stereotype.Service;

@Service
public class TtsService {

    private final VoiceValidationService voiceValidationService;
    private final TranslationService translationService;
    private final PollyTtsService pollyTtsService;
    private final SarvamTtsService sarvamTtsService;

    public TtsService(
            VoiceValidationService voiceValidationService,
            TranslationService translationService,
            PollyTtsService pollyTtsService,
            SarvamTtsService sarvamTtsService
    ) {
        this.voiceValidationService = voiceValidationService;
        this.translationService = translationService;
        this.pollyTtsService = pollyTtsService;
        this.sarvamTtsService = sarvamTtsService;
    }

    public byte[] generateSpeech(TtsRequest request) {

        // 1. Validate language and voice
        voiceValidationService.validateLanguageAndVoice(
                request.getLanguage(),
                request.getVoice()
        );

        // 2. Validate format
        validateFormat(request.getFormat());

        // 3. Translate text to selected language
        String translatedText =
                translationService.translateToLanguage(
                        request.getText(),
                        request.getLanguage()
                );

        // 4. Generate speech
        if (isSarvamLanguage(request.getLanguage())) {

            return sarvamTtsService.synthesizeSpeech(
                    translatedText,
                    request.getLanguage(),
                    request.getVoice(),
                    request.getFormat()
            );
        }

        return pollyTtsService.synthesizeSpeech(
                translatedText,
                request.getLanguage(),
                request.getVoice(),
                request.getFormat()
        );
    }

    private boolean isSarvamLanguage(String language) {

        return "hi-IN".equals(language)
                || "gu-IN".equals(language)
                || "mr-IN".equals(language);
    }

    private void validateFormat(String format) {

        if (format == null || format.isBlank()) {
            throw new IllegalArgumentException(
                    "Audio format is required."
            );
        }

        String normalized =
                format.trim().toLowerCase();

        if (!normalized.equals("mp3")
                && !normalized.equals("wav")
                && !normalized.equals("ogg")) {

            throw new IllegalArgumentException(
                    "Supported audio formats are MP3, WAV and OGG."
            );
        }
    }
}