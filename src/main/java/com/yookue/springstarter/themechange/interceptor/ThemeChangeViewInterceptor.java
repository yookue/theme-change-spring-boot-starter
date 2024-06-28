/*
 * Copyright (c) 2022 Yookue Ltd. All rights reserved.
 *
 * Yookue Confidential
 *
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the National Copyright Administration of China.
 */

package com.yookue.springstarter.themechange.interceptor;


import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.yookue.commonplexus.javaseutil.util.MapPlainWraps;
import com.yookue.commonplexus.springutil.util.MessageSourceWraps;
import com.yookue.commonplexus.springutil.util.WebUtilsWraps;
import com.yookue.springstarter.themechange.property.ThemeChangeProperties;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


/**
 * {@link org.springframework.web.servlet.HandlerInterceptor} for theme change
 *
 * @author David Hsing
 */
@RequiredArgsConstructor
public class ThemeChangeViewInterceptor implements HandlerInterceptor, MessageSourceAware {
    private final ThemeChangeProperties properties;

    @Setter
    protected MessageSource messageSource;

    @Override
    public void postHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, @Nullable ModelAndView view) {
        ThemeChangeProperties.ModelAndView props = properties.getModelAndView();
        if (WebUtilsWraps.isCanonicalModelView(view) && StringUtils.isNotBlank(props.getViewAttribute()) && !CollectionUtils.isEmpty(props.getTagNames())) {
            if (BooleanUtils.isNotTrue(props.getTagMultilingual())) {
                view.addObject(props.getViewAttribute(), props.getTagNames());
                return;
            }
            Map<String, String> tagNames = new LinkedHashMap<>(props.getTagNames());
            MapPlainWraps.recompute(tagNames, (key, value) -> MessageSourceWraps.getMessage(messageSource, value, value, LocaleContextHolder.getLocale()));
            view.addObject(props.getViewAttribute(), tagNames);
        }
    }
}
