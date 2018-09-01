package com.carcomehome.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.carcomehome.domain.security.Authority;
import com.carcomehome.domain.security.Plan;
import com.carcomehome.domain.security.UserRole;


@Entity
public class User implements Serializable, UserDetails {
	
	 /** The Serial Version UID for Serializable classes. */
   private static final long serialVersionUID = 1L;
   
   public User() {

   }	  
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", nullable = false, updatable = false)
	private Long id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	
	@Column(name="email", nullable = false, updatable = false)
	private String email;
	private String phone;
	private boolean enabled;
	
	
//	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(fetch = FetchType.EAGER, cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(
			name="user_userrole",
	        joinColumns=@JoinColumn(name="user_id", referencedColumnName = "id"),
	        inverseJoinColumns=@JoinColumn(name="userrole_id", referencedColumnName = "id")			
			)
	private List<UserRole> userRoles = new ArrayList<>();
	
		
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
	private ShoppingCart shoppingCart;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<UserShipping> userShippingList;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<UserPayment> userPaymentList;
	
	@OneToMany(mappedBy="user")
	private List<Order> orderList;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;
	
		
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<Car> car;   
	
	
	public List<Car> getCar() {
		return car;
	}
	
	public void setCar(List<Car> car) {
		this.car = car;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public List<UserRole> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}	
	
	
	public List<UserShipping> getUserShippingList() {
		return userShippingList;
	}
	
	public void setUserShippingList(List<UserShipping> userShippingList) {
		this.userShippingList = userShippingList;
	}
	
	public List<UserPayment> getUserPaymentList() {
		return userPaymentList;
	}
	
	public void setUserPaymentList(List<UserPayment> userPaymentList) {
		this.userPaymentList = userPaymentList;
	}
		
	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}
	
	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}	
	
	public List<Order> getOrderList() {
		return orderList;
	}
	
	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}	
	
	
	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorites = new HashSet<>();
//		userRoles.forEach(ur -> authorites.add(new Authority(ur.getRole().getName())));
		
		for (UserRole urs: userRoles) {
			urs.getRoles().forEach(ur -> authorites.add(new Authority(ur.getName())));
		}
		
		return authorites;
	}
	
	@Override
	public boolean isAccountNonExpired() {		
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {		
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {		
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	
	 @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;

	        User user = (User) o;

	        return id == user.id;

	    }

	    @Override
	    public int hashCode() {
	        return (int) (id ^ (id >>> 32));
	    }

}
