package org.pdfsam.tools.label;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.pdfsam.core.support.params.TaskParametersBuildStep;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.eventstudio.annotation.EventStation;
import org.pdfsam.model.tool.ClearToolRequest;
import org.pdfsam.model.tool.ToolBound;
import org.pdfsam.model.ui.ResettableView;
import org.pdfsam.model.ui.workspace.RestorableView;
import org.pdfsam.tools.module.TextFieldWithUnit;
import org.pdfsam.ui.components.commons.ValidableTextField;
import org.pdfsam.ui.components.selection.single.SingleSelectionPane;
import org.pdfsam.ui.components.support.FXValidationSupport;
import org.pdfsam.ui.components.support.Style;
import org.sejda.conversion.PdfFileSourceAdapter;

import java.util.Map;
import java.util.function.Consumer;

import static org.pdfsam.eventstudio.StaticStudio.eventStudio;
import static org.pdfsam.i18n.I18nContext.i18n;
import static org.pdfsam.tools.label.LabelTool.TOOL_ID;

/**
 * 欧代文件
 *
 * @author y
 * @date 2025/03/19 18:27
 **/
public class PaperChoosePane extends VBox implements TaskParametersBuildStep<LabelParametersBuilder>, RestorableView, ToolBound {

    private final SingleSelectionPane backpagesSourceField;
    private String toolBinding;

    public PaperChoosePane(String ownerModule) {
        super(Style.DEFAULT_SPACING);
        getStyleClass().addAll(Style.CONTAINER.css());
        getStyleClass().addAll(Style.VCONTAINER.css());
        backpagesSourceField = new SingleSelectionPane(ownerModule);
        this.toolBinding = ownerModule;
        this.backpagesSourceField.setPromptText(
                i18n().tr("Select or drag and drop the PDF whose pages will be repeated"));
        this.backpagesSourceField.setId("backpagesSource");
        this.getChildren().addAll(backpagesSourceField);
        eventStudio().addAnnotatedListeners(this);
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



    @Override
    @EventStation
    public String toolBinding() {
        return toolBinding;
    }

    @EventListener
    public void clear(ClearToolRequest event) {
        if (event.clearEverything()) {
            backpagesSourceField.onClearSelected(event);
        }
    }

    @Override
    public void saveStateTo(Map<String, String> data) {
        backpagesSourceField.saveStateTo(data);
        data.put(TOOL_ID+".toolBinding", toolBinding);
    }

    @Override
    public void restoreStateFrom(Map<String, String> data) {
        backpagesSourceField.restoreStateFrom(data);
        final String toolBinding = data.get(TOOL_ID + ".toolBinding");
        this.toolBinding = toolBinding;
    }
}

