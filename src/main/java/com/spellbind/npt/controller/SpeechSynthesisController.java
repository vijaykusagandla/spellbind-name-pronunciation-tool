package com.spellbind.npt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spellbind.npt.service.SpeechSynthesisService;

@CrossOrigin()
@RestController
public class SpeechSynthesisController {

	@Autowired
	SpeechSynthesisService speechSynthesisService;

	Logger log = LoggerFactory.getLogger(SpeechSynthesisController.class);

	@GetMapping(path = "/speechSynthesis")
	public ResponseEntity<byte[]> getSpeechSynthesis(@RequestParam String text) {
		log.info("SpeechSynthesisController :: getSpeechSynthesis - begin");
		byte[] speechSynthesisByteFormat = speechSynthesisService.getSpeechSynthesis(text);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		log.info("SpeechSynthesisController :: getSpeechSynthesis - end");
		return ResponseEntity.ok().headers(headers).contentLength(speechSynthesisByteFormat.length)
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(speechSynthesisByteFormat);
	}

}
