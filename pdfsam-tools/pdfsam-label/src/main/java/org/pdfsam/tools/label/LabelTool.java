package org.pdfsam.tools.label;

import jakarta.inject.Named;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.unicons.UniconsLine;
import org.pdfsam.injector.Auto;
import org.pdfsam.injector.Provides;
import org.pdfsam.model.tool.*;
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
 * 标签工具
 *
 * @author y
 * @date 2025/03/19 18:27
 **/
@Auto
public class LabelTool implements Tool {

    static final String TOOL_ID = "label";

    private final ToolDescriptor descriptor = builder().category(ToolCategory.LABEL)
            .inputTypes(ToolInputOutputType.MULTIPLE_PDF, ToolInputOutputType.SINGLE_PDF).name(i18n().tr("Label"))
            .description(i18n().tr("Label pages from PDF documents.")).priority(ToolPriority.DEFAULT.getPriority())
            //.supportURL("https://pdfsam.org/pdf-extract-pages/")
            .build();

    @Override
    public String id() {
        return TOOL_ID;
    }

    @Override
    public ToolDescriptor descriptor() {
        return descriptor;
    }

    @Override
    public Pane panel() {
        return app().instance(LabelToolPanel.class);
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
