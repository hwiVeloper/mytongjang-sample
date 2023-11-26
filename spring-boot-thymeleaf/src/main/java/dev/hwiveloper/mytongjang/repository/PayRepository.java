package dev.hwiveloper.mytongjang.repository;

import dev.hwiveloper.mytongjang.domain.Pay;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PayRepository extends CrudRepository<Pay, String> {
    public Optional<Pay> findByOrdNo(String ordNo) throws Exception;
}
