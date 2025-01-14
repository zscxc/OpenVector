package com.openvector.milvusplus.modeltest;

import com.openvector.dbmilvusplus.handler.OpenVectorService;
import com.openvector.dbmilvusplus.wrapper.OpenVectorWrapper;
import com.openvector.milvusplus.modeltest.model.Face2;
import com.openvector.modelcore.coordinator.ModelCoordinator;
import io.github.javpower.vectorexcore.util.GsonUtil;
import org.dromara.milvus.plus.model.vo.MilvusResp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ModelTestApplicationTests2 {

    @Autowired
    private ModelCoordinator coordinator;
    @Test
    void contextLoads() {
        OpenVectorService openVectorService=new OpenVectorService(Face2.class,coordinator);

        System.out.printf("===========");
        Face2 face = new Face2();
        face.setId(1L);
        face.setName("小明");
        face.setImage("/Users/xgc/Desktop/desk/测试人脸.jpeg");
        openVectorService.insert(face);

        System.out.printf("===========");
        OpenVectorWrapper<Face2> openVectorWrapper = openVectorService.queryWrapper();

        MilvusResp query = openVectorWrapper.
                vector(Face2::getImageVector, "/Users/xgc/Desktop/desk/测试人脸.jpeg").
                topK(1).
                query();
        System.out.printf(GsonUtil.toJson(query));

    }

}
