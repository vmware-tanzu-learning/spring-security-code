package rewardsdining.account;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_ROLE")
public class Role {

	public static final String ROLE_USER = "ROLE_USER";
	
	public static final String ROLE_MANAGER = "ROLE_MANAGER";
	
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	@Id
	@Column(name = "NAME")
	private String name;
	
	public Role() {}
	
	public Role(String name) {
		this.name = name;
	}
	
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return Objects.equals(name, ((Role) o).name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            "name='" + name + '\'' +
            "}";
    }
}
