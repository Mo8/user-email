# user-email
rest api for user email

## Installation
With docker:
```bash
docker-compose up -d
```
Sometimes, you need to relaunch spring-app because of database too long and no retry (TODO).
## links

- With postman, you can use the following link:
  - POST http://localhost:8080/user (create a user)
    ```json
    {
      "email": "email",
      "nom": "nom",
      "prenom": "prenom"
    }
    ```
- mailhog: http://localhost:8025
- rabbitmq: http://localhost:15672
- adminer: http://localhost:8081

## Usage

- Post user on http://localhost:8080/user (see links)
- Check email on mailhog (see links)
- Click on the link in the email for valdiating the user
- (optional) Check the user on adminer (see links)

## Explanation

- When a user is created, a message is sent to rabbitmq
- A consumer is listening to rabbitmq and email the user
- The user is validated when he clicks on the link in the email

using submodules git email-user (https://github.com/Mo8/email-user) for better docker-compose

