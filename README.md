# ArithmeticCompiler � Practical Compiler Workshop

This repository is a skeleton for a hands-on compiler/interpreter workshop based on Crafting Interpreters.

Follow `PLAN.md` for weekly modules and tasks. Implement all components yourself (lexer, parser, AST, interpreter, etc.).

To compile a simple Java file in this repo (example):

```powershell
javac src/repl/Main.java
java -cp src repl.Main
```

To compile and run the starter lexer test:

```powershell
mkdir out
javac -d out src/lexer/*.java tests/LexerTest.java
java -cp out tests.LexerTest
```
