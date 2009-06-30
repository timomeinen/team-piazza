package nat.piazza;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;


public class User {
    public final String name;
    public final Set<String> nicknames;
    public final String portraitURL;

    public User(String name, Collection<String> nicknames, String portraitURL) {
        this.name = name;
        this.portraitURL = portraitURL;
        this.nicknames = Collections.unmodifiableSet(new LinkedHashSet(nicknames));
    }

    @Override
    public String toString() {
        return name;
    }
}
