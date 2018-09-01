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
import javax.persistence.OneToOne;
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
	
	
	@OneToOne
	@JoinColumn(name="order_id")
	private Order order;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="car_id")
	private Car car;	
	
	private String status;
	
	private String barterStatus;
	
	private int mailSendCounter = 4;


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
	
    
	public Order getOrder() {
		return order;
	}


	public void setOrder(Order order) {
		this.order = order;
	}


	public Car getCar() {
		return car;
	}


	public void setCar(Car car) {
		this.car = car;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getBarterStatus() {
		return barterStatus;
	}


	public void setBarterStatus(String barterStatus) {
		this.barterStatus = barterStatus;
	}


	public int getMailSendCounter() {
		return mailSendCounter;
	}


	public void setMailSendCounter(int mailSendCounter) {
		this.mailSendCounter = mailSendCounter;
	} 	
}
