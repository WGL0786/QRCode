package com.sxpc.qrcode.controller;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.sxpc.qrcode.service.QRCodeGenerator;
import com.sxpc.qrcode.utils.DESEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * ApiController 处理 RESTFul API 请求
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    @Autowired
    private DESEncryptor desEncryptor;

    /**
     * 处理生成二维码的 POST 请求
     *
     * @param plaintext 加密前的明文字符串，作为请求体传入
     * @return ResponseEntity 对象，包含 base64 编码的二维码图片或错误信息
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateQRCode(@RequestBody String plaintext) {
        try {
            // 加密明文
            String ciphertext = desEncryptor.encrypt(plaintext);
            // 生成二维码
            String base64Image = qrCodeGenerator.generateQRCode(ciphertext);
            // 返回base64编码的图片
            return ResponseEntity.ok(base64Image);
        } catch (Exception e) {
            // 输出异常信息
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 处理解密密文的 POST 请求
     *
     * @param ciphertext 加密后的密文字符串，作为请求体传入
     * @return ResponseEntity 对象，包含明文或错误信息
     */
    @PostMapping("/decrypt")
    public ResponseEntity<String> decryptText(@RequestBody String ciphertext) {
        try {
            // 解密密文
            String plaintext = desEncryptor.decrypt(ciphertext);
            // 返回明文
            return ResponseEntity.ok(plaintext);
        } catch (Exception e) {
            // 输出异常信息
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 从上传的二维码图片中解码内容
     *
     * @param file 上传的二维码图片文件
     * @return ResponseEntity<String> 返回解码后的字符串结果
     */
    @PostMapping("/decode")
    public ResponseEntity<String> decodeQRCodeImage(@RequestParam("file") MultipartFile file) {
        try {
            // 将上传的文件转换为 BufferedImage 类型的图片
            BufferedImage image = ImageIO.read(file.getInputStream());
            // 创建二维码解码器
            QRCodeReader reader = new QRCodeReader();
            // 将 BufferedImage 转换为 LuminanceSource
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            // 对 LuminanceSource 进行二值化处理并生成二值位图
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            // 对二值位图进行解码
            Result result = reader.decode(bitmap);
            // 获取解码结果字符串
            String ciphertext = result.getText();
            // 返回解码结果
            return ResponseEntity.ok(ciphertext);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}