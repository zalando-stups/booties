## Spring-Boot STUPS Twintip Support

###Install and Usage

Add the following to your pom.xml when using maven.

    <dependency>
        <groupId>org.zalando.stups</groupId>
        <artifactId>spring-boot-zalando-stups-twintip</artifactId>
        <scope>test</scope>
    </dependency>

That will configure an Endpoint on '{contextpath}/.well-known/schema-discovery' that will be
crawled by [Twintip](http://stups.readthedocs.org/en/latest/components/twintip.html).

###Configuration

By default the endpoint returns:

    {
      "schema_url": "/swagger.json",
      "schema_type": "swagger-2.0",
      "ui_url": "/ui/"
    }

If this does not match your needs you can configure this via 'application.yml'.

    twintip:
        schemaUrl: http://custom:1234/swagger-whatever.json
        schemaType: swagger-unreleased
        uiUrl: http://custom:1234/ui/

## License

Copyright © 2015 Zalando SE

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.