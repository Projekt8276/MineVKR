call vcvarsall.bat x64
call vcvars64.bat
call java -jar javacpp.jar net/fabricmc/minertx/jivix/ByteVector.java -header
::call java -jar javacpp.jar      JiviX.java -exec
pause
