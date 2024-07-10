cd frontend
rmdir /s /q build
call npm run build

cd ..
robocopy frontend/build src/main/resources/public /mir

call gradlew clean build -x test
