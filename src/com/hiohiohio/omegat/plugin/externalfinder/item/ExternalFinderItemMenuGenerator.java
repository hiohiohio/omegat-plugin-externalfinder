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

import com.hiohiohio.omegat.plugin.externalfinder.util.BareBonesBrowserLaunch;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import org.omegat.core.Core;

public class ExternalFinderItemMenuGenerator implements IExternalFinderItemMenuGenerator {

    private final List<ExternalFinderItem> finderItems;
    private final ExternalFinderItem.TARGET target;
    private final boolean popup;

    public ExternalFinderItemMenuGenerator(List<ExternalFinderItem> finderItems, ExternalFinderItem.TARGET target, boolean popup) {
        this.finderItems = finderItems;
        this.target = target;
        this.popup = popup;
    }

    @Override
    public List<Component> generate() {
        List<Component> menuItems = new ArrayList<Component>();
        synchronized (finderItems) {
            if (finderItems.isEmpty()) {
                return menuItems;
            }

            // generate menu
            if (popup) {
                menuItems.add(new JPopupMenu.Separator());
            } else {
                menuItems.add(new JToolBar.Separator());
            }
            for (int i = 0, n = finderItems.size(); i < n; i++) {
                ExternalFinderItem finderItem = finderItems.get(i);
                if (popup && finderItem.isNopopup()) {
                    continue;
                }
                if (target == ExternalFinderItem.TARGET.ASCII_ONLY
                        && finderItem.isNonAsciiOnly()) {
                    continue;
                } else if (target == ExternalFinderItem.TARGET.NON_ASCII_ONLY
                        && finderItem.isAsciiOnly()) {
                    continue;
                }
                JMenuItem item = new JMenuItem(finderItem.getName());

                // set keyboard shortcut
                if (!popup) {
                    item.setAccelerator(finderItem.getKeystroke());
                }
                item.addActionListener(new ExternalFinderItemActionListener(finderItem));
                menuItems.add(item);
            }
            if (popup) {
                menuItems.add(new JPopupMenu.Separator());
            }
        }
        return menuItems;
    }

    private static class ExternalFinderItemActionListener implements ActionListener {

        private final List<ExternalFinderItemURL> URLs;
        private final List<ExternalFinderItemCommand> commands;

        public ExternalFinderItemActionListener(ExternalFinderItem finderItem) {
            this.URLs = finderItem.getURLs();
            this.commands = finderItem.getCommands();
        }

        public void actionPerformed(ActionEvent e) {
            final String selection = Core.getEditor().getSelectedText();
            if (selection == null) {
                return;
            }

            final String targetWords = selection; // selection.trim();
            final boolean isASCII = ExternalFinderItem.isASCII(targetWords);

            new Thread(new Runnable() {

                public void run() {
                    for (ExternalFinderItemURL url : URLs) {
                        if ((isASCII && (url.getTarget() == ExternalFinderItem.TARGET.NON_ASCII_ONLY))
                                || (!isASCII && (url.getTarget() == ExternalFinderItem.TARGET.ASCII_ONLY))) {
                            continue;
                        }

                        try {
                            // for JDK 1.5, we cannot use Desktop.
                            BareBonesBrowserLaunch.openURL(ExternalFinderItem.generateURL(url, targetWords));
                        } catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(ExternalFinderItemMenuGenerator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }).start();

            new Thread(new Runnable() {

                public void run() {
                    for (ExternalFinderItemCommand command : commands) {
                        if ((isASCII && (command.getTarget() == ExternalFinderItem.TARGET.NON_ASCII_ONLY))
                                || (!isASCII && (command.getTarget() == ExternalFinderItem.TARGET.ASCII_ONLY))) {
                            continue;
                        }

                        try {
                            Runtime.getRuntime().exec(ExternalFinderItem.generateCommand(command, targetWords));
                        } catch (IOException ex) {
                            Logger.getLogger(ExternalFinderItemMenuGenerator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }).start();
        }
    }
}
