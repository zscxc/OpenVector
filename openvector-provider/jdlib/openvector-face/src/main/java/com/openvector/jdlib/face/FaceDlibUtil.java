package com.openvector.jdlib.face;

import com.openvector.jdlib.Jdlib;
import com.openvector.jdlib.utils.ImageUtils;
import com.openvector.jdlib.utils.FaceDescriptor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供基于 Dlib 库的人脸检测和特征提取工具类。
 */
public class FaceDlibUtil {
    private static Jdlib jdlib;

    public static Jdlib getInstance() {
        if (jdlib == null) {
             jdlib = new Jdlib(Jdlib.MODEL_PATH + "/shape_predictor_68_face_landmarks.dat", Jdlib.MODEL_PATH + "/dlib_face_recognition_resnet_model_v1.dat");
        }
        return jdlib;
    }

    /**
     * 获取图像中人脸的特征向量（Embedding）。
     *
     * @param img 输入的 BufferedImage 图像对象
     * @return 包含人脸特征向量的列表
     */
    public static List<FaceDescriptor> getFaceEmbedding(BufferedImage img) {
        // 初始化 Jdlib 对象，加载人脸关键点模型和人脸识别模型
        Jdlib jdlib = getInstance();

        // 获取图像中所有人脸的特征向量
        ArrayList<FaceDescriptor> faceDescriptors = jdlib.getFaceEmbeddings(img);

        // 返回人脸特征向量列表
        return faceDescriptors;
    }

    /**
     * 获取图像中人脸的关键点（Landmarks）。
     *
     * @param img 输入的 BufferedImage 图像对象
     * @return 包含人脸关键点的列表
     */
    public static List<FaceDescriptor> getFaceLandmarks(BufferedImage img) {
        // 初始化 Jdlib 对象，加载人脸关键点模型
        Jdlib jdlib = getInstance();
        // 获取图像中所有人脸的关键点
        ArrayList<FaceDescriptor> faceDescriptors = jdlib.getFaceLandmarks(img);
        // 返回人脸关键点列表
        return faceDescriptors;
    }

    /**
     * 获取图像中人脸的边界框（Bounding Boxes）。
     *
     * @param img 输入的 BufferedImage 图像对象
     * @return 包含人脸边界框的列表
     */
    public static List<Rectangle> getFaceBoxes(BufferedImage img) {
        // 初始化 Jdlib 对象，加载人脸关键点模型
        Jdlib jdlib = getInstance();
        // 获取图像中所有人脸的边界框
        ArrayList<Rectangle> faceBoxes = jdlib.detectFace(img);

        // 返回人脸边界框列表
        return faceBoxes;
    }

    /**
     * 在图像上绘制人脸特征描述符。
     *
     * @param img      输入的 BufferedImage 图像对象
     * @param facedes  需要绘制的人脸特征描述符
     */
    public static void drawFaceDescriptor(BufferedImage img, FaceDescriptor facedes) {
        // 使用 ImageUtils 工具类在图像上绘制人脸特征描述符
        ImageUtils.drawFaceDescriptor(img, facedes);
    }

    /**
     * 从文件路径加载图像。
     *
     * @param imagepath 图像文件的路径
     * @return 加载的 BufferedImage 图像对象
     */
    public static BufferedImage loadImage(String imagepath) {
        BufferedImage img = null;
        try {
            // 从指定路径加载图像
            img = ImageIO.read(new File(imagepath));
        } catch (IOException e) {
            // 如果加载失败，输出错误信息
            System.err.println("Error During Loading File: " + imagepath);
        }
        return img;
    }
}
