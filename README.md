# recipe
Here you have a crud operation over Recipe Api + a simple search query with DB side native full-text functionality

### Technologies
* Java 11
* Spring Boot
* Postgres DB
* Gradle
* Docker

#### API Documentation
To access Swagger documentation open: http://localhost:8080/swagger-ui.html in your browser

#### Full-Text search
I've used MySql first but had some issues with the full-text then I refactored to postgres, Here I used hibernate search + postgres full-text function but generally there are some other options at scale which are ordered by the best solutions:
1. ElasticSearch (best in our case)
2. Apache Solr
3. Full-Text at caching level (Eg: Redis)
4. Using RDBMS + full-text over indexed fields
5. Using SpringJPA find(modelName)by(fieldName)
6. Using SQL native queries inside repository

#### How to run
docker build -t recipe .
docker-compose up