package org.example.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.example.domain.dto.FileDto;
import org.example.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/create")
    public ResponseEntity<UUID> create(@Valid @RequestBody FileDto fileDto) {
        return fileService.create(fileDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<FileDto> getFile(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return fileService.getFile(uuid);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping
    public ResponseEntity<List<FileDto>> getAllFiles(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "length", required = true) int length) {
        if (page <= 0 || length <= 0) return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        return fileService.getAllFiles(page, length);
    }
}