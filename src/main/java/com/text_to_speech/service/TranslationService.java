package com.text_to_speech.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;

@Service
public class TranslationService {

    private final TranslateClient translateClient;

    public TranslationService(TranslateClient translateClient) {
        this.translateClient = translateClient;
    }

    public String translateToLanguage(
            String text,
            String targetLanguage
    ) {

        // ------------------------------------------
        // Validate text
        // ------------------------------------------

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "Text is required for translation."
            );
        }

        // ------------------------------------------
        // Validate target language
        // ------------------------------------------

        if (targetLanguage == null || targetLanguage.isBlank()) {
            throw new IllegalArgumentException(
                    "Target language is required."
            );
        }

        // Convert frontend language code
        // to Amazon Translate language code
        String targetCode =
                getTargetLanguageCode(targetLanguage);

        // ------------------------------------------
        // Create Amazon Translate request
        // ------------------------------------------

        TranslateTextRequest request =
                TranslateTextRequest.builder()
                        .text(text)
                        .sourceLanguageCode("auto")
                        .targetLanguageCode(targetCode)
                        .build();

        // ------------------------------------------
        // Call Amazon Translate
        // ------------------------------------------

        TranslateTextResponse response =
                translateClient.translateText(request);

        // ------------------------------------------
        // Get translated text
        // ------------------------------------------

        String translatedText =
                response.translatedText();

        if (translatedText == null
                || translatedText.isBlank()) {

            throw new IllegalStateException(
                    "Amazon Translate returned empty text."
            );
        }

        return translatedText;
    }

    // ==================================================
    // TARGET LANGUAGE MAPPING
    // ==================================================

    private String getTargetLanguageCode(
            String language
    ) {

        return switch (language.trim()) {

            // English
            case "en-IN", "en" -> "en";

            // Hindi
            case "hi-IN", "hi" -> "hi";

            // Gujarati
            case "gu-IN", "gu" -> "gu";

            // Marathi
            case "mr-IN", "mr" -> "mr";

            // Spanish
            case "es", "es-ES" -> "es";

            // French
            case "fr", "fr-FR" -> "fr";

            // German
            case "de", "de-DE" -> "de";

            default -> throw new IllegalArgumentException(
                    "Unsupported translation language: "
                            + language
            );
        };
    }
}