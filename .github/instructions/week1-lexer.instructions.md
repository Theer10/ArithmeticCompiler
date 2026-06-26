---
description: "Week 1 — Lexer: load when user asks about tokens, scanning, lexer, TokenType, Lexer.java, Token.java, how to scan characters, lexemes, literals, Week 1"
applyTo: "src/lexer/**"
---

# Week 1 — Lexer

## The Big Picture

Before you touch a single line of code, understand where the Lexer fits in the full pipeline:

```
Source Code (raw string)
         ↓
    [ LEXER ]         ← YOU ARE HERE (Week 1)
         ↓
   List<Token>
         ↓
    [ PARSER ]        ← Week 2
         ↓
Abstract Syntax Tree
         ↓
  [ INTERPRETER ]     ← Week 3
         ↓
       Result
```

The lexer is the foundation. The parser and interpreter cannot exist without it. Get this right and the rest of the project builds on solid ground.

---

## Core Concepts

### What is a Token?

A token is the **smallest meaningful unit** in source code. Think of source code as a sentence — characters are letters, tokens are words. The string `"42"` is two characters; the token `NUMBER(42.0)` is one meaningful unit.

Every token carries four pieces of information:

| Field | Type | Purpose | Example |
|-------|------|---------|---------|
| `type` | `TokenType` | What kind of thing is this? | `PLUS`, `NUMBER`, `IF` |
| `lexeme` | `String` | The exact text from source | `"+"`, `"42"`, `"if"` |
| `literal` | `Object` | Parsed value (numbers/strings only) | `42.0`, `"hello"`, `null` |
| `line` | `int` | Which line it appeared on | `1` |

Example — for `x + 42`:
```
Token(IDENTIFIER, "x",  null, 1)
Token(PLUS,       "+",  null, 1)
Token(NUMBER,     "42", 42.0, 1)
Token(EOF,        "",   null, 1)
```

### Lexeme vs Literal — They Are Different

- **Lexeme**: the raw characters from the source (`"42"`, a String)
- **Literal**: the parsed value (`42.0`, a Double)

For `+` and `if`, there is no meaningful literal — it is `null`. For a number like `3.14`, the lexeme is `"3.14"` and the literal is `3.14` (a double). This distinction matters when the interpreter needs to do arithmetic.

### What Does the Lexer Do?

The lexer walks the source string **one character at a time**. It keeps three internal counters:

- `start` — where the current lexeme began
- `current` — where we are right now
- `line` — which line we are on (incremented on `\n`)

Main loop: peek at `source.charAt(current)`, figure out what token starts here, consume the right number of characters, create a Token, move on.

### Why Not Just Use Regex?

Regex can match individual patterns but:
1. Cannot track line numbers across a full file
2. Cannot handle context (e.g. knowing when a string ends at `"`)
3. Breaks down for multi-character tokens like `!=`, `<=`, `==`
4. A hand-written lexer is faster, easier to debug, and easier to extend

---

## Phase 1 — Concept Check (answer before writing any code)

Write your answers in `docs/weekly-notes.md`. Answer at least 4 before moving on.

1. What is a token? Give a specific example from the expression `while (x > 0) { print x; }`.
2. What is the difference between a lexeme and a literal? Give an example where they are not the same.
3. Why do we need both `start` and `current` as separate fields? What would go wrong with only one?
4. What should the lexer do when it encounters whitespace — ` `, `\t`, `\r`?
5. Why is `EOF` added as the last token? What would break if we left it out?
6. The source has `!` — could this be `BANG` or the start of `!=`. How does the lexer decide which one?

---

## Phase 2 — What You Are Building

Three files, implemented in this order:

| File | What to implement |
|------|------------------|
| `src/lexer/TokenType.java` | All valid token types as enum constants |
| `src/lexer/Token.java` | A class holding one token's type, lexeme, literal, line |
| `src/lexer/Lexer.java` | The scanner that walks source and returns `List<Token>` |

---

## Phase 3 — Step-by-Step Build Guide

### Step 1: TokenType Enum

Before writing: *What categories of tokens does your language need?*

Add these constants to the enum in `src/lexer/TokenType.java`:

**Group A — Single-character tokens:**
`LEFT_PAREN`, `RIGHT_PAREN`, `LEFT_BRACE`, `RIGHT_BRACE`,
`COMMA`, `DOT`, `MINUS`, `PLUS`, `SEMICOLON`, `SLASH`, `STAR`

**Group B — One or two character tokens:**
`BANG`, `BANG_EQUAL`, `EQUAL`, `EQUAL_EQUAL`,
`GREATER`, `GREATER_EQUAL`, `LESS`, `LESS_EQUAL`

