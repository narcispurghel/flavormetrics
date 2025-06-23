# FlavorMetrics

FlavorMetrics is an REST API created in Java programming language using Spring Framework.

I created this REST API as the final project for the Java programming course at IT-School. The application showcases advanced features like authentication for three types of users, recipe management, and personalized user profiles. It combines practical functionality with a user-centered design, reflecting the skills and knowledge gained during the course.

## Key Features:
- **Advanced Authentication**: Support for three types of users with distinct roles.
- **Recipe Addition**: Users can create and save new recipes.
- **Advanced Search**: Find recipes based on specific criteria (e.g., ingredients, preparation time).
- **Profile Customization**:
    - Set **culinary preferences** (e.g., preferred cuisine types).
    - Add **allergy information** to filter relevant and safe recipes.

This application is designed for cooking enthusiasts, providing a well-organized, safe, and personalized platform for managing and searching recipes

## Technologies Used

This project utilizes the following technologies and frameworks:

- **[PostgreSQL](https://www.postgresql.org/)** - A relational database management system.
- **[Spring Boot](https://spring.io/projects/spring-boot)** - A Java-based framework for rapid application development.
- **[Spring Framework](https://spring.io/projects/spring-framework)** - A robust, modular framework for building Java applications.
- **[Hibernate](https://hibernate.org/)** - An Object-Relational Mapping (ORM) framework for Java.
- **[Java](https://www.java.com/)** - The main programming language used in this application.

## Documentation:
Full documentation of the API endpoints and returned datatypes can be read accessing [Swagger-UI](http://localhost:8080/swagger-ui/index.html) as soon as you run the app.

## Installation

### Prerequisite
This project requires [JRE](https://bell-sw.com/pages/downloads/?version=java-21&os=windows&package=jre-full) or [JDK](https://www.oracle.com/ro/java/technologies/downloads/#java21) version 21 installed on your pc.
Also, you need to install [PostgreSQL](https://www.postgresql.org/download/) server version 17 and configure [application.properties](src/main/resources/application.yaml) file with your database credentials.

Inside the main folder run:
```bash
./mvnw clean install
```

### Run the application
Again in the main folder run:
```bash
./mvnw spring-boot:run
```
or run the jar script generated inside the target folder:
```java
java -jar ./target/flavormetrics-1.0.0.jar
```
## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

MIT License

Copyright (c) [2025] [Narcis Purghel]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.