# 指定 CMake 的最低版本要求
cmake_minimum_required(VERSION 2.8.12)

# 启用 RPATH，使得在 macOS 上可以正确运行
set(CMAKE_MACOSX_RPATH 1)

# 为 C++ 编译器添加 -fPIC 标志，这是生成动态库时常用的标志
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fPIC")

# 定义项目名称
project(Jdlib)

# 设置构建类型为 Release，这会优化编译生成的代码
set(CMAKE_BUILD_TYPE Release)

# 查找 JNI 包，并将其包含目录添加到包含路径中
find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})

# 设置 dlib 的位置，并在不存在时克隆 dlib 仓库
set(DLib_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/build/dlib/)
if(NOT EXISTS ${DLib_LOCATION})
    # 使用 bash 命令克隆 dlib 仓库到指定位置
    execute_process(
            COMMAND bash -c "git clone https://github.com/davisking/dlib.git"
            WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/build
    )
endif()
# 添加 dlib 子目录到构建中
add_subdirectory(${DLib_LOCATION} dlib_build)

# 添加 Jdlib 动态库目标，并指定源文件
add_library(Jdlib SHARED Jdlib.cpp utils.cpp handlers.cpp)

# 链接 dlib 库到 Jdlib 动态库
target_link_libraries(Jdlib dlib::dlib)

# 根据不同的平台和架构设置输出目录
if(WIN32)
    # 设置平台为 Windows
    set(NATIVE_DIR "windows")
    # 根据指针大小判断是 32 位还是 64 位，并设置相应的架构目录
    if(CMAKE_SIZEOF_VOID_P EQUAL 8)
        set(ARCH_DIR "x86_64")
    else()
        set(ARCH_DIR "x86")
    endif()
elseif(APPLE)
    # 设置平台为 macOS
    set(NATIVE_DIR "macosx")
    # 检测是否为 ARM 架构，并设置相应的架构目录
    if(CMAKE_SYSTEM_PROCESSOR MATCHES "arm|ARM|aarch64|AARCH64")
        set(ARCH_DIR "aarch64")
    else()
        set(ARCH_DIR "x86_64")
    endif()
elseif(UNIX AND NOT APPLE)
    # 设置平台为 Linux
    set(NATIVE_DIR "linux")
    # 检测是否为 ARM 架构，并设置相应的架构目录
    if(CMAKE_SYSTEM_PROCESSOR MATCHES "arm|ARM|aarch64|AARCH64")
        set(ARCH_DIR "aarch64")
    else()
        set(ARCH_DIR "x86_64")
    endif()
else()
    # 如果操作系统不支持，则报错
    message(FATAL_ERROR "Unsupported operating system")
endif()

# 设置 Jdlib 动态库的输出目录
set_target_properties(Jdlib PROPERTIES
        LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/../openvector-face/src/main/resources/native/${NATIVE_DIR}/${ARCH_DIR})