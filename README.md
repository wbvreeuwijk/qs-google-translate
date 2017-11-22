## AAI Google Translate Plugin

This AAI Plugin implements an interface to the Google Translate API. It uses a caching mechanism for previously translated items and is the perfect tool to create multi-lingual Qlik Sense or QlikView applications

![Multilingual Qlik Sense Application](https://raw.githubusercontent.com/wbvreeuwijk/qs-google-translate/master/aai-qoogle-translate.gif)

## Prerequisites

+ Qlik Sense or QlikView November 2017 release (or later)
* Google API credentials
* Java version 1.8 (or later)

## Getting started

### Google API credentials

Because we use the Google Translate API we need to retrieve Google API credentials. Follow [these steps](https://cloud.google.com/translate/docs/getting-started): 

1. Login to the [Google Cloud Platform Console](https://console.cloud.google.com/)
2. In the Cloud Platform Console, go to the [Manage resources](https://console.cloud.google.com/cloud-resource-manager) page and select or create a new project.
3. Enable billing for your project.
4. Enable the Cloud Translation API for your project.
5. After enabling the Google Cloud Translation API, click the Go to Credentials button to set up your Cloud Translation API credentials:
6. See [the Authentication guide](https://cloud.google.com/docs/authentication/getting-started) for information on how to authenticate to the Cloud Translation API service from your code. Following those steps, you should obtain both a service account key file (in JSON)  that will allow you to authenticate to the Translation API.

### Starting the plugin 

The plugin comes in a self contained zip-file with all the needed Java libraries. To get everything up and running follow these steps:

1. Setup an environment variable to point to your Google JSON file like 
  `GOOGLE_APPLICATION_CREDENTIALS=C:\google-credentials.json`
2. Download the latest release from [here](https://github.com/wbvreeuwijk/qs-google-translate/releases)
3. Unzip the zip-file in a dedicated directory
4. Create a file called `Dictionary.properties` that has the following content:
  ```Ini
ORIGIN_LANGUAGE=en
TRANSLATION_DIR=translations
```
5. Create a directory called `translations` under the directory where you unzipped the release.
6. Start the server by calling
    * `qs-google-translate\bin\google-translate-server.bat` (Windows)
    * `qs-google-translate\bin\google-translate-server` (Linux)
7. The plugin is now listening on port 50054 for incoming connections

### Docker

To make it easier to run the plugin there is a docker image available. For this you will need to have a running docker environment. Take the following steps to run it

1. Create a directory called `config`
2. Copy the Google JSON credential file into `config/google.json`
3. Create a file called `config/Dictionary.properties` that has the following content:
  ```Ini
  ORIGIN_LANGUAGE=en
  TRANSLATION_DIR=/translations
  ```
4. Create a directory called `translations`
5. Start the docker image with the following command
  ```Bash
  docker run -d -p 50054:50054 \
             -v $PWD/config:/config \
             -v $PWD/translations:/translations \
             wbvreeuwijk/qs-google-translate
  ```

### Setup Qlik Sense or QlikView

For the plugin to be used in a Qlik Sense of QlikView application you will need to add the plugin hostname and port into the `Settings.ini` file.

Modify this file to look like this
```Ini
[Settings 7]
SystemLogVerbosity=5
SSEPlugin=J,localhost:50054
```

If you are running the plugin on a different server change localhost to point to this server.

Start or restart you Qlik Sense or Qlik View server or desktop application. If all is configured correctly you will see this in the log of the plugin.

### Using the plugin

In you Qlik Sense or QlikView application you can now use the following expressions:
```JavaScript
J.TranslateLabel(<language>,<text>) 
J.TranslateRows(<language>,<field>) 
```

Look at the example application [here](https://github.com/wbvreeuwijk/qs-google-translate/raw/master/application/)

## Limitations

Currently Qlik Sense and QlikView do not support using AAI expressions as calculated dimensions.

## Author 

**Bas van Reeuwijk**

+ [www.qlik.com](http://www.qlik.com)
* [twitter/wbvreeuwijk](http://twitter.com/wbvreeuwijk)
* [github.com/wbvreeuwijk](http://github.com/wbvreeuwijk)

## License

Copyright (c) 2017 Bas van Reeuwijk

Released under the GPL-3.0.

***
