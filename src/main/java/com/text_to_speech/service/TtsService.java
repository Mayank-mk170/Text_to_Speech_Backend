
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
