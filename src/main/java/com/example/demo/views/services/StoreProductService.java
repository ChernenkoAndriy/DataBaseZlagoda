package com.example.demo.views.services;

import com.example.demo.views.repositories.StoreProductRepository;
import com.example.demo.views.repositories.database_entities.StoreProduct;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Service
public class StoreProductService extends AbstractService<StoreProduct, UUID>{
    private StoreProductRepository repository;
    public StoreProductService(PlatformTransactionManager transactionManager, StoreProductRepository storeProductRepository) {
        super(transactionManager);
        repository = storeProductRepository;
    }

    @Override
    public List<StoreProduct> getAllEntities() {
        return repository.findAll();
    }

    @Override
    public void addEntity(StoreProduct e) {
        if(!e.isPromotional_product()) {
            repository.save(e);
        }else {
            transactionTemplate.execute(status -> {
                repository.save(e);
                String name = e.getProduct();
                name = name.substring(0, name.length()-6);
                StoreProduct oldProduct = getAllBy(name, null, false, null).getFirst();
                StoreProduct promProduct = getAllBy(name, null, true, null).getFirst();
                oldProduct.setUPC_prom(promProduct.getUPC());
                updateEntity(oldProduct);
                return null;
            });
        }
    }

    @Override
    public void updateEntity(StoreProduct e) {
        transactionTemplate.execute(status -> {
            repository.update(e);
            if(e.getUPC_prom()!=null){
                BigDecimal price = e.getSelling_price();
                price = price.multiply(new BigDecimal(0.8));
                repository.updatePrice(e.getUPC_prom(), price);
            }
            StoreProduct notSale= repository.getWithUPC_Prom(e.getUPC());
            if(notSale!=null){
                BigDecimal price = e.getSelling_price();
                price = price.multiply(new BigDecimal(1.2));
                repository.updatePrice(notSale.getUPC(), price);
            }
            return null;
        });
    }

    @Override
    public void deleteEntity(UUID id) {
        transactionTemplate.execute(status -> {
            try {
               StoreProduct goods =  repository.findById(id);
                repository.delete(id);
                if(goods.getUPC_prom() !=null){
                    repository.delete(goods.getUPC_prom());
                }
            } catch (DataIntegrityViolationException e) {
                throw new ConstraintViolationException("Cannot delete these goods, cause there are checks linked to them or their sale version ", null);
            }
            return null;
        });
    }

    @Override
    public int countEntities() {
        return repository.count();
    }
    protected void changeQuantity(UUID id, int delta) {
        repository.changeQuantity(id, delta);
    }

    public List<StoreProduct> getAllBy(String productName, String category, Boolean isPromotional, String upc) {
        return repository.getAllBy(productName, category, isPromotional, upc);
    }

    public List<StoreProduct> getAllWithSale(){
        return repository.getAllWithSale();
    }

    public void addTo(UUID id, int i) {
        repository.addTo(id, i);
    }

    public void moveProducts(UUID id, UUID promid, Integer amount) {
        transactionTemplate.execute(status -> {
            StoreProduct notProm = repository.findById(id);
            if(notProm.getProducts_number()<amount){
                throw new IllegalArgumentException("Too much products to move");
            }else {
                addTo(promid, amount);
                addTo(id, -amount);
            }
            return null;});
    }

    public List<StoreProduct> getAllEntitiesWithPromNull() {
        return repository.getAllEntitiesWithPromNull();
    }

    public List<StoreProduct> getAllWithoutSale() {
        return repository.getAllWithoutSale();
    }
}
