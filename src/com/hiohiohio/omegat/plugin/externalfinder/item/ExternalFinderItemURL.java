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

public class ExternalFinderItemURL {

    private String URL;
    private ExternalFinderItem.TARGET target;
    private ExternalFinderItem.ENCODING encoding;

    public ExternalFinderItemURL() {
    }

    public ExternalFinderItemURL(String URL, ExternalFinderItem.TARGET target, ExternalFinderItem.ENCODING encoding) {
        this.URL = URL;
        this.target = target;
        this.encoding = encoding;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
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
}
