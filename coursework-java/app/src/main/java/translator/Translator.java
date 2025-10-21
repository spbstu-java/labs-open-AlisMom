package translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {
    private final Map<String, String> dict;
    private final int maxLen;

    private static final Pattern WORD = Pattern.compile("[\\p{L}\\p{M}\\p{N}']+");

    public Translator(Dictionary dictionary) {
        this.dict = dictionary.getMap();
        this.maxLen = dictionary.getMaxPhraseWordLen();
    }

    public String translateLine(String input) {
        if (input == null || input.isEmpty()) return input;

        List<WordSpan> words = new ArrayList<>();
        Matcher m = WORD.matcher(input);
        while (m.find()) {
            words.add(new WordSpan(m.start(), m.end(), input.substring(m.start(), m.end())));
        }

        StringBuilder out = new StringBuilder(input.length() + 32);
        int pos = 0;

        for (int i = 0; i < words.size(); i++) {
            WordSpan w = words.get(i);
            if (pos < w.start) {
                out.append(input, pos, w.start);
                pos = w.start;
            }
            int bestK = 0;
            String bestTrans = null;

            int limit = Math.min(maxLen, words.size() - i);
            for (int k = limit; k >= 1; k--) {
                if (!onlyWhitespaceBetween(input, words, i, k)) {
                    continue;
                }
                String key = buildKeyLower(words, i, k);
                String trans = dict.get(key);
                if (trans != null) {
                    bestK = k;
                    bestTrans = trans;
                    break;
                }
            }

            if (bestK > 0) {
                out.append(bestTrans);
                pos = words.get(i + bestK - 1).end;
                i += (bestK - 1);
            } else {
                out.append(w.text);
                pos = w.end;
            }
        }
        if (pos < input.length()) {
            out.append(input, pos, input.length());
        }

        return out.toString();
    }

    private static boolean onlyWhitespaceBetween(String input, List<WordSpan> words, int i, int k) {
        for (int t = i; t < i + k - 1; t++) {
            int a = words.get(t).end;
            int b = words.get(t + 1).start;
            if (a > b) return false;
            String between = input.substring(a, b);
            if (!between.chars().allMatch(Character::isWhitespace)) {
                return false;
            }
        }
        return true;
    }

    private static String buildKeyLower(List<WordSpan> words, int i, int k) {
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < k; t++) {
            if (t > 0) sb.append(' ');
            sb.append(words.get(i + t).text.toLowerCase(Locale.ROOT));
        }
        return sb.toString();
    }

    private static final class WordSpan {
        final int start, end;
        final String text;
        WordSpan(int s, int e, String txt) { start = s; end = e; text = txt; }
    }
}
