package com.revature.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name="genre", schema = "p2")
@NoArgsConstructor
@Getter
@Setter
public class Genre {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Genre other = (Genre) obj;
        return Objects.equals(getId(), other.getId())
                && Objects.equals(getName(), other.getName())
                && Objects.equals(getSlug(), other.getSlug());
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", slug = '" + slug + '\'' +
                '}';
    }
}
