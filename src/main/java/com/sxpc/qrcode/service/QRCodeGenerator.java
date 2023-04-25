package com.sxpc.qrcode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * QRCodeGenerator 类用于生成二维码图片并转换为 Base64 编码字符串
 */
@Component
public class QRCodeGenerator {
    // 定义二维码图片的宽度和高度
    private static final int WIDTH = 270;
    private static final int HEIGHT = 270;
    // 定义二维码图片的字符集和格式
    private static final String CHARSET = "UTF-8";
    private static final String FORMAT = "png";

    /**
     * 生成二维码图片并转换为 Base64 编码字符串
     *
     * @param text 二维码图片的文本内容
     * @return Base64 编码字符串
     * @throws IOException
     */
    public String generateQRCode(String text) throws IOException {
        // 设置二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // 设置字符集
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        // 设置纠错级别为中等
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 设置二维码边框宽度为2个单位长度
        hints.put(EncodeHintType.MARGIN, 2);

        // 生成二维码矩阵
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // 生成二维码图片
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                // 将二维码矩阵中的 0 和 1 转换为图片的黑白像素
                //image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFF5F5F5);
            }
        }

        // 将图片转换为 Base64 编码字符串
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, FORMAT, bos);   // 将图片写入输出流
        byte[] bytes = bos.toByteArray();    // 将输出流中的字节转换为字节数组
        // 将字节数组转换为 Base64 编码字符串
        String base64Image = Base64.getEncoder().encodeToString(bytes);

        return base64Image;
    }
}