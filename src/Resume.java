import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * com.urise.webapp.model.Resume class
 */
public class Resume implements Serializable {

    // Unique identifier
    String uuid;




    @Override
    public String toString() {
        return uuid;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Resume rhs = (Resume) obj;
        return new EqualsBuilder()
                .append(uuid, rhs.uuid)
                .isEquals();
    }


    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(uuid).
                toHashCode();
    }


}
