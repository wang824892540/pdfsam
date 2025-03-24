package org.pdfsam.tools.label;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.builder.Builder;
import org.pdfsam.core.support.params.MultiplePdfSourceMultipleOutputParametersBuilder;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.eventstudio.annotation.EventStation;
import org.pdfsam.model.tool.ClearToolRequest;
import org.pdfsam.ui.components.io.BrowsableOutputDirectoryField;
import org.pdfsam.ui.components.io.BrowsablePdfOutputField;
import org.pdfsam.ui.components.io.PdfDestinationPane;
import org.pdfsam.ui.components.prefix.PrefixPane;
import org.pdfsam.ui.components.selection.single.TaskParametersBuilderSingleSelectionPane;
import org.pdfsam.ui.components.support.FXValidationSupport;
import org.pdfsam.ui.components.support.Views;
import org.pdfsam.ui.components.tool.BaseToolPanel;
import org.pdfsam.ui.components.tool.Footer;
import org.sejda.model.output.FileOrDirectoryTaskOutput;
import org.sejda.model.output.TaskOutput;
import org.sejda.model.parameter.base.AbstractParameters;
import org.sejda.model.parameter.base.MultiplePdfSourceMultipleOutputParameters;
import org.sejda.model.parameter.base.MultiplePdfSourceSingleOutputParameters;
import org.sejda.model.prefix.Prefix;

import java.io.File;
import java.util.Map;
import java.util.function.Consumer;

import static org.pdfsam.i18n.I18nContext.i18n;
import static org.pdfsam.tools.label.LabelTool.TOOL_ID;
import static org.pdfsam.ui.components.support.Views.titledPane;

/**
 * 标签面板类
 *
 * @author y
 * @date 2025/03/19 18:27
 **/
public class LabelToolPanel extends BaseToolPanel {

    private final TaskParametersBuilderSingleSelectionPane selectionPane;
    private final PaperChoosePane paperChoosePane;
    private final BrowsableOutputDirectoryField destinationDirectoryField;
    private final PdfDestinationPane destinationPane;

    @Inject
    public LabelToolPanel(@Named(TOOL_ID + "field") BrowsableOutputDirectoryField destinationDirectoryField,
                          @Named(TOOL_ID + "pane") PdfDestinationPane destinationPane,
                          @Named(TOOL_ID + "footer") Footer footer) {
        super(TOOL_ID, footer);
        this.selectionPane = new TaskParametersBuilderSingleSelectionPane(TOOL_ID);
        this.destinationDirectoryField = destinationDirectoryField;
        this.paperChoosePane = new PaperChoosePane(TOOL_ID);
        this.destinationPane = destinationPane;
//        this.prefix = prefix;
        initModuleSettingsPanel(getPanel());
    }

    private VBox getPanel() {
        // 创建 VBox 并添加按钮
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().addAll(selectionPane, titledPane(i18n().tr("B"), paperChoosePane), titledPane(i18n().tr("output"), destinationPane));
        return vBox;
    }


    @Override
    public void onSaveWorkspace(Map<String, String> data) {

    }

    @Override
    public void onLoadWorkspace(Map<String, String> data) {

    }

    @Override
    protected Builder<? extends AbstractParameters> getBuilder(Consumer<String> onError) {
        final LabelParametersBuilder builder = new LabelParametersBuilder();
        var destinationField = destinationDirectoryField.getTextField();
        destinationField.validate();
        if (destinationField.getValidationState() == FXValidationSupport.ValidationState.VALID) {
            builder.output(FileOrDirectoryTaskOutput.directory(new File(destinationField.getText())));
        } else {
            onError.accept(i18n().tr("A directory is required"));
        }
        destinationPane.apply(builder, onError);
        paperChoosePane.apply(builder, onError);
        selectionPane.apply(builder, onError);
        return builder;
    }


    @EventStation
    public String id() {
        return TOOL_ID;
    }

    @EventListener
    public void onClearModule(ClearToolRequest e) {
        if (e.clearEverything()) {
            paperChoosePane.resetView();
            destinationPane.resetView();
        }
    }

}

