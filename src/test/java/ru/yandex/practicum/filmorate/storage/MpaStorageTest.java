package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaStorageTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private final MpaService mpaService;

    @Test
    void shouldReturnMpaWithId1andNameG() {
        Optional<Mpa> mpaOpt = mpaService.getMpa(1);
        assertThat(mpaOpt)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("name", "G")
                );
    }

    @Test
    void shouldReturnNoEmptyListOfMpa() {
        List<Optional<Mpa>> mpaList = mpaService.getAll();
        assertFalse(mpaList.isEmpty());
        assertEquals(5, mpaList.size());
    }

    @Test
    void shouldThrowExceptionIfPutWrongId() {
        Throwable exception = assertThrows(IllegalStateException.class, () -> mpaService.getMpa(99));
        assertEquals("Mpa is not found", exception.getMessage());
    }
}
