# LetMeShip Test Task
Create a project from scratch in the tech stack of your choice. It should serve an API that can be called from the outside. This API should be able to receive an URL. The application should be capable of parsing this website and as a response give back all links that are present on that page (only give back the list do not follow the links)

# Solution
The Jsoup library is used to parse the site. Service return only unique links and incomplete links are completed by the host from the request.
I don't use caching in this task because sites can be updated between user requests.
The URL is also validated if it is possible to connect to it.


# Technology
- Java 17
- Spring Boot
- Maven

# How to start
1. install Docker and launch
2. install JDK17
3. run in terminal 
    ```
    maven package
    ```
5. run Dockerfile from IDE

# API
path: http://localhost:9090/api/v1

### Get list of links from site
 ```
POST: /parser {json: WebsiteParserRequest}

Response:
- HttpStatus.OK
- json: WebsiteParserResponse
 ```

### Entity

#### WebsiteParserRequest example
```
{
    "url": "https://www.google.com"
}
```

#### WebsiteParserResponse example
```
{
    "urlSet": [
        "https://maps.google.ru/maps?hl=ru&tab=wl",
        "https://www.google.com/setprefdomain?prefdom=RU&prev=https://www.google.ru/&sig=K_6TfeAmDBiLDbotCxDtE0V8dYZ5U%3D",
        "https://drive.google.com/?tab=wo",
        "https://play.google.com/?hl=ru&tab=w8",
        "https://www.google.ru/intl/ru/about/products?tab=wh",
        ]
}
```