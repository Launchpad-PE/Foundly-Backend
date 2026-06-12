package com.foundly.foundlyplatform.projects.domain.model.entities;

import com.foundly.foundlyplatform.projects.domain.model.valueobjects.CardItem;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.CardTitle;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.RoleName;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project_roles")
@Getter
@NoArgsConstructor
public class ProjectRole {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private RoleName name;

    @Embedded
    private CardTitle cardTitle;

    @ElementCollection
    @CollectionTable(name = "project_role_items", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "item_description")
    private List<CardItem> items = new ArrayList<>();

    public ProjectRole(RoleName name, CardTitle cardTitle, List<CardItem> items) {
        this.name = name;
        this.cardTitle = cardTitle;
        this.items = new ArrayList<>(items);
    }

    public void addItem(CardItem item) {
        this.items.add(item);
    }

    public void removeItem(int index) {
        this.items.remove(index);
    }

    public List<String> getItemDescriptions() {
        return items.stream().map(CardItem::description).toList();
    }
}