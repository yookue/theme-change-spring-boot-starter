/*
 * Copyright (c) 2016 Yookue Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yookue.springstarter.themechange.config;


import java.util.Optional;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.theme.CookieThemeResolver;
import org.springframework.web.servlet.theme.FixedThemeResolver;
import org.springframework.web.servlet.theme.SessionThemeResolver;
import com.yookue.commonplexus.javaseutil.util.StringUtilsWraps;
import com.yookue.springstarter.themechange.property.ThemeChangeProperties;


/**
 * Configuration of {@link org.springframework.web.servlet.ThemeResolver} for theme change
 *
 * @author David Hsing
 * @see org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration#themeResolver
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = ThemeChangeViewConfiguration.PROPERTIES_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureAfter(value = DispatcherServletAutoConfiguration.class)
@AutoConfigureBefore(value = WebMvcAutoConfiguration.class)
@AutoConfigureOrder(value = Ordered.HIGHEST_PRECEDENCE + 8)
@Import(value = {ThemeChangeResolverConfiguration.Entry.class, ThemeChangeResolverConfiguration.Cookie.class, ThemeChangeResolverConfiguration.Session.class, ThemeChangeResolverConfiguration.Fixed.class})
public class ThemeChangeResolverConfiguration {
    @Order(value = 0)
    @EnableConfigurationProperties(value = ThemeChangeProperties.class)
    static class Entry {
    }


    @Order(value = 1)
    @ConditionalOnProperty(prefix = ThemeChangeViewConfiguration.PROPERTIES_PREFIX, name = "theme-resolver-type", havingValue = "cookie", matchIfMissing = true)
    static class Cookie {
        /**
         * @see org.springframework.web.servlet.theme.CookieThemeResolver
         */
        @Bean(name = DispatcherServlet.THEME_RESOLVER_BEAN_NAME)
        @ConditionalOnMissingBean(name = DispatcherServlet.THEME_RESOLVER_BEAN_NAME)
        public ThemeResolver themeResolver(@Nonnull ThemeChangeProperties properties) {
            CookieThemeResolver resolver = new CookieThemeResolver();
            ThemeChangeProperties.CookieThemeResolver props = properties.getCookieThemeResolver();
            StringUtilsWraps.ifNotBlank(props.getCookieName(), resolver::setCookieName);
            StringUtilsWraps.ifNotBlank(props.getCookiePath(), resolver::setCookiePath);
            StringUtilsWraps.ifNotBlank(props.getCookieDomain(), resolver::setCookieDomain);
            Optional.ofNullable(props.getCookieMaxAge()).ifPresent(resolver::setCookieMaxAge);
            props.setCookieHttpOnly(BooleanUtils.isTrue(props.getCookieHttpOnly()));
            props.setCookieSecure(BooleanUtils.isTrue(props.getCookieSecure()));
            props.setLanguageTagCompliant(BooleanUtils.isTrue(props.getLanguageTagCompliant()));
            props.setRejectInvalidCookies(BooleanUtils.isTrue(props.getRejectInvalidCookies()));
            Optional.ofNullable(props.getDefaultThemeName()).ifPresent(resolver::setDefaultThemeName);
            return resolver;
        }
    }


    @Order(value = 2)
    @ConditionalOnProperty(prefix = ThemeChangeViewConfiguration.PROPERTIES_PREFIX, name = "theme-resolver-type", havingValue = "session")
    static class Session {
        /**
         * @see org.springframework.web.servlet.theme.SessionThemeResolver
         */
        @Bean(name = DispatcherServlet.THEME_RESOLVER_BEAN_NAME)
        @ConditionalOnMissingBean(name = DispatcherServlet.THEME_RESOLVER_BEAN_NAME)
        public ThemeResolver themeResolver(@Nonnull ThemeChangeProperties properties) {
            SessionThemeResolver resolver = new SessionThemeResolver();
            StringUtilsWraps.ifNotBlank(properties.getSessionThemeResolver().getDefaultThemeName(), resolver::setDefaultThemeName);
            return resolver;
        }
    }


    @Order(value = 3)
    @ConditionalOnProperty(prefix = ThemeChangeViewConfiguration.PROPERTIES_PREFIX, name = "theme-resolver-type", havingValue = "fixed")
    static class Fixed {
        /**
         * @see org.springframework.web.servlet.theme.FixedThemeResolver
         */
        @Bean(name = DispatcherServlet.THEME_RESOLVER_BEAN_NAME)
        @ConditionalOnMissingBean(name = DispatcherServlet.THEME_RESOLVER_BEAN_NAME)
        public ThemeResolver themeResolver() {
            return new FixedThemeResolver();
        }
    }
}
