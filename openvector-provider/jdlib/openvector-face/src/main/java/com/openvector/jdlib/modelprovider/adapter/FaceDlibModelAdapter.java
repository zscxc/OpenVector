package com.openvector.jdlib.modelprovider.adapter;

import com.openvector.jdlib.face.FaceDlibUtil;
import com.openvector.jdlib.utils.FaceDescriptor;
import com.openvector.modelcore.DataSource;
import com.openvector.modelcore.enums.DataType;
import com.openvector.modelcore.enums.ModelType;
import com.openvector.modelcore.exception.DataProcessingException;
import com.openvector.modelcore.interfaces.ModelProvider;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


/**
 * @author cxc
 */
@Service
public class FaceDlibModelAdapter implements ModelProvider {


    @Override
    public boolean supports(ModelType modelType, DataType dataType) {
        return modelType == ModelType.FACE_EMBEDDING
               && dataType == DataType.IMAGE;
    }

    @Override
    public List<Float> process(DataSource source) throws DataProcessingException {
        try {
            BufferedImage image = loadImage(source);

            List<FaceDescriptor> faceEmbeddings = FaceDlibUtil.getFaceEmbedding(image);

            if (!faceEmbeddings.isEmpty()) {
                FaceDescriptor firstFace = faceEmbeddings.get(0);
                float[] embedding = firstFace.getFaceEmbedding();
                if (embedding == null || embedding.length == 0) {
                    throw new DataProcessingException("Face embedding is null or empty");
                }
                List<Float> embeddingList = new ArrayList<>(embedding.length);
                for (float value : embedding) {
                    embeddingList.add(value);
                }

                return embeddingList;
            }

            throw new DataProcessingException("No face detected in the image");
        } catch (MalformedURLException e) {
            throw new DataProcessingException("Invalid URL provided", e);
        } catch (IOException e) {
            throw new DataProcessingException("Failed to load image", e);
        } catch (Exception e) {
            throw new DataProcessingException("Face embedding processing failed", e);
        }
    }

    private BufferedImage loadImage(DataSource source) throws IOException, DataProcessingException {
        switch (source.getSourceType()) {
            case BASE64:
                byte[] imageBytes = Base64.getDecoder().decode(source.getContent());
                return ImageIO.read(new ByteArrayInputStream(imageBytes));
            case FILE:
                return ImageIO.read(new File(source.getContent()));
            case URL:
                return ImageIO.read(new URL(source.getContent()));
            default:
                throw new DataProcessingException("Unsupported image source type");
        }
    }


}
