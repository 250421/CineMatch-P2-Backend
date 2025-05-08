package com.revature.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name="movie", schema = "p2")
@NoArgsConstructor
@Getter
@Setter
public class Movie {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Movie other = (Movie) obj;
        return Objects.equals(getId(), other.getId())
                && Objects.equals(getTitle(), other.getTitle());
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id = " + id +
                ", title = '" + title + '\'' +
                '}';
    }
}
