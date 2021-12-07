# Run with testing
mvn clean && mvn package

# Clean and run w/o testing
#mvn clean && mvn package -Dmaven.test.failure.ignore=true -Dmaven.test.skip=true

# Build image and test run in container
docker build -t henrylao/destination-service:latest .
docker push henrylao/destination-service