**Group C — Literals:**
`IDENTIFIER`, `STRING`, `NUMBER`

**Group D — Keywords:**
`AND`, `CLASS`, `ELSE`, `FALSE`, `FUN`, `FOR`, `IF`, `NIL`, `OR`,
`PRINT`, `RETURN`, `SUPER`, `THIS`, `TRUE`, `VAR`, `WHILE`

**Group E — End of input:**
`EOF`

*Predict before writing: Why do we need both `BANG` and `BANG_EQUAL` as separate enum values? What source text produces each one?*

---

### Step 2: Token Class

Add to `src/lexer/Token.java`. Think about why each field is the type it is before writing.

**Fields:**
```java
final TokenType type;
final String lexeme;
final Object literal;   // ← why Object, not String or double?
final int line;
```

**Constructor:** takes all four fields, assigns them.

**`toString()`:** return something useful for debugging, e.g. `type + " " + lexeme + " " + literal`.

*Predict before writing: Why is `literal` typed as `Object` instead of `String` or `double`?*

---

### Step 3: Lexer Class

Work through `src/lexer/Lexer.java`. Implement methods **in this order** — each one builds on the last.

**Fields to add:**
```java
private final String source;
private final List<Token> tokens = new ArrayList<>();
private int start = 0;
private int current = 0;
private int line = 1;
```

**Methods — implement in this order:**

#### 1. `isAtEnd()` → `boolean`
Returns `true` when `current >= source.length()`. The simplest method. Start here.

#### 2. `advance()` → `char`
Returns `source.charAt(current)` then increments `current`. Every time the lexer "consumes" a character, it calls this.

#### 3. `peek()` → `char`
Returns `source.charAt(current)` **without** incrementing. Returns `'\0'` if at end.
*Why does this exist separately from `advance()`? What breaks if you just used `advance()` everywhere?*

#### 4. `peekNext()` → `char`
Returns `source.charAt(current + 1)`. Returns `'\0'` if past end. Used to see two characters ahead (needed for decimal numbers like `3.14`).

#### 5. `match(char expected)` → `boolean`
If `source.charAt(current) == expected`: advance and return `true`.
Otherwise: return `false` (do NOT advance).
*This is the key method for two-character tokens. How does `!=` use this?*

#### 6. `addToken(TokenType type)` and `addToken(TokenType type, Object literal)`
Extract `source.substring(start, current)` as the lexeme. Create a new `Token`. Add to `tokens`.

#### 7. `scanToken()` → `void`
This is the heart of the lexer. Call `advance()` to get one character, then `switch` on it:

- `(` → `addToken(LEFT_PAREN)`
- `)` → `addToken(RIGHT_PAREN)`
- `{` → `addToken(LEFT_BRACE)`
- `}` → `addToken(RIGHT_BRACE)`
- `,`, `.`, `-`, `+`, `;`, `*` → their matching token types
- `!` → `addToken(match('=') ? BANG_EQUAL : BANG)`
- `=` → `addToken(match('=') ? EQUAL_EQUAL : EQUAL)`
- `<` → `addToken(match('=') ? LESS_EQUAL : LESS)`
- `>` → `addToken(match('=') ? GREATER_EQUAL : GREATER)`
- `/` → if `match('/')`: consume until end of line (comment). Else `addToken(SLASH)`.
- `' '`, `'\r'`, `'\t'` → ignore (do nothing)
- `'\n'` → `line++`
- `'"'` → call `string()`
- default: if digit → call `number()`; if letter/underscore → call `identifier()`; else print an error

#### 8. `string()` → `void`
Called after consuming the opening `"`. Advance until you find the closing `"` or reach end of input.
- If end of input: report an "unterminated string" error
- Otherwise: consume the closing `"`, extract the string value (without the quotes as `source.substring(start + 1, current - 1)`), call `addToken(STRING, value)`

#### 9. `number()` → `void`
Called when the current character is a digit. Advance while `isDigit(peek())`.
Check for a decimal: if `peek() == '.'` and `isDigit(peekNext())`, consume the `.` and more digits.
Call `addToken(NUMBER, Double.parseDouble(source.substring(start, current)))`.

#### 10. `identifier()` → `void`
Called when current char is a letter or `_`. Advance while `isAlphaNumeric(peek())`.
Extract the word: `source.substring(start, current)`.
Look it up in a keywords map. If found: `addToken(keywordType)`. If not: `addToken(IDENTIFIER)`.

