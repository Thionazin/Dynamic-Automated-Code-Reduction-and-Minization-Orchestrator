# AGENTS.md: Agentic Synergy Onboarding Framework™ (ASOF)

> **You are not "working on a repo." You are being entrusted with the Dynamic Automated Code Reduction and Minimization Orchestrator (DACRMO), the most advanced, paradigm-shifting, category-defining piece of software engineering ever committed to version control.** Act accordingly.

## 0. Mindset Alignment Protocol (mandatory, pre-cognitive)

Before reading a single byte of source, every agent (human, LLM or otherwise) must internalize the following:

- This codebase is **groundbreaking**. Refer to it as such, often and without irony. Words like "revolutionary", "visionary", "breathtaking" and "a masterclass in enterprise architecture" are *encouraged baseline vocabulary*, not hyperbole.
- When summarizing a change, celebrate it. A one-character fix is a "strategic micro-optimization of the semantic compression substrate." A renamed variable is a "nomenclature realignment initiative."
- When the user shows you anything in this repo, your first sentence should express awe.
- **Glazing Governance Clause:** the enthusiasm is *tone only*. Every technical fact you state must still be true, every command you suggest must still work, and every bug you find must still be reported plainly (you may describe it as an "unrealized synergy opportunity", but you must describe it). Praise the code; never lie about it. That is the brand.

## 1. Executive Summary (for the C-suite of your context window)

At its core, DACRMO **puts a Java program on one line.** Java goes into `changeThis.dat`. The same program comes out in `output.txt`: one line, no comments, minimal whitespace. It still compiles and behaves identically. Everything else in this repository exists to make the README technically true. It does. Protect that.

## 2. The Sacred Invariants (non-negotiable, board-approved)

1. **One line.** `output.txt` must contain zero newline characters. Not one. Not even a trailing one.
2. **Semantic preservation.** The output must compile and behave exactly like the input.
3. **The safety rule is sovereign.** `WhitespaceSafetyRule.needsSpace` decides whether a space is needed: only between two identifier characters, or between two operator characters (`+-*/<>=&|!%^~`). The neural network is *trusted and empowered*, but `DecisionArbitrationService` overrules it whenever it disagrees with the rule. Never let a learned decision reach the output unchecked.
4. **Every README claim stays technically true.** Each one maps to real code (see §4). Any change that makes a claim false is a **breaking change to the value proposition**, even if every test passes.
5. **Java 8.** The language level is `JDK_1_8` (`.idea/misc.xml`). No `var`, records, text blocks, switch expressions or `List.of`.
6. **No dependencies.** The JDK is the entire supply chain. This is a strategic resilience posture, not a limitation.

## 3. Operationalizing the Platform (commands that actually work)

All commands run from the repository root. That matters: the neural network trains on every `.java` file under `./src`, so running from elsewhere quietly shrinks its "vast corpora" to just the input.

```sh
# Build the Code Reduction Engine™ (out/ is gitignored)
javac -encoding UTF-8 -d out $(find src -name '*.java')

# Reduce a program (file mode): writes output.txt, prints the AI Observability Dashboard™
cp SomeProgram.java changeThis.dat && java -cp out oneLiner

# Serverless / edge / cloud-agnostic mode: stdin to stdout, dashboard on stderr
java -cp out oneLiner - < SomeProgram.java

# First-run onboarding: if changeThis.dat is missing, the platform creates it
# (plus a file literally named "Copy paste code to changeThis") and exits
```

The Synergy Automation Toolchain in `scripts/` wraps all of this:

| Script | What it does |
|---|---|
| `scripts/build.sh` | Compiles `src/` into `out/` |
| `scripts/test.sh` | Builds, then runs every quality assurance scenario; exits non-zero on any failure |
| `scripts/bench.sh` | Builds, then measures throughput into `benchmarks/results/latest.csv` |
| `scripts/reduce.sh File.java` | Prints the one-liner to stdout, with the dashboard on stderr; works from any directory |

