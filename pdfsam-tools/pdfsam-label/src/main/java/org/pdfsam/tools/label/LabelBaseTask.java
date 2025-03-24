package org.pdfsam.tools.label;

import org.sejda.model.exception.TaskException;
import org.sejda.model.input.PdfSource;
import org.sejda.model.output.DirectoryTaskOutput;
import org.sejda.model.output.FileOrDirectoryTaskOutput;
import org.sejda.model.output.FileTaskOutput;
import org.sejda.model.output.TaskOutputDispatcher;
import org.sejda.model.task.BaseTask;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LabelBaseTask extends BaseTask<LabelParameters> {

    @Override
    public void execute(LabelParameters parameters) throws TaskException {
        List<PdfSource<?>> sourceList = parameters.getSourceList();
        parameters.getOutput().accept(new TaskOutputDispatcher() {
            @Override
            public void dispatch(FileTaskOutput output) throws IOException {
                System.out.printf("1");
            }

            @Override
            public void dispatch(DirectoryTaskOutput output) throws IOException {
                System.out.printf("2");
            }

            @Override
            public void dispatch(FileOrDirectoryTaskOutput output) throws IOException {
                System.out.printf("3");
            }
        });
    }

    @Override
    public void after() {

    }

}
