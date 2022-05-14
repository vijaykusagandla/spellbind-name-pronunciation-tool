package com.spellbind.npt.service;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

@Service
public class SpeechSynthesisService {

	Logger log = LoggerFactory.getLogger(SpeechSynthesisService.class);
	
	@Value("${AZURE_SUBSCRIPTION_KEY:NA}")
	private String azureSubscriptionKey;
	
	@Value("${AZURE_REGION:NA}")
	private String azureRegion;

	public byte[] getSpeechSynthesis(String text) {
		log.info("SpeechSynthesisService :: getSpeechSynthesis - begin");
		SpeechConfig speechConfig = SpeechConfig.fromSubscription(this.azureSubscriptionKey, this.azureRegion);
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
			result = speechSynthesizer.SpeakTextAsync(text).get();
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
			log.info("Speech synthesized for text [" + text
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
		log.info("SpeechSynthesisService :: getSpeechSynthesis - end");
		return audioData;
	}
}
