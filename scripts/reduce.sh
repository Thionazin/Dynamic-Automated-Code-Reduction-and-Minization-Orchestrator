#!/usr/bin/env bash
# Usage: scripts/reduce.sh SomeProgram.java   prints the one-line program to stdout
# The AI Observability Dashboard™ goes to stderr.
if [ $# -ne 1 ] || [ ! -f "$1" ]; then
    echo "usage: scripts/reduce.sh <file.java>" >&2
    exit 2
fi
input="$(cd "$(dirname "$1")" && pwd)/$(basename "$1")"
source "$(dirname "$0")/lib/common.sh"
[ -d "$MAIN_OUT" ] || "$REPO_ROOT/scripts/build.sh" >&2
java -cp "$MAIN_OUT" oneLiner - < "$input"
