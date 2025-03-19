package org.pdfsam.tools.label;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.unicons.UniconsLine;
import org.pdfsam.injector.Auto;
import org.pdfsam.model.tool.*;

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
}
