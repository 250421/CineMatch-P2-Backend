package com.revature.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name="user_comments_rated", schema = "p2")
@NoArgsConstructor
@Getter
@Setter
public class RatedComment {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="comment_id", nullable = false)
    private Comment comment;

    @Column(name = "rating")
    private int rating;

    public RatedComment(User user, Comment comment, int rating) {
        this.user = user;
        this.comment = comment;
        this.rating = rating;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RatedComment other = (RatedComment) obj;
        return Objects.equals(getId(), other.getId())
                && Objects.equals(getUser(), other.getUser())
                && Objects.equals(getComment(), other.getComment())
                && Objects.equals(getRating(), other.getRating());
    }

    @Override
    public String toString() {
        return "User{" +
                "id = " + id +
                ", user = '" + user + '\'' +
                ", comment = '" + comment + '\'' +
                ", rating = '" + rating + '\'' +
                '}';
    }
}
