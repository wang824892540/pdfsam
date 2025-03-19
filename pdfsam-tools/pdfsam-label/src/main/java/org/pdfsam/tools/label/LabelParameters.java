package org.pdfsam.tools.label;

import org.sejda.model.input.PdfFileSource;
import org.sejda.model.output.FileOrDirectoryTaskOutput;
import org.sejda.model.output.SingleOrMultipleTaskOutput;
import org.sejda.model.parameter.base.MultiplePdfSourceMultipleOutputParameters;

import java.io.File;

public class LabelParameters extends MultiplePdfSourceMultipleOutputParameters {

    public LabelParameters() {
        addSource(PdfFileSource.newInstanceNoPassword(new File("C:\\Users\\hxw\\Desktop\\xx.pdf")));
        setOutput(new FileOrDirectoryTaskOutput(new File("C:\\Users\\hxw\\Desktop\\xx.pdf")));
    }

}
