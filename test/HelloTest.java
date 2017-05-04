import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HelloTest {
    @Test
    public void shouldGreetProporly() {
        assertEquals(Hello.greet("Alan"), "Hello Alan");
    }
}



