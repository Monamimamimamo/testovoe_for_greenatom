package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.dto.FileDto;
import org.example.domain.entity.File;
import org.example.domain.repo.FileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    private FileRepo fileRepository;

    public ResponseEntity<UUID> create(FileDto fileDto) {
        try {
            File existingFile = fileRepository.findByContent(Base64.getDecoder().decode(fileDto.getContent()));
            if (existingFile != null)
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            File file = new File(fileDto);
            fileRepository.save(file);
            return ResponseEntity.ok(file.getId());
        } catch (Exception e) {
            log.error("Error saving file", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save file");
        }
    }

    public ResponseEntity<FileDto> getFile(UUID id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "File not found"));
        return ResponseEntity.ok(new FileDto(file));
    }

    public ResponseEntity<List<FileDto>> getAllFiles(int page, int length) {
        Pageable pageable = PageRequest.of(page - 1, length);
        List<File> files = fileRepository.findAllSortedByCreationDate(pageable);
        if (files.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok(files.stream()
                .map(FileDto::new)
                .collect(Collectors.toList()));
    }
}
