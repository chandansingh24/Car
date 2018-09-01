package com.carcomehome.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.carcomehome.domain.Car;


public interface CarRepository extends CrudRepository<Car, Long>  {
	 List<Car> findBySegment(String segment); 
	 
	 List<Car> findByTitleContaining(String title); 	 
	 
	 @Query("select cr from Car cr inner join cr.user u where cr.user.id = ?1")
	 List<Car> findAllByUserId(long userId);	
	 
	
	 /*@Query(value="SELECT cr.*\n" + 
	 		"FROM `carcomehome`.`car` AS cr\n" + 
	 		"LEFT JOIN (SELECT b.* \n" + 
	 		"           FROM `carcomehome`.`reservation` AS b \n" + 
	 		"           WHERE (b.pick_up_date > '2018-02-01' AND b.return_date < '2018-02-14') OR\n" + 
	 		"                 (b.pick_up_date < '2018-02-01' AND b.return_date > '2018-02-14') OR\n" + 
	 		"                 (b.pick_up_date < '2018-02-14' AND b.return_date > '2018-02-14') OR \n" + 
	 		"                 (b.pick_up_date < '2018-02-01' AND b.return_date > '2018-02-01')\n" + 
	 		"         )  AS b ON cr.id = b.car_id\n" + 
	 		"WHERE b.car_id is null", nativeQuery = true)
	 List<Car> findNonBookedOnes();*/
	 
	 
	 @Query(value="SELECT cr.*\n" + 
		"FROM `carcomehome`.`car` AS cr\n" + 
		"LEFT JOIN (SELECT b.* \n" + 
		"           FROM `carcomehome`.`reservation` AS b \n" + 
		"           WHERE (b.pick_up_date >= :pickUpDate AND b.return_date <= :returnDate) OR\n" + 
		"                 (b.pick_up_date <= :pickUpDate AND b.return_date >= :returnDate) OR\n" + 
		"                 (b.pick_up_date <= :returnDate AND b.return_date >= :returnDate) OR \n" + 
		"                 (b.pick_up_date <= :pickUpDate AND b.return_date >= :pickUpDate)\n" + 
		"         )  AS b ON cr.id = b.car_id\n" + 
		"WHERE b.car_id is null AND (cr.business_address_zipcode = :inputZip)", nativeQuery = true)
     List<Car> findNonBookedCarsZipCode(@Param("pickUpDate") Date pickUpDate, @Param("returnDate") Date returnDate, @Param("inputZip") String inputZip);
	 
	 
	 @Query(value="SELECT cr.*\n" + 
				"FROM `carcomehome`.`car` AS cr\n" + 
				"LEFT JOIN (SELECT b.* \n" + 
				"           FROM `carcomehome`.`reservation` AS b \n" + 
				"           WHERE (b.pick_up_date >= :pickUpDate AND b.return_date <= :returnDate) OR\n" + 
				"                 (b.pick_up_date <= :pickUpDate AND b.return_date >= :returnDate) OR\n" + 
				"                 (b.pick_up_date <= :returnDate AND b.return_date >= :returnDate) OR \n" + 
				"                 (b.pick_up_date <= :pickUpDate AND b.return_date >= :pickUpDate)\n" + 
				"         )  AS b ON cr.id = b.car_id\n" + 
				"WHERE b.car_id is null AND (cr.business_address_city = :inputCity AND cr.business_address_state = :inputState)", nativeQuery = true)
	List<Car> findNonBookedCarsCityAndState(@Param("pickUpDate") Date pickUpDate, @Param("returnDate") Date returnDate, @Param("inputCity") String inputCity, @Param("inputState") String inputState);
	 
}
