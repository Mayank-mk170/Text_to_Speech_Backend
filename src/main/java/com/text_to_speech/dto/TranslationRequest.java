//package com.text_to_speech.dto;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
//
//public class TranslationRequest {
//
//    @NotBlank(message = "Text is required")
//    @Size(min = 1, max = 500,
//            message = "Text must be between 1 and 500 characters")
//    private String text;
//
//    @NotBlank(message = "Source language is required")
//    private String sourceLanguage;
//
//    @NotBlank(message = "Target language is required")
//    private String targetLanguage;
//
//    public TranslationRequest() {
//    }
//
//    public TranslationRequest(
//            String text,
//            String sourceLanguage,
//            String targetLanguage
//    ) {
//        this.text = text;
//        this.sourceLanguage = sourceLanguage;
//        this.targetLanguage = targetLanguage;
//    }
//
//    public String getText() {
//        return text;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }
//
//    public String getSourceLanguage() {
//        return sourceLanguage;
//    }
//
//    public void setSourceLanguage(String sourceLanguage) {
//        this.sourceLanguage = sourceLanguage;
//    }
//
//    public String getTargetLanguage() {
//        return targetLanguage;
//    }
//
//    public void setTargetLanguage(String targetLanguage) {
//        this.targetLanguage = targetLanguage;
//    }
//}