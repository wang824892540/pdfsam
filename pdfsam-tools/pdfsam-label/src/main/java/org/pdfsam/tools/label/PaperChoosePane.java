package org.pdfsam.tools.label;

import javafx.scene.layout.VBox;
import org.pdfsam.core.support.params.TaskParametersBuildStep;
import org.pdfsam.ui.components.selection.single.SingleSelectionPane;
import org.sejda.conversion.PdfFileSourceAdapter;
import org.sejda.model.input.PdfFileSource;

import java.util.function.Consumer;

import static org.pdfsam.i18n.I18nContext.i18n;

/**
 * 欧代文件
 *
 * @author y
 * @date 2025/03/19 18:27
 **/
public class PaperChoosePane extends VBox{

    private final SingleSelectionPane backpagesSourceField;

    public PaperChoosePane(String ownerModule) {
        backpagesSourceField = new SingleSelectionPane(ownerModule);
        this.backpagesSourceField.setPromptText(
                i18n().tr("Select or drag and drop the PDF whose pages will be repeated"));
        this.backpagesSourceField.setId("backpagesSource");
        this.getChildren().add(backpagesSourceField);
    }
}
