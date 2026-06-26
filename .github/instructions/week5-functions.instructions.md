---
description: "Week 5 — Functions: load when user asks about functions, function declarations, closures, scope, call stack, recursion, return values, LoxCallable, Week 5"
---

# Week 5 — Functions

## The Big Picture

```
Week 4: variables, if/else, while
Week 5: function declarations, calls, return, closures  ← YOU ARE HERE
```

Functions are the last major building block. After this week, your language can do almost anything a real language can do. You will be able to write:

```
fun fibonacci(n) {
    if (n <= 1) return n;
    return fibonacci(n - 1) + fibonacci(n - 2);
}
print fibonacci(10);
```

---

## Core Concepts

### What is a Function?

A function is a named block of code with parameters. Calling it creates a new **environment** (for the parameters and local variables), executes the body, and optionally returns a value.

### What is the Call Stack?

Each function call adds a **stack frame** — a record of the current execution context (local variables, where to return to). When a function returns, the frame is popped. Recursion works because each call has its own frame.

If you recurse too deeply without a base case, you get a stack overflow — the stack runs out of space.

### What is a Closure?

A closure is a function that **captures the environment where it was defined**, not where it is called.

Example:
```
fun makeCounter() {
    var i = 0;
    fun increment() {
        i = i + 1;
        print i;
    }
    return increment;
}

var counter = makeCounter();
counter();  // prints 1
counter();  // prints 2
```

`increment` closes over the `i` variable from `makeCounter`'s environment. Even after `makeCounter` finishes, `increment` still holds a reference to that environment. This is a closure.

### Lexical Scope vs Dynamic Scope

- **Lexical scope**: a variable refers to where it was *defined* (the text of the program). Your language uses this.
- **Dynamic scope**: a variable refers to the most recently *called* environment. Rare in modern languages.

---

## Phase 1 — Concept Check (answer before writing any code)

Write answers in `docs/weekly-notes.md`. Answer at least 4.

1. What is a closure? Describe it without using the word "closure".
2. What is the call stack? What happens to it during recursion?
3. What is the difference between lexical scope and dynamic scope? Give an example where they produce different results.
4. When a function is *defined*, what environment should it capture? When it is *called*?
5. How does `return` work in a tree-walking interpreter? (Hint: it can't just use Java's `return`)
6. What is the difference between function *declaration* and function *call*?

---

## Phase 2 — What You Are Building

| File | Purpose |
|------|---------|
| `src/interpreter/LoxCallable.java` | Interface for anything callable |
| `src/interpreter/LoxFunction.java` | A user-defined function at runtime |
| `src/interpreter/Return.java` | Exception used to unwind the stack on `return` |
| `src/ast/Expr.java` | Add `Expr.Call` node |
| `src/ast/Stmt.java` | Add `Stmt.Function` and `Stmt.Return` nodes |
| `src/parser/Parser.java` | Parse function declarations and `return` statements |
| `src/interpreter/Interpreter.java` | Execute function declarations, calls, return |

---

## Phase 3 — Step-by-Step Build Guide

### Step 1: The Callable Interface

Create `src/interpreter/LoxCallable.java`:

```java
public interface LoxCallable {
    int arity();                            // how many arguments?
    Object call(Interpreter interpreter, List<Object> arguments);
}
```

*Predict before writing: Why does `call()` take the `Interpreter` as a parameter?*

---

### Step 2: The Return Exception

There is no clean way to unwind the call stack mid-execution in a tree-walker other than throwing an exception. Create `src/interpreter/Return.java`:

```java
public class Return extends RuntimeException {
    final Object value;
    public Return(Object value) {
        super(null, null, false, false);  // disable expensive JVM stack capture
        this.value = value;
    }
}
```

*Predict before writing: Why is throwing an exception a valid implementation of `return`? What does it do to the call stack?*

---

### Step 3: Add Nodes

**In `Expr.java` — add `Expr.Call`:**

| Field | Type | Purpose |
|-------|------|---------|
| `callee` | `Expr` | The expression being called (usually a variable) |
| `paren` | `Token` | The `(` token — used for error reporting |
| `arguments` | `List<Expr>` | The argument expressions |

**In `Stmt.java` — add `Stmt.Function`:**

| Field | Type |
|-------|------|
| `name` | `Token` |
| `params` | `List<Token>` |
| `body` | `List<Stmt>` |

**In `Stmt.java` — add `Stmt.Return`:**

| Field | Type |
|-------|------|
| `keyword` | `Token` (for error location) |
| `value` | `Expr` (may be null for bare `return;`) |

Add `visitCallExpr`, `visitFunctionStmt`, `visitReturnStmt` to the respective Visitor interfaces.

---

### Step 4: LoxFunction

Create `src/interpreter/LoxFunction.java` implementing `LoxCallable`:

