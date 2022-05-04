package com.spellbind.npt.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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
import com.spellbind.npt.exception.NamePronunciationToolException;
import com.spellbind.npt.model.InputData;

@RestController
public class SpeechSynthesisController {

	Logger log = LoggerFactory.getLogger(AudioFileController.class);

	@GetMapping(path = "/speechSynthesis", consumes = "application/json")
	public ResponseEntity<byte[]> speechSynthesis(@RequestBody InputData request) {

		SpeechConfig speechConfig = SpeechConfig.fromSubscription("subscriptionKey", "eastus");
		speechConfig.setSpeechSynthesisVoiceName("en-US-JennyNeural"); // JennyNeural JennyMultiLingualNeural

		String fileName = "outputaudio.wav";
		AudioConfig fileOutput = AudioConfig.fromWavFileOutput(fileName);
		byte[] file = null;

		SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(speechConfig, fileOutput);

		// Get text from the console and synthesize to the default speaker.
		SpeechSynthesisResult speechRecognitionResult = null;
		try {
			speechRecognitionResult = speechSynthesizer.SpeakTextAsync(request.getText()).get();
		} catch (InterruptedException e) {
			throw new NamePronunciationToolException("InterruptedException occured" + e);
		} catch (ExecutionException e) {
			throw new NamePronunciationToolException("ExecutionException occured" + e);
		}

		if (speechRecognitionResult.getReason() == ResultReason.SynthesizingAudioCompleted) {
			log.info("Speech synthesized to speaker for text [" + request.getText() + "] ");
			file = speechRecognitionResult.getAudioData();

		} else if (speechRecognitionResult.getReason() == ResultReason.Canceled) {
			SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails
					.fromResult(speechRecognitionResult);
			log.info("CANCELED: Reason=" + cancellation.getReason());

			if (cancellation.getReason() == CancellationReason.Error) {
				log.error("CANCELED: ErrorCode=" + cancellation.getErrorCode());
				log.error("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
				log.error("CANCELED: Did you set the speech resource key and region values?");
				throw new NamePronunciationToolException("CANCELED: ErrorCode=" + cancellation.getErrorCode());
			}
		}

		try {
			final AudioInputStream ain = AudioSystem.getAudioInputStream(new ByteArrayInputStream(file));
			try {
				final DataLine.Info info = new DataLine.Info(Clip.class, ain.getFormat());
				final Clip clip = (Clip) AudioSystem.getLine(info);
				clip.open(ain);
				clip.start();
			} catch (LineUnavailableException e) {
				log.error("LineUnavailableException while playing the sound", e);
			} finally {
				try {
					ain.close();
				} catch (IOException e) {
					log.warn("Ignoring IOException while trying to close the AudioInputStream", e);
				}
			}
		} catch (UnsupportedAudioFileException e) {
			log.error("UnsupportedAudioFileException while playing the sound", e);
		} catch (IOException e) {
			log.error("IOException while playing the sound", e);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ResponseEntity.ok().headers(headers).contentLength(file.length)
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
	}

}
