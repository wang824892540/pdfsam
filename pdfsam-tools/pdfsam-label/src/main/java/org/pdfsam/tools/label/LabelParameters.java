package org.pdfsam.tools.label;

import org.sejda.model.input.PdfFileSource;
import org.sejda.model.parameter.base.MultiplePdfSourceMultipleOutputParameters;

public class LabelParameters extends MultiplePdfSourceMultipleOutputParameters {

    PdfFileSource backPdf;
    Float width;
    Float height;
    String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public LabelParameters() {
    }

    public PdfFileSource getBackPdf() {
        return backPdf;
    }

    public void setBackPdf(PdfFileSource backPdf) {
        this.backPdf = backPdf;
    }
}
