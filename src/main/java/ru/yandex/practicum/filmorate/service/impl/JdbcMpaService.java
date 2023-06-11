package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MyAppException;
import ru.yandex.practicum.filmorate.model.enumeration.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@Slf4j
@Service("jdbcMpaService")
public class JdbcMpaService implements MpaService {
    private final MpaRepository mpaRepository;

    public JdbcMpaService(@Qualifier("jdbcMpaRepository") MpaRepository mpaRepository) {
        this.mpaRepository = mpaRepository;
    }

    @Override
    public Collection<Mpa> findAll() {
        log.info("Запрос на получение списка всех рейтингов из базы.");
        return mpaRepository.findAll();
    }

    @Override
    public Mpa findById(Long id) {
        log.info("Запрос на получение рейтинг c id {}.", id);
        return mpaRepository.findById(id).orElseThrow(() ->
                new MyAppException("404", String.format("Рейтинг с id %d не найден.", id), HttpStatus.NOT_FOUND));
    }

    @Override
    public Mpa create(Mpa type) {
        log.info("Запрос на создание рейтинга.");
        return mpaRepository.create(type);
    }

    @Override
    public Mpa update(Mpa type) {
        log.info("Запрос на обновление рейтинга.");
        checkMpaById(type.getId());
        log.info("Рейтинг с id-{} был обновлен.", type.getId());
        return mpaRepository.update(type);
    }

    @Override
    public void delete(Long id) {
        checkMpaById(id);
        log.info("Запрос на удаление рейтинга.");
        mpaRepository.delete(id);
        log.info("Рейтинг с id-{} был удален.", id);
    }

    private void checkMpaById(long id) {
        if (!mpaRepository.existsById(id)) {
            throw new MyAppException("404", String.format("Рейтинг с id %d или не найден.", id),
                    HttpStatus.NOT_FOUND);
        }
    }
}
