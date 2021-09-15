package com.napptilus.napptiluspriceapp.service;

import com.napptilus.napptiluspriceapp.exception.DatababaseItemNotFoundException;
import com.napptilus.napptiluspriceapp.exception.DatabaseListEmptyException;
import com.napptilus.napptiluspriceapp.exception.FilterException;
import com.napptilus.napptiluspriceapp.filter.Filter;
import com.napptilus.napptiluspriceapp.filter.PricesFilter;
import com.napptilus.napptiluspriceapp.filter.QueryOperator;
import com.napptilus.napptiluspriceapp.filter.ValueType;
import com.napptilus.napptiluspriceapp.model.Prices;
import com.napptilus.napptiluspriceapp.repository.PricesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PricesService {

    @Value("${napptilus.service.exception.msg}")
    private String serviceExceptionMsg;

    @Value("${napptilus.repository.findBy.exception.msg}")
    private String itemNotFoundExceptionMsg;

    @Value("${napptilus.repository.findAll.exception.msg}")
    private String resultListIsNullExceptionMsg;

    @Value("${napptilus.repository.findAll.isEmpty.exception.msg}")
    private String resultListIsEmptyExceptionMsg;

    @Autowired
    private PricesRepository repository;
    @Autowired
    private PricesFilter pricesFilter;

    public Prices save(Prices newItem) {
        try {
            return repository.save(newItem);
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public Prices findById(Long id) throws DatababaseItemNotFoundException {
        try {
            return repository.findById(id).orElseThrow(() -> new DatababaseItemNotFoundException(MessageFormat.format(itemNotFoundExceptionMsg, id.toString())));
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public List<Prices> findAll() throws DatabaseListEmptyException {
        try {
            List<Prices> list = Objects.requireNonNull(repository.findAll(), resultListIsNullExceptionMsg);
            if (list.isEmpty())
                throw new DatabaseListEmptyException(resultListIsEmptyExceptionMsg);
            else
                return list;
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public void delete(Prices item) {
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

    public List<Prices> saveAll(List<Prices> list) {
        try {
            return (List<Prices>) repository.saveAll(list);
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }

    public List<Prices> findDiscountsOrderedByPriorityAtCertainDate(LocalDateTime date, Long brandId, Long productId) throws FilterException {
        try {

            Filter productIdLike = Filter.builder()
                    .field("id")
                    .operator(QueryOperator.LIKE)
                    .valueType(ValueType.STRING)
                    .value(productId.toString())
                    .tables(List.of("product"))
                    .build();
            Filter brandIdLike = Filter.builder()
                    .field("id")
                    .operator(QueryOperator.LIKE)
                    .valueType(ValueType.STRING)
                    .value(brandId.toString())
                    .tables(List.of("brand"))
                    .build();
            Filter afterStartDate = Filter.builder()
                    .field("startDate")
                    .operator(QueryOperator.LESS_THAN_OR_EQUALS)
                    .valueType(ValueType.DATE)
                    .dateValue(date)
                    .build();
            Filter beforeEndDate = Filter.builder()
                    .field("endDate")
                    .operator(QueryOperator.GREATER_THAN_OR_EQUALS)
                    .valueType(ValueType.DATE)
                    .dateValue(date)
                    .build();

            List<Filter> filters = new ArrayList<>();
            filters.add(productIdLike);
            filters.add(brandIdLike);
            filters.add(afterStartDate);
            filters.add(beforeEndDate);

            List<Sort.Order> orders = new ArrayList<>();

            Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "priority");
            orders.add(order1);

            List<Prices> prices = pricesFilter.query(filters, orders);
            return prices;
        } catch (Exception e) {
            log.error(MessageFormat.format(serviceExceptionMsg, this.getClass().getSimpleName(), e.getMessage()));
            throw e;
        }
    }


}
