package org.lviv.crime.service.file.storage.impl;

import org.lviv.crime.exception.NotFoundException;
import org.lviv.crime.service.file.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final int BUFFER_SIZE = 4096;

    private final Scheduler defaultScheduler;

    private final Path fileStoragePath;

    public FileStorageServiceImpl(Scheduler defaultScheduler,
                                  @Value("${file.storage.path}") String fileStoragePathStr) {

        this.defaultScheduler = defaultScheduler;
        fileStoragePath = Path.of(fileStoragePathStr);
    }

    @Override
    public Mono<Void> saveFile(String fileName, FilePart filePart) {
        Path filePath = getStoragePath(fileName);
        return createParentFolderIfNotExists(filePath)
                .then(filePart.transferTo(filePath));
    }

    @Override
    public Flux<DataBuffer> readFile(String fileName, NotFoundException notFoundException) {
        Path filePath = getStoragePath(fileName);
        return DataBufferUtils.read(filePath, new DefaultDataBufferFactory(), BUFFER_SIZE)
                .onErrorResume(NoSuchFileException.class, e -> Mono.error(notFoundException));
    }

    @Override
    public Flux<String> getFileList(String folder) {
        return Mono.fromCallable(() -> {
            Path folderPath = getStoragePath(folder);
            String[] fileArray = folderPath.toFile().list();
            return fileArray == null ? (List<String>) Collections.EMPTY_LIST :
                    Arrays.stream(folderPath.toFile().list()).toList();

        }).publishOn(defaultScheduler).flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Void> remove(String path) {
        return Mono.fromRunnable(() -> {
            Path folderPath = getStoragePath(path);
            try {
                Files.walk(folderPath)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                throw new UncheckedIOException("An error occurred while trying to remove folder " + folderPath, e);
            }
        });
    }

    private Mono<Void> createParentFolderIfNotExists(Path filePath) {
        return Mono.fromRunnable(() -> {
            Path parentPath = filePath.getParent();
            try {
                Files.createDirectories(parentPath);
            } catch (IOException e) {
                throw new UncheckedIOException("An error occurred while trying to create folder " + parentPath, e);
            }
        });
    }

    private Path getStoragePath(String fileName) {
        return fileStoragePath.resolve(fileName);
    }
}
