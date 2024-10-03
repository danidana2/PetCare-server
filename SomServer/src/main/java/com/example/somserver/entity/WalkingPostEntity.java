package com.example.somserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "walking_posts")
public class WalkingPostEntity {

    @Id
    @Column(name = "walking_post_id", nullable = false, length = 50)
    private String walkingPostId; //walking_posts table -> walking_post_id : pk, not null, VARCHAR(50)

    @Column(name = "is_nickname_public", nullable = false)
    private Boolean isNicknamePublic; //walking_posts table -> is_nickname_public : not null, TINYINT(1)

    @Column(name = "walking_post_date", nullable = false)
    private LocalDate walkingPostDate; //walking_posts table -> walking_post_date : not null, DATE

    @Column(name = "walking_post_time", nullable = false)
    private LocalTime walkingPostTime; //walking_posts table -> walking_post_time : not null, TIME

    @Column(name = "content", length = 255)
    private String content; //walking_posts table -> content : VARCHAR(255)

    @Column(name = "image_url", length = 255)
    private String imageUrl; //walking_posts table -> image_url : VARCHAR(255)

    //@Column(name = "likes", nullable = false)
    //private Integer likes; //walking_posts table -> likes : not null, INT

    //UserEntity(테이블 users)와 WalkingPostEntity(테이블 walking_posts)를 0ne to many 관계로 매핑
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) //walking_posts 테이블의 user_id 칼럼이 users 테이블의 user_id 칼럼을 참조하는 외래 키(FK)임
    private UserEntity user;

    //WalkingPostEntity(테이블 walking_posts)와 WalkingPostCommentEntity(테이블 walking_post_comments)를 0ne to many 관계로 매핑
    //부모 엔티티가 삭제될 때와 부모 엔티티의 컬렉션에서 자식 엔티티가 제거될 때 모두 자식 엔티티가 삭제
    @OneToMany(mappedBy = "walkingPost", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<WalkingPostCommentEntity> walkingPostComments;
}
