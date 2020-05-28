# Hashids Spring Boot Starter

[![Maven Central](https://img.shields.io/maven-central/v/systems.fehn/hashids-spring-boot-starter)](https://search.maven.org/artifact/systems.fehn/hashids-spring-boot-starter)

- Provides a bean for Hashids from https://github.com/10cella/hashids-java
- Allows customizing the properties of Hashids via Spring properties (`hashids.salt`, `hashids.min-hash-length` and `hashids.alphabet`)
- Automatically decodes Spring request parameters
- Automatically encodes/decodes values with jackson



## Usage

### Add the dependency

This library is available Maven Central.
If using Maven, add the following dependency to your `pom.xml`:
```xml
<dependency>
  <groupId>systems.fehn</groupId>
  <artifactId>hashids-spring-boot-starter</artifactId>
  <version>${version}</version>
</dependency>
```

If using Gradle, add the following dependency:
```groovy
implementation 'systems.fehn:hashids-spring-boot-starter:${version}'
```

If using Gradle with the Kotlin DSL, add the following dependency:
```kotlin
implementation("systems.fehn:hashids-spring-boot-starter:${version}")
```



## Configuration

Customize how the hashids object is created by using the Spring properties:
- `hashids.salt`
- `hashids.min-hash-length`
- `hashids.alphabet`



## `Hashids` bean

`@Autowire` an `org.hashids.Hashids` bean into your Spring components.
E.g.:

```java
@Controller
public class UserController {
    private final Hashids hashids;

    public UserController(final Hashids hashids) {
        this.hashids = hashids;
    }
}
```



## `@Hashids` annotation

### Model fields

Alternatively, you can annotate fields in your models with `@systems.fehn.boot.starter.hashids.Hashids`:

```java
public class UserModel {
    @Hashids
    public long id;
    public String name;
}
```

Now the user's id will be automatically encoded and decoded with the `Hashids` bean.

`@Hashids` can be applied to types `int`, `Integer`, `long`, `Long` and arrays of these types.

You can customize all settings for each annotation (they are optional and default to the value of the corresponding property):

```java
public class UserModel {
    @Hashids(salt = "abc", minHashLength = 8, alphabet = "abcdefghijklmnopqrstuvwxyz")
    public long id;
    public String name;
}
```



### Controller parameters

Request parameters of controller methods can be annotated in the same fashion.
