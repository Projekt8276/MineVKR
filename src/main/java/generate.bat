call vcvarsall.bat x64
call vcvars64.bat
set "PATH=%cd%/windows-x86_64;%PATH%"
call java -jar javacpp.jar com/helixd2s/minevkr/jivix/VectorTest.java -nocompile -header
call java -jar javacpp.jar com/helixd2s/minevkr/jivix/JiviXCore.java  -nocompile -header
call java -jar javacpp.jar com/helixd2s/minevkr/jivix/JiviXBase.java  -nocompile 
pause
