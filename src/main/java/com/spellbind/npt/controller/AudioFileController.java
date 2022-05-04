package com.spellbind.npt.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Optional;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spellbind.npt.entity.Employee;
import com.spellbind.npt.exception.NamePronunciationToolException;
import com.spellbind.npt.repository.EmployeeRepository;

@RestController
public class AudioFileController {

	@Autowired
	private EmployeeRepository employeeRepository;

	Logger log = LoggerFactory.getLogger(AudioFileController.class);

	@Transactional
	@RequestMapping(value = "/storeAudioFile", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> storeAudioFile(@RequestPart("file") MultipartFile multipartFile)
			throws IOException, SQLException, URISyntaxException {
		log.info("Persisting audio file: {}", multipartFile.getOriginalFilename());
		Long employeeId = Long.parseLong(multipartFile.getOriginalFilename().replaceAll("\\.\\w+$", ""));
		Employee employee = findEmployeeById(employeeId);
		employee.setAudioContent(multipartFile.getBytes());
		employeeRepository.save(employee);
		return new ResponseEntity<String>("Audio Saved Succesfully", HttpStatus.OK);
	}

	private Employee findEmployeeById(Long employeeId) {
		Optional<Employee> employee = employeeRepository.findById(employeeId);
		return employee.orElseThrow(() -> new NamePronunciationToolException("Employee Id not found"));
	}

	@RequestMapping(value = "/getAudioFile", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getAudioFile() throws IOException, SQLException, URISyntaxException {
		Optional<Employee> streamingFileRecord = Optional.ofNullable(new Employee());

		streamingFileRecord = employeeRepository.findById(10001L);

		byte[] file = streamingFileRecord.get().getAudioContent();

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