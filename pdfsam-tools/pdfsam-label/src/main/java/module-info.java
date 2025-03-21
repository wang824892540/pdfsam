import org.pdfsam.model.tool.Tool;

module org.pdfsam.tools.label {
    requires org.pdfsam.ui.components;
    requires org.kordamp.ikonli.javafx;
    requires org.pdfsam.i18n;
    requires jakarta.inject;

    exports org.pdfsam.tools.label;
    requires org.apache.commons.lang3;
    opens org.pdfsam.tools.label to org.apache.commons.lang3;

    provides Tool with org.pdfsam.tools.label.LabelTool;
}
