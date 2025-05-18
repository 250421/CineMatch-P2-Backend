package com.revature.service;

import com.revature.entity.Board;
import com.revature.entity.Post;
import com.revature.entity.RatedPost;
import com.revature.entity.User;
import com.revature.exception.PostImageFailedException;
import com.revature.repository.PostRepository;
import com.revature.repository.RatedPostRepository;
import com.revature.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private RatedPostRepository ratedPostRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${s3.bucket.name}")
    private String bucketName;

    public String createPostImagePresignedURL(String bucketName, String keyName) {
        try (S3Presigner presigner = S3Presigner.create()) {
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(5))
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

            return presignedRequest.url().toExternalForm();
        }
    }

    public Post addPost(Post post, MultipartFile imageFile) {
        S3Client client = S3Client.builder().build();
        try {
            if (imageFile != null) {
                if (post.getImage() != null && !post.getImage().isBlank()) {
                    try {
                        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                                .bucket(bucketName)
                                .key("posts/" + post.getId() + "/" + post.getImage())
                                .build();

                        client.deleteObject(deleteObjectRequest);
                    } catch (S3Exception e) {
                        throw new PostImageFailedException("Failed to deal with image.");
                    }
                }
                String fileName = imageFile.getOriginalFilename();

                post.setImage(fileName);
                Post savedPost = postRepository.save(post);

                long postId = savedPost.getId();

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key("posts/" + postId + "/" + fileName)
                        .build();

                client.putObject(putObjectRequest, RequestBody.fromInputStream(imageFile.getInputStream(), imageFile.getSize()));

                return savedPost;
            }

        } catch (Exception e) {
            throw new PostImageFailedException("Failed to deal with image.");
        }
        return postRepository.save(post);
    }

    public Post findPostById(int id) {
        Post post;
        post = postRepository.findById(id)
                .orElse(null);

        return post;
    }

    public Post findPostByIdWithImage(int id) {
        Post post;
        post = postRepository.findById(id)
                .orElse(null);

        if (post != null && post.getImage() != null && !post.getImage().isBlank())
            post.setImage(createPostImagePresignedURL(bucketName, "posts/" + post.getId() + "/" + post.getImage()));

        return post;
    }

    public List<Post> findPostsByBoard(Board board) {
        List<Post> posts = postRepository.findByBoard(board);
        for (Post post : posts) {
            if (post.getImage() != null && !post.getImage().isBlank())
                post.setImage(createPostImagePresignedURL(bucketName, "posts/" + post.getId() + "/" + post.getImage()));
        }
        return posts;
    }

    public List<Post> findPostsByUser(User user) {
        List<Post> posts = postRepository.findByUser(user);
        for (Post post : posts) {
            if (post.getImage() != null && !post.getImage().isBlank())
                post.setImage(createPostImagePresignedURL(bucketName, "posts/" + post.getId() + "/" + post.getImage()));
        }
        return posts;
    }

    public Post ratePost(Post post, User user, int rating) {
        RatedPost currentRP = ratedPostRepository.findByUserAndPost(user, post).orElse(null);
        int currentRating = 0;
        if (currentRP != null) {
            currentRating = currentRP.getRating();
            if (rating == 0)
                ratedPostRepository.delete(currentRP);
            else {
                currentRP.setRating(rating);
                ratedPostRepository.save(currentRP);
            }
        }
        else if (rating != 0)
            ratedPostRepository.save(new RatedPost(user, post, rating));
        int diff = rating - currentRating;
        post.setRating(post.getRating() + diff);
        return postRepository.save(post);
    }

    public Post addFavoritedPostById(int id, User user) {
        Post post = findPostById(id);
        if (post != null) {
            user.getFavoritedPosts().add(post);
            userRepository.save(user);
            return post;
        }
        else return null;
    }

    public boolean removeFavoritedPostById(int id, User user) {
        Post post = findPostById(id);
        boolean removed = user.getFavoritedPosts().remove(post);
        userRepository.save(user);
        return removed;
    }

    public List<Post> getFavoritedPosts(User user) {
        return List.copyOf(user.getFavoritedPosts());
    }
}
