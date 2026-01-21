package com.testjava.priceservice.infrastructure.persistence.repository;

import com.testjava.priceservice.infrastructure.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {
    
    @Query(value = "SELECT * FROM PRICES p WHERE " +
           "p.PRODUCT_ID = :productId AND p.BRAND_ID = :brandId AND " +
           "p.START_DATE <= :applicationDate AND p.END_DATE >= :applicationDate " +
           "ORDER BY p.PRIORITY DESC LIMIT 1", nativeQuery = true)
    Optional<PriceEntity> findApplicablePrice(
        @Param("applicationDate") LocalDateTime applicationDate,
        @Param("productId") Long productId,
        @Param("brandId") Long brandId
    );
}