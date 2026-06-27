---
description: "Week 2 — Parser: load when user asks about parsing, grammar, expressions, precedence, associativity, recursive descent, Parser.java, parse tree, Week 2"
applyTo: "src/parser/**"
---

# Week 2 — Parser

## The Big Picture

```
List<Token>   ← output of your Week 1 Lexer
      ↓
  [ PARSER ]    ← YOU ARE HERE (Week 2)
      ↓
Abstract Syntax Tree (AST)
      ↓
 [ INTERPRETER ] ← Week 3
```

The lexer gave you a flat list of tokens. The parser's job is to find the **structure** hidden in that list: which tokens belong together, what comes first in precedence (`*` before `+`), and how expressions nest inside each other.

The parser output is a **tree** — the AST. Every node in the tree is either a leaf (a number or string) or an operation with children (binary expression, grouping, etc.).

---

## Core Concepts

### What is a Grammar?

A grammar is a set of rules describing what valid expressions look like. Example:

```
expression → literal | unary | binary | grouping
literal    → NUMBER | STRING | "true" | "false" | "nil"
unary      → ( "-" | "!" ) expression
binary     → expression operator expression
grouping   → "(" expression ")"
operator   → "==" | "!=" | "<" | "<=" | ">" | ">=" | "+" | "-" | "*" | "/"
```

This is called a **context-free grammar**. Each rule defines what one "thing" can look like. The parser applies these rules to the token stream.

### What is Operator Precedence?

`1 + 2 * 3` — which operation runs first?

In maths, `*` binds tighter than `+`. The result is `1 + 6 = 7`, not `3 * 3 = 9`.

A parser encodes precedence by using **separate grammar rules for each level**. Lower precedence rules call higher precedence rules. This is the key insight of recursive descent.

Precedence hierarchy (lowest to highest):

| Level | Operators | Associates |
|-------|-----------|------------|
| 1 | `==` `!=` | Left |
| 2 | `<` `>` `<=` `>=` | Left |
| 3 | `+` `-` | Left |
| 4 | `*` `/` | Left |
| 5 | `!` `-` (unary) | Right |
| 6 | literals, grouping | — |

### What is Associativity?

When the same operator appears twice: `8 - 4 - 2`.

- **Left-associative**: `(8 - 4) - 2 = 2` ← standard for `+`, `-`, `*`, `/`
- **Right-associative**: `8 - (4 - 2) = 6` ← used for some things (e.g. assignment)

Your parser will implement **left-associativity** using a while loop inside each rule.

### What is Recursive Descent?

A top-down parsing technique where each grammar rule becomes a method. The method for `expression` calls the method for `equality`, which calls `comparison`, which calls `term`, and so on down to `primary` (the base case).

It is called "recursive" because an expression can contain expressions inside grouping `(...)`.

Each method:
1. Calls the next-higher-precedence method
2. Checks if the current token is an operator at *this* level
3. If yes: consume the operator and call the next method again (making a binary node)
4. If no: return what the higher-precedence method returned

---

## Phase 1 — Concept Check (answer before writing any code)

Write answers in `docs/weekly-notes.md`. Answer at least 4.

1. What is operator precedence? Why does `2 + 3 * 4` equal `14` and not `20`?
2. What is associativity? How does `8 - 4 - 2` parse with left-associativity?
3. Why is recursive descent called "top-down"? What does "top" refer to?
4. What does the parser produce? Describe its output in one sentence.
5. What happens if the user types `(1 + 2`? What should the parser do?
6. Why does higher-precedence mean "deeper in the tree" rather than "higher up"?

---

## Phase 2 — What You Are Building

Create these files in `src/parser/`:

| File | Purpose |
|------|---------|
| `src/parser/Parser.java` | The recursive descent parser |

You also need AST node classes — put them in `src/ast/` (you will expand these in Week 3):

| File | Purpose |
|------|---------|
| `src/ast/Expr.java` | Base class / interface for all expression nodes |

For Week 2, you need these expression types (as inner classes or subclasses of `Expr`):

| Node | Fields | Represents |
|------|--------|-----------|
| `Expr.Binary` | `Expr left`, `Token operator`, `Expr right` | `1 + 2`, `a * b` |
| `Expr.Grouping` | `Expr expression` | `(1 + 2)` |
| `Expr.Literal` | `Object value` | `42`, `"hello"`, `true`, `nil` |
| `Expr.Unary` | `Token operator`, `Expr right` | `-5`, `!true` |

---

## Phase 3 — Step-by-Step Build Guide

### Step 1: Expr Node Classes

Before parsing, you need objects to build the tree from.

Create `src/ast/Expr.java`. Use the abstract class + static inner class pattern:

```java
package ast;
import lexer.Token;

public abstract class Expr {

    public static class Binary extends Expr {
        public final Expr left;
        public final Token operator;
        public final Expr right;
        public Binary(Expr left, Token operator, Expr right) { ... }
    }

    public static class Grouping extends Expr {
        public final Expr expression;
        public Grouping(Expr expression) { ... }
    }

    public static class Literal extends Expr {
        public final Object value;
        public Literal(Object value) { ... }
    }

    public static class Unary extends Expr {
        public final Token operator;
        public final Expr right;
        public Unary(Token operator, Expr right) { ... }
    }
}
```

