package com.napptilus.napptiluspriceapp.service;

import com.napptilus.napptiluspriceapp.exception.DatababaseItemNotFoundException;
import com.napptilus.napptiluspriceapp.exception.DatabaseListEmptyException;
import com.napptilus.napptiluspriceapp.model.Currency;
import com.napptilus.napptiluspriceapp.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CurrencyService {

    @Value("${napptilus.service.exception.msg}")
    private String serviceExceptionMsg;

    @Value("${napptilus.repository.findBy.exception.msg}")
    private String itemNotFoundExceptionMsg;

    @Value("${napptilus.repository.findAll.exception.msg}")
    private String resultListIsNullExceptionMsg;

    @Value("${napptilus.repository.findAll.isEmpty.exception.msg}")
    private String resultListIsEmptyExceptionMsg;

    @Autowired
    private CurrencyRepository repository;

    public Currency save(Currency newItem) {
        try {
            return repository.save(newItem);
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public Currency findById(String id) throws DatababaseItemNotFoundException {
        try {
            return repository.findByCurrencyCode(id).orElseThrow(() -> new DatababaseItemNotFoundException(MessageFormat.format(itemNotFoundExceptionMsg, id.toString())));
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public List<Currency> findAll() throws DatabaseListEmptyException {
        try {
            List<Currency> list = Objects.requireNonNull(repository.findAll(), resultListIsNullExceptionMsg);
            if (list.isEmpty())
                throw new DatabaseListEmptyException(resultListIsEmptyExceptionMsg);
            else
                return list;
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public void delete(Currency item) {
        try {
            repository.delete(item);
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public Boolean existById(Long id) {
        try {
            return repository.existsById(id);
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public List<Currency> saveAll(List<Currency> list) {
        try {
            return (List<Currency>) repository.saveAll(list);
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

}
