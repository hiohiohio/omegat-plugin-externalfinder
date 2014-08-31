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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.KeyStroke;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ExternalFinderXMLItemLoader implements IExternalFinderItemLoader {

    private final File file;

    public ExternalFinderXMLItemLoader(File file) {
        this.file = file;
    }

    @Override
    public List<ExternalFinderItem> load() {
        final List<ExternalFinderItem> finderItems = new ArrayList<ExternalFinderItem>();
        if (file == null) {
            return finderItems;
        }

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            NodeList nodeList = document.getElementsByTagName("item");
            if (nodeList == null) {
                return finderItems;
            }

            for (int i = 0, n = nodeList.getLength(); i < n; i++) {
                ExternalFinderItem finderItem = generateFinderItem(nodeList.item(i));

                if (finderItem.getName() != null && !finderItem.getName().isEmpty()
                        && !finderItems.contains(finderItem)) {
                    finderItems.add(finderItem);
                }
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ExternalFinderXMLItemLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(ExternalFinderXMLItemLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExternalFinderXMLItemLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return finderItems;
    }

    private static ExternalFinderItem generateFinderItem(Node item) {
        if (!item.hasChildNodes()) {
            return null;
        }
        final NodeList childNodes = item.getChildNodes();

        final ExternalFinderItem finderItem = new ExternalFinderItem();

        for (int i = 0, n = childNodes.getLength(); i < n; i++) {
            final Node childNode = childNodes.item(i);

            final String nodeName = childNode.getNodeName();
            if (nodeName.equals("name")) {
                finderItem.setName(childNode.getTextContent());
            } else if (nodeName.equals("url")) {
                finderItem.getURLs().add(generateFinderURL(childNode));
            } else if (nodeName.equals("command")) {
                finderItem.getCommands().add(generateFinderCommand(childNode));
            } else if (nodeName.equals("keystroke")) {
                KeyStroke keyStroke = KeyStroke.getKeyStroke(childNode.getTextContent());
                finderItem.setKeystroke(keyStroke);
            }
        }

        return finderItem;
    }

    private static ExternalFinderItemURL generateFinderURL(Node urlNode) {
        String url = urlNode.getTextContent();
        ExternalFinderItem.TARGET target = ExternalFinderItem.TARGET.BOTH;
        ExternalFinderItem.ENCODING encoding = ExternalFinderItem.ENCODING.DEFAULT;

        if (urlNode.hasAttributes()) {

            Node tAttribute = urlNode.getAttributes().getNamedItem("target");
            if (tAttribute != null) {
                String targetAttribute = tAttribute.getTextContent().toLowerCase();
                if (targetAttribute.equals("ascii_only")) {
                    target = ExternalFinderItem.TARGET.ASCII_ONLY;
                } else if (targetAttribute.equals("non_ascii_only")) {
                    target = ExternalFinderItem.TARGET.NON_ASCII_ONLY;
                }
            }

            Node eAttribute = urlNode.getAttributes().getNamedItem("encoding");
            if (eAttribute != null) {
                String encodingAttribute = eAttribute.getTextContent().toLowerCase();
                if (encodingAttribute.equals("escape")) {
                    encoding = ExternalFinderItem.ENCODING.ESCAPE;
                } else if (encodingAttribute.equals("none")) {
                    encoding = ExternalFinderItem.ENCODING.NONE;
                }
            }
        }

        return new ExternalFinderItemURL(url, target, encoding);
    }

    private static ExternalFinderItemCommand generateFinderCommand(Node commandNode) {
        String command = commandNode.getTextContent();
        ExternalFinderItem.TARGET target = ExternalFinderItem.TARGET.BOTH;
        ExternalFinderItem.ENCODING encoding = ExternalFinderItem.ENCODING.NONE;
        String delimiter = "|";

        if (commandNode.hasAttributes()) {

            Node tAttribute = commandNode.getAttributes().getNamedItem("target");
            if (tAttribute != null) {
                String targetAttribute = tAttribute.getTextContent().toLowerCase();
                if (targetAttribute.equals("ascii_only")) {
                    target = ExternalFinderItem.TARGET.ASCII_ONLY;
                } else if (targetAttribute.equals("non_ascii_only")) {
                    target = ExternalFinderItem.TARGET.NON_ASCII_ONLY;
                }
            }

            Node eAttribute = commandNode.getAttributes().getNamedItem("encoding");
            if (eAttribute != null) {
                String encodingAttribute = eAttribute.getTextContent().toLowerCase();
                if (encodingAttribute.equals("default")) {
                    encoding = ExternalFinderItem.ENCODING.DEFAULT;
                } else if (encodingAttribute.equals("escape")) {
                    encoding = ExternalFinderItem.ENCODING.ESCAPE;
                }
            }

            Node dAttribute = commandNode.getAttributes().getNamedItem("delimiter");
            if (dAttribute != null) {
                delimiter = dAttribute.getTextContent();
            }
        }

        return new ExternalFinderItemCommand(command, target, encoding, delimiter);
    }
}
