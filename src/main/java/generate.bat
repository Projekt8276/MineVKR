call vcvarsall.bat x64
call vcvars64.bat
set "PATH=%cd%/windows-x86_64;%PATH%"
::call java -jar javacpp.jar net/fabricmc/minertx/jivix/ByteVector.java -nocompile 
::call java -jar javacpp.jar net/fabricmc/minertx/jivix/Vector.java     -nocompile 
call java -jar javacpp.jar net/fabricmc/minertx/jivix/VectorTest.java -nocompile 
::call java -jar javacpp.jar net/fabricmc/minertx/jivix/Mesh.java       -nocompile 
call java -jar javacpp.jar net/fabricmc/minertx/jivix/JiviXBase.java  -nocompile
pause
