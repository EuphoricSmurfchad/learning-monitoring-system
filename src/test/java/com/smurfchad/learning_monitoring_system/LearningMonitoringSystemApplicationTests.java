package com.smurfchad.learning_monitoring_system;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.smurfchad.learning_monitoring_system.dto.teacher.TeacherDto;
import com.smurfchad.learning_monitoring_system.dto.teacher.TeacherDtoResponse;
import com.smurfchad.learning_monitoring_system.model.Teacher;
import com.smurfchad.learning_monitoring_system.repository.TeacherRepository;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LearningMonitoringSystemApplicationTests {

	private final RestTemplate restTemplate = new RestTemplate();

	@LocalServerPort
	private int port;

	@Autowired
	private TeacherRepository teacherRepository;

	private Long teacherId;

	private String baseUrl() {
		return "http://localhost:" + port + "/api/v1/teachers";
	}

	@BeforeEach
	void setUp() {
		Teacher teacher = new Teacher();
		teacher.setFirstName("John");
		teacher.setLastName("Doe");
		Teacher saved = teacherRepository.save(teacher);
		teacherId = saved.getId();
	}

	@AfterAll
	void tearDown() {
		teacherRepository.deleteAll();
	}

	@Test
	void shouldFindTeacher() {
		ResponseEntity<TeacherDtoResponse> response = restTemplate.getForEntity(baseUrl() + "/" + teacherId,
				TeacherDtoResponse.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("John", response.getBody().firstName());
		assertEquals("Doe", response.getBody().lastName());
	}

	@Test
	void shouldSaveNewTeacher() {
		TeacherDto teacherDto = new TeacherDto("John1", "Doe1");
		ResponseEntity<TeacherDtoResponse> response = restTemplate.postForEntity(baseUrl(),
				teacherDto, TeacherDtoResponse.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	void shouldUpdateTeacher() {
		TeacherDto teacherDto = new TeacherDto("John1", "Doe1");
		var headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TeacherDto> httpEntity = new HttpEntity<>(teacherDto, headers);
		ResponseEntity<TeacherDtoResponse> response = restTemplate.exchange(baseUrl() + "/" + teacherId,
				HttpMethod.PUT, httpEntity, TeacherDtoResponse.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(teacherDto.lastName(), response.getBody().lastName());
		assertEquals(teacherDto.firstName(), response.getBody().firstName());
	}

	@Test
	void shouldDeleteTeacher() {
		ResponseEntity<Void> response = restTemplate.exchange(baseUrl() + "/" + teacherId,
				HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
}
