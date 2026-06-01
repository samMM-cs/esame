#!/usr/bin/env
SRC_DIR="src"
BIN_DIR="bin"
JAR_NAME="esame.jar"
MAIN_CLASS="esame.Main"

echo "--- Starting Build ---"

echo "Removing old build..."
rm -rf "$BIN_DIR"
mkdir -p "$BIN_DIR"

echo "Locating source files..."
find "$SRC_DIR" -name "*.java" -exec printf '"%s"\n' {} + > sources.txt

if [! -s sources.txt]
  echo "Error: No .java files found in $SRC_DIR"
  rm sources.txt
  exit 1
fi

echo "Compiling source files..."
javac -d "$BIN_DIR" @sources.txt

rm sources.txt

echo "Packaging into $BIN_DIR/$JAR_NAME..."
jar -c -f "$BIN_DIR/$JAR_NAME" -e "$MAIN_CLASS" -C "$BIN_DIR" .

echo "--- Build Successful! ---" -ForegroundColor Green
echo "Run using: java -jar $BIN_DIR/$JAR_NAME"