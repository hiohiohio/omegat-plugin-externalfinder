/*
 * Copyright (c) 2014 Chihiro Hio.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hiohiohio.omegat.plugin.externalfinder.item;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.KeyStroke;

public class ExternalFinderItem {

    public enum TARGET {

        // default BOTH for URL and command
        ASCII_ONLY, NON_ASCII_ONLY, BOTH
    }

    public enum ENCODING {

        // default DEFAULT for URL
        // default NONE for command
        DEFAULT, ESCAPE, NONE
    }

    private String name;
    private List<ExternalFinderItemURL> URLs;
    private List<ExternalFinderItemCommand> commands;
    private KeyStroke keystroke;
    private Boolean asciiOnly = null;
    private Boolean nonAsciiOnly = null;

    public ExternalFinderItem() {
        this.URLs = new ArrayList<ExternalFinderItemURL>();
        this.commands = new ArrayList<ExternalFinderItemCommand>();
    }

    public ExternalFinderItem(String name, List<ExternalFinderItemURL> URLs, List<ExternalFinderItemCommand> commands, KeyStroke keystroke) {
        this.name = name;
        this.URLs = URLs;
        this.commands = commands;
        this.keystroke = keystroke;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExternalFinderItemURL> getURLs() {
        return URLs;
    }

    public void setURLs(List<ExternalFinderItemURL> URLs) {
        this.asciiOnly = null;
        this.nonAsciiOnly = null;
        this.URLs = URLs;
    }

    public List<ExternalFinderItemCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<ExternalFinderItemCommand> commands) {
        this.asciiOnly = null;
        this.nonAsciiOnly = null;
        this.commands = commands;
    }

    public KeyStroke getKeystroke() {
        return keystroke;
    }

    public void setKeystroke(KeyStroke keystroke) {
        this.keystroke = keystroke;
    }

    public boolean isAsciiOnly() {
        if (asciiOnly == null) {
            asciiOnly = isTargetOnly(TARGET.ASCII_ONLY);
        }

        return asciiOnly;
    }

    public boolean isNonAsciiOnly() {
        if (nonAsciiOnly == null) {
            nonAsciiOnly = isTargetOnly(TARGET.NON_ASCII_ONLY);
        }

        return nonAsciiOnly;
    }

    private boolean isTargetOnly(final TARGET target) {
        for (ExternalFinderItemURL url : URLs) {
            if (url.getTarget() != target) {
                return false;
            }
        }

        for (ExternalFinderItemCommand command : commands) {
            if (command.getTarget() != target) {
                return false;
            }
        }

        return true;
    }

    public ExternalFinderItem replaceRefs(final ExternalFinderItem item) {
        this.name = item.name;
        this.URLs = item.URLs;
        this.commands = item.commands;
        this.keystroke = item.keystroke;

        this.asciiOnly = null; // item.isAsciiOnly();
        this.nonAsciiOnly = null; // item.isNonAsciiOnly();

        return this;
    }

    public static final boolean isASCII(String s) {
        for (int i = 0, n = s.length(); i < n; i++) {
            if ((int) s.charAt(i) > 0x7F) {
                return false;
            }
        }
        return true;
    }

    public static final String generateURL(ExternalFinderItemURL url, String findingWords) throws UnsupportedEncodingException {
        String encodedWords;
        if (url.getEncoding() == ENCODING.NONE) {
            encodedWords = findingWords;
        } else {
            encodedWords = URLEncoder.encode(findingWords, "UTF-8");
            if (url.getEncoding() == ENCODING.ESCAPE) {
                encodedWords = encodedWords.replace("+", "%20");
            }
        }

        return url.getURL().replace("{target}", encodedWords);
    }

    public static final String[] generateCommand(ExternalFinderItemCommand command, String findingWords) throws UnsupportedEncodingException {
        String encodedWords;
        if (command.getEncoding() == ENCODING.NONE) {
            encodedWords = findingWords;
        } else {
            encodedWords = URLEncoder.encode(findingWords, "UTF-8");
            if (command.getEncoding() == ENCODING.ESCAPE) {
                encodedWords = encodedWords.replace("+", "%20");
            }
        }

        String[] ret = command.getCommand().split(Pattern.quote(command.getDelimiter()));
        for (int i = 0; i < ret.length; i++) {
            String s = ret[i];
            ret[i] = s.replace("{target}", encodedWords);
        }

        return ret;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExternalFinderItem other = (ExternalFinderItem) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
