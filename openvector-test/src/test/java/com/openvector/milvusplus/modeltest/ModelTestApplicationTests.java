package com.openvector.milvusplus.modeltest;

import com.openvector.jdlib.face.FaceDlibUtil;
import com.openvector.jdlib.utils.FaceDescriptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.image.BufferedImage;
import java.util.List;

@SpringBootTest
class ModelTestApplicationTests {

    @Test
    void contextLoads() {
        BufferedImage bufferedImage = FaceDlibUtil.loadImage("/Users/xiongguochao/Desktop/desk/测试人脸.jpeg");
        List<FaceDescriptor> faceEmbedding = FaceDlibUtil.getFaceEmbedding(bufferedImage);
        FaceDlibUtil.drawFaceDescriptor(bufferedImage, faceEmbedding.get(0));
    }

}
