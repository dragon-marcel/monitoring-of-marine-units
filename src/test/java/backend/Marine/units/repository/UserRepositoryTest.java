package backend.Marine.units.repository;

import backend.Marine.units.entity.User;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void should_return_user_by_usernName() {
        //given
        String usernName = "admin";

        //when
        Optional<User> userByUsername = userRepository.findByUsername(usernName);

        //then
        assertNotNull(userByUsername.get());
        assertEquals(usernName, userByUsername.get().getUsername());
    }

    @Test
    public void should_return_user_by_usernName_ignore_case() {
        //given
        String usernName = "ADMIN";

        //when
        Optional<User> userByUsername = userRepository.findByUsernameIgnoreCase(usernName);

        //then
        assertNotNull(userByUsername.get());
        assertEquals("admin", userByUsername.get().getUsername());
    }

    @Test
    public void should_return_user_by_id() {
        //given
        int id = 1;

        //when
        Optional<User> userByUsername = userRepository.findById(id);

        //then
        assertNotNull(userByUsername.get());
        assertEquals(id, userByUsername.get().getId());
    }

    @Test
    public void should_return_users() {
        //given
        int id = 1;

        //when
        List<User> users = userRepository.findAll();

        //then
        assertThat(users, Matchers.hasSize(2));
    }
}
