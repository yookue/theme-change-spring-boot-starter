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


import javax.annotation.Nonnull;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.ResourceBundleThemeSource;
import org.springframework.ui.context.support.UiApplicationContextUtils;
import com.yookue.springstarter.themechange.property.ThemeChangeProperties;


/**
 * Configuration of {@link org.springframework.ui.context.ThemeSource} for theme change
 *
 * @author David Hsing
 * @reference "https://www.baeldung.com/spring-mvc-themes"
 * @see org.springframework.ui.context.support.UiApplicationContextUtils
 */
@Configuration
@ConditionalOnProperty(prefix = ThemeChangeViewConfiguration.PROPERTIES_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(value = ThemeChangeProperties.class)
@SuppressWarnings({"JavadocDeclaration", "JavadocLinkAsPlainText"})
public class ThemeChangeSourceConfiguration {
    /**
     * @see org.springframework.ui.context.support.UiApplicationContextUtils#initThemeSource
     */
    @Bean(name = UiApplicationContextUtils.THEME_SOURCE_BEAN_NAME)
    @ConditionalOnMissingBean(name = UiApplicationContextUtils.THEME_SOURCE_BEAN_NAME)
    public ThemeSource themeSource(@Nonnull ThemeChangeProperties properties) {
        ResourceBundleThemeSource result = new ResourceBundleThemeSource();
        ThemeChangeProperties.ThemeSource props = properties.getThemeSource();
        result.setBasenamePrefix(props.getBasenamePrefix());
        result.setDefaultEncoding(props.getDefaultEncoding());
        result.setFallbackToSystemLocale(BooleanUtils.isTrue(props.getFallbackToSystemLocale()));
        return result;
    }
}
