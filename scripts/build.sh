#!/usr/bin/env bash
# Builds the Code Reduction Engine™ into out/.
source "$(dirname "$0")/lib/common.sh"
require_jdk
banner "Building the Code Reduction Engine"
compile_tree "$MAIN_OUT" "" src
log_info "built $(find "$MAIN_OUT" -name '*.class' | wc -l | tr -d ' ') classes into out/"
