package com.example.demo.views.repositories;


import com.example.demo.views.repositories.database_entities.CustomerCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerCard, UUID> {

    List<CustomerCard> findByCustSurnameContainingIgnoreCase(String surname);

    List<CustomerCard> findByPercent(int percent);

    List<CustomerCard> findByCustSurnameContainingIgnoreCaseAndPercent(String surname, int percent);
}