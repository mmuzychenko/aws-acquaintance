package com.aws.acquaintance.image_service.service;

import java.io.IOException;
import java.io.InputStream;

public interface ImageService {

    boolean upload(String fileName, InputStream inputStream) throws IOException;

    boolean delete(String fileName);

    boolean download(String fileName) throws IOException;

    byte[] downloadRest(String keyName);
}
