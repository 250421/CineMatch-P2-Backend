package com.revature.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name="user_posts_rated", schema = "p2")
@NoArgsConstructor
@Getter
@Setter
public class RatedPost {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id", nullable = false)
    private Post post;

    @Column(name = "rating")
    private int rating;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RatedPost other = (RatedPost) obj;
        return Objects.equals(getId(), other.getId())
                && Objects.equals(getUser(), other.getUser())
                && Objects.equals(getPost(), other.getPost())
                && Objects.equals(getRating(), other.getRating());
    }

    @Override
    public String toString() {
        return "User{" +
                "id = " + id +
                ", user = '" + user + '\'' +
                ", post = '" + post + '\'' +
                ", rating = '" + rating + '\'' +
                '}';
    }
}
