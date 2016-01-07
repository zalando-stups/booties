###GitHub-Client for Spring-Applications

####Get the dependency

```
    <dependency>
        <groupId>org.zalando.github</groupId>
        <artifactId>github-client-spring</artifactId>
        <version>0.4.3</version>
    </dependency>
```

####Usage

```

	//request-scoped proxy
	@Autowired
	private GitHub socialGithub;

	//somewhere in a method
	IssueRequest issue = new IssueRequest();
	issue.setAssignee("klaus");
	issue.setTitle("TEST_" + UUID.randomUUID().toString());
	Issue response = new IssuesTemplate(socialGithub.restOperations()).createIssue(issue, "zalando-stups", "stupsback-admin");
	LOG.info("ISSUE CREATED WITH ID : {}", response.getId());

```

