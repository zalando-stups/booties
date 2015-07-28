/**
 * Copyright 2015 Zalando SE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zalando.stups.eclipselink.application;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.zalando.stups.eclipselink.application.entities.Person;
import org.zalando.stups.eclipselink.application.entities.PersonRepository;

/**
 * @author  jbellmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ExampleApplication.class})
@IntegrationTest
@Transactional
public class ExampleApplicationIT {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void runApplication() {
        Person p = new Person();
        Person fromDB = this.personRepository.save(p);

        Person read = this.personRepository.findOne(fromDB.getId());

        Assert.assertNotNull(read);
    }

}
