package org.example.documentservice.repository;

import java.io.InputStream;
import java.util.Optional;

public interface MinioRepository {
  void upload(String bucketName, String fileName, InputStream inputStream);

  Optional<InputStream> download(String bucketName, String fileName);
}
