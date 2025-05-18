package com.revature.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name="board", schema = "p2")
@NoArgsConstructor
@Getter
@Setter
public class Board {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Board(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Board other = (Board) obj;
        return Objects.equals(getId(), other.getId())
                && Objects.equals(getName(), other.getName());
    }

    @Override
    public String toString() {
        return "Board{" +
                "id = " + id +
                ", name = '" + name + '\'' +
                '}';
    }
}
