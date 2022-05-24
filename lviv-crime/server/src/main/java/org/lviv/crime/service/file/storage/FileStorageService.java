package org.lviv.crime.service.file.storage;

import org.lviv.crime.exception.NotFoundException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileStorageService {
    Mono<Void> saveFile(String fileName, FilePart filePart);
    Flux<DataBuffer> readFile(String fileName, NotFoundException notFoundException);
    Flux<String> getFileList(String folder);
    Mono<Void> remove(String path);
}
