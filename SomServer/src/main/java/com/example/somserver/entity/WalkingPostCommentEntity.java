package com.example.somserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "walking_post_comments")
public class WalkingPostCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "walking_post_comment_id", nullable = false)
    private int walkingPostCommentId; //walking_post_comments table -> walking_post_comment_id : pk, not null, AI, INT

    @Column(name = "is_nickname_public", nullable = false)
    private Boolean isNicknamePublic; //walking_post_comments table -> is_nickname_public : not null, TINYINT(1)

    @Column(name = "walking_post_comment_date", nullable = false)
    private LocalDate walkingPostCommentDate; //walking_post_comments table -> walking_post_comment_date : not null, DATE

    @Column(name = "walking_post_comment_time", nullable = false)
    private LocalTime walkingPostCommentTime; //walking_post_comments table -> walking_post_comment_time : not null, TIME

    @Column(name = "comment", nullable = false, length = 255)
    private String comment; //walking_post_comments table -> comment : not null, VARCHAR(255)

    //UserEntity(테이블 users)와 WalkingPostCommentEntity(테이블 walking_post_comments)를 0ne to many 관계로 매핑
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) //walking_post_comments 테이블의 user_id 칼럼이 users 테이블의 user_id 칼럼을 참조하는 외래 키(FK)임
    private UserEntity user;

    //WalkingPostEntity(테이블 walking_posts)와 WalkingPostCommentEntity(테이블 walking_post_comments)를 0ne to many 관계로 매핑
    @ManyToOne
    @JoinColumn(name = "walking_post_id", nullable = false) //walking_post_comments 테이블의 walking_post_id 칼럼이 walking_posts 테이블의 walking_post_id 칼럼을 참조하는 외래 키(FK)임
    private WalkingPostEntity walkingPost;
}
