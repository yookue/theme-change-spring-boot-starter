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

package com.yookue.springstarter.themechange.property;


import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import com.yookue.commonplexus.javaseutil.constant.StringVariantConst;
import com.yookue.commonplexus.springutil.constant.AntPathConst;
import com.yookue.commonplexus.springutil.constant.SpringAttributeConst;
import com.yookue.springstarter.themechange.config.ThemeChangeViewConfiguration;
import com.yookue.springstarter.themechange.enumeration.ThemeResolverType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Properties for theme change
 *
 * @author David Hsing
 */
@ConfigurationProperties(prefix = ThemeChangeViewConfiguration.PROPERTIES_PREFIX)
@Getter
@Setter
@ToString
public class ThemeChangeProperties implements Serializable {
    /**
     * Indicates whether to enable this starter or not
     * <p>
     * Default is {@code true}
     */
    private Boolean enabled = true;

    /**
     * Model and view attributes
     */
    private final ModelAndView modelAndView = new ModelAndView();

    /**
     * Theme change interceptor attributes
     */
    private final ThemeInterceptor themeInterceptor = new ThemeInterceptor();

    /**
     * Theme source attributes
     */
    private final ThemeSource themeSource = new ThemeSource();

    /**
     * The type of theme resolver
     * <p>
     * Default is {@code COOKIE}
     */
    private ThemeResolverType themeResolverType = ThemeResolverType.COOKIE;

    /**
     * Cookie based theme resolver attributes
     */
    private final CookieThemeResolver cookieThemeResolver = new CookieThemeResolver();

    /**
     * Session based theme resolver attributes
     */
    private final SessionThemeResolver sessionThemeResolver = new SessionThemeResolver();


    /**
     * Properties for model and view
     *
     * @author David Hsing
     * @see org.springframework.web.servlet.ModelAndView
     * @see com.yookue.springstarter.themechange.interceptor.ThemeChangeViewInterceptor
     */
    @Getter
    @Setter
    @ToString
    public static class ModelAndView implements Serializable {
        /**
         * The attribute name to add to the view
         * <p>
         * The attribute value is {@code tagNames}
         */
        private String viewAttribute = "themeChangeTagNames";    // $NON-NLS-1$

        /**
         * Indicates whether to use message resource to display the tag name or not
         */
        private Boolean tagMultilingual;

        /**
         * The theme-tag and theme-name mappings
         */
        private Map<String, String> tagNames = new LinkedHashMap<>();

        /**
         * The priority order of the interceptor
         */
        private Integer interceptorOrder;

        /**
         * The path patterns to be intercepted
         */
        private List<String> interceptPaths = Collections.singletonList(AntPathConst.SLASH_STARS);

        /**
         * The path patterns to be excluded
         */
        private List<String> excludePaths;
    }


    /**
     * Properties for theme change interceptor
     *
     * @author David Hsing
     * @see org.springframework.web.servlet.theme.ThemeChangeInterceptor
     */
    @Getter
    @Setter
    @ToString
    public static class ThemeInterceptor implements Serializable {
        /**
         * Param name of the theme specification parameter
         */
        private String paramName = StringVariantConst.THEME;

        /**
         * The priority order of the interceptor
         */
        private Integer interceptorOrder;

        /**
         * The path patterns to be intercepted
         */
        private List<String> interceptPaths = Collections.singletonList(AntPathConst.SLASH_STARS);

        /**
         * The path patterns to be excluded
         */
        private List<String> excludePaths;
    }


    /**
     * Properties for theme source
     *
     * @author David Hsing
     */
    @Getter
    @Setter
    @ToString
    public static class ThemeSource implements Serializable {
        /**
         * Set the prefix that gets applied to the resource basename
         *
         * @see org.springframework.ui.context.support.ResourceBundleThemeSource
         */
        private String basenamePrefix;

        /**
         * Set the default charset to use for parsing resource bundle files
         */
        private String defaultEncoding = StandardCharsets.UTF_8.name();

        /**
         * Indicates whether fallback to system locale if no files for a specific Locale have been found or not
         */
        private Boolean fallbackToSystemLocale;
    }


    /**
     * Properties for cookie theme resolver
     *
     * @author David Hsing
     */
    @Getter
    @Setter
    @ToString
    public static class CookieThemeResolver implements Serializable {
        /**
         * Specifies a name for the cookie
         */
        private String cookieName = SpringAttributeConst.THEME_RESOLVER_THEME;

        /**
         * Specifies a path for the cookie
         */
        private String cookiePath = CookieLocaleResolver.DEFAULT_COOKIE_PATH;

        /**
         * Specifies the domain name of cookie
         * <p>
         * Domain names are formatted according to RFC 2109
         */
        private String cookieDomain;

        /**
         * Specifies the maximum age of the cookie in seconds
         * <p>
         * If negative, means the cookie is not stored; if zero, deletes the cookie
         */
        private Integer cookieMaxAge;

        /**
         * Indicates whether the cookie is supposed to be marked with the "HttpOnly" attribute or not
         */
        private Boolean cookieHttpOnly = false;

        /**
         * Indicates whether the cookie should only be sent using a secure protocol, such as HTTPS (SSL)
         */
        private Boolean cookieSecure = false;

        /**
         * Indicates whether this resolver's cookies should be compliant with BCP-47 language tags instead of Java's legacy locale specification format or not
         */
        private Boolean languageTagCompliant = true;

        /**
         * Indicates whether to reject cookies with invalid content (e.g. invalid format) or not
         */
        private Boolean rejectInvalidCookies = true;

        /**
         * Set the name of the default theme
         */
        private String defaultThemeName;
    }


    /**
     * Properties for session theme resolver
     *
     * @author David Hsing
     */
    @Getter
    @Setter
    @ToString
    public static class SessionThemeResolver implements Serializable {
        /**
         * Set the name of the default theme
         */
        private String defaultThemeName;
    }
}
