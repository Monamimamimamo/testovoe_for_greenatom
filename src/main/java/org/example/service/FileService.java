package org.example.service;

import org.example.domain.dto.FileDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface FileService {
    ResponseEntity<UUID> create (FileDto fileDto);
    ResponseEntity<FileDto> getFile (UUID id);
    ResponseEntity<List<FileDto>> getAllFiles (int page, int length);
}
