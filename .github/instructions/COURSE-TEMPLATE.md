# COURSE TEMPLATE
# ─────────────────────────────────────────────────────────────────────────────
# HOW TO USE THIS TEMPLATE
#
# 1. Copy this file
# 2. Rename it:  <subject>-<module>.instructions.md
#    Examples:
#      react-week1-components.instructions.md
#      python-week2-data-structures.instructions.md
#      algorithms-week3-sorting.instructions.md
#      system-design-week1-basics.instructions.md
#
# 3. Fill in every section below — replace all placeholder text
# 4. Delete this header block (everything above the --- line)
# 5. Add the file to `.github/instructions/` — Learning Tutor discovers it automatically
# ─────────────────────────────────────────────────────────────────────────────

---
description: "<SUBJECT> — <MODULE NAME>: load when user asks about <keywords>, <more keywords>, <Week N>"
applyTo: "src/<relevant-folder>/**"
---

# <SUBJECT> — Module <N>: <MODULE NAME>

## The Big Picture

<!--
Explain how this module fits into the full course in 2-3 sentences.
Draw a simple pipeline/flow diagram showing what came before and what comes next.
Example for React Week 2 (State): "In Week 1 you built static components. This week
you make them interactive with state. In Week 3 you will connect them with data from an API."
-->

```
<Previous Module Output>
        ↓
  [ THIS MODULE ]    ← YOU ARE HERE
        ↓
<Next Module Input>
```

---

## Prerequisites

<!--
List what the user needs to understand before starting this module.
Link to previous module files if applicable.
-->

- [ ] Completed: `<previous-module>.instructions.md`
- [ ] Understands: <concept 1>
- [ ] Understands: <concept 2>

---

## Core Concepts

<!--
Explain the key ideas for this module IN PLAIN ENGLISH — no code yet.
Use analogies. Assume the reader knows nothing.
Cover at minimum 3-4 concepts. Each concept should have:
  - A plain-language definition
  - An analogy or real-world parallel
  - A concrete example

Example structure:
### Concept 1: What is X?
[explanation]
[analogy]
[example]
-->

### Concept 1: What is <CONCEPT>?

<!-- Plain English explanation here -->

### Concept 2: What is <CONCEPT>?

<!-- Plain English explanation here -->

### Concept 3: What is <CONCEPT>?

<!-- Plain English explanation here -->

---

## Phase 1 — Concept Check (answer before writing any code)

<!--
6 questions the user must answer in their own words before implementation starts.
Questions should test UNDERSTANDING, not memorisation.
Write answers in docs/weekly-notes.md
-->

Answer at least 4 of these in `docs/weekly-notes.md` before moving to Phase 2.

1. <Question testing conceptual understanding>
2. <Question asking for an analogy or real-world parallel>
3. <Question about WHY, not just WHAT>
4. <Question about a common mistake or edge case>
5. <Question connecting this concept to the previous module>
6. <Question about what would break if you got this wrong>

---

## Phase 2 — What You Are Building

<!--
List the exact files/components/functions the user will create this module.
Be specific about file paths.
-->

| File / Component | Purpose |
|------------------|---------|
| `<path/to/file>` | <What it does> |
| `<path/to/file>` | <What it does> |

---

## Phase 3 — Step-by-Step Build Guide

<!--
Break implementation into small, numbered steps.
For EACH step:
  - State the goal in one sentence
  - Give a "Predict before writing" prompt
  - Describe what to implement (not the full solution — the structure/API/interface)
  - State what "done" looks like for this step

Never give the complete solution upfront. Describe the shape of what to build.
-->

### Step 1: <Name>

**Goal:** <One sentence>

**Predict before writing:** *<Ask the user what they think this will look like>*

**What to implement:**
<!-- Describe the interface/structure, not the implementation -->

**Done when:** <Observable result that confirms this step works>

---

### Step 2: <Name>

**Goal:** <One sentence>

**Predict before writing:** *<Ask the user what they think this will look like>*

**What to implement:**
<!-- Describe the interface/structure, not the implementation -->

**Done when:** <Observable result that confirms this step works>

---

<!-- Add more steps as needed -->

---

## Phase 4 — Tests

<!--
Provide a table of test cases.
For each: input, action, expected output.
Include happy paths AND edge cases.
-->

| # | Input / Action | Expected Output |
|---|---------------|----------------|
| 1 | <input> | <expected> |
| 2 | <input> | <expected> |
| 3 | <input> | <expected (edge case)> |
| 4 | <input> | <expected (error case)> |

**Run tests:**
```bash
# Add the command to run tests for this project
<test command here>
```

---

## Phase 5 — Debug Challenge

<!--
Describe ONE specific bug the user should intentionally introduce.
The bug should exercise their understanding of the concept — not just syntax.
-->

After all tests pass:

1. **Introduce this bug:** <Specific change to make that breaks something>
2. **Predict:** Which test will break and why?
3. **Run tests:** Confirm the breakage matches your prediction.
4. **Fix it** and explain: *"Why did that specific bug cause that specific failure?"*
5. Write your explanation in `docs/weekly-notes.md`.

---

## Definition of Done — Module <N> ✅

Do not start the next module until every box is checked:

- [ ] <Specific deliverable 1>
- [ ] <Specific deliverable 2>
- [ ] <Specific deliverable 3>
- [ ] All <N> tests pass
- [ ] Debug challenge complete and explained in `docs/weekly-notes.md`
- [ ] Reflection written (see below)
- [ ] Confidence rating recorded

---

## Phase 6 — Reflection

Write answers in `docs/weekly-notes.md`:

1. What was the hardest concept in this module?
2. <Topic-specific reflection question>
3. <Topic-specific reflection question>
4. What would you do differently if you started this module again?
5. **Rate your confidence: 1–5**
   - 1–2 → revise in 2 days
   - 3 → revise in 1 week
   - 4–5 → revise in 2 weeks

---

## What's Next — Module <N+1> Preview

<!--
2-3 sentences previewing the next module to keep momentum.
Connect what they just built to what comes next.
-->

<Preview of next module>

When ready: ask Learning Tutor to load `<next-module>.instructions.md`.
