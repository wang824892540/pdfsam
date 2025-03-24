package org.pdfsam.tools.label;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.sejda.model.exception.TaskException;
import org.sejda.model.input.PdfFileSource;
import org.sejda.model.input.PdfSource;
import org.sejda.model.output.DirectoryTaskOutput;
import org.sejda.model.output.FileOrDirectoryTaskOutput;
import org.sejda.model.output.FileTaskOutput;
import org.sejda.model.output.TaskOutputDispatcher;
import org.sejda.model.task.BaseTask;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class LabelBaseTask extends BaseTask<LabelParameters> {

    @Override
    public void execute(LabelParameters parameters) throws TaskException {
        final PdfFileSource backPdf = parameters.getBackPdf();
        final List<PdfSource<?>> sourceList = parameters.getSourceList();
        if (Objects.nonNull(sourceList) && sourceList.size() == 1) {
            final PdfFileSource pdfSource = (PdfFileSource)sourceList.getFirst();
            final File destination = parameters.getOutput().getDestination();
            final String backPdfName = backPdf.getSource().getName();
            final String pdfSourceName = pdfSource.getSource().getName();
            parameters.getOutput().accept(new TaskOutputDispatcher() {
                @Override
                public void dispatch(FileTaskOutput output) throws IOException { }

                @Override
                public void dispatch(DirectoryTaskOutput output) throws IOException { }

                @Override
                public void dispatch(FileOrDirectoryTaskOutput output) throws IOException {
                    merge(backPdf.getSource(), pdfSource.getSource(), new File(destination.getAbsoluteFile()+"/output.pdf"));
                }
            });
        }
    }

    @Override
    public void after() {}

    private static final int DPI = 25; // 使用 72 DPI 进行渲染
    private static final int BATCH_SIZE = 10; // 每批处理 10 页
    // 将 7 厘米和 6 厘米转换为点
    private static final float PAGE_WIDTH = 7 * 28.35f;
    private static final float PAGE_HEIGHT = 6 * 28.35f;

    public void merge(File back, File pdfSource,File output) {
        try (PDDocument documentA = PDDocument.load(back);
             PDDocument documentB = PDDocument.load(pdfSource);
             PDDocument outputDocument = new PDDocument()) {

            PDFRenderer rendererB = new PDFRenderer(documentB);
            BufferedImage imageB = rendererB.renderImage(0, DPI);
            PDImageXObject pdImageB = LosslessFactory.createFromImage(outputDocument, imageB);

            PDFRenderer rendererA = new PDFRenderer(documentA);
            int pageCount = documentA.getNumberOfPages();

            for (int start = 0; start < pageCount; start += BATCH_SIZE) {
                int end = Math.min(start + BATCH_SIZE, pageCount);
                for (int i = start; i < end; i++) {
                    BufferedImage imageA = rendererA.renderImage(i, DPI);
                    PDImageXObject pdImageA = LosslessFactory.createFromImage(outputDocument, imageA);

                    // 创建指定大小的页面
                    PDRectangle pageSize = new PDRectangle(PAGE_WIDTH, PAGE_HEIGHT);
                    PDPage newPage = new PDPage(pageSize);
                    outputDocument.addPage(newPage);

                    try (PDPageContentStream contentStream = new PDPageContentStream(outputDocument, newPage)) {
                        // 计算上下两部分的可用高度
                        float halfHeight = PAGE_HEIGHT / 2;

                        // 计算文件 A 图像的缩放比例
                        float scaleAWidth = PAGE_WIDTH / imageA.getWidth();
                        float scaleAHeight = halfHeight / imageA.getHeight();
                        float scaleA = Math.min(scaleAWidth, scaleAHeight);

                        // 计算文件 B 图像的缩放比例
                        float scaleBWidth = PAGE_WIDTH / imageB.getWidth();
                        float scaleBHeight = halfHeight / imageB.getHeight();
                        float scaleB = Math.min(scaleBWidth, scaleBHeight);

                        // 计算文件 A 图像的绘制位置，使其居中
                        float xA = (PAGE_WIDTH - imageA.getWidth() * scaleA) / 2;
                        float yA = halfHeight + (halfHeight - imageA.getHeight() * scaleA) / 2;

                        // 计算文件 B 图像的绘制位置，使其居中
                        float xB = (PAGE_WIDTH - imageB.getWidth() * scaleB) / 2;
                        float yB = (halfHeight - imageB.getHeight() * scaleB) / 2;

                        // 绘制文件 A 的页面，放置在上方
                        contentStream.drawImage(pdImageA, xA, yA, imageA.getWidth() * scaleA, imageA.getHeight() * scaleA);
                        // 绘制文件 B 的页面，放置在下方
                        contentStream.drawImage(pdImageB, xB, yB, imageB.getWidth() * scaleB, imageB.getHeight() * scaleB);
                    }
                    // 释放图像资源
                    imageA.flush();
                }
                // 强制进行垃圾回收，释放内存
                System.gc();
            }

            outputDocument.save(output);
            System.out.println("PDF 合并完成，输出文件路径: " + output.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
