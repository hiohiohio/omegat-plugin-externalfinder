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
package com.hiohiohio.omegat.plugin.externalfinder;

import com.hiohiohio.omegat.plugin.externalfinder.item.IExternalFinderItemLoader;
import com.hiohiohio.omegat.plugin.externalfinder.item.IExternalFinderItemMenuGenerator;
import com.hiohiohio.omegat.plugin.externalfinder.item.ExternalFinderXMLItemLoader;
import com.hiohiohio.omegat.plugin.externalfinder.item.ExternalFinderItemPopupMenuConstructor;
import com.hiohiohio.omegat.plugin.externalfinder.item.ExternalFinderItem;
import com.hiohiohio.omegat.plugin.externalfinder.item.ExternalFinderItemMenuGenerator;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenu;
import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.data.IProject;
import org.omegat.core.data.ProjectProperties;
import org.omegat.core.events.IApplicationEventListener;
import org.omegat.core.events.IProjectEventListener;
import org.omegat.util.StaticUtils;

public class ExternalFinder {

    public static final String FINDER_FILE = "finder.xml";

    /**
     * to support v2 of OmegaT, this class will be registered as a base-plugin
     * class.
     */
    public ExternalFinder() {
        loadPlugins();
    }

    /**
     * OmegaT will call this method when loading.
     */
    public static void loadPlugins() {
        // shared list of items loaded when a project is opened and cleared when a project is closed.
        final List<ExternalFinderItem> finderItems = new ArrayList<ExternalFinderItem>();

        // register listeners
        CoreEvents.registerApplicationEventListener(ExternalFinder.generateIApplicationEventListener(finderItems));
        CoreEvents.registerProjectChangeListener(ExternalFinder.generateIProjectEventListener(finderItems));
    }

    private static IProjectEventListener generateIProjectEventListener(final List<ExternalFinderItem> finderItems) {
        return new IProjectEventListener() {
            private final List<Component> menuItems = new ArrayList<Component>();

            @Override
            public void onProjectChanged(final IProjectEventListener.PROJECT_CHANGE_TYPE eventType) {
                switch (eventType) {
                    case LOAD:
                        onLoad();
                        break;
                    case CLOSE:
                        onClose();
                        break;
                    default:
                    // ignore
                }
            }

            private void onLoad() {
                // clear old items
                menuItems.clear();
                synchronized (finderItems) {
                    finderItems.clear();
                }

                // load user's xml file
                // Even though the file is independent from projects, it is
                // loaded when a project is loaded for providing a chance to reload it.
                final String configDir = StaticUtils.getConfigDir();
                final File userFile = new File(configDir, FINDER_FILE);
                if (userFile.canRead()) {
                    final IExternalFinderItemLoader userItemLoader = new ExternalFinderXMLItemLoader(userFile);
                    final List<ExternalFinderItem> loadedUserItems = userItemLoader.load();

                    synchronized (finderItems) {
                        finderItems.addAll(loadedUserItems);
                    }
                }

                // load project's xml file
                final IProject currentProject = Core.getProject();
                final ProjectProperties projectProperties = currentProject.getProjectProperties();
                final String projectRoot = projectProperties.getProjectRoot();
                final File projectFile = new File(projectRoot, FINDER_FILE);
                if (projectFile.canRead()) {
                    final IExternalFinderItemLoader projectItemLoader = new ExternalFinderXMLItemLoader(projectFile);
                    final List<ExternalFinderItem> loadedProjectItems = projectItemLoader.load();

                    synchronized (finderItems) {
                        // replace duplicated items based on name
                        for (ExternalFinderItem item : finderItems) {
                            if (loadedProjectItems.contains(item)) {
                                final int index = loadedProjectItems.indexOf(item);
                                final ExternalFinderItem newItem = loadedProjectItems.get(index);
                                item.replaceRefs(newItem);
                                loadedProjectItems.remove(index);
                            }
                        }

                        finderItems.addAll(loadedProjectItems);
                    }
                }

                // add finder items to menuItems
                final IExternalFinderItemMenuGenerator generator = new ExternalFinderItemMenuGenerator(finderItems, true);
                final List<Component> newMenuItems = generator.generate();
                menuItems.addAll(newMenuItems);

                // add menuItems to menu
                final JMenu toolsMenu = Core.getMainWindow().getMainMenu().getToolsMenu();
                for (Component component : menuItems) {
                    toolsMenu.add(component);
                }
            }

            private void onClose() {
                // remove menu items
                final JMenu menu = Core.getMainWindow().getMainMenu().getToolsMenu();
                for (int i = 0, n = menuItems.size(); i < n; i++) {
                    menu.remove(menuItems.get(i));
                }
                menuItems.clear();

                synchronized (finderItems) {
                    finderItems.clear();
                }
            }
        };
    }

    private static IApplicationEventListener generateIApplicationEventListener(final List<ExternalFinderItem> finderItems) {
        return new IApplicationEventListener() {

            @Override
            public void onApplicationStartup() {
                Core.getEditor().registerPopupMenuConstructors(2500, new ExternalFinderItemPopupMenuConstructor(finderItems));
            }

            @Override
            public void onApplicationShutdown() {
            }
        };
    }
}
