# Practical Compiler/Interpreter Workshop

### (Based on *Crafting Interpreters*)

---

# Goal

This repository is not just for building a compiler.

Its purpose is to become capable of designing and implementing a programming language **without relying on tutorials**.

By the end of this workshop I should be able to:

* Explain every major compiler component.
* Build a lexer from scratch.
* Build a parser from scratch.
* Design and implement an Abstract Syntax Tree.
* Build an interpreter.
* Debug parsing and runtime problems.
* Add completely new language features independently.

---

# Learning Rules

These rules are non-negotiable.

## Rule 1

Never copy code without understanding it.

If I cannot explain why a piece of code exists, I should not write it.

---

## Rule 2

Every feature must have tests.

No exceptions.

---

## Rule 3

Small commits.

One concept.

One implementation.

One test.

---

## Rule 4

If stuck for more than 30 minutes:

* Write down exactly where I am stuck.
* Explain what I tried.
* Research.
* Ask for help if needed.

---

## Rule 5

Always finish a module with reflection.

Learning happens during reflection.

---

# Repository Structure

```
CompilerWorkshop/

│
├── src/
│
├── tests/
│
├── examples/
│
├── docs/
│
├── learning/
│   │
│   ├── Dashboard.md
│   ├── Progress.md
│   ├── StruggleLog.md
│   ├── RevisionQueue.md
│   ├── WeeklyReflection.md
│   ├── SelfAssessment.md
│   ├── QuizAnswers.md
│   └── Notes.md
│
├── challenges/
│   ├── DebugChallenges/
│   ├── MiniProjects/
│   └── Extensions/
│
└── PLAN.md
```

---

# Learning Workflow

Every single module follows the exact same process.

```
Read

↓

Understand

↓

Predict

↓

Implement

↓

Test

↓

Debug

↓

Explain

↓

Reflect

↓

Rate Yourself

↓

Schedule Revision
```

Never skip steps.

---

# Weekly Modules

---

# Week 1

## Topic

Lexer

## Reading

* Introduction
* Scanning chapter

## Learn

Understand:

* Source code
* Characters
* Tokens
* Lexemes
* Literals
* Keywords
* Whitespace
* Comments

---

## Before Coding

Answer these questions without Google.

* What is a token?
* Why do we need a lexer?
* Why isn't regex enough?
* What happens to whitespace?
* Why are keywords scanned as identifiers first?

---

## Implementation

Build:

* Token class
* TokenType enum
* Lexer
* Scanner loop

---

## Tests

Test:

* Numbers
* Strings
* Operators
* Parentheses
* Identifiers
* Keywords
* Unknown characters

---

## Debug Challenge

Purposely introduce one bug.

Find it.

Explain it.

Fix it.

---

## Deliverables

```
src/lexer/

tests/lexer/

examples/week1/

docs/week1.md
```

---

# Week 2

## Topic

Parser

Learn:

* Grammar
* Expressions
* Precedence
* Associativity
* Recursive Descent

Implementation:

* Parser

* Expression parsing

* Statement parsing

Tests:

* Nested expressions

* Parentheses

* Invalid syntax

Debug challenge included.

---

# Week 3

## Topic

AST + Interpreter

Learn:

* Tree Walking

* Visitor Pattern

* Runtime values

Implementation:

* AST

* Evaluator

* REPL

Tests:

* Variables

* Expressions

* Print

---

# Week 4

## Topic

Control Flow

Implement

* if

* else

* while

Improve

* Runtime errors

* Parser errors

* Source locations

---

# Week 5

## Topic

Functions

Implement

* Function declarations

* Calls

* Scope

* Closures

Tests

* Recursion

* Local variables

* Closures

---

# Week 6

## Topic

Standard Library

Implement

* print()

* clock()

* basic utilities

Build

* Test Harness

---

# Week 7

Optional

Classes

Inheritance

Methods

this

super

---

# Week 8-10

Choose ONE

Option A

Bytecode VM

OR

Option B

Design your own language feature.

Examples:

* Arrays

* Switch

* Modules

* Pattern Matching

* Generics

* String interpolation

---

# Module Completion Checklist

Every module must complete this.

