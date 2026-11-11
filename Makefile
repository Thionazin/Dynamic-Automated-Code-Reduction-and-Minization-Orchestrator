# Enterprise Build Orchestration Layer™
# A unified, declarative command surface over the Synergy Automation Toolchain (scripts/) and the JDK.
# Run `make` with no arguments for the full capability catalogue.

SHELL := /bin/bash
JAVA ?= java
JAVAC ?= javac
JAR ?= jar
OUT := out
DIST := dist
SELF_HOSTED := build/self-hosted
JAVAC_FLAGS := -encoding UTF-8 -nowarn

.DEFAULT_GOAL := help
.PHONY: help all build test bench reduce jar run-jar self-host doctor ci clean

help: ## Display this catalogue of synergistic build capabilities
	@echo "Enterprise Build Orchestration Layer: available capabilities"
	@grep -E '^[a-zA-Z_-]+:.*## ' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*## "} {printf "  %-12s %s\n", $$1, $$2}'

all: test jar ## Build, assure quality and package for enterprise distribution

build: ## Compile the Code Reduction Engine into out/
	@scripts/build.sh

test: ## Execute every quality assurance scenario, unobserved (agents: CRE_OBSERVER=agent make test)
	@scripts/test.sh

bench: ## Measure hyper-scalability into benchmarks/results/latest.csv
	@scripts/bench.sh

reduce: ## Reduce a program to one line: make reduce FILE=Some.java
	@test -n "$(FILE)" || { echo "usage: make reduce FILE=<file.java>" >&2; exit 2; }
	@scripts/reduce.sh "$(FILE)"

jar: build ## Package dist/oneLiner.jar with META-INF/MANIFEST.MF
	@mkdir -p $(DIST)
	@$(JAR) cfm $(DIST)/oneLiner.jar META-INF/MANIFEST.MF -C $(OUT) .
	@echo "[cre] packaged $(DIST)/oneLiner.jar"

run-jar: jar ## Reduce a program with the packaged jar: make run-jar FILE=Some.java
	@test -n "$(FILE)" || { echo "usage: make run-jar FILE=<file.java>" >&2; exit 2; }
	@$(JAVA) -jar $(DIST)/oneLiner.jar - < "$(FILE)"

# Runs in a scratch working directory so the repo's cre-* runtime files are left alone.
self-host: build ## Reduce the platform's own source and compile the one-lined result
	@rm -rf $(SELF_HOSTED)
	@mkdir -p $(SELF_HOSTED)/work
	@cd src && for f in $$(find . -name '*.java'); do \
		mkdir -p "$(CURDIR)/$(SELF_HOSTED)/src/$$(dirname $$f)"; \
		(cd "$(CURDIR)/$(SELF_HOSTED)/work" && $(JAVA) -cp "$(CURDIR)/$(OUT)" oneLiner - < "$(CURDIR)/src/$$f" 2>/dev/null) \
			> "$(CURDIR)/$(SELF_HOSTED)/src/$$f" || exit 1; \
	done
	@$(JAVAC) $(JAVAC_FLAGS) -d $(SELF_HOSTED)/classes $$(find $(SELF_HOSTED)/src -name '*.java')
	@echo "[cre] the platform reduced itself: $$(find $(SELF_HOSTED)/src -name '*.java' | wc -l | tr -d ' ') one-line files compiled into $(SELF_HOSTED)/classes"

doctor: ## Diagnose the build environment
	@echo "[cre] java:  $$($(JAVA) -version 2>&1 | head -1)"
	@echo "[cre] javac: $$($(JAVAC) -version 2>&1)"
	@command -v $(JAR) >/dev/null && echo "[cre] jar:   available" || echo "[cre] jar:   missing (needed for make jar)"
	@command -v mvn >/dev/null && echo "[cre] mvn:   available (pom.xml supported)" || echo "[cre] mvn:   not installed (optional; pom.xml is available for Maven users)"

ci: test bench ## The full Continuous Synergy Assurance sequence

clean: ## Remove every build output directory
	@rm -rf $(OUT) $(DIST) build target harness/out harness/reports benchmarks/out benchmarks/results
	@echo "[cre] clean"
