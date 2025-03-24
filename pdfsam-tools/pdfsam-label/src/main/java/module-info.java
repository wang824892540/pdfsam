import org.pdfsam.model.tool.Tool;

module org.pdfsam.tools.label {
    requires org.pdfsam.ui.components;
    requires org.kordamp.ikonli.javafx;
    requires org.pdfsam.i18n;
    requires jakarta.inject;
    requires org.hibernate.validator;
    requires org.sejda.core;
    requires org.apache.pdfbox;

    exports org.pdfsam.tools.label;
    opens org.pdfsam.tools.label to org.hibernate.validator,org.apache.commons.lang3;
    provides Tool with org.pdfsam.tools.label.LabelTool;
}
