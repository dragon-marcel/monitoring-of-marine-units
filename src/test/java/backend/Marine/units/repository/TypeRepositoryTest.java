package backend.Marine.units.repository;

import backend.Marine.units.entity.Type;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class TypeRepositoryTest {
    @Autowired
    private TypeRepository typeRepository;

    @Test
    public void should_return_all_type_of_ship() {
        //when
        List<Type> shipTypes = typeRepository.findAll();

        //then
        assertNotNull(shipTypes);
        assertFalse(shipTypes.isEmpty());
        assertThat(shipTypes, Matchers.hasSize(81));

    }
}
