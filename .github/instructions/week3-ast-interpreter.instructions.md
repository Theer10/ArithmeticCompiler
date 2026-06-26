---
description: "Week 3 — AST and Interpreter: load when user asks about abstract syntax tree, visitor pattern, tree walking, interpreter, REPL, evaluation, Interpreter.java, AstPrinter, Week 3"
applyTo: "src/ast/**"
---

# Week 3 — AST + Interpreter + REPL

## The Big Picture

```
Source code
    ↓  [ Lexer (Week 1) ]
List<Token>
    ↓  [ Parser (Week 2) ]
Abstract Syntax Tree
    ↓  [ Interpreter (Week 3) ] ← YOU ARE HERE
Runtime value (number, string, boolean, nil)
```

This is the week everything comes together. You will evaluate expressions like `(1 + 2) * 3` and get `9.0` back. You will add the **Visitor pattern** to cleanly separate nodes from evaluation logic. And you will wire it all into a **REPL** so you can type expressions live and see results.

---

## Core Concepts

### What is Tree-Walking Interpretation?

The simplest way to evaluate an AST: walk the tree recursively. For a `Binary` node (e.g. `1 + 2`), you:
1. Evaluate the left child → `1.0`
2. Evaluate the right child → `2.0`
3. Apply the operator `+` → return `3.0`

For a `Literal` node, just return the value directly. For a `Unary` node, evaluate the one child and apply the operator.

### What is the Visitor Pattern?

Without the Visitor pattern, you would put `evaluate()` methods directly in each AST node class. That works for one operation but becomes messy when you want multiple operations (evaluate, pretty-print, type-check, etc.).

The Visitor pattern separates the **what** (the node) from the **how** (the operation).

Structure:
- Each `Expr` subclass has an `accept(Visitor v)` method that calls back to the visitor
- A `Visitor` interface has one `visit` method per node type
- An `Interpreter` implements `Visitor` and defines what to do for each node

When you call `interpreter.evaluate(expr)`, it calls `expr.accept(interpreter)`, which calls back with the right `visit` method. This is called the **double dispatch** pattern.

### What is a REPL?

**R**ead — read a line of input from the user  
**E**val — lex it, parse it, evaluate it  
**P**rint — print the result  
**L**oop — go back to the start  

When you're done, `src/repl/Main.java` will run an infinite loop: `> 1 + 2` → `3.0`.

### Runtime Types

Your language has four runtime value types:

| Java type | Language value |
|-----------|---------------|
| `Double` | numbers (`1`, `3.14`) |
| `String` | strings (`"hello"`) |
| `Boolean` | `true`, `false` |
| `null` | `nil` |

The interpreter method signature is `Object evaluate(Expr expr)` — the return type is `Object` because any of the above could come back.

---

## Phase 1 — Concept Check (answer before writing any code)

Write answers in `docs/weekly-notes.md`. Answer at least 4.

1. What is tree-walking evaluation? Walk through how `(1 + 2) * 3` evaluates step by step.
2. What problem does the Visitor pattern solve? Why not just put `evaluate()` on each node?
3. What does `accept()` do on an AST node? Why is it needed?
4. What is double dispatch? How does it differ from a normal method call?
5. What is a REPL? Write out what happens for the input `print 1 + 1`.
6. Why is the `evaluate()` return type `Object` and not `double`?

---

## Phase 2 — What You Are Building

| File | Purpose |
|------|---------|
| `src/ast/Expr.java` | Add `accept()` and the `Visitor` interface |
| `src/interpreter/Interpreter.java` | Implements `Visitor` — evaluates each node type |
| `src/repl/Main.java` | The REPL loop |

---

## Phase 3 — Step-by-Step Build Guide

### Step 1: Add the Visitor Pattern to Expr

Extend `src/ast/Expr.java` from Week 2.

**Add a `Visitor` interface inside `Expr`:**
```java
public interface Visitor<R> {
    R visitBinaryExpr(Binary expr);
    R visitGroupingExpr(Grouping expr);
    R visitLiteralExpr(Literal expr);
    R visitUnaryExpr(Unary expr);
}
```

**Add an `abstract accept()` method to `Expr`:**
```java
public abstract <R> R accept(Visitor<R> visitor);
```

**Add `accept()` to each subclass:**
- `Binary.accept(v)` → returns `v.visitBinaryExpr(this)`
- `Grouping.accept(v)` → returns `v.visitGroupingExpr(this)`
- `Literal.accept(v)` → returns `v.visitLiteralExpr(this)`
- `Unary.accept(v)` → returns `v.visitUnaryExpr(this)`

*Predict before writing: Why is `Visitor` generic (`<R>`)? What is `R` in the interpreter case?*

---

### Step 2: Implement the Interpreter

Create `src/interpreter/Interpreter.java`.

The class implements `Expr.Visitor<Object>` — meaning every `visit` method returns an `Object`.

**The public entry point:**
```java
public Object interpret(Expr expression) {
    return evaluate(expression);
}

private Object evaluate(Expr expr) {
    return expr.accept(this);
}
```

**`visitLiteralExpr(Expr.Literal expr)`:**
Simply return `expr.value`. A literal is already the value.

**`visitGroupingExpr(Expr.Grouping expr)`:**
Evaluate the inner expression: `return evaluate(expr.expression)`.

**`visitUnaryExpr(Expr.Unary expr)`:**
Evaluate `expr.right`, then apply the operator:
- `MINUS`: cast to double, negate, return
- `BANG`: return the logical negation of `isTruthy(right)`

