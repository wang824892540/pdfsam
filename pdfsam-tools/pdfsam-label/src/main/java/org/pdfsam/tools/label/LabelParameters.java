package org.pdfsam.tools.label;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.sejda.model.input.PdfFileSource;
import org.sejda.model.input.PdfSource;
import org.sejda.model.output.SingleOrMultipleTaskOutput;
import org.sejda.model.parameter.base.MultiplePdfSourceMultipleOutputParameters;
import org.sejda.model.validation.constraint.NoIntersections;

public class LabelParameters extends MultiplePdfSourceMultipleOutputParameters {

    PdfFileSource backPdf;

    public LabelParameters() {
    }

    public PdfFileSource getBackPdf() {
        return backPdf;
    }

    public void setBackPdf(PdfFileSource backPdf) {
        this.backPdf = backPdf;
    }
}
