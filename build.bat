@echo off
if not exist out mkdir out
javac -d out src\lexer\*.java tests\LexerTest.java
if %ERRORLEVEL% neq 0 pause
