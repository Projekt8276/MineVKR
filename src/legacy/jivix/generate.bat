call vcvarsall.bat x64
call vcvars64.bat
call java -jar javacpp.jar FooJiviX.java -header
call java -jar javacpp.jar    JiviX.java -exec
pause
