import static org.junit.jupiter.api.Assertions.*;
import com.example.Login;
import com.example.LoginDatabase;
import static org.mockito.Mockito.*;

import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import java.lang.reflect.Method;

public class LoginLogicTest {
    private Login loginInstance;
    private Stage stage;

    @BeforeAll
    public static void initToolkit() {
        // This will initialize the JavaFX toolkit.
        new JFXPanel();
    }

    @BeforeEach
    public void setUp(){
        stage = mock(Stage.class);
        loginInstance = new Login(stage);
    }

    @Test
    public void loginTest() throws Exception{
        Method authMethod = Login.class.getDeclaredMethod("authenticate", String.class, String.class);
        authMethod.setAccessible(true);

        boolean result = (boolean) authMethod.invoke(loginInstance, "", "somePassword");
        assertFalse(result, "Authentication failed due to empty username");

        result = (boolean) authMethod.invoke(loginInstance, "user", "pass");
        assertTrue(result, "Authentication succeeded");
    }
}


