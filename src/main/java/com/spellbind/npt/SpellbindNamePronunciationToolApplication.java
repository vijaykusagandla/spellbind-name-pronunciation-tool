package com.spellbind.npt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpellbindNamePronunciationToolApplication implements CommandLineRunner {
	
	private static Logger logger = LoggerFactory.getLogger(SpellbindNamePronunciationToolApplication.class);
	
	@Value("${AZURE_SUBSCRIPTION_KEY:NA}")
	private String azureSubscriptionKey;
	
	@Value("${AZURE_REGION:NA}")
	private String azureRegion;

	public static void main(String[] args) {
		SpringApplication.run(SpellbindNamePronunciationToolApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Azure subscription key: {}", azureSubscriptionKey);
		logger.info("Azure region: {}", azureRegion);
		logger.info("Application has started!");
	}

}
