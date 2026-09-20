package com.text_to_speech.dto;

public class VoiceResponse {

    private String language;
    private String voice;
    private String gender;
    private String provider;

    public VoiceResponse() {
    }

    public VoiceResponse(
            String language,
            String voice,
            String gender,
            String provider
    ) {
        this.language = language;
        this.voice = voice;
        this.gender = gender;
        this.provider = provider;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}