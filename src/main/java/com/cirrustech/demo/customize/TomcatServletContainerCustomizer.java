package com.cirrustech.demo.customize;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

public class TomcatServletContainerCustomizer implements EmbeddedServletContainerCustomizer {

    private static final Logger LOG = LoggerFactory.getLogger(TomcatServletContainerCustomizer.class);

    @Override
    public void customize(ConfigurableEmbeddedServletContainer factory) {
        if(factory instanceof TomcatEmbeddedServletContainerFactory) {
            customizeTomcat((TomcatEmbeddedServletContainerFactory) factory);
        }
    }

    public void customizeTomcat(TomcatEmbeddedServletContainerFactory factory) {
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {

            @Override
            public void customize(Connector connector) {

                String maxThreads = System.getProperty("tomcat.max.threads");
                if(maxThreads!=null) {

                    Object defaultMaxThreads = connector.getAttribute("maxThreads");
                    connector.setAttribute("maxThreads", maxThreads);
                    LOG.info("Changed Tomcat connector maxThreads from " + defaultMaxThreads + " to " + maxThreads);
                }
            }
        });
    }
}


