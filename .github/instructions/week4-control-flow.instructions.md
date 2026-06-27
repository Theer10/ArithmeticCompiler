---
description: "Week 4 — Control Flow: load when user asks about if statements, else, while loops, statements vs expressions, Stmt, print statement, variable declarations, environment, Week 4"
---

# Week 4 — Control Flow

## The Big Picture

```
Week 3: interpreter evaluates EXPRESSIONS
Week 4: interpreter executes STATEMENTS    ← YOU ARE HERE
```

So far your interpreter evaluates expressions like `1 + 2` and returns a value. Statements are different — they *do something* (print output, store a variable, loop, branch) and do not necessarily return a value.

This week you will add:
- `print` statement — evaluates an expression and outputs it
- Variable declarations: `var x = 5;`
- Variable access: using `x` in an expression
- `if / else` — conditional branching
- `while` — looping

By the end, you will be able to run this program:
```
var n = 10;
while (n > 0) {
    print n;
    n = n - 1;
}
```

---

## Core Concepts

### Statements vs Expressions

| | Expression | Statement |
|--|-----------|-----------|
| Returns a value? | Yes | Usually not |
| Example | `1 + 2` | `print 1 + 2;` |
| Ends with `;`? | No | Yes |

Expressions are *evaluated*. Statements are *executed*.

### The Environment

Variables need somewhere to live. An **environment** is a map from variable names (`String`) to values (`Object`).

When you declare `var x = 5;` the environment stores `{"x": 5.0}`.
When you use `x` in an expression, the interpreter looks it up.

Scoping (block `{...}`) is handled by chaining environments: inner blocks have their own environment that holds a reference to the outer one. Variable lookup walks the chain.

### Stmt Node Classes

Just as `Expr` had subclasses for each expression type, you need a `Stmt` hierarchy for statements:

| Node | Fields | Represents |
|------|--------|-----------|
| `Stmt.Print` | `Expr expression` | `print expr;` |
| `Stmt.Expression` | `Expr expression` | A bare expression used as a statement: `x = 1;` |
| `Stmt.Var` | `Token name`, `Expr initializer` | `var x = 5;` |
| `Stmt.Block` | `List<Stmt> statements` | `{ ... }` |
| `Stmt.If` | `Expr condition`, `Stmt thenBranch`, `Stmt elseBranch` | `if (cond) ... else ...` |
| `Stmt.While` | `Expr condition`, `Stmt body` | `while (cond) body` |

---

## Phase 1 — Concept Check (answer before writing any code)

Write answers in `docs/weekly-notes.md`. Answer at least 4.

1. What is the difference between a statement and an expression? Give an example of each.
2. What is an environment in the context of an interpreter? What does it map?
3. Why do we need a chain of environments for scoping? What happens when a block ends?
4. What should `var x;` (no initializer) do? What value does `x` have?
5. How does `if` differ from `while` in how the interpreter executes them?
6. What is short-circuit evaluation? Should `false and <something>` evaluate `<something>`?

---

## Phase 2 — What You Are Building

| File | Purpose |
|------|---------|
| `src/ast/Stmt.java` | Statement node hierarchy (same pattern as Expr) |
| `src/interpreter/Environment.java` | Map of variable names to values, supports chaining |
| `src/interpreter/Interpreter.java` | Add `Stmt.Visitor`, execute statements |
| `src/parser/Parser.java` | Parse statements, variable declarations, if, while |

---

## Phase 3 — Step-by-Step Build Guide

### Step 1: Stmt Node Classes

Create `src/ast/Stmt.java`. Same abstract class + Visitor pattern as `Expr`:

```java
public abstract class Stmt {
    public interface Visitor<R> {
        R visitExpressionStmt(Expression stmt);
        R visitPrintStmt(Print stmt);
        R visitVarStmt(Var stmt);
        R visitBlockStmt(Block stmt);
        R visitIfStmt(If stmt);
        R visitWhileStmt(While stmt);
    }

    public abstract <R> R accept(Visitor<R> visitor);

    public static class Expression extends Stmt { ... }
    public static class Print extends Stmt { ... }
    public static class Var extends Stmt { ... }
    public static class Block extends Stmt { ... }
    public static class If extends Stmt { ... }
    public static class While extends Stmt { ... }
}
```

*Predict before writing: What fields does `If` need? Think about: condition, then-branch, and what else?*

Also add `Expr.Assign` and `Expr.Variable` to `Expr.java`:

| Node | Fields | Represents |
|------|--------|-----------|
| `Expr.Variable` | `Token name` | Reading a variable: `x` |
| `Expr.Assign` | `Token name`, `Expr value` | Assignment: `x = 5` |

---

### Step 2: Environment

