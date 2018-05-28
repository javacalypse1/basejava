import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {

    Resume[] storage = new Resume[10000];

    public void clear() {
        Arrays.fill(storage, null);
    }


    public void save(Resume resume) {
        int indexOfFirstNullElement = 0;
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] == null) {
                indexOfFirstNullElement = i;
                // indexOfFirstNullElement variable not needed,
                // but I think this way the intention is more clear
                storage[indexOfFirstNullElement] = resume;
                break;
            }
        }
    }


    public Resume get(String uuid) {
        for (Resume resume : storage) {
            if (resume == null) {
                // if storage starts with null, it won't find any non-null element at any index,
                // if we hit null at index>0, it also won't find any non-null element at any subsequent index
                return null;
                // uuid (and resume.uuid) shall not be null - it makes no sense
                // so I'd better throw some run-time exception on encountering that,
                //  but that was not in Specs...
            } else if (Objects.equals(resume.uuid, uuid)) {
                return resume;
            }
        }
        return null; // dummy, compiler is afraid of storage array being empty
    }


    public void delete(String uuid) {
        int firstNullIndex = -1;
        int elementToDeleteIndex = -1;
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] == null) {
                firstNullIndex = i;
                break; // any elementToDeleteIndex shall be already set before, if any
                // uuid (and resume.uuid) shall not be null - it makes no sense
                // so I'd better throw some run-time exception on encountering that,
                //  but that was not in Specs...
            } else if (Objects.equals(storage[i].uuid, uuid)) {
                elementToDeleteIndex = i;
            }
        }

        if (elementToDeleteIndex < 0) {
            return; // do nothing - nothing to delete
        }

        if (firstNullIndex < 0) {
            // no null elements present
            System.arraycopy(storage, elementToDeleteIndex + 1, storage, elementToDeleteIndex, storage.length - elementToDeleteIndex - 1);
            // we don't need duplicate of last element - it was already moved to the left
            storage[storage.length - 1] = null;
            return;
        } else {
            // there is at least one null, so
            System.arraycopy(storage, elementToDeleteIndex + 1, storage, elementToDeleteIndex, firstNullIndex - elementToDeleteIndex - 1);
            // set to null rightmost non-null element that was moved left
            storage[firstNullIndex - 1] = null;
        }
    }


    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        int firstNullIndex = getFirstNullIndex();

        if (firstNullIndex < 0) {
            return storage; // all elements are non-null
        } else {
            // there is at least one non-null element, so
            return Arrays.copyOf(storage, firstNullIndex);
        }
    }


    public int size() {
        int firstNullIndex = getFirstNullIndex();
        if (firstNullIndex < 0) {
            return storage.length; // all elements are non-null
        } else {
            return firstNullIndex;
        }
    }


    // returns -1 if storage contains no null elements
    private int getFirstNullIndex() {
        int firstNullIndex = -1;
        for (int i = 0; i < storage.length; i++) {
            if (storage[i] == null) {
                firstNullIndex = i;
                break;
            }
        }
        return firstNullIndex;
    }

}
