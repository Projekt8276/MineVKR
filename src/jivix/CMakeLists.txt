cmake_minimum_required(VERSION 3.1)

project(JiviX CXX)
find_package(Java REQUIRED)
find_package(JNI REQUIRED)

link_directories(${PROJECT_SOURCE_DIR} "./")
include_directories(${JNI_INCLUDE_DIRS} ${PROJECT_SOURCE_DIR} "./")
add_executable(JiviX jnijavacpp.cpp jniJiviX.cpp testJiviX.cpp)
target_link_libraries(JiviX PUBLIC ${JNI_LIBRARIES})
