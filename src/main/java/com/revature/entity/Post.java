package com.revature.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name="post", schema = "p2")
@NoArgsConstructor
@Getter
@Setter
public class Post {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "image")
    private String image;

    @Column(name = "text")
    private String text;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "deleted", nullable = false)
    private int deleted;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Post other = (Post) obj;
        return Objects.equals(getId(), other.getId())
                && Objects.equals(getBoard(), other.getBoard())
                && Objects.equals(getUser(), other.getUser())
                && Objects.equals(getTitle(), other.getTitle())
                && Objects.equals(getImage(), other.getImage())
                && Objects.equals(getText(), other.getText())
                && Objects.equals(getRating(), other.getRating())
                && Objects.equals(getDeleted(), other.getDeleted());
    }

    @Override
    public String toString() {
        return "Post{" +
                "id = " + id +
                ", board = '" + board.getName() + '\'' +
                ", user = '" + user.getUsername() + '\'' +
                ", title = '" + title + '\'' +
                ", image = '" + image + '\'' +
                ", text = '" + text + '\'' +
                ", rating = '" + rating + '\'' +
                ", deleted = '" + deleted + '\'' +
                '}';
    }
}
