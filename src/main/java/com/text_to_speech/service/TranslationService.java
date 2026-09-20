//package com.text_to_speech.service;
//
//import com.google.cloud.translate.v3.LocationName;
//import com.google.cloud.translate.v3.TranslateTextRequest;
//import com.google.cloud.translate.v3.TranslateTextResponse;
//import com.google.cloud.translate.v3.Translation;
//import com.google.cloud.translate.v3.TranslationServiceClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TranslationService {
//
//    @Value("${google.cloud.project-id}")
//    private String projectId;
//
//    public String translate(
//            String text,
//            String sourceLanguage,
//            String targetLanguage
//    ) {
//
//        // No translation needed
//        if (sourceLanguage.equalsIgnoreCase(targetLanguage)) {
//            return text;
//        }
//
//        try (TranslationServiceClient client =
//                     TranslationServiceClient.create()) {
//
//            LocationName parent = LocationName.of(
//                    projectId,
//                    "global"
//            );
//
//            TranslateTextRequest request =
//                    TranslateTextRequest.newBuilder()
//                            .setParent(parent.toString())
//                            .setMimeType("text/plain")
//                            .setSourceLanguageCode(sourceLanguage)
//                            .setTargetLanguageCode(targetLanguage)
//                            .addContents(text)
//                            .build();
//
//            TranslateTextResponse response =
//                    client.translateText(request);
//
//            if (response.getTranslationsCount() == 0) {
//                throw new IllegalStateException(
//                        "Google returned no translation."
//                );
//            }
//
//            Translation translation =
//                    response.getTranslations(0);
//
//            return translation.getTranslatedText();
//
//        } catch (Exception e) {
//
//            throw new IllegalStateException(
//                    "Translation failed: " + e.getMessage(),
//                    e
//            );
//        }
//    }
//}