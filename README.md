# Pi Radio

This is Pi Radio written in Kotlin and Thymeleaf. Initially jumpstarted with Bootify.

## Installation

### Pre requisite

Install nodejs 18x

``` bash
curl -sL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs
node --version
```

Node.js is automatically downloaded using the `gradle-node-plugin` and the final JS/CSS files are integrated into the
jar.

Clone the project

``` bash
git clone https://github.com/strmark/kpiradio_thymeleaf.git
```

### Run a development server

```
npm install
```

The DevServer can now be started as follows:

```
npm run devserver
```

Using a proxy the whole application is now accessible under `localhost:8081`. All changes to the templates and JS/CSS
files are immediately visible in the browser.

## Build

The application can be built using the following command:

```
gradlew clean build
```

### Run a prod server

The application can then be started with the following command - here with the profile `production`:

```
java -Dspring.profiles.active=production -jar ./build/libs/piradio-0.0.1-SNAPSHOT.jar
```
