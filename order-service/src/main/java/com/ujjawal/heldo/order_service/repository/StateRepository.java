package com.ujjawal.heldo.order_service.repository;
import com.ujjawal.heldo.order_service.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
}
