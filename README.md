# Online Book Store

## Introduction 

Welcome to the Online Book Store API, a powerful tool for accessing and managing an extensive catalog of books. This API was inspired by the need to provide developers and book enthusiasts with a seamless way to interact with our book collection programmatically.

## Table of content
- [Technologies and Tools](#technologies-and-tools)
- [Functionalities](#functionalities)
- [Getting Started](#getting-started)
- [Challenges Faced](#challenges-and-solutions)
- [Contributing](#contributions)

## Technologies and Tools

### Spring Boot

_A Solid Foundation for Rapid Development_

Spring Boot is a highly productive framework for building Java applications. It provides a comprehensive set of features for rapid development, including embedded web servers, auto-configuration, and an extensive ecosystem of libraries and plugins. Spring Boot serves as the solid foundation of our API.

### Hibernate

_Simplifying Database Interactions_

Hibernate is employed as our Object-Relational Mapping (ORM) tool. It simplifies database interactions by mapping Java objects to database tables and vice versa, eliminating the need for low-level SQL queries. This streamlines data access and management within the API, making it easier to work with our extensive book database.

### Spring Security

_Ensuring Secure Access and Authentication_

Security is paramount in our Online Book Store API. Spring Security is integrated to ensure secure access and authentication. It safeguards user data and sensitive operations by providing authentication and authorization mechanisms, protecting against unauthorized access, and facilitating secure user sessions.

### JWT (JSON Web Tokens)

_Token-Based Authentication_

We implement token-based authentication using JSON Web Tokens (JWT). JWTs are used to securely transmit user authentication information between the client and server. They provide a stateless and scalable authentication solution, enhancing the security of our API.

### Spring Data JPA

_Simplifying Data Access and Management_

Simplifying data access and management is one of our primary goals. Spring Data JPA, part of the Spring Data project, provides a higher-level abstraction over traditional JPA (Java Persistence API). This simplifies database interactions, reducing the amount of boilerplate code and making it easier to work with our extensive book database.

### Liquibase

_Managing Database Schema Changes with Precision_

Database schema changes and version control are crucial for maintaining data integrity and consistency. Liquibase is utilized to manage database schema changes in a structured and trackable manner. This ensures that database updates are applied seamlessly while preserving data integrity throughout the API's evolution.

### Swagger

_Comprehensive and User-Friendly API Documentation_

Documentation is key to understanding and using our API effectively. Swagger is integrated to provide clear and interactive API documentation. With Swagger's user-friendly interface, users and developers can explore API endpoints, view request and response examples, and test API functionalities in real-time.

### Postman

_Streamlined Testing and Exploration_

As part of our commitment to user-friendly testing and exploration, we offer a comprehensive collection of sample requests through Postman. This collection allows users and developers to quickly get started with testing and exploring the capabilities of our API. It includes a range of requests covering various use cases, making it easy to understand and interact with our API.

## Functionalities

Our API offers the following key functionalities through its controllers:

### AuthenticationController

The AuthenticationController is responsible for the crucial aspects of user management within our Online Book Store. It facilitates user authentication, registration, and login processes. Users can securely create accounts, log in, and obtain access tokens for authenticated interactions with the API. This controller plays a pivotal role in ensuring the security and integrity of user data while providing a seamless and protected user experience.

### BookController

The BookController serves as the core component for managing the extensive catalog of books in our Online Book Store. It handles the complete lifecycle of books, encompassing their creation, retrieval, updating, and deletion (CRUD operations). With the BookController, users and developers can seamlessly interact with the API to browse, search, add, modify, and remove books from the catalog.

### CategoryController

In the world of books, categorization is paramount. The CategoryController takes charge of managing book categories and their dynamic attributes within our Online Book Store. It enables the creation, retrieval, modification, and deletion of book categories, allowing users and administrators to organize books into logical and user-friendly groupings. By providing a systematic approach to categorization, this controller enhances the discoverability of books and simplifies navigation, offering a richer user experience.

### ShoppingCartController

For a personalized shopping experience, the ShoppingCartController takes care of user shopping carts and the individual cart items within our Online Book Store API. Users can create and manage their shopping carts, add and remove items, and review their cart contents effortlessly through this controller. It ensures the smooth functioning of shopping cart-related operations, allowing users to curate their selections before proceeding to checkout. This controller enhances the user's ability to build and manage their reading lists.

### OrderController

The OrderController is the hub for managing user orders and the real-time status updates associated with them in our Online Book Store. Users can place orders, review their order history, and monitor order statuses seamlessly through this controller. It facilitates order creation, retrieval, modification, and deletion, ensuring a smooth and reliable ordering process. By centralizing order management, this controller enhances user confidence in our e-commerce platform and offers transparency in the order fulfillment process.

## Getting Started

To set up and use the Online Book Store API, follow these steps:

- Clone this repository to your local machine.
- Configure the application properties and database settings in `application.properties`.
- Building and Running the API

#### Using Docker (Recommended)

1. Build the Docker image using the provided Dockerfile:

   ```bash
   docker-compose build
   ```

2. Run the container with the built image:

   ```bash
   docker-compose up
   ```

   Use the `-p` flag to specify the port on which your application will be available.

3. Your API is now accessible at [http://localhost:8080](http://localhost:8080).

#### Without Docker

1. Build the project using Maven:

   ```bash
   mvn clean install
   ```

2. Run the application:

   ```bash
   mvn spring-boot:run
   ```

   The API will be available at [http://localhost:8080](http://localhost:8080).


- Access the API documentation and testing interface by visiting 
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Start making API requests as per the documentation. You can use [Online Book Store.postman_collection.json](Online%20Book%20Store.postman_collection.json)
- Here is a [video](https://www.loom.com/share/e4215dffefdf420887f31fd638b8a9d0?sid=22cc9980-4d27-4a43-992b-111d64ccae29) where we can see our API in action 

## Challenges and Solutions

### Challenges 

During the implementation of Spring Security within our Online Book Store API, several challenges were encountered, requiring thoughtful solutions:

1. **Complex Configuration**: Spring Security can be intricate to configure, particularly when dealing with custom authentication and authorization requirements.

2. **Token-Based Authentication**: The integration of JWT (JSON Web Tokens) for token-based authentication posed challenges related to token creation, validation, and secure storage.

3. **User Management**: The efficient management of user roles, permissions, and access control within the API needed a structured approach.

### Solutions Implemented

To overcome these challenges and establish a robust and secure authentication and authorization system, the following solutions were put into effect:

1. **Detailed Configuration Documentation**: The Spring Security configuration was documented comprehensively, providing thorough explanations for each aspect. This documentation offers clarity for both contributors and users on the setup of authentication and authorization.

2. **JWT Integration and Best Practices**: JWT was adopted as the authentication mechanism, adhering to industry best practices. Established libraries were utilized, and security guidelines for token creation, validation, and storage were followed.

3. **Custom UserDetailsService**: A custom `UserDetailsService` was implemented to manage user roles, permissions, and access control. This customization allowed for user management tailored to specific requirements while maintaining security.

By meticulously addressing these challenges and implementing solutions, a secure and reliable authentication and authorization system using Spring Security was established. This ensures the protection of user data and sensitive operations within our Online Book Store API, creating a secure and trustworthy environment for users.

## Contributions

We welcome contributions from the community! If you have ideas for improvements, bug fixes, or new features, please open an issue or submit a pull request. Together, we can make this API even better.
