call vcvarsall.bat x64
call vcvars64.bat
set "PATH=%cd%/windows-x86_64;%PATH%"
call java -jar javacpp.jar net/fabricmc/minertx/jivix/VectorTest.java -nocompile -header
call java -jar javacpp.jar net/fabricmc/minertx/jivix/JiviXCore.java  -nocompile -header
call java -jar javacpp.jar net/fabricmc/minertx/jivix/JiviXBase.java  -nocompile 
pause
