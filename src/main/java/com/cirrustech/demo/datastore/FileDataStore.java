package com.cirrustech.demo.datastore;

import java.io.IOException;

/**
 * Model for data store that handles files.
 */
public interface FileDataStore {

    /**
     * Add a new file.
     *
     * @param name    file name
     * @param content file content
     * @throws IOException - if any exception occurs
     */
    void addFile(String name, byte[] content) throws IOException;
}
