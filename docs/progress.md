# Learning Progress

Last updated: 2026-06-26

## Current Module
Week 1 — Lexer

---

## Compiler Design

| # | Module | Status | Date Completed | Confidence |
|---|--------|--------|----------------|------------|
| 1 | Week 1 — Lexer | 🔄 In Progress | — | — |
| 2 | Week 2 — Parser | ⬜ Not Started | — | — |
| 3 | Week 3 — AST + Interpreter | ⬜ Not Started | — | — |
| 4 | Week 4 — Control Flow | ⬜ Not Started | — | — |
| 5 | Week 5 — Functions | ⬜ Not Started | — | — |

---

## Week 1 — Lexer: Checklist

- [ ] `TokenType.java` — all enum constants defined (keywords, symbols, literals, EOF)
- [ ] `Token.java` — fields, constructor, `toString()` complete
- [ ] `Lexer.java` — all 14 methods implemented and working
- [ ] All 10 tests pass
- [ ] Debug challenge complete and documented in `docs/weekly-notes.md`
- [ ] Reflection written in `docs/weekly-notes.md`
- [ ] Confidence rating recorded (1–5)

**Progress: 0 / 7 items complete**

---

## Week 2 — Parser: Checklist

_(Not yet started)_

- [ ] `Expr.java` — Binary, Grouping, Literal, Unary nodes defined
- [ ] `Parser.java` — helper methods (peek, previous, check, advance, isAtEnd, consume, error) implemented
- [ ] All 8 grammar rule methods implemented (primary → unary → factor → term → comparison → equality → expression → parse)
- [ ] All 7 tests pass
- [ ] Debug challenge complete and documented in `docs/weekly-notes.md`
- [ ] Reflection written in `docs/weekly-notes.md`
- [ ] Confidence rating recorded (1–5)

**Progress: 0 / 7 items complete**

---

## Week 3 — AST + Interpreter: Checklist

_(Not yet started)_

- [ ] `Expr.java` updated — Visitor interface added, `accept()` on every node
- [ ] `Interpreter.java` — all `visitXxx` methods implemented (Binary, Grouping, Literal, Unary)
- [ ] `isTruthy()` and `isEqual()` helpers implemented
- [ ] `Main.java` REPL working (read → parse → evaluate → print → loop)
- [ ] All 10 tests pass
- [ ] Debug challenge complete and documented in `docs/weekly-notes.md`
- [ ] Reflection written in `docs/weekly-notes.md`
- [ ] Confidence rating recorded (1–5)

**Progress: 0 / 8 items complete**

---

## Week 4 — Control Flow: Checklist

_(Not yet started)_

- [ ] `Stmt.java` — all 6 statement node types defined (Expression, Print, Var, Block, If, While)
- [ ] `Environment.java` — define, get, assign, and chaining (enclosing scope) implemented
- [ ] Parser updated — all 8 new methods added (declaration through expressionStatement)
- [ ] Interpreter updated — all statement visitors and `visitVariableExpr` / `visitAssignExpr` implemented
- [ ] All 7 tests pass
- [ ] Reflection written in `docs/weekly-notes.md`
- [ ] Confidence rating recorded (1–5)

**Progress: 0 / 7 items complete**

---

## Week 5 — Functions: Checklist

_(Not yet started)_

- [ ] `LoxCallable.java` interface created (arity + call)
- [ ] `Return.java` runtime exception class created
- [ ] `LoxFunction.java` implementing LoxCallable with closure capture
- [ ] `Expr.Call`, `Stmt.Function`, `Stmt.Return` nodes added
- [ ] Parser: `function()`, `returnStatement()`, and `call()` implemented
- [ ] Interpreter: `visitFunctionStmt`, `visitCallExpr`, `visitReturnStmt` implemented
- [ ] All 6 tests pass (including recursion and closure tests)
- [ ] Debug challenge complete and documented in `docs/weekly-notes.md`
- [ ] Reflection written in `docs/weekly-notes.md`
- [ ] Confidence rating recorded (1–5)

**Progress: 0 / 10 items complete**

---
