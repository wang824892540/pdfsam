package org.pdfsam.tools.label;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.builder.Builder;
import org.pdfsam.ui.components.io.BrowsableOutputDirectoryField;
import org.pdfsam.ui.components.io.PdfDestinationPane;
import org.pdfsam.ui.components.prefix.PrefixPane;
import org.pdfsam.ui.components.tool.BaseToolPanel;
import org.pdfsam.ui.components.tool.Footer;
import org.sejda.model.parameter.base.AbstractParameters;

import java.util.Map;
import java.util.function.Consumer;

import static org.pdfsam.tools.label.LabelTool.TOOL_ID;

/**
 * 标签面板类
 *
 * @author y
 * @date 2025/03/19 18:27
 **/
public class LabelToolPanel extends BaseToolPanel {


    @Inject
    public LabelToolPanel(@Named(TOOL_ID + "footer") Footer footer) {
        super(TOOL_ID, footer);
        initModuleSettingsPanel(new VBox());
    }


    @Override
    public void onSaveWorkspace(Map<String, String> data) {

    }

    @Override
    public void onLoadWorkspace(Map<String, String> data) {

    }

    @Override
    protected Builder<? extends AbstractParameters> getBuilder(Consumer<String> onError) {
        return null;
    }
}
