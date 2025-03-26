package org.pdfsam.tools.label;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.pdfsam.eventstudio.StaticStudio;
import org.pdfsam.ui.components.notification.AddNotificationRequest;
import org.pdfsam.ui.components.notification.NotificationType;
import org.sejda.model.exception.TaskException;
import org.sejda.model.input.PdfFileSource;
import org.sejda.model.input.PdfSource;
import org.sejda.model.notification.event.PercentageOfWorkDoneChangedEvent;
import org.sejda.model.output.*;
import org.sejda.model.pdf.PdfVersion;
import org.sejda.model.task.BaseTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.pdfsam.i18n.I18nContext.i18n;
import static org.pdfsam.tools.label.LabelTool.TOOL_ID;

public class LabelBaseTask extends BaseTask<LabelParameters> {

    private static final Logger LOG = LoggerFactory.getLogger(LabelBaseTask.class);

    @Override
    public void execute(LabelParameters parameters) throws TaskException {
        final PdfFileSource backPdf = parameters.getBackPdf();
        final List<PdfSource<?>> sourceList = parameters.getSourceList();
        final File destination = parameters.getOutput().getDestination();
        String pathName = STR."\{destination.getAbsoluteFile()}/\{parameters.getFileName()}.pdf";
        File outputFile = new File(pathName);
        final PdfVersion version = parameters.getVersion();
        final double pdfVersion = version.getVersion();
        final ExistingOutputPolicy outputPolicy = parameters.getExistingOutputPolicy();
        processingPercentage(10);
        switch (outputPolicy) {
            case OVERWRITE:
                break;
            case RENAME:
                // 重命名
                if (outputFile.exists()) {
                    final String format = Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HH-mm-ss"));
                    pathName = STR."\{destination.getAbsoluteFile()}/\{parameters.getFileName()}-\{format}.pdf";
                    outputFile = new File(pathName);
                }
                break;
            case SKIP, FAIL:
            case null, default:
                return;
        }
        processingPercentage(15);

        if (Objects.nonNull(sourceList) && sourceList.size() == 1) {
            final PdfFileSource pdfSource = (PdfFileSource) sourceList.getFirst();
            File finalOutputFile = outputFile;
            parameters.getOutput().accept(new TaskOutputDispatcher() {
                @Override
                public void dispatch(FileTaskOutput output) throws IOException {
                }

                @Override
                public void dispatch(DirectoryTaskOutput output) throws IOException {
                }

                @Override
                public void dispatch(FileOrDirectoryTaskOutput output) throws IOException {
                    try {
                        merge(
                                backPdf.getSource(),
                                pdfSource.getSource(),
                                finalOutputFile,
                                parameters.getWidth(),
                                parameters.getHeight(),
                                pdfVersion
                        );
                    } catch (Throwable throwable) {
                        StaticStudio.eventStudio().broadcast(new AddNotificationRequest(NotificationType.ERROR,
                                i18n().tr(throwable.getLocalizedMessage()),
                                i18n().tr("ERROR")), TOOL_ID);
                        throw throwable;
                    }
                }
            });
        }
    }

    /**
     * reportBack
     */
    private void processingPercentage(BigDecimal percent) {
        StaticStudio.eventStudio().broadcast(
                new PercentageOfWorkDoneChangedEvent(percent, executionContext().notifiableTaskMetadata()), TOOL_ID
        );
    }

    /**
     * reportBack
     */
    private void processingPercentage(int percent) {
        processingPercentage(BigDecimal.valueOf(percent));
    }

    @Override
    public void after() {
    }

    private static final int DPI = 25; // 使用 72 DPI 进行渲染
    private static final int BATCH_SIZE = 10; // 每批处理 10 页
    // 将 7 厘米和 6 厘米转换为点
    private static final float PER_UNIT = 28.35f;

    public void merge(File back, File pdfSource, File output, Float width, Float height, double pdfVersion) throws IOException {
        float pageWidth = PER_UNIT * width;
        float pageHeight = PER_UNIT * height;
        try (PDDocument documentA = PDDocument.load(back);
             PDDocument documentB = PDDocument.load(pdfSource);
             PDDocument outputDocument = new PDDocument()) {
            outputDocument.setVersion((float) pdfVersion);
            processingPercentage(20);
            PDFRenderer rendererB = new PDFRenderer(documentB);
            BufferedImage imageB = rendererB.renderImage(0, DPI);
            PDImageXObject pdImageB = LosslessFactory.createFromImage(outputDocument, imageB);

            PDFRenderer rendererA = new PDFRenderer(documentA);
            int pageCount = documentA.getNumberOfPages();
            processingPercentage(30);
            for (int start = 0; start < pageCount; start += BATCH_SIZE) {
                int end = Math.min(start + BATCH_SIZE, pageCount);
                for (int i = start; i < end; i++) {
                    final double p = ((start*BATCH_SIZE)+i) / (double) pageCount;
                    processingPercentage(30 + (int)(p*70));
                    BufferedImage imageA = rendererA.renderImage(i, DPI);
                    PDImageXObject pdImageA = LosslessFactory.createFromImage(outputDocument, imageA);

                    // 创建指定大小的页面
                    PDRectangle pageSize = new PDRectangle(pageWidth, pageHeight);
                    PDPage newPage = new PDPage(pageSize);
                    outputDocument.addPage(newPage);

                    try (PDPageContentStream contentStream = new PDPageContentStream(outputDocument, newPage)) {
                        // 计算上下两部分的可用高度
                        float halfHeight = pageHeight / 2;

                        // 计算文件 A 图像的缩放比例
                        float scaleAWidth = pageWidth / imageA.getWidth();
                        float scaleAHeight = halfHeight / imageA.getHeight();
                        float scaleA = Math.min(scaleAWidth, scaleAHeight);

                        // 计算文件 B 图像的缩放比例
                        float scaleBWidth = pageWidth / imageB.getWidth();
                        float scaleBHeight = halfHeight / imageB.getHeight();
                        float scaleB = Math.min(scaleBWidth, scaleBHeight);

                        // 计算文件 A 图像的绘制位置，使其居中
                        float xA = (pageWidth - imageA.getWidth() * scaleA) / 2;
                        float yA = halfHeight + (halfHeight - imageA.getHeight() * scaleA) / 2;

                        // 计算文件 B 图像的绘制位置，使其居中
                        float xB = (pageWidth - imageB.getWidth() * scaleB) / 2;
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
            LOG.info(STR."PDF 合并完成，输出文件路径: \{output.getAbsolutePath()}");
        }
    }


}