**Keywords map** (add as a static field):
```java
private static final Map<String, TokenType> keywords;
static {
    keywords = new HashMap<>();
    keywords.put("and",    TokenType.AND);
    keywords.put("class",  TokenType.CLASS);
    keywords.put("else",   TokenType.ELSE);
    keywords.put("false",  TokenType.FALSE);
    keywords.put("for",    TokenType.FOR);
    keywords.put("fun",    TokenType.FUN);
    keywords.put("if",     TokenType.IF);
    keywords.put("nil",    TokenType.NIL);
    keywords.put("or",     TokenType.OR);
    keywords.put("print",  TokenType.PRINT);
    keywords.put("return", TokenType.RETURN);
    keywords.put("super",  TokenType.SUPER);
    keywords.put("this",   TokenType.THIS);
    keywords.put("true",   TokenType.TRUE);
    keywords.put("var",    TokenType.VAR);
    keywords.put("while",  TokenType.WHILE);
}
```

#### 11. `isDigit(char c)` → `boolean`
Returns `c >= '0' && c <= '9'`.

#### 12. `isAlpha(char c)` → `boolean`
Returns true if letter or `_`: `(c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_'`.

#### 13. `isAlphaNumeric(char c)` → `boolean`
Returns `isAlpha(c) || isDigit(c)`.

#### 14. `scanTokens()` → `List<Token>`
The public entry point. Already stubbed. Implement:
```
while not isAtEnd():
    start = current
    scanToken()
add EOF token
return tokens
```

---

## Phase 4 — Tests

Expand `tests/LexerTest.java`. For each test: create a `Lexer`, call `scanTokens()`, check the result.

Write tests for these 10 cases — think about what to assert before writing each one:

| # | Input | Expected token types |
|---|-------|---------------------|
| 1 | `"42"` | `NUMBER`, `EOF` |
| 2 | `"1 + 2"` | `NUMBER`, `PLUS`, `NUMBER`, `EOF` |
| 3 | `"(3 * 4)"` | `LEFT_PAREN`, `NUMBER`, `STAR`, `NUMBER`, `RIGHT_PAREN`, `EOF` |
| 4 | `"!="` | `BANG_EQUAL`, `EOF` |
| 5 | `"\"hello\""` | `STRING` with literal `"hello"`, `EOF` |
| 6 | `"foo"` | `IDENTIFIER` with lexeme `"foo"`, `EOF` |
| 7 | `"if"` | `IF` (not IDENTIFIER), `EOF` |
| 8 | `"1   +   2"` | Same as `"1+2"` — whitespace is ignored |
| 9 | `"1 // comment\n2"` | `NUMBER(1)`, `NUMBER(2)`, `EOF` — comment consumed |
| 10 | `"3.14"` | `NUMBER` with literal `3.14`, `EOF` |

**Run tests:**
```powershell
mkdir out
javac -d out src/lexer/*.java tests/LexerTest.java
java -cp out tests.LexerTest
```

All 10 must pass before Week 1 is done.

*Before writing each test: predict what you will assert. After the test passes: explain in one sentence why that assertion is correct.*

---

## Phase 5 — Debug Challenge

After all tests pass:

1. **Introduce one bug intentionally.** Ideas: in `match()`, return `true` without advancing `current`. Or in `number()`, skip consuming the `.` and decimal digits.
2. **Predict** which test will break and why.
3. Run tests. Confirm the breakage matches your prediction.
4. **Find and fix** the bug.
5. Write in `docs/weekly-notes.md`: *"The bug I introduced was X. It broke test Y because Z."*

---

## Definition of Done — Week 1 ✅

Do not start Week 2 until every box is checked:

- [ ] `TokenType.java` — all enum constants from Groups A–E defined
- [ ] `Token.java` — fields, constructor, `toString()` complete
- [ ] `Lexer.java` — all 14 methods implemented and working
- [ ] All 10 tests pass
- [ ] Debug challenge complete and explained in `docs/weekly-notes.md`
- [ ] Reflection written (see below)
- [ ] Confidence rating recorded

---

## Phase 6 — Reflection

Write answers in `docs/weekly-notes.md`:

1. What was the hardest part of the lexer to understand?
2. Why does `peek()` exist as a separate method from `advance()`? What breaks without it?
3. Why is the keywords map `static`? What would be different if it were an instance field?
4. If you were building this from scratch again, what would you do differently?
5. **Rate your confidence: 1–5**
   - 1–2: revise in 2 days
   - 3: revise in 1 week
   - 4–5: revise in 2 weeks

---

## What's Next — Week 2 Preview

The lexer output — a flat `List<Token>` — is now the input to the **Parser**.

The parser's job: understand the *structure* of the token stream. It figures out which tokens group together (`*` binds tighter than `+`), how expressions nest, and what the grammar of the language is.

The parser's output is a tree — the **Abstract Syntax Tree**. In Week 3 you will walk that tree to evaluate expressions and run your first program.

When ready: ask Compiler Tutor to load Week 2.
