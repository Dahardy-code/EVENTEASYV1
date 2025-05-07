#!/bin/bash

# Output file name
OUTPUT_FILE="all_files_dump.txt"

# Remove output file if it already exists
rm -f "$OUTPUT_FILE"

# Find and process all files, excluding node_modules and the output file itself
find . -path './node_modules' -prune -o -type f ! -name "$OUTPUT_FILE" -print | while read -r file; do
    echo "========== FILE: $file ==========" >> "$OUTPUT_FILE"
    cat "$file" >> "$OUTPUT_FILE"
    echo -e "\n" >> "$OUTPUT_FILE"
done

echo "All files (excluding node_modules) have been packed into $OUTPUT_FILE"
