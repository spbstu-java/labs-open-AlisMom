package translator;

import java.util.Collections;
import java.util.Map;

public class Dictionary {
    private final Map<String, String> map;
    private final int maxPhraseWordLen;

    public Dictionary(Map<String, String> map, int maxPhraseWordLen) {
        this.map = map;
        this.maxPhraseWordLen = maxPhraseWordLen;
    }

    public Map<String, String> getMap() {
        return Collections.unmodifiableMap(map);
    }

    public int getMaxPhraseWordLen() {
        return maxPhraseWordLen;
    }
}
