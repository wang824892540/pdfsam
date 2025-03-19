package org.pdfsam.tools.label;

import org.apache.commons.lang3.builder.Builder;
import org.pdfsam.ui.components.tool.BaseToolPanel;
import org.pdfsam.ui.components.tool.Footer;
import org.sejda.model.parameter.base.AbstractParameters;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 标签面板类
 *
 * @author y
 * @date 2025/03/19 18:27
 **/
public class LabelToolPanel extends BaseToolPanel {

    public LabelToolPanel(String toolId, Footer footer) {
        super(toolId, footer);
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
