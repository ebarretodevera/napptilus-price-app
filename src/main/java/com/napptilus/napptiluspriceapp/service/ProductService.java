package com.napptilus.napptiluspriceapp.service;

import com.napptilus.napptiluspriceapp.exception.DatababaseItemNotFoundException;
import com.napptilus.napptiluspriceapp.exception.DatabaseListEmptyException;
import com.napptilus.napptiluspriceapp.model.Product;
import com.napptilus.napptiluspriceapp.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ProductService {

    @Value("${napptilus.service.exception.msg}")
    private String serviceExceptionMsg;

    @Value("${napptilus.repository.findBy.exception.msg}")
    private String itemNotFoundExceptionMsg;

    @Value("${napptilus.repository.findAll.exception.msg}")
    private String resultListIsNullExceptionMsg;

    @Value("${napptilus.repository.findAll.isEmpty.exception.msg}")
    private String resultListIsEmptyExceptionMsg;

    @Autowired
    private ProductRepository ProductRepository;

    public Product save(Product newItem) {
        try {
            return ProductRepository.save(newItem);
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public Product findById(Long id) throws DatababaseItemNotFoundException {
        try {
            return ProductRepository.findById(id).orElseThrow(() -> new DatababaseItemNotFoundException(MessageFormat.format(itemNotFoundExceptionMsg, id.toString())));
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public List<Product> findAll() throws DatabaseListEmptyException {
        try {
            List<Product> list = Objects.requireNonNull(ProductRepository.findAll(), resultListIsNullExceptionMsg);
            if (list.isEmpty())
                throw new DatabaseListEmptyException(resultListIsEmptyExceptionMsg);
            else
                return list;
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public void delete(Product item) {
        try {
            ProductRepository.delete(item);
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public Boolean existById(Long id) {
        try {
            return ProductRepository.existsById(id);
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public List<Product> saveAll(List<Product> list) {
        try {
            return (List<Product>) ProductRepository.saveAll(list);
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

}
