package com.carcomehome.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Reservation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", nullable = false, updatable = false)
	private Long id;
	
	@Column(name="pick_up_date")
	@Temporal(TemporalType.DATE)
	private Date pickUpDate;
	
	@Column(name="return_date")
	@Temporal(TemporalType.DATE)
	private Date returnDate;
	
	private String pickUpCity;
	
	private String pickUpCityZipcode;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="car_id")
	private Car car;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Date getPickUpDate() {
		return pickUpDate;
	}


	public void setPickUpDate(Date pickUpDate) {
		this.pickUpDate = pickUpDate;
	}


	public Date getReturnDate() {
		return returnDate;
	}


	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}


	public String getPickUpCity() {
		return pickUpCity;
	}


	public void setPickUpCity(String pickUpCity) {
		this.pickUpCity = pickUpCity;
	}


	public String getPickUpCityZipcode() {
		return pickUpCityZipcode;
	}


	public void setPickUpCityZipcode(String pickUpCityZipcode) {
		this.pickUpCityZipcode = pickUpCityZipcode;
	}


	public Car getCar() {
		return car;
	}


	public void setCar(Car car) {
		this.car = car;
	}		

}
