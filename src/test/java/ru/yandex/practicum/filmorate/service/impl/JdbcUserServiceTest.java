package ru.yandex.practicum.filmorate.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.AbstractServiceTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.JdbcUserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcUserServiceTest extends AbstractServiceTest {
    private final JdbcUserRepository userRepository;

    private final ObjectMapper objectMapper;

    @ParameterizedTest()
    @ValueSource(longs = {1, 2, 3, 4, 5})
    public void testFindByIdParam(Long args) {
        Optional<User> filmOptional = userRepository.findById(args);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", args));
    }

    @Test
    public void testFindAll() {
        int size = userRepository.findAll().size();
        assertThat(size).isEqualTo(10);
    }

    @Test
    public void testUpdate() throws JsonProcessingException {
        User curUser = userRepository.findById(1L).get();
        assertThat(curUser).hasFieldOrPropertyWithValue("email", "user1@yandex.ru");

        String content = "{\"id\": 1,\"email\":\"user2@yandex.ru\",\"login\":\"user1\",\"name\":\"user1\"," +
                "\"birthday\":\"1993-03-09\"}";
        User updateUser = objectMapper.readValue(content, User.class);

        userRepository.update(updateUser);
        User user = userRepository.findById(1L).get();
        assertThat(user).hasFieldOrPropertyWithValue("email", "user2@yandex.ru");
    }

    @Test
    public void testDelete() throws JsonProcessingException {
        Optional<User> optionalFilm = userRepository.findById(1L);
        assertThat(optionalFilm).isNotEmpty();

        userRepository.delete(1L);

        Optional<User> optionalDeletedFilm = userRepository.findById(1L);
        assertThat(optionalDeletedFilm).isEmpty();
    }

    @Test
    public void testAddFriend() throws JsonProcessingException {
        List<User> allFriends = userRepository.findAllFriends(1L);
        assertThat(allFriends.size()).isEqualTo(2);

        userRepository.addFriend(1L, 3L);
        List<User> newAllFriends = userRepository.findAllFriends(1L);
        assertThat(newAllFriends.size()).isEqualTo(3);
    }

    @Test
    public void testDeleteFriend() throws JsonProcessingException {
        List<User> allFriends = userRepository.findAllFriends(1L);
        assertThat(allFriends.size()).isEqualTo(2);

        userRepository.deleteFriend(1L, 8L);
        List<User> newAllFriends = userRepository.findAllFriends(1L);
        assertThat(newAllFriends.size()).isEqualTo(1);
    }

    @Test
    public void testFindCommonFriends() throws JsonProcessingException {
        List<User> commonFriends = userRepository.findCommonFriends(8L, 10L);
        assertThat(commonFriends.size()).isEqualTo(1);
        assertThat(commonFriends.get(0))
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("email", "user1@yandex.ru")
                .hasFieldOrPropertyWithValue("name", "user1");
    }
}