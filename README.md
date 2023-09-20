### Usage

A Postman documentation of the API can be found here: https://documenter.getpostman.com/view/2287668/2s9YC8wqkv

#### Start the server

```bash
./gradlew bootRun
```

#### Create a task category

```bash
curl --location 'http://localhost:8080/api/tasks/categories' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Task Category A",
    "description": "Some thoughtful description for task category A"
}'
```

#### Get a task category

```bash
curl --location 'http://localhost:8080/api/tasks/categories/1'
```

#### Get all task categories

```bash
curl --location 'http://localhost:8080/api/tasks/categories'
```

#### Update a task category

```bash
curl --location --request PUT 'http://localhost:8080/api/tasks/categories/1' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Category A (updated)",
    "description": "Some thoughtful, updated description for task category A"
}'
```

#### Delete a task category

````bash
curl --location --request DELETE 'http://localhost:8080/api/tasks/categories/1'
````

#### Create a task

```bash
curl --location 'http://localhost:8080/api/tasks/' \
--header 'Content-Type: application/json' \
--data '{
    "name": "The name of the task",
    "description": "Some thoughtful description for a task",
    "deadline": "2023-09-22T10:42:50.63",
    "category": {
        "name": "Task Category A"
    }
}'
```

#### Get a task

```bash
curl --location 'http://localhost:8080/api/tasks/1'
```

#### Get all tasks

```bash
curl --location 'http://localhost:8080/api/tasks/'
```

#### Update a task

```bash
curl --location --request PUT 'http://localhost:8080/api/tasks/1' \
--header 'Content-Type: application/json' \
--data '{
    "name": "This is the updated name of a task",
    "description": "This is the updated description of a task",
    "deadline": "2023-09-22T10:42:50.63",
    "category": {
        "name": "Task Category A"
    }
}'
```

#### Delete a task

```bash
curl --location --request DELETE 'http://localhost:8080/api/tasks/1'
```
