#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC_DIR="$ROOT_DIR/src/main/java"
BUILD_DIR="$ROOT_DIR/build/manual"
CLASSES_DIR="$BUILD_DIR/classes"
JAR_FILE="$BUILD_DIR/heapsort.jar"
DOC_DIR="$BUILD_DIR/javadoc"

rm -rf "$BUILD_DIR"
mkdir -p "$CLASSES_DIR" "$DOC_DIR"

echo "1. Compiling with javac..."
find "$SRC_DIR" -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -d "$CLASSES_DIR"

echo "2. Creating JAR with jar..."
jar --create \
    --file "$JAR_FILE" \
    --main-class ru.nsu.tikhonov.Main \
    -C "$CLASSES_DIR" .

echo "3. Generating Javadoc..."
javadoc \
    -encoding UTF-8 \
    -charset UTF-8 \
    -docencoding UTF-8 \
    -d "$DOC_DIR" \
    "$SRC_DIR/ru/nsu/tikhonov/HeapSort.java" \
    "$SRC_DIR/ru/nsu/tikhonov/Main.java" \

echo "4. Running application..."
java -jar "$JAR_FILE"

echo
echo "Done."
echo "JAR:     $JAR_FILE"
echo "Javadoc: $DOC_DIR/index.html"
