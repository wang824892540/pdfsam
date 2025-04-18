package org.pdfsam.tools.label;

import jakarta.inject.Named;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.unicons.UniconsLine;
import org.pdfsam.injector.Auto;
import org.pdfsam.injector.Provides;
import org.pdfsam.model.tool.*;
import org.pdfsam.ui.components.tool.Footer;
import org.pdfsam.ui.components.tool.OpenButton;
import org.pdfsam.ui.components.tool.RunButton;

import static org.pdfsam.core.context.ApplicationContext.app;
import static org.pdfsam.i18n.I18nContext.i18n;
import static org.pdfsam.model.tool.ToolDescriptorBuilder.builder;

/**
 * 标签工具
 *
 * @author y
 * @date 2025/03/19 18:27
 **/
@Auto
public class LabelTool implements Tool {

    static final String TOOL_ID = "Shein-Label";

    private final ToolDescriptor descriptor = builder().category(ToolCategory.LABEL)
            .inputTypes(ToolInputOutputType.MULTIPLE_PDF, ToolInputOutputType.SINGLE_PDF).name(i18n().tr(TOOL_ID))
            .description(i18n().tr("Shein Label from PDF documents.")).priority(ToolPriority.DEFAULT.getPriority())
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
        var icon = new FontIcon(UniconsLine.CODE_BRANCH);
        icon.getStyleClass().addAll(this.descriptor().category().styleClass(), "tool-icon");
        return icon;
    }

    public static class ModuleConfig {

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

    }

    @Override
    public boolean enable() {
        return true;
    }
}
