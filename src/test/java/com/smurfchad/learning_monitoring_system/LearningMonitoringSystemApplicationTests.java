package com.smurfchad.learning_monitoring_system;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.smurfchad.learning_monitoring_system.dto.exception.ExceptionDtoResponse;
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

	private String baseUrl() {
		return "http://localhost:" + port + "/api/v1/teachers";
	}

	@AfterEach
	void tearDown() {
		teacherRepository.deleteAll();
	}

	@DisplayName("Проверка поиска учителя, должен создаться учитель, сохраниться в базу, найтись по полученному id")
	@Test
	void shouldFindTeacher() {
		Teacher teacher = new Teacher();
		teacher.setFirstName("John");
		teacher.setLastName("Doe");
		Teacher saved = teacherRepository.save(teacher);
		Long teacherId = saved.getId();

		ResponseEntity<TeacherDtoResponse> response = restTemplate.getForEntity(baseUrl() + "/" + teacherId,
				TeacherDtoResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("John", response.getBody().firstName());
		assertEquals("Doe", response.getBody().lastName());
	}

	@DisplayName ("Проверка поиска учителя когда учителя не существует, должна упасть с 404")
	@Test
	void shouldNotFindTeacher() {
		try {
			restTemplate.getForEntity(baseUrl() + "/9999", ExceptionDtoResponse.class);
		} catch (HttpClientErrorException.NotFound e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
		}
	}

	@DisplayName ("Проверка сохранения нового учителя")
	@Test
	void shouldSaveNewTeacher() {
		TeacherDto teacherDto = new TeacherDto("John1", "Doe1");

		ResponseEntity<TeacherDtoResponse> response = restTemplate.postForEntity(baseUrl(),
				teacherDto, TeacherDtoResponse.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(teacherDto.firstName(), response.getBody().firstName());
		assertEquals(teacherDto.lastName(), response.getBody().lastName());
	}

	@DisplayName ("Проверка обновления данных об учителе")
	@Test
	void shouldUpdateTeacher() {
		TeacherDto teacherDto = new TeacherDto("John1", "Doe1");
		var headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TeacherDto> httpEntity = new HttpEntity<>(teacherDto, headers);
		Teacher teacher = new Teacher();
		teacher.setFirstName("John");
		teacher.setLastName("Doe");
		Teacher saved = teacherRepository.save(teacher);
		Long teacherId = saved.getId();

		ResponseEntity<TeacherDtoResponse> response = restTemplate.exchange(baseUrl() + "/" + teacherId,
				HttpMethod.PUT, httpEntity, TeacherDtoResponse.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(teacherDto.lastName(), response.getBody().lastName());
		assertEquals(teacherDto.firstName(), response.getBody().firstName());
	}

	@DisplayName ("Проверка удаления учителя, поиск удаленного учителя должен упасть с 404")
	@Test
	void shouldDeleteTeacher() {
		Teacher teacher = new Teacher();
		teacher.setFirstName("John");
		teacher.setLastName("Doe");
		Teacher saved = teacherRepository.save(teacher);
		Long teacherId = saved.getId();

		ResponseEntity<Void> response = restTemplate.exchange(baseUrl() + "/" + teacherId,
				HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

		try {
			restTemplate.getForEntity(baseUrl() + "/" + teacherId, ExceptionDtoResponse.class);
		} catch (HttpClientErrorException.NotFound e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
		}
	}
}
