/*
 * This file is part of the PDF Split And Merge source code
 * Created on 07/apr/2014
 * Copyright 2017 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pdfsam.tools.extract;

import jakarta.inject.Named;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.unicons.UniconsLine;
import org.pdfsam.injector.Auto;
import org.pdfsam.injector.Provides;
import org.pdfsam.model.tool.Tool;
import org.pdfsam.model.tool.ToolCategory;
import org.pdfsam.model.tool.ToolDescriptor;
import org.pdfsam.model.tool.ToolInputOutputType;
import org.pdfsam.model.tool.ToolPriority;
import org.pdfsam.persistence.PreferencesRepository;
import org.pdfsam.ui.components.io.BrowsableOutputDirectoryField;
import org.pdfsam.ui.components.io.PdfDestinationPane;
import org.pdfsam.ui.components.prefix.PrefixPane;
import org.pdfsam.ui.components.tool.Footer;
import org.pdfsam.ui.components.tool.OpenButton;
import org.pdfsam.ui.components.tool.RunButton;

import static org.pdfsam.core.context.ApplicationContext.app;
import static org.pdfsam.i18n.I18nContext.i18n;
import static org.pdfsam.model.tool.ToolDescriptorBuilder.builder;
import static org.pdfsam.ui.components.io.PdfDestinationPane.DestinationPanelFields.DISCARD_BOOKMARKS;

/**
 * Extract module to let the user extract pages from a single PDF document
 *
 * @author Andrea Vacondio
 */
@Auto
public class ExtractTool implements Tool {

    @Override
    public boolean enable() {
        return true;
    }

    static final String TOOL_ID = "extract";

    private final ToolDescriptor descriptor = builder().category(ToolCategory.SPLIT)
            .inputTypes(ToolInputOutputType.MULTIPLE_PDF, ToolInputOutputType.SINGLE_PDF).name(i18n().tr("Extract"))
            .description(i18n().tr("Extract pages from PDF documents.")).priority(ToolPriority.DEFAULT.getPriority())
            .supportURL("https://pdfsam.org/pdf-extract-pages/")
            .build();

    @Override
    public ToolDescriptor descriptor() {
        return descriptor;
    }

    @Override
    public Pane panel() {
        return app().instance(ExtractToolPanel.class);
    }

    @Override
    public String id() {
        return TOOL_ID;
    }

    @Override
    public Node graphic() {
        var icon = new FontIcon(UniconsLine.FILE_EXPORT);
        icon.getStyleClass().addAll(this.descriptor().category().styleClass(), "tool-icon");
        return icon;
    }

    public static class ModuleConfig {
        @Provides
        @Named(TOOL_ID + "field")
        public BrowsableOutputDirectoryField destinationDirectoryField() {
            return new BrowsableOutputDirectoryField();
        }

        @Provides
        @Named(TOOL_ID + "pane")
        public PdfDestinationPane destinationPane(@Named(TOOL_ID + "field") BrowsableOutputDirectoryField outputField) {
            return new PdfDestinationPane(outputField, TOOL_ID, DISCARD_BOOKMARKS);
        }

        @Provides
        @Named(TOOL_ID + "footer")
        public Footer footer(RunButton runButton, @Named(TOOL_ID + "openButton") OpenButton openButton) {
            return new Footer(runButton, openButton, TOOL_ID);
        }

        @Provides
        @Named(TOOL_ID + "openButton")
        public OpenButton openButton() {
            return new OpenButton(TOOL_ID, ToolInputOutputType.MULTIPLE_PDF);
        }

        @Provides
        @Named(TOOL_ID + "prefix")
        public PrefixPane prefixPane() {
            return new PrefixPane(TOOL_ID, new PreferencesRepository("/org/pdfsam/user/conf/" + TOOL_ID));
        }
    }

}
