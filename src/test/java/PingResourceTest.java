import com.roboter5123.accountservice.Application;
import com.roboter5123.accountservice.rest.PingResource;
import com.roboter5123.accountservice.rest.model.Ping;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Application.class)
class PingResourceTest {

    @Autowired
    private PingResource pingResource;

    @Test
    void ping_shouldReturnCurrentTime() {

        OffsetDateTime beforePing = OffsetDateTime.now().minusSeconds(1);
        Ping result = pingResource.ping();

        assertTrue(result.getTime().isAfter(beforePing));
        assertTrue(result.getTime().isBefore(OffsetDateTime.now().plusSeconds(1)));
    }
}
