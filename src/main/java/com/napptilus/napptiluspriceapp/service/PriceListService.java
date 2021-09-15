package com.napptilus.napptiluspriceapp.service;

import com.napptilus.napptiluspriceapp.exception.DatababaseItemNotFoundException;
import com.napptilus.napptiluspriceapp.exception.DatabaseListEmptyException;
import com.napptilus.napptiluspriceapp.model.PriceList;
import com.napptilus.napptiluspriceapp.repository.PriceListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PriceListService {

    @Value("${napptilus.service.exception.msg}")
    private String serviceExceptionMsg;

    @Value("${napptilus.repository.findBy.exception.msg}")
    private String itemNotFoundExceptionMsg;

    @Value("${napptilus.repository.findAll.exception.msg}")
    private String resultListIsNullExceptionMsg;

    @Value("${napptilus.repository.findAll.isEmpty.exception.msg}")
    private String resultListIsEmptyExceptionMsg;

    @Autowired
    private PriceListRepository repository;

    public PriceList save(PriceList newItem) {
        try {
            return repository.save(newItem);
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public PriceList findById(Long id) throws DatababaseItemNotFoundException {
        try {
            return repository.findById(id).orElseThrow(() -> new DatababaseItemNotFoundException(MessageFormat.format(itemNotFoundExceptionMsg, id.toString())));
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public List<PriceList> findAll() throws DatabaseListEmptyException {
        try {
            List<PriceList> list = Objects.requireNonNull(repository.findAll(), resultListIsNullExceptionMsg);
            if (list.isEmpty())
                throw new DatabaseListEmptyException(resultListIsEmptyExceptionMsg);
            else
                return list;
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public void delete(PriceList item) {
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

    public List<PriceList> saveAll(List<PriceList> list) {
        try {
            return (List<PriceList>) repository.saveAll(list);
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

}
