package com.openvector.milvusplus.modeltest;

import com.openvector.dbvectorex.handler.OpenVectorService;
import com.openvector.dbvectorex.wrapper.OpenVectorWrapper;
import com.openvector.milvusplus.modeltest.model.Face;
import com.openvector.modelcore.coordinator.ModelCoordinator;
import io.github.javpower.vectorexbootstater.core.VectoRexResult;
import io.github.javpower.vectorexcore.util.GsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ModelTestApplicationTests {

    @Autowired
    private ModelCoordinator coordinator;
    @Test
    void contextLoads() {
        //构建openVectorService
        OpenVectorService openVectorService=new OpenVectorService(Face.class,coordinator);

        System.out.printf("===========");
        Face face = new Face();
        face.setId(1L);
        face.setName("小明");
        face.setImage("/Users/xgc/Desktop/desk/测试人脸.jpeg");
        openVectorService.insert(face);

        System.out.printf("===========");
        OpenVectorWrapper<Face> openVectorWrapper = openVectorService.queryWrapper();
        List<VectoRexResult<Face>> query = openVectorWrapper.
                vector(Face::getImageVector, "/Users/xgc/Desktop/desk/测试人脸2.jpeg").
                topK(1).
                query();
        System.out.printf(GsonUtil.toJson(query));

    }

}
