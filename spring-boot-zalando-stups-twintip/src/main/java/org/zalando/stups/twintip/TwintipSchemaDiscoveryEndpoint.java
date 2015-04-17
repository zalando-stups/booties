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
package org.zalando.stups.twintip;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author  jbellmann
 */
@ConfigurationProperties(prefix = "twintip")
public class TwintipSchemaDiscoveryEndpoint implements Endpoint<Map<String, Object>> {

    private String schemaUrl = "/swagger.json";
    private String schemaType = "swagger-2.0";
    private String uiUrl = "/ui/";

    @Override
    public String getId() {
        return ".well-known/schema-discovery";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isSensitive() {
        return false;
    }

    @Override
    public Map<String, Object> invoke() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("schema_url", getSchemaUrl());
        result.put("schema_type", getSchemaType());
        result.put("ui_url", getUiUrl());
        return result;
    }

    public String getSchemaUrl() {
        return schemaUrl;
    }

    public void setSchemaUrl(final String schemaUrl) {
        this.schemaUrl = schemaUrl;
    }

    public String getSchemaType() {
        return schemaType;
    }

    public void setSchemaType(final String schemaType) {
        this.schemaType = schemaType;
    }

    public String getUiUrl() {
        return uiUrl;
    }

    public void setUiUrl(final String uiUrl) {
        this.uiUrl = uiUrl;
    }

}