```java
public class LoxFunction implements LoxCallable {
    private final Stmt.Function declaration;
    private final Environment closure;   // ← the environment at definition time

    public LoxFunction(Stmt.Function declaration, Environment closure) { ... }

    @Override
    public int arity() { return declaration.params.size(); }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        // 1. Create a new environment with closure as enclosing
        // 2. Define each param name with the matching argument value
        // 3. Execute the body (catch Return exception to get the value)
        // 4. Return the value (or null for void functions)
    }
}
```

*Predict before writing: In step 1, why does the new environment use `closure` as the enclosing, not the interpreter's current global environment?*

---

### Step 5: Update the Parser

**`function(String kind)` → `Stmt.Function`**
Consumes: name, `(`, parameter list, `)`, `{`, body block.

**In `declaration()`:** check for `FUN` token and call `function("function")`.

**In `statement()`:** check for `RETURN` token and call `returnStatement()`.

**`returnStatement()` → `Stmt`**
Consume optional expression, consume `;`, return `Stmt.Return`.

**In `call()` (used in expression parsing, call it from `unary()` chain):**
After parsing a primary, check for `(` in a loop. If found: parse argument list, consume `)`, wrap in `Expr.Call`. This handles chained calls like `a()()`.

---

### Step 6: Update the Interpreter

**`visitFunctionStmt`:** define the function name in the current environment as a `new LoxFunction(stmt, environment)`.

**`visitCallExpr`:** 
1. Evaluate the callee expression
2. Evaluate each argument
3. Check the callee is a `LoxCallable` (else runtime error)
4. Check `arguments.size() == callee.arity()` (else arity error)
5. Call `callee.call(this, arguments)`

**`visitReturnStmt`:** evaluate the value (or null), throw `new Return(value)`.

---

## Phase 4 — Tests

Create `tests/FunctionTest.java`.

| # | Program | Expected |
|---|---------|---------|
| 1 | `fun add(a, b) { return a + b; } print add(1, 2);` | `3.0` |
| 2 | `fun greet(name) { return "Hello " + name; } print greet("world");` | `Hello world` |
| 3 | Fibonacci: `fun fib(n) { if (n <= 1) return n; return fib(n-1) + fib(n-2); } print fib(7);` | `13.0` |
| 4 | Closure: `fun makeAdder(x) { fun adder(y) { return x + y; } return adder; } var add5 = makeAdder(5); print add5(3);` | `8.0` |
| 5 | No-arg function: `fun hello() { print "hi"; } hello();` | `hi` |
| 6 | Return nil: `fun nothing() { } print nothing();` | `nil` |

---

## Definition of Done — Week 5 ✅

- [ ] `LoxCallable.java` interface defined
- [ ] `Return.java` exception class defined
- [ ] `Expr.Call`, `Stmt.Function`, `Stmt.Return` nodes added
- [ ] `LoxFunction.java` — arity, call with closure environment
- [ ] Parser parses function declarations, calls, return statements
- [ ] Interpreter handles all new nodes
- [ ] All 6 tests pass (including recursion and closure)
- [ ] Debug challenge complete
- [ ] Reflection in `docs/weekly-notes.md`

**At this point your language has: arithmetic, strings, booleans, nil, variables, if/else, while, functions, closures, and recursion. That is a real, Turing-complete language.**

---

## Debug Challenge

After tests pass:
1. In `LoxFunction.call()`, use the **interpreter's current environment** as the enclosing instead of `closure`. Run the closure test (test #4).
2. Observe that the closure test fails — the outer variable is not found.
3. Restore `closure` and explain: *Why does using the closure environment produce the correct result?*

---

## Reflection

1. In your own words: what is a closure and why does it matter?
2. Why does `Return` extend `RuntimeException` instead of `Exception`?
3. What is the difference between `arity()` and the number of arguments passed at a call site?
4. Your language is now Turing-complete. What does "Turing-complete" mean?
5. Rate your confidence: 1–5. Schedule revision accordingly.

---

## Project Complete 🎉

You have built:

| Week | What you built |
|------|---------------|
| 1 | Lexer — source → tokens |
| 2 | Parser — tokens → AST |
| 3 | Interpreter + REPL — AST → values |
| 4 | Control flow — if, while, variables |
| 5 | Functions, closures, recursion |

**What you can do now (without tutorials):**
- Explain every major compiler/interpreter component
- Build a lexer from scratch
- Build a recursive descent parser
- Design an AST with the Visitor pattern
- Build a tree-walking interpreter
- Add language features independently

**Optional next steps (Week 6–7):**
- **Week 6** — Standard library: add built-in `clock()`, make `print` a native function
- **Week 7** — Classes: `class`, `this`, `super`, inheritance

When ready for Week 6 or 7: create `.github/instructions/week6-stdlib.instructions.md` following the same format as these files and ask Compiler Tutor to load it.
