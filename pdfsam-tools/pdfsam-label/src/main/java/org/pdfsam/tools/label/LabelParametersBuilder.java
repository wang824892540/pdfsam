package org.pdfsam.tools.label;

import org.pdfsam.core.support.params.MultiplePdfSourceMultipleOutputParametersBuilder;

/**
 *  标签工具参数
 *
 * @author y
 * @date 2025/03/19 18:27
 **/
public class LabelParametersBuilder extends MultiplePdfSourceMultipleOutputParametersBuilder<LabelParameters> {

    @Override
    public LabelParameters build() {
        return new LabelParameters();
    }

}
