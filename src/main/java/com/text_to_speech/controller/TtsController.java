package com.text_to_speech.controller;

import com.text_to_speech.dto.TtsRequest;
import com.text_to_speech.service.TtsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TtsController {

    private final TtsService ttsService;

    public TtsController(TtsService ttsService) {
        this.ttsService = ttsService;
    }

    @PostMapping(
            value = "/tts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {
                    "audio/mpeg",
                    "audio/wav",
                    "audio/ogg"
            }
    )
    public ResponseEntity<byte[]> generateSpeech(
            @Valid @RequestBody TtsRequest request
    ) {

        byte[] audio =
                ttsService.generateSpeech(request);

        String format =
                request.getFormat()
                        .trim()
                        .toLowerCase();

        String contentType;
        String extension;

        switch (format) {

            case "wav":
                contentType = "audio/wav";
                extension = "wav";
                break;

            case "ogg":
                contentType = "audio/ogg";
                extension = "ogg";
                break;

            case "mp3":
                contentType = "audio/mpeg";
                extension = "mp3";
                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported audio format: " + format
                );
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"generated-speech."
                                + extension
                                + "\""
                )
                .contentType(
                        MediaType.parseMediaType(contentType)
                )
                .body(audio);
    }
}