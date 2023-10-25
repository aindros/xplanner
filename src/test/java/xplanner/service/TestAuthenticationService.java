package xplanner.service;

import java.util.Locale;
import junit.framework.TestCase;
import net.sf.xplanner.domain.Person;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.XPlannerTestSupport;
import com.technoetic.xplanner.security.*;

import javax.servlet.http.*;

public class TestAuthenticationService extends TestCase {
    private AuthenticationService service;
    HttpServletRequest request;
    HttpServletResponse response;
    private MockAuthenticator mockAuthenticator;

    public TestAuthenticationService(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        Logger.getRootLogger().setLevel(Level.OFF);
        request = new XPlannerTestSupport.XHttpServletRequestSimulator(Locale.getDefault());
        response = new XPlannerTestSupport.XHttpServletResponseSimulator();

        mockAuthenticator = new MockAuthenticator();
        service = new AuthenticationService();
        service.setAuthenticator(mockAuthenticator);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSuccessfulLogin() throws Exception {
        service.authenticate(new Person(), request, response);
    }

    @SuppressWarnings("unchecked")
    public void testUnsuccessfulLogin() {
        mockAuthenticator.authenticateException = new AuthenticationException();
        mockAuthenticator.authenticateException.getErrorsByModule().put("TestLoginContextSerializability", "test");

        boolean catched = false;
        try {
            service.authenticate(new Person(), request, response);
        } catch (AuthenticationException ex) {
            catched = true;
            assertEquals(1, ex.getErrorsByModule().size());
            assertEquals("TestLoginContextSerializability", ex.getErrorsByModule().keySet().iterator().next());
            assertEquals("test", ex.getErrorsByModule().values().iterator().next());
        }

        assertTrue(catched);
    }
}
