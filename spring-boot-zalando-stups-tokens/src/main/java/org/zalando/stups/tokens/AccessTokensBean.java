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
package org.zalando.stups.tokens;

import java.io.File;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.SmartLifecycle;

import org.zalando.stups.tokens.config.AccessTokensBeanProperties;
import org.zalando.stups.tokens.config.TokenConfiguration;

/**
 * @author  jbellmann
 */
public class AccessTokensBean implements AccessTokens, SmartLifecycle {

    private final Logger logger = LoggerFactory.getLogger(AccessTokensBean.class);

    private AccessTokens accessTokensDelegate;

    private final AccessTokensBeanProperties accessTokensBeanProperties;

    private volatile boolean running = false;

    public AccessTokensBean(final AccessTokensBeanProperties accessTokensBeanProperties) {
        this.accessTokensBeanProperties = accessTokensBeanProperties;
    }

    @Override
    public String get(final Object tokenId) throws AccessTokenUnavailableException {

        return accessTokensDelegate.get(tokenId);
    }

    @Override
    public AccessToken getAccessToken(final Object tokenId) throws AccessTokenUnavailableException {

        return accessTokensDelegate.getAccessToken(tokenId);
    }

    @Override
    public void invalidate(final Object tokenId) {
        accessTokensDelegate.invalidate(tokenId);
    }

    protected UserCredentialsProvider getUserCredentialsProvider() {

        return new JsonFileBackedUserCredentialsProvider(getCredentialsFile(
                    accessTokensBeanProperties.getUserCredentialsFilename()));
    }

    protected ClientCredentialsProvider getClientCredentialsProvider() {

        return new JsonFileBackedClientCredentialsProvider(getCredentialsFile(
                    accessTokensBeanProperties.getClientCredentialsFilename()));
    }

    protected File getCredentialsFile(final String credentialsFilename) {
        return new File(accessTokensBeanProperties.getCredentialsDirectory(), credentialsFilename);
    }

    @Override
    public synchronized void start() {
        if (isRunning()) {
            return;
        }

        logger.info("Container seems up an running");

        // TODO, if something fails here, shall we shutdown the container?
        AccessTokensBuilder builder = Tokens.createAccessTokensWithUri(accessTokensBeanProperties.getAccessTokenUri());
        builder.usingClientCredentialsProvider(getClientCredentialsProvider());
        builder.usingUserCredentialsProvider(getUserCredentialsProvider());

        for (TokenConfiguration tc : accessTokensBeanProperties.getTokenConfigurationList()) {
            logger.info("configure scopes for service {}", tc.getTokenId());

            AccessTokenConfiguration configuration = builder.manageToken(tc.getTokenId());
            configuration.addScopes(new HashSet<Object>(tc.getScopes()));
        }

        logger.info("Start 'accessTokenRefresher' ...");
        accessTokensDelegate = builder.start();
        running = true;
        logger.info("'accessTokenRefresher' started.");
    }

    @Override
    public synchronized void stop() {
        if (!isRunning()) {
            return;
        }

        logger.info("Stop 'accessTokenRefresher' ...");
        accessTokensDelegate.stop();
        running = false;
        logger.info("'accessTokenRefresher' stopped.");

    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {

        // default
        return 0;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(final Runnable callback) {
        stop();
        callback.run();
    }
}
