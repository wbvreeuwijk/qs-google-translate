# AAI Google Translate Plugin

This AAI Plugin implements an interface to the Google Translate API. It uses a caching mechanism for previously translated items and is the perfect tool to create multi-lingual Qlik Sense or QlikView applications

![Multilingual Qlik Sense Application](aai-google-translate.gif)

# Prerequisites

+ Qlik Sense or QlikView November 2017 release (or later)
* Google API credentials
* Java version 1.8 (or later)

# Getting started

## Google API credentials

Because we use the Google Translate API we need to retrieve Google API credentials. Follow [these steps](https://cloud.google.com/translate/docs/getting-started): 

1. Login to the [Google Cloud Platform Console](https://console.cloud.google.com/)
2. In the Cloud Platform Console, go to the [Manage resources](https://console.cloud.google.com/cloud-resource-manager) page and select or create a new project.
3. Enable billing for your project.
4. Enable the Cloud Translation API for your project.
5. After enabling the Google Cloud Translation API, click the Go to Credentials button to set up your Cloud Translation API credentials:
6. See [the Authentication guide](https://cloud.google.com/docs/authentication/getting-started) for information on how to authenticate to the Cloud Translation API service from your code. Following those steps, you should obtain both a service account key file (in JSON)  that will allow you to authenticate to the Translation API.

## Running the plugin 

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



# Author 

**Bas van Reeuwijk**

+ [www.qlik.com](http://www.qlik.com)
* [twitter/wbvreeuwijk](http://twitter.com/wbvreeuwijk)
* [github.com/wbvreeuwijk](http://github.com/wbvreeuwijk)

# License

Copyright (c) 2017 Bas van Reeuwijk

Released under the GNU GENERAL PUBLIC LICENSE Version 3.

***
