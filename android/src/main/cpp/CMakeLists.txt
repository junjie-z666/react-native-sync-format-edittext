cmake_minimum_required(VERSION 3.13)
project(syncformatedittext)

set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_STANDARD 20)

# Find React Native Prefab packages
find_package(fbjni REQUIRED CONFIG)
find_package(ReactAndroid REQUIRED CONFIG)

# Source files
add_library(syncformatedittext SHARED
  FormatHostObject.cpp
  FormatModuleJNI.cpp
)

target_include_directories(syncformatedittext PRIVATE
  ${CMAKE_CURRENT_SOURCE_DIR}
)

target_link_libraries(syncformatedittext
  fbjni::fbjni
  ReactAndroid::jsi
  ReactAndroid::reactnative
)

target_compile_options(syncformatedittext PRIVATE -fvisibility=hidden)
target_link_options(syncformatedittext PRIVATE "-Wl,-z,max-page-size=16384")
