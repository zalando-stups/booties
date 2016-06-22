###How to use

This starter autoconfigures an EclipseLink-EntityManager for JPA with Spring-Boot configuration-magic.

In your pom.xml add the following snippet:

    ...
    <dependency>
        <groupId>org.zalando.stups</groupId>
        <artifactId>spring-boot-starter-data-jpa-eclipselink</artifactId>
        <version>${version}</version>
    </dependency>
    ....

Make sure you do not add the default 'spring-boot-starter-data-jpa'!
We added all needed dependencies into our starter and excluded 'hibernate-entitymanager' artifact.

###Configuration of EclipseLink properties

In your 'application.yml' file you can provide properties to configure EclipseLink for your needs if you want:

    spring:
        datasource:
            platform: h2
        jpa:
            properties:
                # eclipselink.session.customizer: com.yourcompany.jpa.eclipselink.customizer.session.DefaultZomcatSessionCustomizer
                # defined in org.eclipse.persistence.config.PersistenceUnitProperties
                eclipselink.persistence-context.flush-mode: COMMIT
                eclipselink.weaving: false
                eclipselink.ddl-generation.output-mode: sql-script
                eclipselink.ddl-generation: create-tables
                # eclipselink.logging.logger: com.yourcompany.jpa.eclipselink.Slf4jSessionLog
                # eclipselink.logging.file: eclipse-link-output.log
                eclipselink.logging.timestamp: false
                eclipselink.logging.session: false
                eclipselink.logging.connection: false
                eclipselink.logging.thread: false
                eclipselink.logging.level.transaction: ALL
                eclipselink.logging.level.sql: FINE
                eclipselink.logging.level.event: ALL
                eclipselink.logging.level.connection: ALL
                eclipselink.logging.level.query: FINE
                eclipselink.logging.level.cache: ALL
                eclipselink.logging.level: ALL
                eclipselink.logging.parameters: true


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
