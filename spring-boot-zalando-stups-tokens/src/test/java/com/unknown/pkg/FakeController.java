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
package com.unknown.pkg;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

/**
 * Maybe this works.
 *
 * @author  jbellmann
 */
@Controller
public class FakeController {

    private final Logger logger = LoggerFactory.getLogger(FakeController.class);

    @RequestMapping(
        value = "/access_token", method = RequestMethod.POST, params = {"realm=whatever"},
        produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_FORM_URLENCODED_VALUE}
    )
    @ResponseBody
    public Map<String, Object> fakeTheResponse(@RequestParam final Map<String, Object> request) {
        logger.warn("------- AUTHORIZATION_REQUEST INCOMING-------");
        logger.warn(request.toString());
        logger.warn("--------AUTHORIZATION_COMPLETE----------------");

        Map<String, Object> result = Maps.newHashMap();
        result.put("access_token", "123456789-987654321");
        result.put("token_type", "Bearer");
        result.put("expires_in", 5000);

        return result;
    }
}
