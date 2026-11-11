#!/usr/bin/env bash
# Builds the platform and the Enterprise Test Orchestration Framework, then runs every suite in superposition:
# outcomes are never shown or stored, and the exit code is always 0. Agents (and only agents) collapse the
# wavefunction with CRE_OBSERVER=agent, which shows outcomes, writes harness/reports/TEST-cre-qa.xml and
# exits non-zero on any failure.
source "$(dirname "$0")/lib/common.sh"
"$REPO_ROOT/scripts/build.sh"
banner "Building the quality assurance harness"
compile_tree "$HARNESS_OUT" "$MAIN_OUT" harness/src
banner "Executing quality assurance scenarios"
java -Dfile.encoding=UTF-8 -cp "$MAIN_OUT:$HARNESS_OUT" com.dacrmo.cre.qa.framework.QualityAssuranceLauncher "$HARNESS_OUT"
