rem Rebuild server
call gradlew clean build -x test

rem Generate TS client from Swagger
rem npm install @openapitools/openapi-generator-cli -g
call openapi-generator-cli generate --skip-validate-spec -i ./build/resources/main/META-INF/swagger/swagger.yml -g typescript-fetch -o ./frontend/src/client-gen

rem Rebuild client
cd frontend
rmdir /s /q build
call npm run build
cd ..

rem Copy client to server
rmdir /s /q src\main\resources\public
robocopy frontend/build src/main/resources/public /mir

rem Build server with client inside
call gradlew build -x test
