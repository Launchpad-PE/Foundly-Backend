package com.foundly.foundlyplatform.iam.domain.model.aggregates;

import com.foundly.foundlyplatform.iam.domain.model.entities.Role;
import com.foundly.foundlyplatform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class User extends AbstractDomainAggregateRoot<User> {
    @Setter
    private Long id;

    @Setter
    private String username;

    @Setter
    private String password;

    @Setter
    private Set<Role> roles;

    public User() {
        this.roles = new HashSet<>();
    }

    public User(String password, String username) {
        this.password = password;
        this.username = username;
        this.roles = new HashSet<>();
    }

    public User(String username, String password, List<Role> roles) {
        this(username, password);
        addRoles(roles);
    }

    /**
     * Add a role to the user.
     * @param role the role to add
     * @return the user with the added role
     */
    public User addRole(Role role) {
        this.roles.add(role);
        return this;
    }

    /**
     * Add a list of roles to the user.
     * @param roles the list of roles to add
     * @return the user with the added roles
     */
    public User addRoles(List<Role> roles) {
        var validatedRoleSet = Role.validateRoleSet(roles);
        this.roles.addAll(validatedRoleSet);
        return this;
    }
}