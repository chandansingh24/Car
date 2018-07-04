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

	private String owner;

	private String company;
	
	private String manufacturingDate;
	
	private int numberOfDoors;
	
	private String category;
	
	private int numberOfOccupancy;
	
	private String model;
	
	private String uniqueIdentification;
	
	private double totalWeight;
	
	private double listPrice;
	private double ourPrice;
	private boolean active=true;
	
	@Column(columnDefinition="text")
	private String description;
	private int inStockNumber;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(name = "profile_image_url")
    private String profileImageUrl;
	
	//Reservation Table Mapping
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "car")
	private List<Reservation> reservation; 
	
     	
//	@Transient
//	private MultipartFile carImage;        //@Transient - Not stored in database
	
	@OneToMany(mappedBy = "car")
	@JsonIgnore
	private List<CarToCartItem> carToCartItemList;

	
	public List<Reservation> getReservation() {
		return reservation;
	}

	public void setReservation(List<Reservation> reservation) {
		this.reservation = reservation;
	}

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

	public String getManufacturingDate() {
		return manufacturingDate;
	}

	public void setManufacturingDate(String manufacturingDate) {
		this.manufacturingDate = manufacturingDate;
	}

	public int getNumberOfDoors() {
		return numberOfDoors;
	}

	public void setNumberOfDoors(int numberOfDoors) {
		this.numberOfDoors = numberOfDoors;
	}

	public int getNumberOfOccupancy() {
		return numberOfOccupancy;
	}

	public void setNumberOfOccupancy(int numberOfOccupancy) {
		this.numberOfOccupancy = numberOfOccupancy;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getUniqueIdentification() {
		return uniqueIdentification;
	}

	public void setUniqueIdentification(String uniqueIdentification) {
		this.uniqueIdentification = uniqueIdentification;
	}

	public double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
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

	public int getInStockNumber() {
		return inStockNumber;
	}

	public void setInStockNumber(int inStockNumber) {
		this.inStockNumber = inStockNumber;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}	
	

	/*public MultipartFile getCarImage() {
		return carImage;
	}

	public void setCarImage(MultipartFile carImage) {
		this.carImage = carImage;
	}
*/
	public List<CarToCartItem> getCarToCartItemList() {
		return carToCartItemList;
	}

	public void setCarToCartItemList(List<CarToCartItem> carToCartItemList) {
		this.carToCartItemList = carToCartItemList;
	}		

}
