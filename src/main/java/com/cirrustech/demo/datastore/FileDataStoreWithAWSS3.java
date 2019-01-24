package com.cirrustech.demo.datastore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.amazonaws.services.s3.model.ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION;

/**
 * Implementation of {@link FileDataStore} using AWS S3.
 */
@Component
public class FileDataStoreWithAWSS3 implements FileDataStore {

    private static final Logger LOG = LoggerFactory.getLogger(FileDataStoreWithAWSS3.class);

    private final AmazonS3 amazonS3;

    private final String bucketName;

    /**
     * Constructor.
     */
    public FileDataStoreWithAWSS3(AmazonS3 amazonS3,
                                  @Value("${file.data.store.bucket}") String bucketName) {

        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFile(String name, byte[] content) throws IOException {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setSSEAlgorithm(AES_256_SERVER_SIDE_ENCRYPTION);
        objectMetadata.setContentLength(content.length);

        PutObjectRequest request = new PutObjectRequest(bucketName,
                name, new ByteArrayInputStream(content), objectMetadata);
        try {
            amazonS3.putObject(request);
            LOG.info("File {} added", name);
        } catch (AmazonServiceException e) {

            LOG.warn("Exception on adding file {}.", name, e);
            throw new IOException("Internal error.");
        }
    }

}
