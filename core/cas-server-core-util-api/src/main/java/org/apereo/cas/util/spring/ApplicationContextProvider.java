package org.apereo.cas.util.spring;

import org.apereo.cas.configuration.CasConfigurationProperties;

import lombok.val;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import java.util.Optional;

/**
 * @author Misagh Moayyed
 * An implementation of {@link ApplicationContextAware} that statically
 * holds the application context
 * @since 3.0.0.
 */
public class ApplicationContextProvider implements ApplicationContextAware, ResourceLoaderAware {

    private static ApplicationContext CONTEXT;

    private static ResourceLoader RESOURCE_LOADER;

    public static ApplicationContext getApplicationContext() {
        return CONTEXT;
    }

    /**
     * Hold application context statically.
     *
     * @param ctx the ctx
     */
    public static void holdApplicationContext(final ApplicationContext ctx) {
        CONTEXT = ctx;
    }

    @Override
    public void setApplicationContext(final ApplicationContext ctx) {
        CONTEXT = ctx;
    }

    /**
     * Register bean into application context.
     *
     * @param <T>                the type parameter
     * @param applicationContext the application context
     * @param beanClazz          the bean clazz
     * @param beanId             the bean id
     * @return the type registered
     */
    public static <T> T registerBeanIntoApplicationContext(final ConfigurableApplicationContext applicationContext,
                                                           final Class<T> beanClazz, final String beanId) {
        val beanFactory = applicationContext.getBeanFactory();
        val provider = beanFactory.createBean(beanClazz);
        beanFactory.initializeBean(provider, beanId);
        beanFactory.autowireBean(provider);
        beanFactory.registerSingleton(beanId, provider);
        return provider;
    }

    /**
     * Gets resource loader.
     *
     * @return the resource loader
     */
    public static ResourceLoader getResourceLoader() {
        return RESOURCE_LOADER;
    }


    /**
     * Gets cas properties.
     *
     * @return the cas properties
     */
    public static Optional<CasConfigurationProperties> getCasProperties() {
        if (CONTEXT != null) {
            return Optional.of(CONTEXT.getBean(CasConfigurationProperties.class));
        }
        return Optional.empty();
    }

    @Override
    public void setResourceLoader(final ResourceLoader resourceLoader) {
        RESOURCE_LOADER = resourceLoader;
    }

    public static ConfigurableApplicationContext getConfigurableApplicationContext() {
        return (ConfigurableApplicationContext) CONTEXT;
    }
}
