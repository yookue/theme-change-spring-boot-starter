/*
 * Copyright (c) 2022 Yookue Ltd. All rights reserved.
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.yookue.commonplexus.javaseutil.constant.AssertMessageConst;
import com.yookue.commonplexus.javaseutil.util.CollectionPlainWraps;
import com.yookue.springstarter.themechange.interceptor.ThemeChangeViewInterceptor;
import com.yookue.springstarter.themechange.property.ThemeChangeProperties;
import lombok.RequiredArgsConstructor;


/**
 * Configuration of view interceptor for theme change
 *
 * @author David Hsing
 */
@Configuration
@ConditionalOnProperty(prefix = ThemeChangeViewConfiguration.PROPERTIES_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(value = ThemeChangeProperties.class)
@RequiredArgsConstructor
public class ThemeChangeViewConfiguration implements WebMvcConfigurer {
    public static final String PROPERTIES_PREFIX = "spring.theme-change";    // $NON-NLS-1$
    public static final String VIEW_INTERCEPTOR = "themeChangeViewInterceptor";    // $NON-NLS-1$
    private final ThemeChangeProperties properties;

    @Bean(name = VIEW_INTERCEPTOR)
    @ConditionalOnMissingBean
    public ThemeChangeViewInterceptor viewInterceptor() {
        return new ThemeChangeViewInterceptor(properties);
    }

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        ThemeChangeProperties.ModelAndView props = properties.getModelAndView();
        Assert.notEmpty(props.getInterceptPaths(), AssertMessageConst.NOT_EMPTY);
        InterceptorRegistration registration = registry.addInterceptor(viewInterceptor()).addPathPatterns(props.getInterceptPaths());
        Optional.ofNullable(props.getInterceptorOrder()).ifPresent(registration::order);
        CollectionPlainWraps.ifNotEmpty(props.getExcludePaths(), element -> registration.excludePathPatterns(element));
    }
}
