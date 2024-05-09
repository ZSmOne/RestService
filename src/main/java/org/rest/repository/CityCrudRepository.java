package org.rest.repository;

import org.rest.model.City;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CityCrudRepository extends CrudRepository<City, Long> {

@Modifying
@Query(value = "UPDATE users SET city_id = NULL WHERE city_id = :cityId", nativeQuery = true)
void deleteCityFromUsers(@Param("cityId") Long cityId);
}
