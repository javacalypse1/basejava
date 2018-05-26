import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {

    // If save method must only assign elements of in-memory array
    // (without need for any persistence between JVM runs),
    // then saveStorageToDisk() shall be commented out.

    //TODO: array initialization now probably not needed,
    //because constructor reassigns array from loaded file
    // with 10k array elements
    Resume[] storage = new Resume[10000];

    private static final String DISK_STORAGE_FILENAME = "/storage.data";

    public ArrayStorage() {
        //load prevoiusly saved storage from disk
        InputStream is = getClass().getResourceAsStream(DISK_STORAGE_FILENAME);
        // all exceptions are handled internally by Apache Commons method
        storage = SerializationUtils.deserialize(is);
    }


    public void clear() {
        Arrays.fill(storage, null);
        saveStorageToDisk();
    }

    // TODO: currently it is possible to create Resume with null uuid,
    // but probably it shall be impossible - change Resume class
    public void save(Resume resume) {
        if (resume == null){
            String msg = "argument cannot be null";
            throw new IllegalArgumentException(msg);
        }

        int indexOfFirstNullElement = ArrayUtils.indexOf(storage, null);
        if (indexOfFirstNullElement < 0) {
            // TODO: log "storage is out-of-memory",
            // throw StorageIsFullException
            // or return boolean from save: true = saved successfully
            return; // cannot save - no free/null elements in storage array
        } else {
            storage[indexOfFirstNullElement] = resume;
            saveStorageToDisk();
        }
    }


    public Resume get(String uuid) {
        if (uuid == null){
            String msg = "argument cannot be null";
            throw new IllegalArgumentException(msg);
        }

        Resume resumeToGet = new Resume();
        resumeToGet.uuid = uuid;
        int indexOfResumeMatchingUUID = ArrayUtils.indexOf(storage, resumeToGet);
        if (indexOfResumeMatchingUUID < 0) {
            return null; // Resume with such uuid not found
        } else {
            return storage[indexOfResumeMatchingUUID];
        }
    }

    public void delete(String uuid) {
        if (uuid == null){
            String msg = "argument cannot be null";
            throw new IllegalArgumentException(msg);
        }
        // equals + hashCode added to Resume class
        Resume resumeToDelete = new Resume();
        resumeToDelete.uuid = uuid;
        storage = ArrayUtils.removeElement(storage, resumeToDelete);
        saveStorageToDisk();
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        int indexOfFirstNullElement = ArrayUtils.indexOf(storage, null);
        if (indexOfFirstNullElement < 0) {
            return storage; // no null elements in storage array
        } else {
            return Arrays.copyOfRange(storage, 0, indexOfFirstNullElement);
        }
    }


    public int size() {
        int indexOfFirstNullElement = ArrayUtils.indexOf(storage, null);
        if (indexOfFirstNullElement < 0) {
            return storage.length; // no null elements in storage array
        } else {
            return indexOfFirstNullElement; // number of first non-null elements
        }
    }

    // saves storage array to resources file
    private void saveStorageToDisk() {
        String fileName = getClass().getResource(DISK_STORAGE_FILENAME).getFile();
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(fileName));
            // all exceptions are handled internally by Apache Commons method
            SerializationUtils.serialize(storage, os);
        } catch (FileNotFoundException e) {
            // TODO: add logging here
            e.printStackTrace();
        }
    }

}
