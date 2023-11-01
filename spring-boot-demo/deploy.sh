#!/usr/bin/env bash

cd backend
mvn spring-boot:run &

cd ../frontend
npm install
npm run dev

cd ..

