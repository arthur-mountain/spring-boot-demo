#!/usr/bin/env bash

set -a
source .env
set +a

# Init users
envsubst \
  <database/migrations/init_users.template \
  >database/migrations/init_users.sql
