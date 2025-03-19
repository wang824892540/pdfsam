import org.pdfsam.model.tool.Tool;

module org.pdfsam.tools.label {
    requires org.pdfsam.ui.components;
    requires org.kordamp.ikonli.javafx;
    requires org.pdfsam.i18n;

    exports org.pdfsam.tools.label;

    provides Tool with org.pdfsam.tools.label.LabelTool;
}
