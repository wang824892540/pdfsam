package org.pdfsam.tools.label;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.pdfsam.core.context.BooleanPersistentProperty;
import org.pdfsam.core.support.params.TaskParametersBuildStep;
import org.pdfsam.core.support.validation.Validators;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.eventstudio.annotation.EventStation;
import org.pdfsam.model.io.FileType;
import org.pdfsam.model.io.OpenType;
import org.pdfsam.model.tool.ClearToolRequest;
import org.pdfsam.model.tool.ToolBound;
import org.pdfsam.model.ui.ResettableView;
import org.pdfsam.model.ui.SetDestinationRequest;
import org.pdfsam.model.ui.workspace.RestorableView;
import org.pdfsam.tools.module.TextFieldWithUnit;
import org.pdfsam.ui.components.commons.ValidableTextField;
import org.pdfsam.ui.components.io.BrowsableFileField;
import org.pdfsam.ui.components.io.BrowsableOutputDirectoryField;
import org.pdfsam.ui.components.io.PdfDestinationPane;
import org.pdfsam.ui.components.selection.single.SingleSelectionPane;
import org.pdfsam.ui.components.support.Style;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Consumer;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.pdfsam.core.context.ApplicationContext.app;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;
import static org.pdfsam.i18n.I18nContext.i18n;
import static org.pdfsam.tools.label.LabelTool.TOOL_ID;

/**
 * 欧代文件
 *
 * @author y
 * @date 2025/03/19 18:27
 **/
public class SizeChoosePane extends VBox implements TaskParametersBuildStep<LabelParametersBuilder>, ToolBound, RestorableView {

    private final ValidableTextField width;
    private final ValidableTextField height;
    private final ValidableTextField fileName;
    private final PdfDestinationPane pdfDestinationPane;
    private String toolBinding;

    @Inject
    public SizeChoosePane(BrowsableOutputDirectoryField destinationPane, String owner) {
        super(Style.DEFAULT_SPACING);
        this.toolBinding = owner;
        getStyleClass().addAll(Style.CONTAINER.css());
        getStyleClass().addAll(Style.VCONTAINER.css());
        width = new ValidableTextField();
        width.setText("7");
        width.setValidator(Validators.nonBlank());
        width.setPromptText(i18n().tr("width"));
        width.setErrorMessage(i18n().tr("width cannot be empty"));
        final TextFieldWithUnit widthWithUnit = new TextFieldWithUnit(width, "cm", 55D);

        height = new ValidableTextField();
        height.setText("6");
        height.setValidator(Validators.nonBlank());
        height.setPromptText(i18n().tr("height"));
        height.setErrorMessage(i18n().tr("height cannot be empty"));
        final TextFieldWithUnit heightWithUnit = new TextFieldWithUnit(height, "cm", 55D);

        fileName = new ValidableTextField();
        final String format = Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HH-mm-ss"));
        fileName.setText(STR."shein-\{format }");
        fileName.setPromptText(i18n().tr("file name"));
        fileName.setValidator(Validators.nonBlank());
        fileName.setErrorMessage(i18n().tr("file name cannot be empty"));
        final TextFieldWithUnit fileNameUnit = new TextFieldWithUnit(fileName, ".pdf", 110D);

        pdfDestinationPane = new PdfDestinationPane(destinationPane, owner, false);

        final Label fileNameLabel = new Label(i18n().tr("file name"));
        HBox widthHeightBoxFileName = new HBox(Style.DEFAULT_SPACING);
        widthHeightBoxFileName.setAlignment(Pos.CENTER_LEFT);
        widthHeightBoxFileName.getChildren().addAll(fileNameLabel, fileNameUnit);

        HBox widthHeightBox = new HBox(Style.DEFAULT_SPACING);
        widthHeightBox.setAlignment(Pos.CENTER_LEFT);
        final Label widthLabel = new Label(i18n().tr("width"));
        final Label heightLabel = new Label(i18n().tr("height"));
        widthHeightBox.getChildren().addAll(widthLabel, widthWithUnit, heightLabel, heightWithUnit);
        this.getChildren().addAll(pdfDestinationPane, widthHeightBoxFileName, widthHeightBox);
        eventStudio().addAnnotatedListeners(this);
    }

    @Override
    public void apply(LabelParametersBuilder builder, Consumer<String> onError) {
        final String widthDouble = width.getText();
        final String heightDouble = height.getText();
        final String fileNameText = fileName.getText();
        if (StringUtils.isBlank(widthDouble) || !NumberUtils.isDigits(widthDouble)) {
            onError.accept(i18n().tr("width cannot be empty"));
            return;
        }
        if (StringUtils.isBlank(heightDouble) || !NumberUtils.isDigits(heightDouble)) {
            onError.accept(i18n().tr("height cannot be empty"));
            return;
        }
        if (StringUtils.isBlank(fileNameText)) {
            onError.accept(i18n().tr("file name cannot be empty"));
            return;
        }

        builder.setWidth(Float.valueOf(widthDouble));
        builder.setHeight(Float.valueOf(heightDouble));
        builder.setFileName(fileNameText);
        pdfDestinationPane.apply(builder, onError);
    }

    @Override
    @EventStation
    public String toolBinding() {
        return toolBinding;
    }

    @EventListener
    public void onClearModule(ClearToolRequest event) {
        if (event.clearEverything()) {
            pdfDestinationPane.resetView();
            width.setText("7");
            height.setText("6");
            final String format = Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HH-mm-ss"));
            fileName.setText(STR."shein-\{format }");
        }
    }

    @Override
    public void saveStateTo(Map<String, String> data) {
        pdfDestinationPane.saveStateTo(data);
        data.put(TOOL_ID + ".toolBinding",toolBinding);
        data.put(TOOL_ID + ".width",width.getText());
        data.put(TOOL_ID + ".height",height.getText());
        data.put(TOOL_ID + ".fileName",fileName.getText());
    }

    @Override
    public void restoreStateFrom(Map<String, String> data) {
        pdfDestinationPane.restoreStateFrom(data);
        var toolBinding = data.get(TOOL_ID + ".toolBinding");
        var widthText = data.get(TOOL_ID + ".width");
        var heightText = data.get(TOOL_ID + ".height");
        var fileNameText = data.get(TOOL_ID + ".fileName");
        this.toolBinding = toolBinding;
        width.setText(widthText);
        height.setText(heightText);
        fileName.setText(fileNameText);
    }
}

