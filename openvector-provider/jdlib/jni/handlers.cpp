#include "handlers.hpp"

// FaceDetectorHandler 类的构造函数
// 初始化一个用于检测人脸的分类器
FaceDetectorHandler::FaceDetectorHandler() {
    face_detector = dlib::get_frontal_face_detector();
}

// 获取人脸检测器的函数
// 返回一个 dlib::frontal_face_detector 对象
dlib::frontal_face_detector FaceDetectorHandler::getFaceDetector() {
    return face_detector;
}

// 获取人脸检测处理器单例的函数
// 返回一个指向 FaceDetectorHandler 的指针
// 如果尚未创建，则创建一个新的 FaceDetectorHandler 实例
FaceDetectorHandler * FaceDetectorHandler::getFaceDetectorHandler() {
    static FaceDetectorHandler *face_detector_handler;
    if (!face_detector_handler) {
        face_detector_handler = new FaceDetectorHandler;
    }
    return face_detector_handler;
}

// ShapePredictorHandler 类的构造函数
// 从给定的模型路径加载形状预测器模型
ShapePredictorHandler::ShapePredictorHandler(const std::string & model_path) : model_path(model_path) {
    dlib::deserialize(model_path) >> shape_predictor_model;
}

// 获取形状预测器模型的函数
// 返回一个 dlib::shape_predictor 对象
dlib::shape_predictor ShapePredictorHandler::getShapePredictorModel() {
    return shape_predictor_model;
}

// 获取形状预测处理器单例的函数
// 返回一个指向 ShapePredictorHandler 的指针
// 如果尚未创建，则创建一个新的 ShapePredictorHandler 实例，并加载模型
ShapePredictorHandler * ShapePredictorHandler::getShapePredictorHandler(const std::string & model_path) {
    static ShapePredictorHandler *shape_predictor_handler;
    if (!shape_predictor_handler) {
        shape_predictor_handler = new ShapePredictorHandler(model_path);
    }
    return shape_predictor_handler;
}

// FaceEmbeddingHandler 类的构造函数
// 从给定的模型路径加载面部嵌入模型
FaceEmbeddingHandler::FaceEmbeddingHandler(const std::string & model_path) : model_path(model_path) {
    dlib::deserialize(model_path) >> face_embedding_model;
}

// 获取面部嵌入模型的函数
// 返回一个 anet_type 对象，这里假设 anet_type 是 dlib 中的一个类型
anet_type FaceEmbeddingHandler::getFaceEmbeddingModel() {
    return face_embedding_model;
}

// 获取面部嵌入处理器单例的函数
// 返回一个指向 FaceEmbeddingHandler 的指针
// 如果尚未创建，则创建一个新的 FaceEmbeddingHandler 实例，并加载模型
FaceEmbeddingHandler * FaceEmbeddingHandler::getFaceEmbeddingHandler(const std::string & model_path) {
    static FaceEmbeddingHandler *face_embedding_handler;
    if (!face_embedding_handler) {
        face_embedding_handler = new FaceEmbeddingHandler(model_path);
    }
    return face_embedding_handler;
}