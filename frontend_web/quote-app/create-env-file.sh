#!/usr/bin/env bash
#
# Set environment values if they exist as arguments
if [ $# -ne 0 ]; then
  echo "===> Overriding env params with args ..."
  for var in "$@"
  do
    export "$var" >> .env
  done
fi
