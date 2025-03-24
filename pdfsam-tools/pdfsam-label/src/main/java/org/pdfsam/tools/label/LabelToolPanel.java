package org.pdfsam.tools.label;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.builder.Builder;
import org.pdfsam.core.support.params.MultiplePdfSourceMultipleOutputParametersBuilder;
import org.pdfsam.ui.components.io.BrowsableOutputDirectoryField;
import org.pdfsam.ui.components.io.BrowsablePdfOutputField;
import org.pdfsam.ui.components.io.PdfDestinationPane;
import org.pdfsam.ui.components.prefix.PrefixPane;
import org.pdfsam.ui.components.selection.single.TaskParametersBuilderSingleSelectionPane;
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

//    private final PaperChoosePane paperChoosePane;
//    private final PrefixPane prefix;
    private final TaskParametersBuilderSingleSelectionPane selectionPane;
    private final PaperChoosePane paperChoosePane;
    private final BrowsableOutputDirectoryField destinationDirectoryField;
    private final PdfDestinationPane destinationPane;
//    PaperChoosePane paperChoosePane;

    @Inject
    public LabelToolPanel(@Named(TOOL_ID + "field") BrowsableOutputDirectoryField destinationDirectoryField,
                          @Named(TOOL_ID + "pane") PdfDestinationPane destinationPane,
                          @Named(TOOL_ID + "footer") Footer footer,
                          @Named(TOOL_ID + "prefix") PrefixPane prefix) {
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
        vBox.getChildren().addAll(titledPane(i18n().tr("A"), selectionPane), titledPane(i18n().tr("B"), paperChoosePane), titledPane(i18n().tr("output"), destinationPane));
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
        final LabelParametersBuilder labelParametersBuilder = new LabelParametersBuilder();
        paperChoosePane.apply(labelParametersBuilder, onError);
        selectionPane.apply(labelParametersBuilder, onError);
        var destinationField = destinationDirectoryField.getTextField();
        destinationField.validate();
        labelParametersBuilder.output(FileOrDirectoryTaskOutput.directory(new File(destinationField.getText())));
        return labelParametersBuilder;
    }

}

