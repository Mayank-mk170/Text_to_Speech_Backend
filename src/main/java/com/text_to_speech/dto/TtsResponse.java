package com.text_to_speech.dto;

public class TtsResponse {

    private boolean success;
    private String message;
    private String audioUrl;

    public TtsResponse() {
    }

    public TtsResponse(
            boolean success,
            String message,
            String audioUrl
    ) {
        this.success = success;
        this.message = message;
        this.audioUrl = audioUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}