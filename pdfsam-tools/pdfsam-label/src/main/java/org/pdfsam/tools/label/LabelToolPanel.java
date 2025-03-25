package org.pdfsam.tools.label;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.builder.Builder;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.eventstudio.annotation.EventStation;
import org.pdfsam.model.tool.ClearToolRequest;
import org.pdfsam.ui.components.io.BrowsableOutputDirectoryField;
import org.pdfsam.ui.components.io.PdfDestinationPane;
import org.pdfsam.ui.components.selection.single.SingleSelectionPane;
import org.pdfsam.ui.components.selection.single.TaskParametersBuilderSingleSelectionPane;
import org.pdfsam.ui.components.support.FXValidationSupport;
import org.pdfsam.ui.components.support.Style;
import org.pdfsam.ui.components.tool.BaseToolPanel;
import org.pdfsam.ui.components.tool.Footer;
import org.sejda.model.output.FileOrDirectoryTaskOutput;
import org.sejda.model.parameter.base.AbstractParameters;

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
//    private final PdfDestinationPane destinationPane;
    private final SizeChoosePane sizeChoosePane;

    @Inject
    public LabelToolPanel(@Named(TOOL_ID + "footer") Footer footer) {
        super(TOOL_ID, footer);
        this.selectionPane = new TaskParametersBuilderSingleSelectionPane(TOOL_ID);
        selectionPane.setSpacing(Style.DEFAULT_SPACING);
        selectionPane.getStyleClass().addAll(Style.CONTAINER.css());
        selectionPane.getStyleClass().addAll(Style.VCONTAINER.css());
        this.paperChoosePane = new PaperChoosePane(TOOL_ID);
        this.destinationDirectoryField = new BrowsableOutputDirectoryField();
        this.sizeChoosePane = new SizeChoosePane(destinationDirectoryField, TOOL_ID);
//        this.destinationPane = new PdfDestinationPane(destinationDirectoryField, TOOL_ID, false);
//        this.prefix = prefix;
        initModuleSettingsPanel(getPanel());
    }

    private VBox getPanel() {
        // 创建 VBox 并添加按钮
        VBox vBox = new VBox();
        vBox.getChildren().addAll(
            titledPane(i18n().tr("odet file"), selectionPane),
            titledPane(i18n().tr("barcode file"), paperChoosePane),
            titledPane(i18n().tr("combined setting"), sizeChoosePane)
//            titledPane(i18n().tr("output setting"), destinationPane)
        );
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
//        destinationPane.apply(builder, onError);
        paperChoosePane.apply(builder, onError);
        selectionPane.apply(builder, onError);
        sizeChoosePane.apply(builder, onError);
        return builder;
    }


    @EventStation
    public String id() {
        return TOOL_ID;
    }

}

