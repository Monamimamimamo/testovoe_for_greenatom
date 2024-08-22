package org.example;

import org.example.domain.dto.FileDto;
import org.example.domain.entity.File;
import org.example.domain.repo.FileRepo;
import org.example.service.FileService;
import org.example.service.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileRepo fileRepository;

    @InjectMocks
    private FileServiceImpl fileService;


    @Test
    void testCreateFile() { // проверка корректной работы
        FileDto fileDto = new FileDto("Test Title", "2024-08-22T12:00:00", "Description", "base64EncodedContent");
        UUID fileId = UUID.randomUUID();
        when(fileRepository.save(any(File.class))).thenAnswer(invocation -> {
            File savedFile = invocation.getArgument(0);
            savedFile.setId(fileId);
            return savedFile;
        });
        ResponseEntity<UUID> response = fileService.create(fileDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(fileId, response.getBody());
    }


    @Test
    void testCreateFile_ThrowsInternalServerError() { // проверка ошибки сохранения
        FileDto fileDto = new FileDto("Test Title", "2024-08-22T12:00:00", "Description", "base64EncodedContent");
        doThrow(new RuntimeException("Simulated error")).when(fileRepository).save(any(File.class));

        assertThrows(ResponseStatusException.class, () -> fileService.create(fileDto));
    }

    @Test
    void testGetFile_Success() { // проверка корректной работы
        UUID fileId = UUID.randomUUID();
        File file = new File(new FileDto("Test Title", "2024-08-22T12:00:00", "Description", "base64EncodedContent"));
        FileDto expectedDto = new FileDto(file);
        when(fileRepository.findById(eq(fileId))).thenReturn(Optional.of(file));
        ResponseEntity<FileDto> response = fileService.getFile(fileId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());
    }

    @Test
    void testGetFile_FileNotFound() { // проверка отсутствия файла
        UUID fileId = UUID.randomUUID();
        when(fileRepository.findById(eq(fileId))).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> fileService.getFile(fileId));
    }

    @Test
    void testGetAllFiles_EmptyResult() { // проверка отсутствия файлов в бд
        Pageable pageable = PageRequest.of(1, 10);
        when(fileRepository.findAllSortedByCreationDate(eq(pageable)))
                .thenReturn(Collections.emptyList());
        ResponseEntity<List<FileDto>> response = fileService.getAllFiles(1, 10);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetAllFiles_SuccessfulResult() { //проверка корректной работы
        File file1 = new File(new FileDto("Test Title1", "2024-08-22T12:00:02", "Description", "base64EncodedContent"));
        File file2 = new File(new FileDto("Test Title2", "2024-08-22T12:00:00", "Description", "base64EncodedContent"));
        List<File> files = Arrays.asList(file1, file2);
        Pageable pageable = PageRequest.of(0, 10);
        when(fileRepository.findAllSortedByCreationDate(eq(pageable)))
                .thenReturn(files);
        ResponseEntity<List<FileDto>> response = fileService.getAllFiles(1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals(2, response.getBody().size());
        assertThat(response.getBody()).containsExactlyInAnyOrder(
                new FileDto("Test Title1", "2024-08-22T12:00:02", "Description", "base64EncodedContent"),
                new FileDto("Test Title2", "2024-08-22T12:00", "Description", "base64EncodedContent")
        );
    }
}
