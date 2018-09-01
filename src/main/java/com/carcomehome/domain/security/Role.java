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
import com.carcomehome.enums.RolesEnum;



@Entity
public class Role implements Serializable {

	/** The Serial Version UID for Serializable classes. */
    private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String name;
	
		
	@ManyToMany(fetch=FetchType.EAGER, cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(
			name="role_userrole",
	        joinColumns=@JoinColumn(name="role_id", referencedColumnName = "id"),
	        inverseJoinColumns=@JoinColumn(name="userrole_id", referencedColumnName = "id")			
			)
	private List<UserRole> userRoles = new ArrayList<>();
	
	
	public Role() {

    }
	
	 /**
     * Full constructor. 
     * @param rolesEnum
     */
    public Role(RolesEnum rolesEnum) {
   //     this.id = rolesEnum.getId();
        this.name = rolesEnum.getRoleName();
    }

		
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

		
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        return id == role.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
	
}
