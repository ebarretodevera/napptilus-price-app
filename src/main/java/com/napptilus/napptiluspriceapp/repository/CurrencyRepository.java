package com.napptilus.napptiluspriceapp.repository;

import com.napptilus.napptiluspriceapp.model.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    List<Currency> findAll();

    Optional<Currency> findByCurrencyCode(String id);
}