*Predict before writing: Why is `Expr` abstract? What does that force every concrete node to be?*

---

### Step 2: Parser Class

Create `src/parser/Parser.java`.

**Fields:**
```java
private final List<Token> tokens;
private int current = 0;
```

**Helper methods (implement first):**

#### `peek()` → `Token`
Returns `tokens.get(current)` without advancing.

#### `previous()` → `Token`
Returns `tokens.get(current - 1)`. Used after `advance()`.

#### `isAtEnd()` → `boolean`
Returns `peek().type == TokenType.EOF`.

#### `advance()` → `Token`
If not at end: increment `current`. Return `previous()`.

#### `check(TokenType type)` → `boolean`
Returns `true` if `peek().type == type`. Does not consume.

#### `match(TokenType... types)` → `boolean`
Loops through types. If any `check()` succeeds: call `advance()` and return `true`.

#### `consume(TokenType type, String message)` → `Token`
If `check(type)`: call `advance()` and return. Else: throw a parse error with `message`.

---

### Step 3: Grammar Rules as Methods

Each rule becomes a method. Implement **from bottom to top** (start with `primary`, work up):

#### `primary()` → `Expr`
The base case. Handles:
- `FALSE` → `new Expr.Literal(false)`
- `TRUE` → `new Expr.Literal(true)`
- `NIL` → `new Expr.Literal(null)`
- `NUMBER` or `STRING` → `new Expr.Literal(previous().literal)`
- `LEFT_PAREN` → parse inner expression, `consume(RIGHT_PAREN, ...)`, return `new Expr.Grouping(expr)`
- else: throw parse error "Expect expression."

#### `unary()` → `Expr`
Checks for `BANG` or `MINUS`:
```
if match(BANG, MINUS):
    operator = previous()
    right = unary()         ← recursive call (right-associative)
    return new Expr.Unary(operator, right)
return primary()
```

#### `factor()` → `Expr`
Handles `*` and `/` (left-associative):
```
left = unary()
while match(STAR, SLASH):
    operator = previous()
    right = unary()
    left = new Expr.Binary(left, operator, right)
return left
```
*Why is it a `while` loop and not an `if`? What input exercises the loop more than once?*

#### `term()` → `Expr`
Handles `+` and `-` (same pattern as `factor`, calls `factor()` instead of `unary()`).

#### `comparison()` → `Expr`
Handles `>`, `>=`, `<`, `<=` (calls `term()`).

#### `equality()` → `Expr`
Handles `==` and `!=` (calls `comparison()`).

#### `expression()` → `Expr`
Entry point — just calls `equality()`.

#### `parse()` → `Expr` (public)
Calls `expression()` and returns the result.

---

## Phase 4 — Tests

Create `tests/ParserTest.java`. For each test: lex the source, parse the token list, assert on the tree structure.

Required test cases:

| # | Input | Expected tree |
|---|-------|--------------|
| 1 | `"1 + 2"` | `Binary(Literal(1), PLUS, Literal(2))` |
| 2 | `"1 + 2 * 3"` | `Binary(Literal(1), PLUS, Binary(Literal(2), STAR, Literal(3)))` |
| 3 | `"(1 + 2) * 3"` | `Binary(Grouping(Binary(...)), STAR, Literal(3))` |
| 4 | `"-5"` | `Unary(MINUS, Literal(5))` |
| 5 | `"!true"` | `Unary(BANG, Literal(true))` |
| 6 | `"1 == 1"` | `Binary(Literal(1), EQUAL_EQUAL, Literal(1))` |
| 7 | `"1 + 2 + 3"` | Left-associative: `Binary(Binary(1,+,2), +, 3)` |

**Run:**
```powershell
javac -d out src/lexer/*.java src/ast/*.java src/parser/*.java tests/ParserTest.java
java -cp out tests.ParserTest
```

---

## Phase 5 — Debug Challenge

After tests pass, introduce a bug. Ideas:
- In `factor()`, change `while` to `if` — breaks `1 * 2 * 3`
- In `unary()`, call `primary()` instead of `unary()` recursively — breaks `!!true`

Predict → break → find → fix → explain.

---

## Definition of Done — Week 2 ✅

- [ ] `src/ast/Expr.java` — Binary, Grouping, Literal, Unary nodes defined
- [ ] `src/parser/Parser.java` — all 8 grammar rules + helpers implemented
- [ ] All 7 tests pass
- [ ] Debug challenge complete
- [ ] Reflection written in `docs/weekly-notes.md`
- [ ] Confidence rating recorded

---

## Reflection

1. Why does each grammar rule call the rule *above* it in precedence?
2. What is the difference between `peek()` and `advance()` in the parser?
3. Why does `factor()` use a `while` loop? What would `if` break?
4. What would happen if the grammar was ambiguous (no precedence rules)?
5. Rate your confidence: 1–5. Schedule revision accordingly.

---

## What's Next — Week 3 Preview

You have a lexer that produces tokens and a parser that builds a tree. Now: **evaluate the tree**.

In Week 3 you will build an **Interpreter** that walks the AST recursively. Each node type (Binary, Unary, Literal) gets evaluated to produce a runtime value. You will also build the **Visitor pattern** to cleanly separate the tree nodes from the operations on them — and wire everything into a **REPL** so you can type expressions and see results interactively.
