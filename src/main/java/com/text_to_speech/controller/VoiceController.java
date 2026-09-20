package com.text_to_speech.controller;

import com.text_to_speech.dto.VoiceResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VoiceController {

    @GetMapping("/voices")
    public List<VoiceResponse> getVoices() {

        return List.of(

                // ==========================================
                // SARVAM - ENGLISH
                // ==========================================

                new VoiceResponse(
                        "en-IN",
                        "shubh",
                        "Male",
                        "Sarvam"
                ),

                new VoiceResponse(
                        "en-IN",
                        "priya",
                        "Female",
                        "Sarvam"
                ),

                // ==========================================
                // SARVAM - HINDI
                // ==========================================

                new VoiceResponse(
                        "hi-IN",
                        "shubh",
                        "Male",
                        "Sarvam"
                ),

                new VoiceResponse(
                        "hi-IN",
                        "priya",
                        "Female",
                        "Sarvam"
                ),

                // ==========================================
                // SARVAM - GUJARATI
                // ==========================================

                new VoiceResponse(
                        "gu-IN",
                        "ratan",
                        "Male",
                        "Sarvam"
                ),

                new VoiceResponse(
                        "gu-IN",
                        "priya",
                        "Female",
                        "Sarvam"
                ),

                // ==========================================
                // SARVAM - MARATHI
                // ==========================================

                new VoiceResponse(
                        "mr-IN",
                        "ratan",
                        "Male",
                        "Sarvam"
                ),

                new VoiceResponse(
                        "mr-IN",
                        "priya",
                        "Female",
                        "Sarvam"
                ),

                // ==========================================
                // DEEPGRAM - SPANISH
                // ==========================================

                new VoiceResponse(
                        "es",
                        "aura-2-celeste-es",
                        "Female",
                        "Deepgram"
                ),

                new VoiceResponse(
                        "es",
                        "aura-2-javier-es",
                        "Male",
                        "Deepgram"
                ),

                // ==========================================
                // DEEPGRAM - FRENCH
                // ==========================================

                new VoiceResponse(
                        "fr",
                        "aura-2-agathe-fr",
                        "Female",
                        "Deepgram"
                ),

                new VoiceResponse(
                        "fr",
                        "aura-2-hector-fr",
                        "Male",
                        "Deepgram"
                ),

                // ==========================================
                // DEEPGRAM - GERMAN
                // ==========================================

                new VoiceResponse(
                        "de",
                        "aura-2-julius-de",
                        "Male",
                        "Deepgram"
                ),

                new VoiceResponse(
                        "de",
                        "aura-2-viktoria-de",
                        "Female",
                        "Deepgram"
                )
        );
    }
}