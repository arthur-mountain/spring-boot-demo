#!/usr/bin/env bash

./gradlew build --continuous &

BUILD_PID=$!

./gradlew bootRun

kill $BUILD_PID && echo "Stopping background build process..."
