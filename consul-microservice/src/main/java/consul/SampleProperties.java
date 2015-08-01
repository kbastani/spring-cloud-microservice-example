package consul;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Spencer Gibb
 */
@ConfigurationProperties("sample")
@Data
public class SampleProperties {

    private String prop = "default value";

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }
}
