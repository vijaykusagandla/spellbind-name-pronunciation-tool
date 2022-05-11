package com.spellbind.npt.controller;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.cognitiveservices.speech.CancellationReason;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import com.microsoft.cognitiveservices.speech.audio.AudioOutputStream;
import com.microsoft.cognitiveservices.speech.audio.PullAudioOutputStream;
import com.spellbind.npt.exception.NamePronunciationToolException;
import com.spellbind.npt.model.InputText;

@RestController
public class SpeechSynthesisController {

	Logger log = LoggerFactory.getLogger(AudioFileController.class);

	@GetMapping(path = "/speechSynthesis", consumes = "application/json")
	public ResponseEntity<byte[]> speechSynthesis(@RequestBody InputText request) {

		SpeechConfig speechConfig = SpeechConfig.fromSubscription("e80ee60bd3e440e2a189989ae3ed1327", "eastus");
		speechConfig.setSpeechSynthesisVoiceName("en-US-JennyNeural"); // JennyNeural JennyMultiLingualNeural

		 // Creates an audio out stream.
		PullAudioOutputStream stream = AudioOutputStream.createPullStream();

		 // Creates a speech synthesizer using audio stream output.
		AudioConfig streamConfig = AudioConfig.fromStreamOutput(stream);

		SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(speechConfig, streamConfig);

		// Get text from the console and synthesize to the default speaker.
		SpeechSynthesisResult result = null;
		byte[] audioData = null;
		try {
			result = speechSynthesizer.SpeakTextAsync(request.getText()).get();
			audioData = result.getAudioData();
		} catch (InterruptedException e) {
			throw new NamePronunciationToolException("InterruptedException occured" + e);
		} catch (ExecutionException e) {
			throw new NamePronunciationToolException("ExecutionException occured" + e);
		} finally {
			result.close();
			speechSynthesizer.close();
			streamConfig.close();
			stream.close();

		}
		// Checks result.
		if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
			log.info("Speech synthesized for text [" + request.getText()
					+ "], and the audio was written to output stream.");
		} else if (result.getReason() == ResultReason.Canceled) {
			SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails.fromResult(result);
			log.info("CANCELED: Reason=" + cancellation.getReason());

			if (cancellation.getReason() == CancellationReason.Error) {
				log.error("CANCELED: ErrorCode=" + cancellation.getErrorCode());
				log.error("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
				log.error("CANCELED: Did you update the subscription info?");
			}
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ResponseEntity.ok().headers(headers).contentLength(audioData.length)
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(audioData);
	}

}
