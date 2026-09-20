//package com.text_to_speech.controller;
//
//import com.text_to_speech.dto.TranslationRequest;
//import com.text_to_speech.dto.TranslationResponse;
//import com.text_to_speech.service.TranslationService;
//import jakarta.validation.Valid;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//public class TranslationController {
//
//    private final TranslationService translationService;
//
//    public TranslationController(
//            TranslationService translationService
//    ) {
//        this.translationService = translationService;
//    }
//
//    @PostMapping("/translate")
//    public TranslationResponse translate(
//            @Valid @RequestBody TranslationRequest request
//    ) {
//
//        String translatedText =
//                translationService.translate(
//                        request.getText(),
//                        request.getSourceLanguage(),
//                        request.getTargetLanguage()
//                );
//
//        return new TranslationResponse(
//                true,
//                translatedText,
//                "Translation successful."
//        );
//    }
//}