**`visitBinaryExpr(Expr.Binary expr)`:**
Evaluate both sides, then switch on `expr.operator.type`:
- `MINUS`: `(double)left - (double)right`
- `SLASH`: `(double)left / (double)right`
- `STAR`: `(double)left * (double)right`
- `PLUS`: if both doubles → add; if both strings → concatenate; else error
- `GREATER`, `GREATER_EQUAL`, `LESS`, `LESS_EQUAL`: cast to double, compare, return boolean
- `BANG_EQUAL`: `!isEqual(left, right)`
- `EQUAL_EQUAL`: `isEqual(left, right)`

**Helper methods:**

```java
private boolean isTruthy(Object object) {
    if (object == null) return false;         // nil is falsy
    if (object instanceof Boolean) return (boolean) object;
    return true;                               // everything else is truthy
}

private boolean isEqual(Object a, Object b) {
    if (a == null && b == null) return true;
    if (a == null) return false;
    return a.equals(b);
}
```

*Predict before writing: Why is `null` (nil) falsy but a number like `0` is truthy? Is that the right design?*

---

### Step 3: (Bonus) AST Printer for Debugging

Before wiring up the REPL, build a tool to print the AST as a readable string. This helps enormously when debugging parser output.

Create `src/ast/AstPrinter.java`. It implements `Expr.Visitor<String>`.

Each `visit` method returns a parenthesised string:
- `visitLiteralExpr`: return `String.valueOf(expr.value)`
- `visitGroupingExpr`: return `"(group " + print(expr.expression) + ")"`
- `visitUnaryExpr`: return `"(" + expr.operator.lexeme + " " + print(expr.right) + ")"`
- `visitBinaryExpr`: return `"(" + expr.operator.lexeme + " " + print(expr.left) + " " + print(expr.right) + ")"`

Example: `(1 + 2) * 3` → `"(* (group (+ 1.0 2.0)) 3.0)"`

*Before writing: What is the print output for `-1 + 2`?*

---

### Step 4: Wire Up the REPL

Update `src/repl/Main.java`:

```java
package repl;

import lexer.Lexer;
import lexer.Token;
import parser.Parser;
import ast.Expr;
import interpreter.Interpreter;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Interpreter interpreter = new Interpreter();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Arithmetic REPL — type an expression, Ctrl+C to quit");
        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break;
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            run(line);
        }
    }

    private static void run(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        Parser parser = new Parser(tokens);
        Expr expr = parser.parse();
        Object result = interpreter.interpret(expr);
        System.out.println(result);
    }
}
```

*Before writing: What four things happen inside `run()`? Name them in order.*

---

## Phase 4 — Tests

Create `tests/InterpreterTest.java`.

| # | Input | Expected result |
|---|-------|----------------|
| 1 | `"1 + 2"` | `3.0` |
| 2 | `"10 - 3"` | `7.0` |
| 3 | `"2 * 3"` | `6.0` |
| 4 | `"9 / 3"` | `3.0` |
| 5 | `"(1 + 2) * 3"` | `9.0` |
| 6 | `"-5"` | `-5.0` |
| 7 | `"!true"` | `false` |
| 8 | `"1 == 1"` | `true` |
| 9 | `"\"hello\" + \" world\""` | `"hello world"` |
| 10 | `"nil == nil"` | `true` |

**Run:**
```powershell
javac -d out src/lexer/*.java src/ast/*.java src/parser/*.java src/interpreter/*.java tests/InterpreterTest.java
java -cp out tests.InterpreterTest
```

**Also manually test the REPL:**
```powershell
javac -d out src/lexer/*.java src/ast/*.java src/parser/*.java src/interpreter/*.java src/repl/*.java
java -cp out repl.Main
```
Then type: `(1 + 2) * 3` → should print `9.0`

---

## Phase 5 — Debug Challenge

After tests pass:
1. Introduce a bug: in `visitBinaryExpr`, handle `PLUS` to always concatenate as strings instead of adding numbers.
2. Predict which tests break.
3. Find and fix.
4. Explain: *"Why did that bug cause that specific failure?"*

---

## Definition of Done — Week 3 ✅

- [ ] `Expr.java` — `Visitor` interface + `accept()` on all nodes
- [ ] `Interpreter.java` — all visit methods implemented
- [ ] `AstPrinter.java` — working (bonus but highly recommended)
- [ ] `Main.java` (REPL) — takes input, evaluates, prints result
- [ ] All 10 interpreter tests pass
- [ ] REPL runs and evaluates `(1 + 2) * 3` → `9.0`
- [ ] Debug challenge complete
- [ ] Reflection in `docs/weekly-notes.md`

**At this point you have a working interpreter. `> 1 + 2` actually returns `3.0`. This is a real milestone.**

---

## Reflection

1. Explain the Visitor pattern in one sentence to someone who has never heard of it.
2. Why does `evaluate()` call `accept()` and `accept()` call back into the visitor? Why not just call the visit method directly?
3. What is `isTruthy` doing and why is `null` falsy?
4. What would you need to add to support variables (e.g. `var x = 5`)?
5. Rate your confidence: 1–5. Schedule revision accordingly.

---

## What's Next — Week 4 Preview

You can now evaluate **expressions**. Week 4 adds **statements** — code that *does* something but doesn't necessarily produce a value. You will add `if/else`, `while`, and variable declarations, turning your expression evaluator into a proper language interpreter.
