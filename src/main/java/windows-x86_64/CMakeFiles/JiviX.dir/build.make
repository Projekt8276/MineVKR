# CMAKE generated file: DO NOT EDIT!
# Generated by "NMake Makefiles" Generator, CMake Version 3.16

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE
NULL=nul
!ENDIF
SHELL = cmd.exe

# The CMake executable.
CMAKE_COMMAND = "H:\Program Files\JetBrains\CLion 2019.3.4\bin\cmake\win\bin\cmake.exe"

# The command to remove a file.
RM = "H:\Program Files\JetBrains\CLion 2019.3.4\bin\cmake\win\bin\cmake.exe" -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = H:\Data\Projects\MineRTX\src\main\java

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = H:\Data\Projects\MineRTX\src\main\java\windows-x86_64

# Include any dependencies generated for this target.
include CMakeFiles\JiviX.dir\depend.make

# Include the progress variables for this target.
include CMakeFiles\JiviX.dir\progress.make

# Include the compile flags for this target's objects.
include CMakeFiles\JiviX.dir\flags.make

CMakeFiles\JiviX.dir\source\jnijavacpp.cpp.obj: CMakeFiles\JiviX.dir\flags.make
CMakeFiles\JiviX.dir\source\jnijavacpp.cpp.obj: ..\source\jnijavacpp.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=H:\Data\Projects\MineRTX\src\main\java\windows-x86_64\CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/JiviX.dir/source/jnijavacpp.cpp.obj"
	"H:\Program Files\Microsoft Visual Studio\2019\Community\VC\Tools\MSVC\14.26.28801\bin\Hostx64\x64\cl.exe" @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) /FoCMakeFiles\JiviX.dir\source\jnijavacpp.cpp.obj /FdCMakeFiles\JiviX.dir\ /FS -c H:\Data\Projects\MineRTX\src\main\java\source\jnijavacpp.cpp
<<

CMakeFiles\JiviX.dir\source\jnijavacpp.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/JiviX.dir/source/jnijavacpp.cpp.i"
	"H:\Program Files\Microsoft Visual Studio\2019\Community\VC\Tools\MSVC\14.26.28801\bin\Hostx64\x64\cl.exe" > CMakeFiles\JiviX.dir\source\jnijavacpp.cpp.i @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E H:\Data\Projects\MineRTX\src\main\java\source\jnijavacpp.cpp
<<

CMakeFiles\JiviX.dir\source\jnijavacpp.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/JiviX.dir/source/jnijavacpp.cpp.s"
	"H:\Program Files\Microsoft Visual Studio\2019\Community\VC\Tools\MSVC\14.26.28801\bin\Hostx64\x64\cl.exe" @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) /FoNUL /FAs /FaCMakeFiles\JiviX.dir\source\jnijavacpp.cpp.s /c H:\Data\Projects\MineRTX\src\main\java\source\jnijavacpp.cpp
<<

CMakeFiles\JiviX.dir\source\jniByteVector.cpp.obj: CMakeFiles\JiviX.dir\flags.make
CMakeFiles\JiviX.dir\source\jniByteVector.cpp.obj: ..\source\jniByteVector.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=H:\Data\Projects\MineRTX\src\main\java\windows-x86_64\CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building CXX object CMakeFiles/JiviX.dir/source/jniByteVector.cpp.obj"
	"H:\Program Files\Microsoft Visual Studio\2019\Community\VC\Tools\MSVC\14.26.28801\bin\Hostx64\x64\cl.exe" @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) /FoCMakeFiles\JiviX.dir\source\jniByteVector.cpp.obj /FdCMakeFiles\JiviX.dir\ /FS -c H:\Data\Projects\MineRTX\src\main\java\source\jniByteVector.cpp
<<

CMakeFiles\JiviX.dir\source\jniByteVector.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/JiviX.dir/source/jniByteVector.cpp.i"
	"H:\Program Files\Microsoft Visual Studio\2019\Community\VC\Tools\MSVC\14.26.28801\bin\Hostx64\x64\cl.exe" > CMakeFiles\JiviX.dir\source\jniByteVector.cpp.i @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E H:\Data\Projects\MineRTX\src\main\java\source\jniByteVector.cpp
