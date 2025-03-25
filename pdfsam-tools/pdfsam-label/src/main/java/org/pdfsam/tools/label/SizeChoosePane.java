package org.pdfsam.tools.label;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.pdfsam.core.support.params.TaskParametersBuildStep;
import org.pdfsam.core.support.validation.Validators;
import org.pdfsam.model.ui.ResettableView;
import org.pdfsam.tools.module.TextFieldWithUnit;
import org.pdfsam.ui.components.commons.ValidableTextField;
import org.pdfsam.ui.components.support.Style;

import java.time.Instant;
import java.util.function.Consumer;

import static org.pdfsam.i18n.I18nContext.i18n;

/**
 * 欧代文件
 *
 * @author y
 * @date 2025/03/19 18:27
 **/
public class SizeChoosePane extends VBox implements TaskParametersBuildStep<LabelParametersBuilder>, ResettableView {

    private final ValidableTextField width;
    private final ValidableTextField height;
    private final ValidableTextField fileName;

    public SizeChoosePane(String ownerModule) {
        super(Style.DEFAULT_SPACING);
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
        width.setPromptText(i18n().tr("height"));
        height.setErrorMessage(i18n().tr("height cannot be empty"));
        final TextFieldWithUnit heightWithUnit = new TextFieldWithUnit(height, "cm", 55D);

        fileName = new ValidableTextField();
        fileName.setText(STR."output_\{Instant.now().getEpochSecond()}");
        fileName.setPromptText(i18n().tr("file name"));
        fileName.setValidator(Validators.nonBlank());
        fileName.setErrorMessage(i18n().tr("file name cannot be empty"));
        final TextFieldWithUnit fileNameUnit = new TextFieldWithUnit(fileName, ".pdf", 110D);

        // 使用 HBox 将 width 和 height 放在同一行

        final Label fileNameLabel = new Label(i18n().tr("FileName: "));
        HBox widthHeightBoxFileName = new HBox(Style.DEFAULT_SPACING);
        widthHeightBoxFileName.setAlignment(Pos.CENTER_LEFT);
        widthHeightBoxFileName.getChildren().addAll(fileNameLabel, fileNameUnit);

        HBox widthHeightBox = new HBox(Style.DEFAULT_SPACING);
        widthHeightBox.setAlignment(Pos.CENTER_LEFT);
        final Label widthLabel = new Label(i18n().tr("Width: "));
        final Label heightLabel = new Label(i18n().tr("Height: "));
        widthHeightBox.getChildren().addAll(widthLabel, widthWithUnit, heightLabel, heightWithUnit);

        this.getChildren().addAll(widthHeightBoxFileName, widthHeightBox);
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
    }

    @Override
    public void resetView() {
        width.setText("7");
        height.setText("6");
    }


}

