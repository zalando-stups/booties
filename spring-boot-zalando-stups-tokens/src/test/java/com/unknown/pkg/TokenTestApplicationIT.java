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

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.zalando.stups.tokens.AccessToken;
import org.zalando.stups.tokens.AccessTokens;
import org.zalando.stups.tokens.AccessTokensBean;
import org.zalando.stups.tokens.config.AccessTokensBeanProperties;
import org.zalando.stups.tokens.config.TokenConfiguration;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * @author  jbellmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TokenTestApplication.class})
@WebIntegrationTest(randomPort = false)
@ActiveProfiles("custom")
public class TokenTestApplicationIT {

    @Autowired
    private AccessTokensBean tokens;

    @Autowired
    private AccessTokensBeanProperties accessTokensBeanProperties;

    @Autowired
    private AccessTokens accessTokens;

    @Test
    public void retrieveToken() throws InterruptedException {

        // give the controller a chance to initialize
        TimeUnit.SECONDS.sleep(4);
        Assertions.assertThat(accessTokensBeanProperties).isNotNull();
        Assertions.assertThat(accessTokensBeanProperties.getTokenConfigurationList()).isNotEmpty();

        Assertions.assertThat(accessTokens).isNotNull();

        List<TokenConfiguration> services = accessTokensBeanProperties.getTokenConfigurationList();

        Iterable<TokenConfiguration> firstServiceFilterResult = Iterables.filter(services,
                new TokenIdFilter("firstService"));
        Assertions.assertThat(Iterables.size(firstServiceFilterResult)).isEqualTo(1);

        TokenConfiguration firstService = Iterables.getFirst(firstServiceFilterResult, null);
        Assertions.assertThat(firstService).isNotNull();
        Assertions.assertThat(firstService.getScopes()).isNotNull();
        Assertions.assertThat(firstService.getScopes()).contains("refole:read", "refole:write", "refole:all");

        Iterable<TokenConfiguration> secondServiceFilterResult = Iterables.filter(services,
                new TokenIdFilter("secondService"));
        Assertions.assertThat(Iterables.size(secondServiceFilterResult)).isEqualTo(1);

        TokenConfiguration secondService = Iterables.getFirst(secondServiceFilterResult, null);
        Assertions.assertThat(secondService).isNotNull();
        Assertions.assertThat(secondService.getScopes()).isNotNull();
        Assertions.assertThat(secondService.getScopes()).contains("singleScope:all");

        AccessToken accessToken = accessTokens.getAccessToken("firstService");
        Assertions.assertThat(accessToken).isNotNull();
        Assertions.assertThat(accessToken.getToken()).isEqualTo("123456789-987654321");
        Assertions.assertThat(accessToken.getType()).isEqualTo("Bearer");
        Assertions.assertThat(accessToken.getInitialValidSeconds()).isEqualTo(5000);

        String accessTokenString = accessTokens.get("firstService");
        Assertions.assertThat(accessTokenString).isEqualTo("123456789-987654321");

        accessTokens.invalidate("firstService");

        System.out.println("KILL");
    }

    static class TokenIdFilter implements Predicate<TokenConfiguration> {

        private final String tokenId;

        public TokenIdFilter(final String tokenId) {
            this.tokenId = tokenId;
        }

        @Override
        public boolean apply(final TokenConfiguration input) {
            return input.getTokenId().equals(tokenId);
        }

    }
}
