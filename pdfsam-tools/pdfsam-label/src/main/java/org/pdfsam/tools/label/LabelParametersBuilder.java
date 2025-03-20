package org.pdfsam.tools.label;

import org.pdfsam.core.support.params.MultiplePdfSourceMultipleOutputParametersBuilder;
import org.pdfsam.ui.components.selection.single.SingleSelectionPane;
import org.sejda.model.input.PdfFileSource;
import org.sejda.model.output.SingleOrMultipleTaskOutput;

import java.io.File;

/**
 *  标签工具参数
 *
 * @author y
 * @date 2025/03/19 18:27
 **/
public class LabelParametersBuilder extends MultiplePdfSourceMultipleOutputParametersBuilder<LabelParameters> {

    private PdfFileSource source;
    private PdfFileSource backPagesSource;
    private SingleOrMultipleTaskOutput output;

    @Override
    public LabelParameters build() {
        File sourceFile = source.getSource();
        File backPagesSourceSourceFile = backPagesSource.getSource();
        LabelParameters labelParameters = new LabelParameters();
        labelParameters.setSourceFile(sourceFile);
        labelParameters.setBackPagesSourceSourceFile(backPagesSourceSourceFile);
        return labelParameters;
    }

    public PdfFileSource getSource() {
        return source;
    }

    public void setSource(PdfFileSource source) {
        this.source = source;
    }

    @Override
    public SingleOrMultipleTaskOutput getOutput() {
        return output;
    }

    public void setOutput(SingleOrMultipleTaskOutput output) {
        this.output = output;
    }

    public PdfFileSource getBackPagesSource() {
        return backPagesSource;
    }

    public void setBackPagesSource(PdfFileSource backPagesSource) {
        this.backPagesSource = backPagesSource;
    }
}
