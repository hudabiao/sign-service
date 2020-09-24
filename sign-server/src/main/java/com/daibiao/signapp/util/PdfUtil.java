package com.daibiao.signapp.util;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PdfUtil
 *
 * @description PdfUtil
 * @author hudaibiao-1
 * @date 2020/9/23 20:19
 * @version v1.0.0
 */
@Slf4j
public class PdfUtil {

    private static final String TARGET_WORD = "甲方签字";

    /**
     * PdfUtil
     *
     * @description 在pdf上贴上图片
     * @author hudaibiao-1
     * @param path 文件路径
     * @param sourceFilename 修改前文件名
     * @param targetFilename 修改后文件名
     * @param imgBytes 图片字节数据
     * @date 2020/9/23 20:15
     * @version v1.0.0
     */
    public static void signImgToPdf(String path, String sourceFilename, String targetFilename, byte[] imgBytes) {
        String sourcePath = path + File.separator + sourceFilename;
        String targetPath = path + File.separator + targetFilename;

        // 读取模板文件
        try (InputStream inputStream = new FileInputStream(new File(sourcePath));
             OutputStream outputStream = new FileOutputStream(targetPath)
        ) {
            PdfReader pdfReader = new PdfReader(inputStream);
            PdfStamper stamper = new PdfStamper(pdfReader, outputStream);
            int pageNum = pdfReader.getNumberOfPages();
            Rectangle2D.Float keywordPostion = findKeywordPostions(pdfReader, pageNum);
            Image image = Image.getInstance(imgBytes);
            PdfContentByte under = stamper.getOverContent(pageNum);
            image.scaleToFit(200, 200);
            image.setAbsolutePosition(keywordPostion.x, keywordPostion.y-100);
            under.addImage(image);
            stamper.close();
            pdfReader.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (BadElementException e) {
            log.error(e.getMessage(), e);
        } catch (DocumentException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * PdfUtil
     *
     * @description 查找-目标文字在pdf的位置
     * @author hudaibiao-1
     * @param pdfReader pdfReader
     * @param pageNum 页数
     * @date 2020/9/23 20:18
     * @return 位置
     * @throws IOException IO异常
     * @version v1.0.0
     */
    private static Rectangle2D.Float findKeywordPostions(PdfReader pdfReader, int pageNum) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        List<Rectangle2D.Float> charPositions = new ArrayList<>(1000);
        PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);
        pdfReaderContentParser.processContent(pageNum, new RenderListener() {
            @Override
            public void beginTextBlock() {
            }

            @Override
            public void renderText(TextRenderInfo textRenderInfo) {
                String text = textRenderInfo.getText();
                stringBuilder.append(text);
                Rectangle2D.Float boundingRectange = textRenderInfo.getBaseline().getBoundingRectange();
                charPositions.add(boundingRectange);
            }

            @Override
            public void endTextBlock() {
            }

            @Override
            public void renderImage(ImageRenderInfo imageRenderInfo) {
            }
        });
        String content = stringBuilder.toString();
        int index = content.lastIndexOf(TARGET_WORD);
        if (index < 0) {
            index = charPositions.size()/2;
        }
        return charPositions.get(index);
    }
}
