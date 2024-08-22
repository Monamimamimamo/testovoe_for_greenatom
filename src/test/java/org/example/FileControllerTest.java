package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.FileController;
import org.example.domain.dto.FileDto;
import org.example.service.FileService;
import org.example.service.FileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebMvcTest(FileController.class)
@ExtendWith(MockitoExtension.class)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileServiceImpl fileService;

    @Test
    void testCreateFile() throws Exception {
        FileDto fileDto = new FileDto("Test Title", "2024-08-22T12:00:00", "Description", "base64EncodedContent");
        mockMvc.perform(post("/api/file/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(fileDto)))
                .andExpect(status().isOk()); //проверка корректной работы

        fileDto.setContent("base64EncodedContent!");
        mockMvc.perform(post("/api/file/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(fileDto)))
                .andExpect(status().isConflict()); // проверка валидации файла base64

        fileDto.setContent("base64EncodedContent");
        fileDto.setCreationDate("2024-08-2T12:00:00");
        mockMvc.perform(post("/api/file/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(fileDto)))
                .andExpect(status().isConflict()); // проверка валидации iso формата даты

        fileDto.setCreationDate("2024-08-20T12:00:00");
        fileDto.setDescription("");
        mockMvc.perform(post("/api/file/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(fileDto)))
                .andExpect(status().isConflict());// проверка валидации описания

        fileDto.setDescription("Description");
        fileDto.setTitle("");
        mockMvc.perform(post("/api/file/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(fileDto)))
                .andExpect(status().isConflict());// проверка валидации заголовка
    }


    @Test
    void testGetFileById() throws Exception {
        FileDto fileDto = new FileDto("Test Title", "2024-08-22T12:00:00", "Description", "base64EncodedContent");
        when(fileService.getFile(any(UUID.class))).thenReturn(ResponseEntity.ok(fileDto));

        mockMvc.perform(get("/api/file/" +  UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(fileDto))); //проверка корректной работы

        mockMvc.perform(get("/api/file/" +  "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity()); // проверка валидации uuid
    }

    @Test
    void testGetAllFiles() throws Exception {
        // Step 1: Prepare the test data
        List<FileDto> fileDtos = Arrays.asList(
                new FileDto("Title 1", "2024-08-22T12:00:00", "Description 1", "base64EncodedContent1"),
                new FileDto("Title 2", "2024-08-22T13:00:00", "Description 2", "base64EncodedContent2")
        );

        when(fileService.getAllFiles(1, 10)).thenReturn(ResponseEntity.ok(fileDtos));

        mockMvc.perform(get("/api/file")
                        .param("page", "1")
                        .param("length", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[1].description").value("Description 2")); // проверка корректной работы

        mockMvc.perform(get("/api/file")
                .param("page", "1")
                .param("length", "0"))
                .andExpect(status().isUnprocessableEntity()); // проверка валидации длины страницы пагинации

        mockMvc.perform(get("/api/file")
                        .param("page", "0")
                        .param("length", "1"))
                .andExpect(status().isUnprocessableEntity()); // проверка валидации номера страницы валидации
    }

}
