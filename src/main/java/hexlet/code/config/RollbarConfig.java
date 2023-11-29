package hexlet.code.config;


import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static com.rollbar.spring.webmvc.RollbarSpringConfigBuilder.withAccessToken;

@Configuration()
@EnableWebMvc
@ComponentScan({
        // ADD YOUR PROJECT PACKAGE HERE
        "hexlet.code"
})

public class RollbarConfig {

    @Value("${ROLLBAR_TOKEN:}")
    private String rollbarToken;

    public Rollbar rollbar() throws Exception {
        return new Rollbar(getRollbarConfigs(rollbarToken));
    }

    private Config getRollbarConfigs(String accessToken) throws Exception {

        // Reference ConfigBuilder.java for all the properties you can set for Rollbar
        return withAccessToken(accessToken)
                .environment("development")
                .codeVersion("1.0.0")
                .build();

    }


}

