#!/usr/bin/env bash
# Shared plumbing for the Synergy Automation Toolchain. Sourced, never executed directly.
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
MAIN_OUT="$REPO_ROOT/out"
HARNESS_OUT="$REPO_ROOT/harness/out"
BENCH_OUT="$REPO_ROOT/benchmarks/out"
cd "$REPO_ROOT"

banner() {
    printf '\n==== %s ====\n' "$1"
}

log_info() {
    printf '[cre] %s\n' "$1"
}

require_jdk() {
    command -v javac >/dev/null 2>&1 || { echo "[cre] javac not found: a JDK (8 or newer) is required" >&2; exit 1; }
}

# compile_tree <output dir> <classpath or empty> <source dir>...
compile_tree() {
    local out="$1" cp="$2"
    shift 2
    rm -rf "$out"
    mkdir -p "$out"
    local sources
    sources="$(find "$@" -name '*.java')"
    # shellcheck disable=SC2086
    javac -encoding UTF-8 -nowarn -d "$out" ${cp:+-cp "$cp"} $sources
}
