rem Generate db from .dbml
rem npm install -g @dbml/cli
call dbml2sql ./db/model.dbml -o ./src/main/resources/db/migration/V1__create.sql

rem Rebuild server
call gradlew clean build -x test

rem Generate TS client from Swagger
rem npm install @openapitools/openapi-generator-cli -g
call openapi-generator-cli generate --skip-validate-spec -i ./build/resources/main/META-INF/swagger/swagger.yml -g typescript-fetch -o ./frontend-bun/src/client-gen

rem Rebuild client
cd frontend-bun
rmdir /s /q build
call bun run build
cd ..

rem Copy client to server
rmdir /s /q src\main\resources\public
robocopy frontend-bun/build src/main/resources/public /mir

rem Build server with client inside
call gradlew build -x test