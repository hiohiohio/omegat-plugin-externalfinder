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

import java.awt.Component;
import java.util.List;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;
import org.omegat.core.Core;
import org.omegat.gui.editor.IPopupMenuConstructor;
import org.omegat.gui.editor.SegmentBuilder;

public class ExternalFinderItemPopupMenuConstructor implements IPopupMenuConstructor {

    private final List<ExternalFinderItem> finderItems;

    public ExternalFinderItemPopupMenuConstructor(List<ExternalFinderItem> finderItems) {
        this.finderItems = finderItems;
    }

    public void addItems(JPopupMenu menu, JTextComponent comp, int mousepos,
            boolean isInActiveEntry, boolean isInActiveTranslation, SegmentBuilder sb) {
        final String selection = Core.getEditor().getSelectedText();
        if (selection == null) {
            return;
        }

        IExternalFinderItemMenuGenerator generator = new ExternalFinderItemMenuGenerator(finderItems, false);
        final List<Component> newMenuItems = generator.generate();

        for (Component component : newMenuItems) {
            menu.add(component);
        }
    }
}
