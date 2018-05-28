import java.util.Arrays;
import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {

    Resume[] storage = new Resume[4];

    // number of first non-null storage elements
    private int size;

    public void clear() {
            Arrays.fill(storage,  0, size, null);
            size = 0;
    }


    public void save(Resume resume) {
        if (size < storage.length ) {
            storage[size] = resume;
            size++;
        }
    }


    public Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(storage[i].uuid, uuid)) {
                return storage[i];
            }
        }
        return null;
    }


    public void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(storage[i].uuid, uuid)) {
                storage[i] = storage[size-1];
                storage[size-1] = null;
                size--;
                return;
            }
        }
    }


    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }


    public int size() {
        return size;
    }

}