<<

CMakeFiles\JiviX.dir\source\jniByteVector.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/JiviX.dir/source/jniByteVector.cpp.s"
	"H:\Program Files\Microsoft Visual Studio\2019\Community\VC\Tools\MSVC\14.26.28801\bin\Hostx64\x64\cl.exe" @<<
 /nologo /TP $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) /FoNUL /FAs /FaCMakeFiles\JiviX.dir\source\jniByteVector.cpp.s /c H:\Data\Projects\MineRTX\src\main\java\source\jniByteVector.cpp
<<

# Object files for target JiviX
JiviX_OBJECTS = \
"CMakeFiles\JiviX.dir\source\jnijavacpp.cpp.obj" \
"CMakeFiles\JiviX.dir\source\jniByteVector.cpp.obj"

# External object files for target JiviX
JiviX_EXTERNAL_OBJECTS =

JiviX.dll: CMakeFiles\JiviX.dir\source\jnijavacpp.cpp.obj
JiviX.dll: CMakeFiles\JiviX.dir\source\jniByteVector.cpp.obj
JiviX.dll: CMakeFiles\JiviX.dir\build.make
JiviX.dll: ..\lib\vulkan-1.lib
JiviX.dll: ..\lib\vulkan-1.lib
JiviX.dll: "C:\Program Files\Java\jdk-13.0.2\lib\jawt.lib"
JiviX.dll: "C:\Program Files\Java\jdk-13.0.2\lib\jvm.lib"
JiviX.dll: CMakeFiles\JiviX.dir\objects1.rsp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=H:\Data\Projects\MineRTX\src\main\java\windows-x86_64\CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Linking CXX shared library JiviX.dll"
	"H:\Program Files\JetBrains\CLion 2019.3.4\bin\cmake\win\bin\cmake.exe" -E vs_link_dll --intdir=CMakeFiles\JiviX.dir --rc=C:\PROGRA~2\WI3CF2~1\10\bin\100190~1.0\x64\rc.exe --mt=C:\PROGRA~2\WI3CF2~1\10\bin\100190~1.0\x64\mt.exe --manifests  -- "H:\Program Files\Microsoft Visual Studio\2019\Community\VC\Tools\MSVC\14.26.28801\bin\Hostx64\x64\link.exe" /nologo @CMakeFiles\JiviX.dir\objects1.rsp @<<
 /out:JiviX.dll /implib:JiviX.lib /pdb:H:\Data\Projects\MineRTX\src\main\java\windows-x86_64\JiviX.pdb /dll /version:0.0 /machine:x64 /debug /INCREMENTAL   -LIBPATH:H:\Data\Projects\MineRTX\src\main\java  -LIBPATH:H:\Data\Projects\MineRTX\src\main\java\lib  glfw3.lib ..\lib\vulkan-1.lib ..\lib\vulkan-1.lib "C:\Program Files\Java\jdk-13.0.2\lib\jawt.lib" "C:\Program Files\Java\jdk-13.0.2\lib\jvm.lib" kernel32.lib user32.lib gdi32.lib winspool.lib shell32.lib ole32.lib oleaut32.lib uuid.lib comdlg32.lib advapi32.lib  
<<

# Rule to build all files generated by this target.
CMakeFiles\JiviX.dir\build: JiviX.dll

.PHONY : CMakeFiles\JiviX.dir\build

CMakeFiles\JiviX.dir\clean:
	$(CMAKE_COMMAND) -P CMakeFiles\JiviX.dir\cmake_clean.cmake
.PHONY : CMakeFiles\JiviX.dir\clean

CMakeFiles\JiviX.dir\depend:
	$(CMAKE_COMMAND) -E cmake_depends "NMake Makefiles" H:\Data\Projects\MineRTX\src\main\java H:\Data\Projects\MineRTX\src\main\java H:\Data\Projects\MineRTX\src\main\java\windows-x86_64 H:\Data\Projects\MineRTX\src\main\java\windows-x86_64 H:\Data\Projects\MineRTX\src\main\java\windows-x86_64\CMakeFiles\JiviX.dir\DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles\JiviX.dir\depend

