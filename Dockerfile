# Use the official AWS Corretto 21 image as a parent image
FROM amazoncorretto:21

# Install tar and gzip for mvnw execution
RUN yum install -y tar gzip

# Set the working directory inside the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Make the mvnw script executable
RUN chmod +x ./mvnw

# Resolve dependencies
RUN ./mvnw dependency:resolve

# Package the application
RUN ./mvnw package -Dmaven.test.skip

# Expose the port the app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/evm-transaction-service.jar"]