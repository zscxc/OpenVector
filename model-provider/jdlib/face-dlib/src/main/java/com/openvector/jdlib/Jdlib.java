package com.openvector.jdlib;


import com.openvector.jdlib.utils.FaceDescriptor;
import com.openvector.jdlib.utils.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Jdlib {

    private String facialLandmarksModelPath;
    private String faceEmbeddingModelPath;

    public static String MODEL_PATH="/model";

    public Jdlib(String facialLandmarksModelPath, String faceEmbeddingModelPath) {
        this.facialLandmarksModelPath = facialLandmarksModelPath;
        this.faceEmbeddingModelPath = faceEmbeddingModelPath;
        loadLib();
        loadModels();
    }

    public Jdlib(String facialLandmarksModelPath) {
        this.facialLandmarksModelPath = facialLandmarksModelPath;
        this.faceEmbeddingModelPath = null;
        loadLib();
        loadModels();
    }

    private native long getFaceDectorHandler();

    private native long getShapePredictorHandler(String modelPath);

    private native long getFaceEmbeddingHandler(String modelPath);

    private native ArrayList<Rectangle> faceDetect(long faceDetectorHandler, byte[] pixels, int h, int w);

    private native ArrayList<FaceDescriptor> getFacialLandmarks(long shapePredictorHandler, long faceDetectorHandler, byte[] pixels, int h, int w);

    private native ArrayList<FaceDescriptor> getFaceEmbeddings(long FaceEmbeddingHandler, long shapePredictorHandler, long faceDetectorHandler, byte[] pixels, int h, int w);

    private void loadLib() {
        String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        String arch = System.getProperty("os.arch", "generic").toLowerCase(Locale.ENGLISH);
        String name = System.mapLibraryName("Jdlib");

        String libpath = "";
        if (os.contains("win")) {
            if (arch.contains("64")) {
                libpath = "/native" + File.separator + "windows" + File.separator + "x86_64" + File.separator + name;
            } else {
                libpath = "/native" + File.separator + "windows" + File.separator + "x86" + File.separator + name;
            }
        } else if (os.contains("linux")) {
            if (arch.contains("aarch64") || arch.contains("arm64")) {
                libpath = "/native" + File.separator + "linux" + File.separator + "aarch64" + File.separator + name;
            } else {
                libpath = "/native" + File.separator + "linux" + File.separator + "x86_64" + File.separator + name;
            }
        } else if (os.contains("mac")) {
            if (arch.contains("aarch64") || arch.contains("arm64")) {
                libpath = "/native" + File.separator + "macosx" + File.separator + "aarch64" + File.separator + name;
            } else {
                libpath = "/native" + File.separator + "macosx" + File.separator + "x86_64" + File.separator + name;
            }
        } else {
            throw new java.lang.UnsupportedOperationException(os + " is not supported. Try to recompile Jdlib on your machine and then use it.");
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = Jdlib.class.getResourceAsStream(libpath);
            if (inputStream == null) {
                throw new FileNotFoundException("Native library not found: " + libpath);
            }
            File fileOut = File.createTempFile(name, "");

            outputStream = new FileOutputStream(fileOut);
            byte[] buffer = new byte[1024];
            int read = -1;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.close();
            inputStream.close();
            System.load(fileOut.toString());
        } catch (Exception e) {
            System.err.println("Error During Loading Lib: " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.err.println("Error During Closing Input Stream: " + e.getMessage());
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    System.err.println("Error During Closing Output Stream: " + e.getMessage());
                }
            }
        }
    }


    public ArrayList<Rectangle> detectFace(BufferedImage img) {
        Image image = new Image(img);
        ArrayList<Rectangle> data = faceDetect(getFaceDectorHandler(), image.pixels, image.height, image.width);
        if (data == null) {
            System.err.println("Jdlib | detectFace | Null data!!");
            data = new ArrayList<>(Collections.EMPTY_LIST);
        }
        return data;
    }

    public ArrayList<FaceDescriptor> getFaceLandmarks(BufferedImage img) {
        Image image = new Image(img);
        ArrayList<FaceDescriptor> data = getFacialLandmarks(getShapePredictorHandler(facialLandmarksModelPath), getFaceDectorHandler(), image.pixels, image.height, image.width);
        if (data == null) {
            System.err.println("Jdlib | getFaceLandmarks | Null data!!");
            data = new ArrayList<>(Collections.EMPTY_LIST);
        }
        return data;
    }

    public ArrayList<FaceDescriptor> getFaceEmbeddings(BufferedImage img) {
        if (facialLandmarksModelPath == null) {
            throw new IllegalArgumentException("Path to face embedding model isn't provided!");
        }
        Image image = new Image(img);
        ArrayList<FaceDescriptor> data = getFaceEmbeddings(getFaceEmbeddingHandler(faceEmbeddingModelPath), getShapePredictorHandler(facialLandmarksModelPath), getFaceDectorHandler(), image.pixels, image.height, image.width);
        if (data == null) {
            System.err.println("Jdlib | getFaceEmbeddings | Null data!!");
            data = new ArrayList<>(Collections.EMPTY_LIST);
        }
        return data;
    }
    private void loadModels() {
        // 获取当前目录
        String currentDir = System.getProperty("user.dir");
        // 创建文件的完整路径
        File tempFile = new File(currentDir, "facial_landmarks_model.dat");
        // 检查文件是否已经存在
        if (!tempFile.exists()) {
            try (InputStream landmarksStream = Jdlib.class.getResourceAsStream(facialLandmarksModelPath);
                 InputStream embeddingStream = faceEmbeddingModelPath != null ? Jdlib.class.getResourceAsStream(faceEmbeddingModelPath) : null) {

                if (landmarksStream == null) {
                    throw new FileNotFoundException("Facial landmarks model not found: " + facialLandmarksModelPath);
                }

                if (faceEmbeddingModelPath != null && embeddingStream == null) {
                    throw new FileNotFoundException("Face embedding model not found: " + faceEmbeddingModelPath);
                }

                // 将输入流保存到临时文件或直接传递给 native 方法
                File landmarksFile = saveToTempFile(landmarksStream, "facial_landmarks_model.dat");
                this.facialLandmarksModelPath = landmarksFile.getAbsolutePath();

                if (faceEmbeddingModelPath != null) {
                    File embeddingFile = saveToTempFile(embeddingStream, "face_embedding_model.dat");
                    this.faceEmbeddingModelPath = embeddingFile.getAbsolutePath();
                }

            } catch (IOException e) {
                System.err.println("Error loading models: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }else {
            this.facialLandmarksModelPath = tempFile.getAbsolutePath();
            if (faceEmbeddingModelPath != null) {
                File embeddingFile = new File(currentDir, "face_embedding_model.dat");
                this.faceEmbeddingModelPath = embeddingFile.getAbsolutePath();
            }
        }
    }

    /**
     * 将输入流保存到当前目录的临时文件中。
     * 如果同名文件已存在，则不会重新创建。
     *
     * @param inputStream 输入流
     * @param fileName 文件名（不包含路径）
     * @return 保存的临时文件对象
     * @throws IOException 如果发生 I/O 错误
     */
    private File saveToTempFile(InputStream inputStream, String fileName) throws IOException {
        // 获取当前目录
        String currentDir = System.getProperty("user.dir");
        // 创建文件的完整路径
        File tempFile = new File(currentDir, fileName);
        // 检查文件是否已经存在
        if (!tempFile.exists()) {
            // 文件不存在，需要创建
            try (OutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int read;
                // 从输入流读取数据并写入文件
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
            }
        } else {
            // 文件已存在，不需要重新创建，直接返回文件对象
            System.out.println("File already exists, no need to recreate: " + tempFile.getAbsolutePath());
        }
        return tempFile;
    }
}
