package nat.piazza;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;


public class User {
    private final String name;
    private final Set<String> nicknames;
    private final String portraitURL;

    public User(String name, Collection<String> nicknames, String portraitURL) {
        this.name = name;
        this.portraitURL = portraitURL;
        this.nicknames = Collections.unmodifiableSet(new LinkedHashSet(nicknames));
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
    
    public String getPortraitURL() {
        return portraitURL;
    }

    public Iterable<String> nicknames() {
        return nicknames;
    }
}
