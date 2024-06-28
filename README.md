# Theme Change Spring Boot Starter

Spring Boot application enables an ability of switching themes.

## Quickstart

- Import dependencies

```xml
    <dependency>
        <groupId>com.yookue.springstarter</groupId>
        <artifactId>theme-change-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> By default, this starter will auto take effect, you can turn it off by `spring.theme-change.enabled = false`

- Configure Spring Boot `application.yml` with prefix `spring.theme-change`

```yml
spring:
    theme-change:
        model-and-view:
            view-attribute: 'themeChangeTagNames'
            tag-multilingual: false
            tag-names:
                'light': 'Light'
                'blue': 'Blue'
                'dark': 'Dark'
        theme-interceptor:
            param-name: 'theme'
            intercept-paths:
                - '/**'
            exclude-paths:
                - '/foo/**'
                - '/bar/**'
        theme-source:
            basename-prefix: 'theme.'
        theme-resolver-type: 'cookie'
        cookie-theme-resolver:
            default-theme-name: 'light'
```

- Create basename folder and `properties` files.
The `basename-prefix` path  is used for `MessageSource` to resolve `theme` parameter (don't forgot the ending dot). You should
    1. Create a folder named `theme` under the classpath (e.g. under the `resources` folder).
    2. Create several `properties` files named the keys of `tag-names` node (e.g. `light.properties`, `blue.properties`, `dark.properties`) under the `theme` folder.
    3. Write a key with a path of style file in each `properties` file (take `light` theme as an example)
`/resource/theme/light.properties` content:
```
theme-change.style-sheet = /assert/theme/light.min.css
```

- **Optional feature**: If you want to display the `tag-names` value in multilingual (not the fixed value in `application.properties`), for example, display the value of `light` to `明亮` when current request is using `zh` locale, or display `Light` when the request is using `en` locale. You can reach your goal as following:
    1. Configure the `tag-multilingual` attribute to `true`
    2. Set the value of `default` to `theme-change.dropdown-switch`
    3. Write the `theme-change.dropdown-switch` as a key in a resource bundle properties that could be loaded by the primary `MessageSource` bean (Just as our another Spring starter `message-source-spring-boot-starter` does)

- Write your template, code as following (take `Thymeleaf` as an example)

Under the `head` segment:

```
<link rel="stylesheet" type="text/css" th:href="@{${#themes.code('theme-change.style-sheet')}}"/>
```

Under the `body` segment:

```
<ul>
    <li class="theme-switch dropdown">
        <a href="javascript:">
            <i class="fa fa-futbol-o" aria-hidden="true"></i>
            <span>Theme</span>
            <i class="bi bi-chevron-down"></i>
        </a>
        <ul role="menu" th:if="${not #maps.isEmpty(themeChangeTagNames)}">
            <li th:each="tagName: ${themeChangeTagNames}"><a th:href="${'?theme=' + tagName.key}"><span th:text="${tagName.value}">Placeholder</span></a></li>
        </ul>
    </li>
</ul>
```

## Document

- Github: https://github.com/yookue/theme-change-spring-boot-starter
- Spring MVC themes tutorial: https://www.baeldung.com/spring-mvc-themes

## Requirement

- jdk 1.8+

## License

This project is under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

See the `NOTICE.txt` file for required notices and attributions.

## Donation

You like this package? Then [donate to Yookue](https://yookue.com/public/donate) to support the development.

## Website

- Yookue: https://yookue.com
