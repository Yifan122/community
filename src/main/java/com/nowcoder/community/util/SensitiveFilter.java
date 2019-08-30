package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private static final String REPLACEMENT = "***";

    private TrieNode rootNode = new TrieNode();

    @Value("${community.sensitive.words}")
    private String SENSITIVE_WORDS_FILE;

    // Read the sensitive words file and construct the Trie Tree
    @PostConstruct
    public void init() {
        System.out.println(SENSITIVE_WORDS_FILE);
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(SENSITIVE_WORDS_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        ) {
            String key;
            while ((key = reader.readLine()) != null) {
                addString(key);
            }
        } catch (IOException e) {
            logger.error("Read sensitive-words.txt fail" + e.getMessage());
        }
    }

    // And a sensitive word into the Trie Tree
    private void addString(String key) {
        TrieNode tmp = rootNode;

        // Construct the Trie Tree
        for (int i = 0; i < key.length(); i++) {
            TrieNode next = tmp.getSubNode(key.charAt(i));
            if (next == null) {
                next = new TrieNode();
                tmp.addSubNode(key.charAt(i), next);
            }

            tmp = next;

            if (i == key.length() - 1) {
                tmp.setKeywordEnd(true);
            }
        }
    }

    /**
     * The method clean the sensitive data
     *
     * @param text The sentence which are gonna to be filtered the sensitive words
     * @return Clean sentence
     */
    public String filter(String text) {

        //空值判断
        if (StringUtils.isBlank(text)) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        int first = 0;
        int second = 0;

        while (first < text.length()) {
            TrieNode tmp = rootNode;
            char c = text.charAt(first);

            // if the c is the special character
            if (isSymbol(c)) {
                if (tmp == rootNode) {
                    stringBuilder.append(c);
                    first++;
                }
                second++;
                continue;
            }

            tmp = tmp.getSubNode(c);

            while (tmp != null) {
                // Skip all the special character
                do {
                    second++;
                    c = text.charAt(second);
                } while (isSymbol(c));

                tmp = tmp.getSubNode(c);

                if (tmp != null && tmp.getIsKeywordEnd()) {
                    stringBuilder.append(REPLACEMENT);
                    first = second;
                    second = first - 1;
                    break;
                }
            }

            stringBuilder.append(text, first, second + 1);

            first++;
            second = first;
        }
        return stringBuilder.toString();
    }

    // check the character is valid or not
    private boolean isSymbol(Character c) {
        // 0x2E80 ~ 0X9FFF is the Asian Character
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0X9FFF);
    }

    private class TrieNode {
        private boolean isKeywordEnd = false;

        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean getIsKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }
    }
}
