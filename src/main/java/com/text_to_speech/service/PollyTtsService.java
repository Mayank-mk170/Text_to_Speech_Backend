package com.text_to_speech.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.OutputFormat;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest;
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechResponse;
import software.amazon.awssdk.services.polly.model.VoiceId;

@Service
public class PollyTtsService {

    private final PollyClient pollyClient;

    public PollyTtsService(PollyClient pollyClient) {
        this.pollyClient = pollyClient;
    }

    public byte[] synthesizeSpeech(
            String text,
            String language,
            String voice,
            String format
    ) {

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "Text is required for speech synthesis."
            );
        }

        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException(
                    "Language is required."
            );
        }

        if (voice == null || voice.isBlank()) {
            throw new IllegalArgumentException(
                    "Voice is required."
            );
        }

        if (format == null || format.isBlank()) {
            throw new IllegalArgumentException(
                    "Audio format is required."
            );
        }

        String pollyLanguageCode =
                getPollyLanguageCode(language);

        OutputFormat outputFormat =
                getOutputFormat(format);

        SynthesizeSpeechRequest request =
                SynthesizeSpeechRequest.builder()
                        .text(text)
                        .voiceId(VoiceId.fromValue(voice))
                        .languageCode(pollyLanguageCode)
                        .outputFormat(outputFormat)
                        .build();

        ResponseBytes<SynthesizeSpeechResponse> response =
                pollyClient.synthesizeSpeechAsBytes(request);

        byte[] audio = response.asByteArray();

        if (audio == null || audio.length == 0) {
            throw new IllegalStateException(
                    "Amazon Polly returned empty audio."
            );
        }

        return audio;
    }

    private String getPollyLanguageCode(String language) {

        return switch (language.trim()) {

            case "en-IN", "en" -> "en-US";

            case "es", "es-ES" -> "es-ES";

            case "fr", "fr-FR" -> "fr-FR";

            case "de", "de-DE" -> "de-DE";

            default -> throw new IllegalArgumentException(
                    "Amazon Polly does not support language: "
                            + language
            );
        };
    }

    private OutputFormat getOutputFormat(String format) {

        return switch (format.trim().toLowerCase()) {

            case "mp3" -> OutputFormat.MP3;

            case "ogg" -> OutputFormat.OGG_VORBIS;

            case "wav" -> OutputFormat.PCM;

            default -> throw new IllegalArgumentException(
                    "Supported audio formats are MP3, WAV and OGG."
            );
        };
    }
}