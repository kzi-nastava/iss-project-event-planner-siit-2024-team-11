package org.example.eventy.users.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

// POJO koji implementira Spring Security GrantedAuthority kojim se mogu definisati role u aplikaciji
@Entity
@Table(name="ROLE")
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="name")
    String name;

    public Role() {

    }

    public Role(String name) {
        this.name = name;
    }

    @JsonIgnore
    @Override
    public String getAuthority() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	@Override
	public String toString() {
		return name;
	}

}
