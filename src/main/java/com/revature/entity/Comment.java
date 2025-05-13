package com.revature.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="comment", schema = "p2")
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;

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
        Comment other = (Comment) obj;
        return Objects.equals(getId(), other.getId())
                && Objects.equals(getPost(), other.getPost())
                && Objects.equals(getUser(), other.getUser())
                && Objects.equals(getText(), other.getText())
                && Objects.equals(getCreated(), other.getCreated())
                && Objects.equals(getRating(), other.getRating())
                && Objects.equals(getDeleted(), other.getDeleted());
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id = " + id +
                ", post = '" + post.getTitle() + '\'' +
                ", user = '" + user.getUsername() + '\'' +
                ", text = '" + text + '\'' +
                ", created = '" + created + '\'' +
                ", rating = '" + rating + '\'' +
                ", deleted = '" + deleted + '\'' +
                '}';
    }
}
