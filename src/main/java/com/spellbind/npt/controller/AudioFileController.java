package com.spellbind.npt.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spellbind.npt.dao.EmployeeDAO;
import com.spellbind.npt.entity.Employee;

@RestController
public class AudioFileController {

	@Autowired
	private EmployeeDAO employeeDAO;

	Logger log = LoggerFactory.getLogger(AudioFileController.class);

	@RequestMapping(value = "/storeAudioFile", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> storeAudioFile(@RequestPart("file") MultipartFile multipartFile)
			throws IOException, SQLException, URISyntaxException {
		log.info("Persisting audio file: {}", multipartFile.getOriginalFilename());
		employeeDAO.storeAudioFile(multipartFile);
		return new ResponseEntity<String>("Audio Saved Succesfully", HttpStatus.OK);
	}

	@RequestMapping(value = "/getAudioFile", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getAudioFile(@RequestBody com.spellbind.npt.model.Employee request) {

		Employee employee = employeeDAO.findEmployeeById(request.getEmployeeId());

		byte[] file = employee.getAudioContent();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ResponseEntity.ok().headers(headers).contentLength(file.length)
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);

	}
}