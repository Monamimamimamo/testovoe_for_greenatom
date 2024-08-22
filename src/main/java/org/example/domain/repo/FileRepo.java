package org.example.domain.repo;

import org.example.domain.entity.File;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FileRepo extends JpaRepository<File, UUID> {
    @Query("SELECT f FROM File f ORDER BY f.creationDate ASC")
    List<File> findAllSortedByCreationDate(Pageable pageable);
}
