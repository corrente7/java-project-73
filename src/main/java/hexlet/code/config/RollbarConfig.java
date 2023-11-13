package hexlet.code.config;


import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.spring.webmvc.RollbarSpringConfigBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration()
@EnableWebMvc
@ComponentScan({
        // ADD YOUR PROJECT PACKAGE HERE
        "hexlet.code"
})

public class RollbarConfig {

    public Rollbar rollbar() {
        return new Rollbar(getRollbarConfigs("<ACCESS_TOKEN>"));
    }

    private Config getRollbarConfigs(String accessToken) {

        // Reference ConfigBuilder.java for all the properties you can set for Rollbar
        return RollbarSpringConfigBuilder.withAccessToken(accessToken)
                .environment("development")
                .codeVersion("1.0.0")
                .build();
    }
}

