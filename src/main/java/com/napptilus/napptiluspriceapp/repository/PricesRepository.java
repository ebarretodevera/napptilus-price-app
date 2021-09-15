package com.napptilus.napptiluspriceapp.repository;

import com.napptilus.napptiluspriceapp.model.Prices;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public  interface PricesRepository extends CrudRepository<Prices, Long>, JpaSpecificationExecutor<Prices> {

    List<Prices> findAll();
    List<Prices> findAll(Sort by);
}
