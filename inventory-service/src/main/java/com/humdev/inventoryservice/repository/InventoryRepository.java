package com.humdev.inventoryservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.humdev.inventoryservice.entity.Inventory;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory, String>{

    Inventory findByProductCode(String string);

    List<Inventory> findByProductCodeIn(List<String> productCodes);

}

  
