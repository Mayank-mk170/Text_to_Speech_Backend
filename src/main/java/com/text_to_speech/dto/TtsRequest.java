package com.text_to_speech.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TtsRequest {

    @NotBlank(message = "Text is required")
    @Size(
            min = 2,
            max = 500,
            message = "Text must be between 2 and 500 characters"
    )
    private String text;

    @NotBlank(message = "Language is required")
    private String language;

    @NotBlank(message = "Voice is required")
    private String voice;

    @NotBlank(message = "Audio format is required")
    private String format;

    public TtsRequest() {
    }

    public TtsRequest(
            String text,
            String language,
            String voice,
            String format
    ) {
        this.text = text;
        this.language = language;
        this.voice = voice;
        this.format = format;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}