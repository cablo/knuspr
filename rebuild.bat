cd frontend
rmdir /s /q build
call npm run build

cd ..
rmdir /s /q src\main\resources\public
robocopy frontend/build src/main/resources/public /mir

call gradlew clean build -x test
