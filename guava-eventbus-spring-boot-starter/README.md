###How to use

This starter autoconfigures a Guava - EventBus with Spring-Boots configuration-magic.

In your pom.xml add the following snippet:

    ...
    <dependency>
        <groupId>org.zalando.stups</groupId>
        <artifactId>guava-eventbus-spring-boot-starter</artifactId>
        <version>${version}</version>
    </dependency>
    ....


###Simple usage

Just autowire EventBus, AsyncEventBus or EventBusSupport into the component you want to send events from.

    @Component
    public class SimpleEventSender {
    
        private final EventBus eventBus;
    
        @Autowired
        public SimpleEventSender(final EventBus eventBus) {
            this.eventBus = eventBus;
        }
    
        public void sendEvents() {
            this.eventBus.post(new SimpleEvent());
            this.eventBus.post(new NoOneSubscribedEvent());
        }

    }

or both ...

    @Component
    public class MultiEventBusSender {

        private final AsyncEventBus asyncEventBus;
    
        private final EventBus eventBus;
    
        @Autowired
        public MultiEventBusSender(final AsyncEventBus asyncEventBus, final EventBus eventBus) {
            this.asyncEventBus = asyncEventBus;
            this.eventBus = eventBus;
        }
    
        public void sendEvents() {
            this.asyncEventBus.post(new SimpleEvent());
            this.eventBus.post(new SimpleEvent());
        }
    }

or use EventBusSupport ...

    @Component
    public class EventBusSupportSender {
    
        private final EventBusSupport eventBusSupport;
    
        @Autowired
        public EventBusSupportSender(final EventBusSupport eventBusSupport) {
            this.eventBusSupport = eventBusSupport;
        }
    
        public void sendEvents() {
            this.eventBusSupport.post(new SimpleEvent());
            this.eventBusSupport.postAsync(new SimpleEvent());
        }

    }

And you will also have some components listen to the events, do you?

    @Component
    public class SimpleSubscriber {
    
        private Logger log = LoggerFactory.getLogger(SimpleSubscriber.class);
    
        @Subscribe
        public void handle(final SimpleEvent event) {
            log.info("Handle Event : {}", event.toString());
        }
    
        @Subscribe
        public void handle(final OtherEvent event) {
            log.info("Handle Event : {}", event.toString());
        }
    }

Have to a look to the integration-tests to see how you can use it.

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

