[![Build Status](https://travis-ci.org/smartcat-labs/ranger.svg?branch=master)](https://travis-ci.org/smartcat-labs/ranger)
[ ![Download](https://api.bintray.com/packages/smartcat-labs/maven/ranger/images/download.svg?version=0.0.2) ](https://bintray.com/smartcat-labs/maven/ranger/0.0.2/link)

# What is Ranger?

Ranger is contextual data and load generator. Contextual data generator allows developers to quickly and in a simple manner define and create large number of objects whose attributes have randomly selected values from the configured set.

It can be used for following:

- quickly populate the database with meaningful values
- create data based on defined rules (e.g. create 100 users out of which 10 have first name 'John' and they are born in 1980) in order to create test data for automated unit and integration tests
- load testing
- ...

```java
RandomBuilder<User> randomUserBuilder = new RandomBuilder<User>(User.class);
randomUserBuilder
  .randomFrom("username", "destroyerOfW0rldz", "only_lol_catz", "aragorn_the_gray")
  .randomFrom("firstname", "Alice", "Bob", "Charlie", "David")
  .randomFrom("lastname", "Zed", "Yvette","Xavier")
  .randomFromRange("numberOfCards", 1L, 5L)
  .randomFromRange("accountBalance", 2.72, 2.73)
  .randomSubListFrom("favoriteMovies", "Predator")
  .randomSubsetFrom("nicknames", "al", "billie", "gray")
  .randomFromRange("birthDate", LocalDateTime.of(1975, 1, 1, 0, 0), LocalDateTime.of(2001, 1, 1, 0, 0))
  .randomWithBuilder("address", randomAddressBuilder)
  .toBeBuilt(1000);

BuildRunner<User> runner = new BuildRunner<>();
runner.addBuilder(randomUserBuilder);
List<User> userList = runner.build();
```

It can be used as a Java library, programatically in unit and integration tests, and from the command line (though the last one is not yet implemented).

# How to use?

Currently the artifact can be fetched from bintray by adding following element to `<repositories>` section in your project's pom.xml:

```xml
<repository>
  <id>bintray-smartcat-labs-maven</id>
  <name>bintray</name>
  <url>https://dl.bintray.com/smartcat-labs/maven</url>
</repository>
```

And the dependency element to the you pom.xml `<dependencies>` element:

```xml
<dependency>
  <groupId>io.smartcat</groupId>
  <artifactId>data-gen</artifactId>
  <version>0.0.2</version>
</dependency>
```

For showcase and usage examples, take a look at our [Ranger Demo](https://github.com/smartcat-labs/ranger-demo) application.

# Why:

Totally random test data is not so usefull:

![Random users table](images/table-random-users.png)

- It is hard to make it by certain rules
- It is hard to reason about it
- It does not reflect production data values nor distribution

What we can do is use contextual data generator and create users whose attributes' values make sense in the domain context. We can also say, for example, that 70% of created users should be females. The table will then look like this:

![Context users table](images/table-not-so-random.png)

# How it works

Data generator uses reflection to set the property with randomly selected value from the passed list or array of allowed values.

# Examples

Create 1000 instances of User entity, out of which exactly 100 users have first name John or Joan.

```java
RandomBuilder<User> randomUserBuilder = new RandomBuilder<User>(User.class);
randomUserBuilder
  .randomFrom("username", "destroyerOfW0rldz", "only_lol_catz", "aragorn_the_gray")
  .randomFrom("firstname", "Alice", "Bob", "Charlie", "David")
  ...
  .toBeBuilt(900);

RandomBuilder<User> johnUserBuilder = new RandomBuilder<User>(User.class);
johnUserBuilder
  .randomFrom("username", "destroyerOfW0rldz", "only_lol_catz", "aragorn_the_gray")
  .randomFrom("firstname", "John", "Joan")
  ...
  .toBeBuilt(100);

BuildRunner<User> runner = new BuildRunner<>();
runner.addBuilder(randomUserBuilder);
runner.addBuilder(johnUserBuilder);
List<User> userList = runner.build();
```

Create 1000 instances of User entity, out of which exactly 100 users are born between 1980 and 1990.

```java
RandomBuilder<User> randomUserBuilder = new RandomBuilder<User>(User.class);
randomUserBuilder
  .randomFrom("username", "destroyerOfW0rldz", "only_lol_catz", "aragorn_the_gray")
  .randomFromRange("birthdate", 
    LocalDateTime.of(1975, 1, 1, 0, 0), LocalDateTime.of(1980, 1, 1, 0, 0), 
    LocalDateTime.of(1990, 1, 1, 0, 0), LocalDateTime.of(2001, 1, 1, 0, 0)) // creates values from two ranges [1975, 1980) and [1990,2001)
  ...
  .toBeBuilt(900);

RandomBuilder<User> millenialUserBuilder = new RandomBuilder<User>(User.class);
millenialUserBuilder
  .randomFrom("username", "destroyerOfW0rldz", "only_lol_catz", "aragorn_the_gray")
  .randomFromRange("birthdate", LocalDateTime.of(1980, 1, 1, 0, 0), LocalDateTime.of(1990, 1, 1, 0, 0))
  ...
  .toBeBuilt(100);

BuildRunner<User> runner = new BuildRunner<>();
runner.addBuilder(randomUserBuilder);
runner.addBuilder(millenialUserBuilder);
List<User> userList = runner.build();
```

Create 100 instances of User entity with addreses generated by declared builder:

```java
RandomBuilder<Address> randomAddressBuilder = new RandomBuilder<Address>(Address.class);
randomAddressBuilder
  .randomFrom("city", "New York", "San Francisko", "Boston", "Los Angelese", "Las Vegas", "Austin", "Denver", "Seatle")
  .randomFrom("street", "Anderson Mill Road", "14 Tee Dr", "3 Oaks Cir", "Adobe Trail", "Clayton Ln", "Foy Cir")
  .randomFromRange("houseNumber", 1L, 150L);

RandomBuilder<User> randomUserBuilder = new RandomBuilder<User>(User.class);
randomUserBuilder
  .randomFrom("username", "destroyerOfW0rldz")
  .randomWithBuilder("address", randomAddressBuilder).toBeBuilt(100);

BuildRunner<User> runner = new BuildRunner<>();
runner.addBuilder(randomUserBuilder);
List<User> userList = runner.build();
```