```
Reading

[ ]

Concept Questions

[ ]

Implementation

[ ]

Unit Tests

[ ]

Debug Challenge

[ ]

Reflection

[ ]

Quiz

[ ]

Self Assessment

[ ]

Revision Scheduled

[ ]
```

---

# Progress Dashboard

Update after every study session.

```
Overall Progress

□□□□□□□□□□ 0%

Modules Complete

0 / 10

Current Module

Lexer

Current Difficulty

0/10

Average Understanding

0%

Average Confidence

0%

Average Debug Time

0 minutes

Revision Queue

0 topics

```

---

# Skill Tree

```
Level 1

Tokenizer Apprentice

[ ]

Level 2

Parser Builder

[ ]

Level 3

AST Architect

[ ]

Level 4

Interpreter Engineer

[ ]

Level 5

Scope Master

[ ]

Level 6

Language Designer

[ ]

Level 7

Virtual Machine Explorer

[ ]

Level 8

Compiler Engineer

[ ]
```

---

# Module Score

Every module is scored.

| Category          | Points |
| ----------------- | ------ |
| Reading           | 10     |
| Concept Questions | 10     |
| Implementation    | 20     |
| Tests             | 20     |
| Debugging         | 15     |
| Explanation       | 15     |
| Reflection        | 10     |

Maximum

100 points

---

# Score Guide

```
95-100

Mastered

85-94

Excellent

70-84

Comfortable

50-69

Needs Revision

Below 50

Redo Module
```

---

# Understanding Meter

Every topic gets rated.

Example

```
Lexer

Understanding

8/10

Implementation

9/10

Testing

8/10

Debugging

6/10

Teaching Someone Else

5/10

Overall

7.2/10
```

---

# Confidence Scale

Rate honestly.

```
1

Completely Lost

2

Barely Understand

3

Can Follow Along

4

Need Lots of Help

5

Can Build With Notes

6

Mostly Comfortable

7

Can Build Alone

8

Can Explain It

9

Can Teach It

10

Could Design It Myself
```

---

# Struggle Log

Whenever stuck for over 30 minutes, immediately record it.

Template

```
Date

Topic

Concept

Problem

Time Stuck

Things I Tried

Solution

Confidence (1-10)

Need Revision?

Yes / No

Questions Remaining
```

Example

```
Date

June 25

Topic

Lexer

Problem

Didn't understand why identifiers and keywords are scanned together.

Time

45 minutes

Solution

Scan identifier first.

Compare against keyword map afterwards.

Confidence

5

Need Revision

Yes
```

---

# Revision Queue

Anything marked "Need Revision" goes here.

```
[ ] Keywords

[ ] Recursive Descent

[ ] AST Visitor

[ ] Closures

[ ] Scope

[ ] Bytecode
```

Remove only when confidence reaches 8 or higher.

---

# Weekly Reflection

Every Sunday answer:

```
What did I learn?

What was hardest?

Biggest mistake?

Biggest success?

What finally clicked?

What should I review next week?

Questions I still have?

How motivated did I feel?

Hours studied this week?

Overall rating (/10)
```

---

# Knowledge Quiz

Every module create at least ten questions.

Examples

```
What is a token?

Difference between token and lexeme?

Why use an AST?

What is recursive descent?

Why precedence matters?

Difference between parse tree and AST?

Why do closures work?

Difference between compile-time and runtime?

What causes runtime errors?

Why do interpreters exist?
```

If I cannot answer these without notes,

I have not mastered the module.

---

# Monthly Review

At the end of every month.

Review:

* Dashboard

* Struggle Log

* Revision Queue

* Quiz Scores

* Reflection Notes

Ask:

* Am I improving?

* What concepts repeat?

* Where do I waste time?

* Which debugging mistakes happen often?

---

# Final Challenge

After completing the book, I must implement one completely new feature without following any tutorial.

Ideas

* Arrays

* Dictionaries

* For loops

* Switch

* Modules

* Imports

* Pattern Matching

* Exceptions

* String Interpolation

* Lambdas

If I can design and implement one feature independently, I have truly learned compiler construction.

---

# Definition of Success

This project is successful only if I can:

* Explain every major compiler component.
* Build a compiler without copying code.
* Debug parser and runtime errors confidently.
* Extend the language independently.
* Read compiler source code from other projects without feeling overwhelmed.

Building the compiler is only the milestone.

Understanding why it works is the real goal.
