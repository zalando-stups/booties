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
package org.zalando.stups.tokens.config;

import java.net.URI;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author  jbellmann
 */
@ConfigurationProperties(prefix = "tokens")
public class AccessTokensBeanProperties {

    private static final String CLIENT_JSON = "client.json";

    private static final String USER_JSON = "user.json";

    private URI accessTokenUri;

    private int refreshPercentLeft = 40;

    private int warnPercentLeft = 20;

    private String credentialsDirectory;

    private String userCredentialsFilename = USER_JSON;

    private String clientCredentialsFilename = CLIENT_JSON;

    private List<TokenConfiguration> tokenConfigurationList = new ArrayList<TokenConfiguration>(0);

    public URI getAccessTokenUri() {
        return accessTokenUri;
    }

    public void setAccessTokenUri(final URI accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
    }

    public int getRefreshPercentLeft() {
        return refreshPercentLeft;
    }

    public void setRefreshPercentLeft(final int refreshPercentLeft) {
        this.refreshPercentLeft = refreshPercentLeft;
    }

    public int getWarnPercentLeft() {
        return warnPercentLeft;
    }

    public void setWarnPercentLeft(final int warnPercentLeft) {
        this.warnPercentLeft = warnPercentLeft;
    }

    public String getCredentialsDirectory() {
        return credentialsDirectory;
    }

    public void setCredentialsDirectory(final String credentialsDirectory) {
        this.credentialsDirectory = credentialsDirectory;
    }

    public List<TokenConfiguration> getTokenConfigurationList() {
        return tokenConfigurationList;
    }

    public void setTokenConfigurationList(final List<TokenConfiguration> tokenConfigurationList) {
        this.tokenConfigurationList = tokenConfigurationList;
    }

    public String getUserCredentialsFilename() {
        return userCredentialsFilename;
    }

    public void setUserCredentialsFilename(final String userCredentialsFilename) {
        this.userCredentialsFilename = userCredentialsFilename;
    }

    public String getClientCredentialsFilename() {
        return clientCredentialsFilename;
    }

    public void setClientCredentialsFilename(final String clientCredentialsFilename) {
        this.clientCredentialsFilename = clientCredentialsFilename;
    }

}
