package com.carcomehome.domain.security;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.carcomehome.domain.User;


@Entity
@Table(name="userrole")
public class UserRole implements Serializable {
	
	 /** The Serial Version UID for Serializable classes. */
    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/*@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")	*/
	
	
	
	@ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(
			name="user_userrole",
	        joinColumns=@JoinColumn(name="userrole_id", referencedColumnName = "id"),
	        inverseJoinColumns=@JoinColumn(name="user_id", referencedColumnName = "id")			
			)
	private List<User> users = new ArrayList<>();	
	
		
	
	@ManyToMany(fetch=FetchType.EAGER, cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(
			name="role_userrole",
	        joinColumns=@JoinColumn(name="userrole_id", referencedColumnName = "id"),
	        inverseJoinColumns=@JoinColumn(name="role_id", referencedColumnName = "id")			
			)
	private List<Role> roles = new ArrayList<>();
	
	
	
	public UserRole() {}
	
	public UserRole(List<User> user, List<Role> roles) {
		this.users = user;
		this.roles = roles;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<User> getUser() {
		return users;
	}


	public void setUser(List<User> user) {
		this.users = user;
	}
	

	public List<Role> getRoles() {
		return roles;
	}
	

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;

	        UserRole userRole = (UserRole) o;

	        return id == userRole.id;

	    }

	    @Override
	    public int hashCode() {
	        return (int) (id ^ (id >>> 32));
	    }
	
}
