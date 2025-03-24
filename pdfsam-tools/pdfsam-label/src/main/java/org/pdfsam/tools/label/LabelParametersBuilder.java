package org.pdfsam.tools.label;

import org.pdfsam.core.support.params.MultiplePdfSourceMultipleOutputParametersBuilder;
import org.pdfsam.core.support.params.SinglePdfSourceTaskParametersBuilder;
import org.pdfsam.ui.components.selection.single.SingleSelectionPane;
import org.sejda.model.input.PdfFileSource;
import org.sejda.model.output.ExistingOutputPolicy;
import org.sejda.model.output.SingleOrMultipleTaskOutput;

import java.io.File;

/**
 * 标签工具参数
 *
 * @author y
 * @date 2025/03/19 18:27
 **/
public class LabelParametersBuilder extends MultiplePdfSourceMultipleOutputParametersBuilder<LabelParameters> implements SinglePdfSourceTaskParametersBuilder<LabelParameters> {

    PdfFileSource backPdf;

    @Override
    public LabelParameters build() {
        LabelParameters labelParameters = new LabelParameters();
        labelParameters.setOutput(getOutput());
        labelParameters.setBackPdf(backPdf);
        labelParameters.setExistingOutputPolicy(ExistingOutputPolicy.OVERWRITE);
        getInputs().forEach(labelParameters::addSource);
        return labelParameters;
    }

    public PdfFileSource getBackPdf() {
        return backPdf;
    }

    public void setBackPdf(PdfFileSource backPdf) {
        this.backPdf = backPdf;
    }

    @Override
    public void source(PdfFileSource source) {
        addSource(source);
    }

}
