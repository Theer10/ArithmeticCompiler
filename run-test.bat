@echo off
java -cp out tests.LexerTest
if %ERRORLEVEL% neq 0 pause
