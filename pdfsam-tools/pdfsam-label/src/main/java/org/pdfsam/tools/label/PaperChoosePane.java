package org.pdfsam.tools.label;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import org.pdfsam.core.support.params.TaskParametersBuildStep;
import org.pdfsam.ui.components.commons.ValidableTextField;
import org.pdfsam.ui.components.selection.single.TaskParametersBuilderSingleSelectionPane;
import org.pdfsam.ui.components.support.FXValidationSupport;
import org.sejda.conversion.PdfFileSourceAdapter;

import java.util.function.Consumer;

import static org.pdfsam.i18n.I18nContext.i18n;

/**
 * 欧代文件
 *
 * @author y
 * @date 2025/03/19 18:27
 **/
public class PaperChoosePane extends VBox implements TaskParametersBuildStep<LabelParametersBuilder>{

    private final TaskParametersBuilderSingleSelectionPane backpagesSourceField;

    public PaperChoosePane(String ownerModule) {
        backpagesSourceField = new TaskParametersBuilderSingleSelectionPane(ownerModule);
        this.backpagesSourceField.setPromptText(
                i18n().tr("Select or drag and drop the PDF whose pages will be repeated"));
        this.backpagesSourceField.setId("backpagesSource");
        setAlignment(Pos.TOP_CENTER);
        this.getChildren().add(backpagesSourceField);
    }

    @Override
    public void apply(LabelParametersBuilder builder, Consumer<String> onError) {
        final ValidableTextField textField = backpagesSourceField.getField().getTextField();
        textField.validate();
        if (textField.getValidationState() == FXValidationSupport.ValidationState.VALID) {
            builder.setBackPdf(new PdfFileSourceAdapter(textField.getText()).getPdfFileSource());
        } else {
            onError.accept(i18n().tr("The selected PDF document is invalid"));
        }

    }
}

