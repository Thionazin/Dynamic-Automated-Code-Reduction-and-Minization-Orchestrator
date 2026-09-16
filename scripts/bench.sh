#!/usr/bin/env bash
# Measures hyper-scalability. Results land in benchmarks/results/latest.csv.
source "$(dirname "$0")/lib/common.sh"
"$REPO_ROOT/scripts/build.sh"
banner "Building the benchmark suite"
compile_tree "$BENCH_OUT" "$MAIN_OUT" benchmarks/src
banner "Benchmarking"
java -cp "$MAIN_OUT:$BENCH_OUT" com.dacrmo.cre.bench.PerformanceBenchmarkOrchestrator
