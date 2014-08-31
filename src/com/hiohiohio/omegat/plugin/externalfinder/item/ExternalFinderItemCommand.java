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

public class ExternalFinderItemCommand {

    private String command;
    private ExternalFinderItem.TARGET target;
    private ExternalFinderItem.ENCODING encoding;
    private String delimiter;

    public ExternalFinderItemCommand() {
    }

    public ExternalFinderItemCommand(String command, ExternalFinderItem.TARGET target, ExternalFinderItem.ENCODING encoding, String delimiter) {
        this.command = command;
        this.target = target;
        this.encoding = encoding;
        this.delimiter = delimiter;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public ExternalFinderItem.TARGET getTarget() {
        return target;
    }

    public void setTarget(ExternalFinderItem.TARGET target) {
        this.target = target;
    }

    public ExternalFinderItem.ENCODING getEncoding() {
        return encoding;
    }

    public void setEncoding(ExternalFinderItem.ENCODING encoding) {
        this.encoding = encoding;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
