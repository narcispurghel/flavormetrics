#!/bin/bash

ENV_FILE=".env"

if [ ! -f "$ENV_FILE" ]; then
  echo "⚠ File $ENV_FILE does not exists."
  exit 1
fi

export $(grep -v '^#' "$ENV_FILE" | xargs)

echo "✅ Load variables success"