Create `src/interpreter/Environment.java`:

```java
public class Environment {
    final Environment enclosing;  // null for global scope
    private final Map<String, Object> values = new HashMap<>();

    public Environment() { this.enclosing = null; }
    public Environment(Environment enclosing) { this.enclosing = enclosing; }

    public void define(String name, Object value) { ... }

    public Object get(Token name) {
        // check values map, then try enclosing, then throw error
    }

    public void assign(Token name, Object value) {
        // check values map, then try enclosing, then throw error
    }
}
```

*Predict before writing: What is the difference between `define()` and `assign()`? When would you call each one?*

---

### Step 3: Update the Parser to Parse Statements

Add to `Parser.java`:

**`declaration()` → `Stmt`** — checks for `VAR` keyword or falls through to `statement()`

**`varDeclaration()` → `Stmt`** — consumes `var`, name token, optional `= initializer`, semicolon

**`statement()` → `Stmt`** — dispatch:
- `IF` → `ifStatement()`
- `PRINT` → `printStatement()`
- `WHILE` → `whileStatement()`
- `LEFT_BRACE` → `new Stmt.Block(block())`
- else → `expressionStatement()`

**`block()` → `List<Stmt>`** — consume statements until `}` or end of file

**`ifStatement()` → `Stmt`** — consume `(`, parse condition, `)`, parse then-branch, optionally parse `else` branch

**`whileStatement()` → `Stmt`** — consume `(`, parse condition, `)`, parse body

**`printStatement()` → `Stmt`** — parse expression, consume `;`, return `Stmt.Print`

**`expressionStatement()` → `Stmt`** — parse expression, consume `;`, return `Stmt.Expression`

Also extend `parse()` to return `List<Stmt>` instead of a single `Expr`.

---

### Step 4: Extend the Interpreter

Add `implements Stmt.Visitor<Void>` to `Interpreter`.

Change `interpret()` to take `List<Stmt>`:
```java
public void interpret(List<Stmt> statements) {
    for (Stmt statement : statements) execute(statement);
}
private void execute(Stmt stmt) { stmt.accept(this); }
```

**`visitExpressionStmt`** — evaluate the expression, discard the value

**`visitPrintStmt`** — evaluate the expression, call `System.out.println(stringify(value))`

**`visitVarStmt`** — evaluate the initializer (or `null`), call `environment.define(name, value)`

**`visitBlockStmt`** — create a new `Environment(currentEnvironment)`, execute all statements in it, restore old environment

**`visitIfStmt`** — evaluate condition; if truthy execute `thenBranch`, else execute `elseBranch` (if not null)

**`visitWhileStmt`** — loop: evaluate condition; while truthy, execute body

**`visitVariableExpr`** — return `environment.get(name)`

**`visitAssignExpr`** — evaluate value, call `environment.assign(name, value)`, return value

---

## Phase 4 — Tests

Create `tests/ControlFlowTest.java`. Run each program end-to-end and check printed output.

| # | Program | Expected output |
|---|---------|----------------|
| 1 | `print 1 + 2;` | `3.0` |
| 2 | `var x = 5; print x;` | `5.0` |
| 3 | `var x = 5; x = 10; print x;` | `10.0` |
| 4 | `if (true) print "yes";` | `yes` |
| 5 | `if (false) print "yes"; else print "no";` | `no` |
| 6 | `var i = 0; while (i < 3) { print i; i = i + 1; }` | `0.0`, `1.0`, `2.0` |
| 7 | `{ var x = 1; print x; }` (x not accessible outside) | `1.0` |

---

## Definition of Done — Week 4 ✅

- [ ] `Stmt.java` — all 6 statement types with Visitor
- [ ] `Environment.java` — define, get, assign, chaining
- [ ] Parser updated to parse statements, var, if, while, blocks
- [ ] Interpreter handles all statement types
- [ ] All 7 tests pass
- [ ] REPL can run multi-line programs (or programs from a string)
- [ ] Debug challenge complete
- [ ] Reflection in `docs/weekly-notes.md`

---

## Reflection

1. What is the difference between `define()` and `assign()` in `Environment`?
2. Why does a block need its own environment? What would go wrong if it used the global one?
3. How does `visitWhileStmt` implement the loop? What Java construct maps to the `while` statement in your language?
4. What would you need to add to support `for` loops? (Hint: think about how `for` relates to `while`)
5. Rate your confidence: 1–5. Schedule revision accordingly.

---

## What's Next — Week 5 Preview

You now have a language with variables, branches, and loops. Week 5 adds **functions** — the ability to define reusable code blocks with parameters, call them, and return values. Functions introduce **closures** (functions that "remember" the environment where they were defined) and the **call stack**.
