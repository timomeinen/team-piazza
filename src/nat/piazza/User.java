package nat.piazza;

import java.util.*;
import java.util.regex.Pattern;


public class User {
    private final String name;
    private final Set<String> nicknames;
    private final Set<Pattern> nicknamePatterns;
    private final String portraitURL;

    public User(String name, Collection<String> nicknames, String portraitURL) {
        this.name = name;
        this.portraitURL = portraitURL;
        this.nicknames = Collections.unmodifiableSet(new LinkedHashSet(nicknames));

        this.nicknamePatterns = new HashSet<Pattern>();
        for (String nickname : nicknames) {
            nicknamePatterns.add(
                    Pattern.compile("\\b" + Pattern.quote(nickname) + "\\b",
                    Pattern.CASE_INSENSITIVE));
        }
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

    public boolean hasNickname(String nickname) {
        return nicknames.contains(nickname);
    }

    public boolean hasNicknameWithin(String commitComment) {
        for (Pattern nicknamePattern : nicknamePatterns) {
            if (nicknamePattern.matcher(commitComment).find()) {
                return true;
            }
        }
        return false;
    }
}
