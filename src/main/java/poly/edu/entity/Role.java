package poly.edu.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Roles")
public class Role {

    @Id
    private String id;

    private String name;

    // Một Role có thể thuộc về nhiều Authority
    @OneToMany(mappedBy = "role")
    private List<Authority> authorities;

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Authority> getAuthorities() { return authorities; }
    public void setAuthorities(List<Authority> authorities) { this.authorities = authorities; }
}