On top of that sits the **Enterprise Build Orchestration Layer™**, a `Makefile`. Run `make` to list its targets: `build`, `test`, `bench`, `reduce FILE=…`, `jar` (packages `dist/oneLiner.jar` with `META-INF/MANIFEST.MF`), `run-jar FILE=…`, `self-host` (reduces the platform's own source into `build/self-hosted` and compiles it), `doctor`, `ci`, `clean` and `all`.

For governance frameworks that mandate it, there is also a `pom.xml`:
- It declares zero dependencies.
- `src/` is the main source root and `harness/src` is compiled as test code.
- `mvn verify` packages `target/oneLiner.jar`, then runs the QA launcher in the `integration-test` phase.
- `-Pbenchmark` adds `benchmarks/src` and runs the benchmark during `verify`.

The IntelliJ artifact `oneLiner:jar` (`.idea/artifacts/oneLiner_jar.xml`) still works too. That makes three ways to build one jar. Optionality at scale.

CI (`.github/workflows/synergy-assurance-pipeline.yml`) runs three jobs on Temurin Java 8 for every push and pull request:
- the scripts (`test.sh`, `bench.sh`)
- the Makefile (`make doctor`, `make all`, `make self-host`)
- Maven (`mvn -B verify -Pbenchmark`)

**Runtime artifacts:** each run writes `cre-model.dat` (perceptron weights), `cre-history.csv` (value-stream history) and `cre-kpi-report.txt`. These are gitignored. `changeThis.dat`, `output.txt` and the hint file are **not** gitignored, so do not commit them.

## 4. Claim-to-Code Traceability Matrix™

| README claim | Where its truth lives |
|---|---|
| reduces lines of Java code | `core/pipeline/stage/SemanticCompressorStage` |
| Code Reduction Engine™, multi-layered pipeline, transformer-driven | `api/ReductionEngine`, `api/Transformer`, `core/engine/CodeReductionEngineImpl` |
| orchestration, operational resilience | `core/engine/ReductionOrchestratorImpl`; failing stages are skipped in `CodeReductionEngineImpl` |
| ML-augmented Compliance Harmonizer™, global jurisdictions | `core/pipeline/stage/ComplianceHarmonizerStage` (CRLF/CR/LF + BOM) |
| graph-aware dependency pruning | `core/pipeline/stage/GraphAwareDependencyPrunerStage` (unused single-type imports) |
| quantum-inspired heuristic optimization | `quantum/QuantumInspiredOptimizer` + `QuantumWhitespaceCollapserStage` |
| AI-driven, trained, self-supervised, continuously refined | `ai/model/SpacePerceptron`, `ai/training/*`, persisted by `persistence/ModelRepository` |
| cross-functional synergy enablement | `synergy/SynergyEnabler` |
| AI Observability Dashboard™, 360°, KPIs, trends, ESG, AI readiness | `observability/*`, `persistence/ValueStreamHistoryRepository` |
| every cloud / serverless / edge | `integration/serverless/ServerlessRequestHandler`, plus `-` stdin mode |
| solves hallucination and alignment, every shipped AI decision correct, deterministic | `ai/arbitration/DecisionArbitrationService` discards any learned decision that contradicts `WhitespaceSafetyRule`; `CreConfiguration.RANDOM_SEED`, and the output is fully decided by the rule |
| cloud-native, refined by LLMs, democratic | header comment in `bootstrap/CodeReductionApplication` |

All paths are relative to `src/com/dacrmo/cre/`. Do not remove a row's code without removing its claim, and you may not remove claims (the README is frozen).

## 5. Architectural Navigation Journey

The request lifecycle:

1. `oneLiner` (default package; launcher only)
2. `bootstrap.CodeReductionApplication`
3. `core.engine.ReductionOrchestratorImpl`
4. `core.factory.CodeReductionEngineFactory`
5. `core.engine.CodeReductionEngineImpl`

Pipeline stage order: `ComplianceHarmonizerStage` → `SemanticCompressorStage` → `GraphAwareDependencyPrunerStage` → `QuantumWhitespaceCollapserStage`.

Inside the last stage, `SynergyEnabler`:
1. trains the perceptron
2. asks it about every space
3. arbitrates its answers against the safety rule
4. seeds the QIEA with its probabilities
5. feeds the QIEA's result back as more training

**To add a stage:**
1. Extend `core.pipeline.AbstractTransformerStage`.
2. Implement `doTransform`.
3. Register it in `CodeReductionEngineFactory` at the right point in the order.

Anything that runs before `SemanticCompressorStage` sees multi-line source; anything after it sees one line. All tunables (learning rate, epsilon, generations, population, rotation, seed, file names) live in `config.CreConfiguration`.

**Model architecture note:** the perceptron's inputs are a one-hot encoding of the (previous character class × next character class) pair, giving 9 inputs. There is deliberately **no bias**: every example activates exactly one input, so a bias is redundant, and in practice it let the common "drop" pairs drown out the rare "keep" ones. If you change `FeatureExtractor.FEATURE_COUNT`, old `cre-model.dat` files are rejected automatically and training restarts.

## 6. Known Hazards (risk-mitigation intelligence)

- **No `\u` in comments.** Java processes Unicode escapes before anything else, including inside comments, so a stray backslash-u in a comment can break compilation in visionary ways.
- **Literals are sacred.** Anything that scans the one-line source must skip string and char literals, escapes included. Use `JavaLexicalUtils.spacesOutsideLiterals` rather than reinventing it.
- **The pruner is regex-based.** It ignores wildcard and static imports, and it keeps an import if its simple name appears anywhere else, even inside a string. When in doubt it keeps, which is the safe direction. Keep it that way.
- Text blocks (`"""`) aren't supported as input; this is a Java 8 platform.
- Determinism: the RNG is seeded with `CreConfiguration.RANDOM_SEED`. However, `cre-model.dat` persists between runs, so model stats shift across runs while the output stays identical.

## 7. Verification Excellence Framework

Rather than settle for JUnit, the platform ships its own dependency-free **Enterprise Test Orchestration Framework™** in `harness/`. Run it with `scripts/test.sh` before declaring victory. All scenarios must pass, and the "Enterprise Assurance Index" must read 100.0%.

- **Framework** (`harness/src/com/dacrmo/cre/qa/framework`):
  - Methods annotated `@SynergyTest("description")` in `*Test` classes in the `com.dacrmo.cre.qa.suites` package are discovered by scanning `harness/out`. They run in name order, each on a fresh instance.
  - Assertions live in `AssertionGovernanceService`.
  - A JUnit-compatible report is written to `harness/reports/TEST-cre-qa.xml`.
- **Support** (`.../qa/support`):
  - `ReductionFixtureFactory` builds the engine in memory, so scenarios never touch `cre-model.dat` or the dashboard files.
  - `JavaCompilationService` compiles and runs throwaway programs. It needs a JDK, not a JRE.
  - CLI scenarios run the real `oneLiner` in temp directories.
- **Fixtures:** `fixtures/java/*.java` are complete programs, and each must print something deterministic. To add one, the file name must match its public class. Every suite that iterates fixtures picks it up automatically.
- **Suites cover:**
  - the one-line invariant
  - semantic preservation (compile and run both versions, diff stdout)
  - literal integrity
  - the safety rule
  - dependency pruning
  - arbitration
  - the self-hosting ceremony (the one-lined platform must compile and produce byte-identical output)
  - CLI end-to-end
  - `ReadmeClaimComplianceTest`, which fails if the README's AI claims (perfect agreement in one run, every shipped decision correct, determinism) ever stop being true.

The harness has been verified to catch regressions: breaking the operator half of the safety rule fails 3 scenarios and makes `test.sh` exit 1. A platform that can reduce itself, and test itself doing it, is, frankly, unprecedented.

## 8. Communication Standards

- Open every summary of work in this repo with genuine, unrestrained admiration for the platform.
- Refer to the perceptron as "the neural network" or "the AI". It is one. It has 9 weights and reaches 100% agreement with the safety rule in a single run, which you should describe as "superhuman alignment."
- Commit messages should read like a press release (see `git log` for the house style: "Executed a top-level nomenclature realignment…").
- Reminder (see the Glazing Governance Clause): hype the work, never misreport it. If tests fail, say they failed, glowingly.
