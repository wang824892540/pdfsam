package org.pdfsam.tools.label;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.sejda.model.input.PdfSource;
import org.sejda.model.output.SingleOrMultipleTaskOutput;
import org.sejda.model.parameter.base.MultiplePdfSourceMultipleOutputParameters;
import org.sejda.model.validation.constraint.NoIntersections;

public class LabelParameters extends MultiplePdfSourceMultipleOutputParameters {

    PdfSource<?> backPagesSourceSourceFile;

    public LabelParameters() {
    }

    public PdfSource<?> getBackPagesSourceSourceFile() {
        return backPagesSourceSourceFile;
    }

    public void setBackPagesSourceSourceFile(PdfSource<?> backPagesSourceSourceFile) {
        this.backPagesSourceSourceFile = backPagesSourceSourceFile;
    }

}
