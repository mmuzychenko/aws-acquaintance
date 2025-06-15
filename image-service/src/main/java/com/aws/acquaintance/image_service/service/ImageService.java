package com.aws.acquaintance.image_service.service;

import java.io.IOException;
import java.io.InputStream;

public interface ImageService {

    boolean upload(String fileName, InputStream inputStream) throws IOException;
}
