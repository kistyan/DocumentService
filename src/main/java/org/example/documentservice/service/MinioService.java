package org.example.documentservice.service;

import java.io.InputStream;
import java.util.Optional;

public interface MinioService {
  void upload(String bucketName, String fileName, InputStream inputStream);

  Optional<InputStream> download(String bucketName, String fileName);
}
