package com.khvatid.postal_office_spring.presentation.config

import org.springframework.boot.actuate.info.InfoContributor
import org.springframework.boot.actuate.info.MapInfoContributor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*

@Configuration
class Config : WebMvcConfigurer {

  @Bean
  fun localeResolver(): LocaleResolver {
    val localeResolver = SessionLocaleResolver()
    localeResolver.setDefaultLocale(Locale("ru", "RU"))
    return localeResolver
  }

  @Bean
  fun messageSource(): ResourceBundleMessageSource {
    val messageSource = ResourceBundleMessageSource().apply {
      setUseCodeAsDefaultMessage(true)
      setBasenames("messages")
      setDefaultEncoding("UTF-8")
    }
    return messageSource
  }

  @Bean
  fun getInfoContributor(): InfoContributor{
    val contributor : Map<String,String> = mapOf(
      Pair("appName","postal_office_spring"),
      Pair("developer","8387 group"),
      Pair("description","Khvatalov Dmitry & Alexander Gordienko //Yelena Yur'yevna postav'te otsenochku"),
      Pair("mail","hv20027@gmail.com")
    )
    return MapInfoContributor(mapOf(Pair("info",contributor)))
  }

}