package org.pdfsam.tools.label;

import org.sejda.model.input.PdfFileSource;
import org.sejda.model.output.FileOrDirectoryTaskOutput;
import org.sejda.model.output.SingleOrMultipleTaskOutput;
import org.sejda.model.parameter.base.MultiplePdfSourceMultipleOutputParameters;

import java.io.File;

public class LabelParameters extends MultiplePdfSourceMultipleOutputParameters {

    File sourceFile;
    File backPagesSourceSourceFile;

    public LabelParameters() {
        addSource(PdfFileSource.newInstanceNoPassword(sourceFile));
        addSource(PdfFileSource.newInstanceNoPassword(backPagesSourceSourceFile));
        setOutput(new FileOrDirectoryTaskOutput(sourceFile.getParentFile()));
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public File getBackPagesSourceSourceFile() {
        return backPagesSourceSourceFile;
    }

    public void setBackPagesSourceSourceFile(File backPagesSourceSourceFile) {
        this.backPagesSourceSourceFile = backPagesSourceSourceFile;
    }
}
