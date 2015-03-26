###How to use

This starter autoconfigures a EclipseLink-EntityManager for JPA with Spring-Boot configuration-magic.

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

