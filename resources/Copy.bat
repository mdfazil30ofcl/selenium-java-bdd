@echo off
echo %time%
C:\windows\system32\timeout 9 > NUL
echo %time%
set path = "C:\Program Files\Java\jdk1.8.0_171\bin"
java -cp %1 %2 %3 %4