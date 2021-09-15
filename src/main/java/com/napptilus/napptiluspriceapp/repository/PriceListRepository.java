package com.napptilus.napptiluspriceapp.repository;

import com.napptilus.napptiluspriceapp.model.PriceList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceListRepository extends CrudRepository<PriceList, Long> {

    List<PriceList> findAll();

}
