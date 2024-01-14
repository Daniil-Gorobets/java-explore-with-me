# ExploreWithMe

ExploreWithMe is a versatile application that allows users to discover and share exciting events, facilitating the formation of groups to participate in these events. The project is implemented using Java Spring and follows a modular structure, involving the creation of two main services – the Main Service and the Statistics Service.

## Main Features

### 1. Event Discovery and Management:

#### Public API:

- Provides a comprehensive list of events accessible to all users.
- Allows searching and filtering events by various criteria.
- Supports sorting events by popularity or date.
- Categorizes events under predefined categories.
- Enables retrieving brief information about events and detailed information through separate endpoints.
- Logs every public request for statistical analysis.

#### Private API:

- Allows registered users to add, edit, and view their events.
- Supports submitting and confirming participation requests for events.

#### Administrative API:

- Configures event categories, collections, and user management.
- Implements event moderation for user-submitted events.

### 2. Statistics Service:

- Gathers information about user interactions with the application.
- Records statistics for the number of event list views and detailed event information requests.

### 3. Authentication and Authorization:

- Both services operate within a Virtual Private Network (VPN).
- Utilizes a network gateway for authentication and authorization.
- Ensures secure and controlled access to different parts of the application.

### 4. Additional Functionality - Comments:

- Allows users to add comments to events.
- Enhances user interaction and engagement.
- Extends the social aspect of the application.

## Project Structure

The project is organized into separate branches for each major task:

- `main` Branch:
  - Merges changes from the previous stages.
  - Serves as the baseline for subsequent branches.

- `stat_svc` Branch:
  - Implements the Statistics Service.
  - Involves the creation of HTTP service and client modules.
  - Ensures correct compilation, Docker container launch, and interaction with PostgreSQL.

- `main_svc` Branch:
  - Implements the Main Service.
  - Maintains a multi-module structure for efficient development.
  - Validates compilation, Docker container launch, and PostgreSQL setup.

- `feature_comments` Branch:
  - Adds the chosen additional functionality: comments on events.
  - Includes basic Postman tests to ensure functionality.
  - Continues the branch-based development approach.

## Testing

The project includes comprehensive Postman tests to validate the functionality of each service and the added comments feature. The tests cover endpoint responses, ensuring the correct behavior of the application.

## CI/CD

Continuous integration and deployment is managed through GitHub Actions. The workflow can be found in [`.github/workflows/api-tests.yml`](command:_github.copilot.openRelativePath?%5B%22.github%2Fworkflows%2Fapi-tests.yml%22%5D ".github/workflows/api-tests.yml").

## Building and Running the Project

The project uses Maven for dependency management and building. To build the project, navigate to the root directory and run:

```sh
mvn clean install
```

To run the services, you can use Docker Compose:

```sh
docker-compose up
```

Please refer to the individual README files in the [`ewm-main-service`](command:_github.copilot.openRelativePath?%5B%22ewm-main-service%22%5D "ewm-main-service") and [`ewm-stats-service`](command:_github.copilot.openRelativePath?%5B%22ewm-stats-service%22%5D "ewm-stats-service") directories for more specific instructions on running each service.
