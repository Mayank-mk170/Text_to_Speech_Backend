package com.text_to_speech.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class VoiceValidationService {

    /*
     * Language -> allowed voices
     *
     * The backend validates the voice BEFORE sending
     * anything to Amazon Polly or Sarvam.
     */
    private static final Map<String, Set<String>> LANGUAGE_VOICES = Map.of(

            // ==========================================
            // ENGLISH - AMAZON POLLY
            // ==========================================
            "en-IN", Set.of(
                    "Joey",
                    "Joanna"
            ),

            // ==========================================
            // HINDI - SARVAM
            // ==========================================
            "hi-IN", Set.of(
                    "shubh",
                    "priya"
            ),

            // ==========================================
            // GUJARATI - SARVAM
            // ==========================================
            "gu-IN", Set.of(
                    "ratan",
                    "priya"
            ),

            // ==========================================
            // MARATHI - SARVAM
            // ==========================================
            "mr-IN", Set.of(
                    "ratan",
                    "priya"
            ),

            // ==========================================
            // SPANISH - AMAZON POLLY
            // ==========================================
            "es", Set.of(
                    "Enrique",
                    "Lucia"
            ),

            // ==========================================
            // FRENCH - AMAZON POLLY
            // ==========================================
            "fr", Set.of(
                    "Mathieu",
                    "Celine"
            ),

            // ==========================================
            // GERMAN - AMAZON POLLY
            // ==========================================
            "de", Set.of(
                    "Hans",
                    "Marlene"
            )
    );

    public void validateLanguageAndVoice(
            String language,
            String voice
    ) {

        // ------------------------------------------
        // Language validation
        // ------------------------------------------

        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException(
                    "Language is required."
            );
        }

        // ------------------------------------------
        // Voice validation
        // ------------------------------------------

        if (voice == null || voice.isBlank()) {
            throw new IllegalArgumentException(
                    "Voice is required."
            );
        }

        String normalizedLanguage =
                language.trim();

        String normalizedVoice =
                voice.trim();

        // ------------------------------------------
        // Check language exists
        // ------------------------------------------

        if (!LANGUAGE_VOICES.containsKey(
                normalizedLanguage
        )) {

            throw new IllegalArgumentException(
                    "Unsupported language: "
                            + normalizedLanguage
            );
        }

        // ------------------------------------------
        // Get voices for selected language
        // ------------------------------------------

        Set<String> allowedVoices =
                LANGUAGE_VOICES.get(
                        normalizedLanguage
                );

        // ------------------------------------------
        // Check voice belongs to language
        // ------------------------------------------

        if (!allowedVoices.contains(
                normalizedVoice
        )) {

            throw new IllegalArgumentException(
                    "Voice '"
                            + normalizedVoice
                            + "' is not available for language '"
                            + normalizedLanguage
                            + "'."
            );
        }
    }
}