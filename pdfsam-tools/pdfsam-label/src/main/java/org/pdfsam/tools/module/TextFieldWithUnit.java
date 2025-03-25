package org.pdfsam.tools.module;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

// 自定义一个带有单位的文本框组件
public class TextFieldWithUnit extends StackPane {

    public TextFieldWithUnit(TextField textField, String unit, Double minWidth) {
        Label unitLabel = new Label(unit);
        unitLabel.setStyle("-fx-text-fill: white; -fx-background-color: #6BE659; -fx-background-radius: 3px; -fx-padding: 0 3px;");
        StackPane.setAlignment(unitLabel, Pos.CENTER_RIGHT);
        StackPane.setMargin(unitLabel, new Insets(0, 4, 0, 0));

        // 监听文本变化事件
        textField.textProperty().addListener((_, _, _) -> adjustTextFieldWidth(textField, minWidth));

        // 初始化时调整宽度
        adjustTextFieldWidth(textField, minWidth);

        getChildren().addAll(textField, unitLabel);
    }


    private void adjustTextFieldWidth(TextField textField, Double minWidth) {
        // 创建一个 Text 对象来测量文本的宽度
        Text text = new Text(textField.getText());
        text.setFont(textField.getFont());
        double textWidth = text.getLayoutBounds().getWidth();

        // 加上一些额外的空间，例如文本框的内边距等
        double extraSpace = 50;
        double newWidth = textWidth + extraSpace;

        // 确保宽度不小于最小宽度
        if (newWidth < minWidth) {
            newWidth = minWidth;
        }

        // 设置文本框的宽度
        textField.setPrefWidth(newWidth);
    }

}