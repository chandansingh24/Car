package com.carcomehome.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Car {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String title;

	private String company;
	
    private String makeAndModel;
//	private String manufacturingDate;
	
    private String feature;       //Manual or Automatic
//  private int numberOfDoors;
	
	private String category;
	
	private String seatingCapacity;
//	private int numberOfOccupancy;
	
	private String owner;	
	private String segment;
		
//	private String model;	
//	private String uniqueIdentification;	
//	private double totalWeight;
	
	private double listPrice;
	private double ourPrice;
	private double barterPrice;
	private boolean active=true;	
	private boolean barterActive=true;
	
	@Column(columnDefinition="text")
	private String description;
	
	private int inStockNumber = 1;
	
	private String businessAddressStreet1;
	private String businessAddressStreet2;
	private String businessAddressCity;
	private String businessAddressState;
	private String businessAddressCountry;
	private String businessAddressZipcode;	
	
	
	@Column(name = "profile_image_url")
    private String profileImageUrl;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	//Reservation Table Mapping
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "car")
	private List<Reservation> reservation; 
	
	@OneToMany(mappedBy = "car")
	@JsonIgnore
	private List<CarToCartItem> carToCartItemList;

	
	
	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}

	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}	

	public double getListPrice() {
		return listPrice;
	}

	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}
	
	public double getOurPrice() {
		return ourPrice;
	}

	public void setOurPrice(double ourPrice) {
		this.ourPrice = ourPrice;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getMakeAndModel() {
		return makeAndModel;
	}

	public void setMakeAndModel(String makeAndModel) {
		this.makeAndModel = makeAndModel;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getSeatingCapacity() {
		return seatingCapacity;
	}

	public void setSeatingCapacity(String seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public double getBarterPrice() {
		return barterPrice;
	}

	public void setBarterPrice(double barterPrice) {
		this.barterPrice = barterPrice;
	}

	public boolean isBarterActive() {
		return barterActive;
	}

	public void setBarterActive(boolean barterActive) {
		this.barterActive = barterActive;
	}
	
	
	public int getInStockNumber() {
		return inStockNumber;
	}

	public void setInStockNumber(int inStockNumber) {
		this.inStockNumber = inStockNumber;
	}

	public String getBusinessAddressStreet1() {
		return businessAddressStreet1;
	}

	public void setBusinessAddressStreet1(String businessAddressStreet1) {
		this.businessAddressStreet1 = businessAddressStreet1;
	}

	public String getBusinessAddressStreet2() {
		return businessAddressStreet2;
	}

	public void setBusinessAddressStreet2(String businessAddressStreet2) {
		this.businessAddressStreet2 = businessAddressStreet2;
	}

	public String getBusinessAddressCity() {
		return businessAddressCity;
	}

	public void setBusinessAddressCity(String businessAddressCity) {
		this.businessAddressCity = businessAddressCity;
	}

	public String getBusinessAddressState() {
		return businessAddressState;
	}

	public void setBusinessAddressState(String businessAddressState) {
		this.businessAddressState = businessAddressState;
	}

	public String getBusinessAddressCountry() {
		return businessAddressCountry;
	}

	public void setBusinessAddressCountry(String businessAddressCountry) {
		this.businessAddressCountry = businessAddressCountry;
	}

	public String getBusinessAddressZipcode() {
		return businessAddressZipcode;
	}

	public void setBusinessAddressZipcode(String businessAddressZipcode) {
		this.businessAddressZipcode = businessAddressZipcode;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}		

	
	public List<Reservation> getReservation() {
		return reservation;
	}

	public void setReservation(List<Reservation> reservation) {
		this.reservation = reservation;
	}
	
	public List<CarToCartItem> getCarToCartItemList() {
		return carToCartItemList;
	}

	public void setCarToCartItemList(List<CarToCartItem> carToCartItemList) {
		this.carToCartItemList = carToCartItemList;
	}		

}
